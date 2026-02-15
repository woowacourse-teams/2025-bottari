package com.bottari.bottari.view.splash

import com.bottari.bottari.BuildConfig
import com.bottari.core.domain.network.NetworkManager
import com.bottari.core.domain.usecase.appConfig.CheckForceUpdateUseCase
import com.bottari.core.domain.usecase.appConfig.GetPermissionFlagUseCase
import com.bottari.core.domain.usecase.appConfig.SavePermissionFlagUseCase
import com.bottari.core.domain.usecase.fcm.SaveFcmTokenUseCase
import com.bottari.core.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.core.domain.usecase.member.RegisterMemberUseCase
import com.bottari.core.ui.base.FlowBaseViewModel
import com.bottari.logger.BottariLogger
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    networkManager: NetworkManager,
    private val registerMemberUseCase: RegisterMemberUseCase,
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
    private val savePermissionFlagUseCase: SavePermissionFlagUseCase,
    private val saveFcmTokenUseCase: SaveFcmTokenUseCase,
    private val getPermissionFlagUseCase: GetPermissionFlagUseCase,
    private val checkForceUpdateUseCase: CheckForceUpdateUseCase,
) : FlowBaseViewModel<SplashUiState, SplashUiEvent>(SplashUiState()) {
    private val isConnected: Boolean = networkManager.isConnected.value

    init {
        initializeApp()
    }

    fun savePermissionFlag() {
        launch {
            savePermissionFlagUseCase(true)
                .onFailure { emitEvent(SplashUiEvent.SavePermissionFlagFailure) }
        }
    }

    fun checkRegisteredMember() {
        if (isConnected.not()) {
            emitEvent(SplashUiEvent.Offline(false))
            return
        }

        launch {
            checkRegisteredMemberUseCase()
                .onSuccess { result ->
                    if (result.isRegistered) {
                        saveFcmToken()
                        return@launch
                    }
                    registerMember()
                }.onFailure { emitEvent(SplashUiEvent.LoginFailure) }
        }
    }

    private fun initializeApp() =
        when {
            isConnected.not() -> handleOffline()
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
                            SplashUiEvent.Offline(true)
                        } else {
                            SplashUiEvent.IncompletePermissionFlow
                        },
                    )
                }.onFailure { emitEvent(SplashUiEvent.GetPermissionFlagFailure) }
        }
    }

    private fun checkPermissionFlag() {
        launch {
            getPermissionFlagUseCase()
                .onSuccess { permissionFlag ->
                    updateState { copy(hasPermissionFlag = permissionFlag) }
                    if (!permissionFlag) {
                        updateState { copy(isReady = true) }
                        emitEvent(SplashUiEvent.IncompletePermissionFlow)
                        return@launch
                    }
                    checkRegisteredMember()
                }.onFailure { emitEvent(SplashUiEvent.GetPermissionFlagFailure) }
        }
    }

    private fun handleForceUpdate() {
        launch {
            checkForceUpdateUseCase(BuildConfig.APP_VERSION_CODE)
                .onSuccess { isForceUpdate ->
                    if (isForceUpdate) {
                        emitEvent(SplashUiEvent.ForceUpdate)
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
                        emitEvent(SplashUiEvent.RegisterFailure)
                    }
            }
        }
    }

    private fun onLoginReady() {
        updateState { copy(isReady = true) }
        emitEvent(SplashUiEvent.LoginSuccess(currentState.hasPermissionFlag))
    }

    private suspend fun fetchFcmToken(): String? =
        runCatching { FirebaseMessaging.getInstance().token.await() }
            .onFailure { exception -> BottariLogger.error(exception.message, exception) }
            .getOrNull()
}
