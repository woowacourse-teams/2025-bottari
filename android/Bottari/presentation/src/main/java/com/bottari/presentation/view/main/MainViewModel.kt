package com.bottari.presentation.view.main

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.usecase.CommonUseCaseProvider
import com.bottari.di.usecase.MemberUseCaseProvider
import com.bottari.domain.model.exception.BottariException
import com.bottari.domain.model.exception.onApiError
import com.bottari.domain.model.exception.onApiException
import com.bottari.domain.model.exception.onSuccess
import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.usecase.appConfig.CheckForceUpdateUseCase
import com.bottari.domain.usecase.appConfig.GetPermissionFlagUseCase
import com.bottari.domain.usecase.appConfig.SavePermissionFlagUseCase
import com.bottari.domain.usecase.fcm.SaveFcmTokenUseCase
import com.bottari.domain.usecase.member.CheckRegisteredMemberUseCase
import com.bottari.domain.usecase.member.RegisterMemberUseCase
import com.bottari.logger.BottariLogger
import com.bottari.presentation.BuildConfig
import com.bottari.presentation.common.base.BaseViewModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class MainViewModel(
    private val registerMemberUseCase: RegisterMemberUseCase,
    private val checkRegisteredMemberUseCase: CheckRegisteredMemberUseCase,
    private val savePermissionFlagUseCase: SavePermissionFlagUseCase,
    private val saveFcmTokenUseCase: SaveFcmTokenUseCase,
    private val getPermissionFlagUseCase: GetPermissionFlagUseCase,
    private val checkForceUpdateUseCase: CheckForceUpdateUseCase,
) : BaseViewModel<MainUiState, MainUiEvent>(MainUiState()) {
    init {
        checkForceUpdate()
    }

    fun checkRegisteredMember() {
        updateState { copy(isLoading = true) }

        launch {
            checkRegisteredMemberUseCase()
                .onSuccess { result -> handleCheckRegistrationResult(result) }
                .onApiError { emitEvent(MainUiEvent.AuthorizeFailure) }
        }
    }

    fun savePermissionFlag() {
        launch { savePermissionFlagUseCase(true) }
    }

    private fun checkPermissionFlag() {
        launch {
            getPermissionFlagUseCase()
                .onSuccess { permissionFlag -> handlePermissionFlag(permissionFlag) }
                .onApiError { handlePermissionFlag(false) }
        }
    }

    private fun handleCheckRegistrationResult(result: RegisteredMember) {
        if (result.isRegistered) return saveFcmToken()
        registerMember()
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

    private fun registerMember() {
        launch {
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            registerMemberUseCase(fcmToken)
                .onSuccess {
                    updateState { copy(isLoading = false, isReady = true) }
                    emitEvent(MainUiEvent.LoginSuccess(currentState.hasPermissionFlag))
                }.onApiException { bottariException ->
                    when (bottariException) {
                        is BottariException.InvalidException -> emitEvent(MainUiEvent.RegisterFailure.InvalidException)
                        is BottariException.DuplicatedException -> emitEvent(MainUiEvent.RegisterFailure.DuplicatedException)
                        else -> emitEvent(MainUiEvent.RegisterFailure.UnexpectedException)
                    }
                }.onApiError { emitEvent(MainUiEvent.RegisterFailure.UnexpectedException) }
        }
    }

    private fun saveFcmToken() {
        launch {
            val fcmToken = FirebaseMessaging.getInstance().token.await()
            saveFcmTokenUseCase(fcmToken)
                .onApiError { throwable -> BottariLogger.error(throwable.message, throwable) }
                .onApiException { exception -> BottariLogger.error(exception.message, exception) }

            updateState { copy(isLoading = false, isReady = true) }
            emitEvent(MainUiEvent.LoginSuccess(currentState.hasPermissionFlag))
        }
    }

    private fun checkForceUpdate() {
        if (BuildConfig.DEBUG) return checkPermissionFlag()
        updateState { copy(isLoading = true) }

        launch {
            checkForceUpdateUseCase(BuildConfig.APP_VERSION_CODE)
                .onSuccess { isForceUpdate ->
                    if (isForceUpdate) return@onSuccess emitEvent(MainUiEvent.ForceUpdate)
                    checkPermissionFlag()
                }.onApiError { throwable -> BottariLogger.error(throwable.message, throwable) }

            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MainViewModel(
                        MemberUseCaseProvider.registerMemberUseCase,
                        MemberUseCaseProvider.checkRegisteredMemberUseCase,
                        CommonUseCaseProvider.savePermissionFlagUseCase,
                        CommonUseCaseProvider.saveFcmTokenUseCase,
                        CommonUseCaseProvider.getPermissionFlagUseCase,
                        CommonUseCaseProvider.checkForceUpdateUseCase,
                    )
                }
            }
    }
}
