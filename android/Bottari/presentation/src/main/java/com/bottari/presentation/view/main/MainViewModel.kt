package com.bottari.presentation.view.main

import com.bottari.core.domain.usecase.appConfig.CheckForceUpdateUseCase
import com.bottari.core.domain.usecase.appConfig.GetPermissionFlagUseCase
import com.bottari.core.domain.usecase.appConfig.SavePermissionFlagUseCase
import com.bottari.core.domain.usecase.fcm.SaveFcmTokenUseCase
import com.bottari.core.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.core.domain.usecase.member.RegisterMemberUseCase
import com.bottari.logger.BottariLogger
import com.bottari.presentation.BuildConfig
import com.bottari.presentation.common.base.NetworkBaseViewModel
import com.bottari.presentation.util.NetworkManager
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    networkManager: NetworkManager,
    private val registerMemberUseCase: RegisterMemberUseCase,
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
    private val savePermissionFlagUseCase: SavePermissionFlagUseCase,
    private val saveFcmTokenUseCase: SaveFcmTokenUseCase,
    private val getPermissionFlagUseCase: GetPermissionFlagUseCase,
    private val checkForceUpdateUseCase: CheckForceUpdateUseCase,
) : NetworkBaseViewModel<MainUiState, MainUiEvent>(
        initialState = MainUiState(),
        networkManager = networkManager,
    ) {
    init {
        initializeApp()
    }

    fun savePermissionFlag() {
        launch {
            savePermissionFlagUseCase(true)
                .onSuccess { updateState { copy(hasPermissionFlag = true) } }
                .onFailure { emitEvent(MainUiEvent.SavePermissionFlagFailure) }
        }
    }

    fun checkRegisteredMember() {
        if (isConnected.value.not()) return handleOffline()

        launch {
            checkRegisteredMemberUseCase()
                .onSuccess { result ->
                    if (result.isRegistered) {
                        saveFcmToken()
                        return@launch
                    }
                    registerMember()
                }.onFailure { emitEvent(MainUiEvent.LoginFailure) }
        }
    }

    private fun initializeApp() =
        when {
            isConnected.value.not() -> handleOffline()
            BuildConfig.DEBUG -> checkPermissionFlag()
            else -> handleForceUpdate()
        }

    private fun handleOffline() {
        launch {
            getPermissionFlagUseCase()
                .onSuccess { permissionFlag ->
                    updateState { copy(hasPermissionFlag = permissionFlag, isReady = true) }
                    emitEvent(
                        if (permissionFlag) {
                            MainUiEvent.Offline(true)
                        } else {
                            MainUiEvent.IncompletePermissionFlow
                        },
                    )
                }.onFailure { emitEvent(MainUiEvent.GetPermissionFlagFailure) }
        }
    }

    private fun checkPermissionFlag() {
        launch {
            getPermissionFlagUseCase()
                .onSuccess { permissionFlag ->
                    updateState { copy(hasPermissionFlag = permissionFlag) }
                    if (!permissionFlag) {
                        updateState { copy(isReady = true) }
                        emitEvent(MainUiEvent.IncompletePermissionFlow)
                        return@launch
                    }
                    checkRegisteredMember()
                }.onFailure { emitEvent(MainUiEvent.GetPermissionFlagFailure) }
        }
    }

    private fun handleForceUpdate() {
        launch {
            checkForceUpdateUseCase(BuildConfig.APP_VERSION_CODE)
                .onSuccess { isForceUpdate ->
                    if (isForceUpdate) {
                        emitEvent(MainUiEvent.ForceUpdate)
                        return@launch
                    }
                    checkPermissionFlag()
                }.onFailure { exception ->
                    BottariLogger.error(exception.message, exception)
                    checkPermissionFlag()
                }
        }
    }

    private fun saveFcmToken() {
        launch {
            fetchFcmToken()?.let { fcmToken ->
                saveFcmTokenUseCase(fcmToken)
                    .onFailure { exception -> BottariLogger.error(exception.message, exception) }
            }
        }.invokeOnCompletion { onLoginReady() }
    }

    private fun registerMember() {
        launch {
            fetchFcmToken()?.let { fcmToken ->
                registerMemberUseCase(fcmToken)
                    .onSuccess { onLoginReady() }
                    .onFailure { exception ->
                        BottariLogger.error(exception.message, exception)
                        emitEvent(MainUiEvent.RegisterFailure)
                    }
            }
        }
    }

    private fun onLoginReady() {
        updateState { copy(isReady = true) }
        emitEvent(MainUiEvent.LoginSuccess(currentState.hasPermissionFlag))
    }

    private suspend fun fetchFcmToken(): String? =
        runCatching { FirebaseMessaging.getInstance().token.await() }
            .onFailure { exception -> BottariLogger.error(exception.message, exception) }
            .getOrNull()
}
