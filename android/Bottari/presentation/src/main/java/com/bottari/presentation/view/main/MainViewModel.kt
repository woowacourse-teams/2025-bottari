package com.bottari.presentation.view.main

import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.usecase.appConfig.CheckForceUpdateUseCase
import com.bottari.domain.usecase.appConfig.GetPermissionFlagUseCase
import com.bottari.domain.usecase.appConfig.SavePermissionFlagUseCase
import com.bottari.domain.usecase.fcm.SaveFcmTokenUseCase
import com.bottari.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.domain.usecase.member.RegisterMemberUseCase
import com.bottari.logger.BottariLogger
import com.bottari.presentation.BuildConfig
import com.bottari.presentation.common.base.FlowBaseViewModel
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val registerMemberUseCase: RegisterMemberUseCase,
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
    private val savePermissionFlagUseCase: SavePermissionFlagUseCase,
    private val saveFcmTokenUseCase: SaveFcmTokenUseCase,
    private val getPermissionFlagUseCase: GetPermissionFlagUseCase,
    private val checkForceUpdateUseCase: CheckForceUpdateUseCase,
) : FlowBaseViewModel<MainUiState, MainUiEvent>(MainUiState()) {
    init {
        checkForceUpdate()
    }

    fun savePermissionFlag() {
        launch {
            savePermissionFlagUseCase(true)
                .onFailure { emitEvent(MainUiEvent.SavePermissionFlagFailure) }
        }
    }

    fun checkRegisteredMember() {
        launch {
            checkRegisteredMemberUseCase()
                .onSuccess(::handleCheckRegistrationResult)
                .onFailure { emitEvent(MainUiEvent.LoginFailure) }
        }
    }

    private fun checkForceUpdate() {
        if (BuildConfig.DEBUG) {
            checkPermissionFlag()
            return
        }

        launch {
            checkForceUpdateUseCase(BuildConfig.APP_VERSION_CODE)
                .onSuccess { isForceUpdate ->
                    if (isForceUpdate) {
                        updateState { copy(isReady = true) }
                        emitEvent(MainUiEvent.ForceUpdate)
                        return@onSuccess
                    }
                    checkPermissionFlag()
                }.onFailure { exception -> BottariLogger.error(exception.message, exception) }
        }
    }

    private fun checkPermissionFlag() {
        launch {
            getPermissionFlagUseCase()
                .onSuccess(::handlePermissionFlag)
                .onFailure { emitEvent(MainUiEvent.GetPermissionFlagFailure) }
        }
    }

    private fun handlePermissionFlag(permissionFlag: Boolean) {
        updateState { copy(hasPermissionFlag = permissionFlag) }

        if (!permissionFlag) {
            updateState { copy(isReady = true) }
            emitEvent(MainUiEvent.IncompletePermissionFlow)
            return
        }

        checkRegisteredMember()
    }

    private fun handleCheckRegistrationResult(result: RegisteredMember) {
        if (result.isRegistered) {
            saveFcmToken()
            return
        }
        registerMember()
    }

    private fun registerMember() {
        launch {
            val fcmToken = fetchFcmToken()
            if (fcmToken == null) {
                emitEvent(MainUiEvent.RegisterFailure)
                return@launch
            }

            registerMemberUseCase(fcmToken)
                .onSuccess { onLoginReady() }
                .onFailure { emitEvent(MainUiEvent.RegisterFailure) }
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

    private fun onLoginReady() {
        updateState { copy(isReady = true) }
        emitEvent(MainUiEvent.LoginSuccess(currentState.hasPermissionFlag))
    }

    private suspend fun fetchFcmToken(): String? =
        runCatching { FirebaseMessaging.getInstance().token.await() }
            .onFailure { exception -> BottariLogger.error(exception.message, exception) }
            .getOrNull()
}
