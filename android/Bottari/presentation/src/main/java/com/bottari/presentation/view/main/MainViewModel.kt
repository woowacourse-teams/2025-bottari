package com.bottari.presentation.view.main

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.bottari.di.UseCaseProvider
import com.bottari.domain.model.member.RegisteredMember
import com.bottari.domain.usecase.appConfig.CheckForceUpdateUseCase
import com.bottari.domain.usecase.appConfig.GetPermissionFlagUseCase
import com.bottari.domain.usecase.appConfig.SavePermissionFlagUseCase
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
                .onSuccess { result ->
                    handleCheckRegistrationResult(result)
                }.onFailure { emitEvent(MainUiEvent.LoginFailure) }
        }
    }

    fun savePermissionFlag() {
        launch {
            savePermissionFlagUseCase(true)
                .onFailure { emitEvent(MainUiEvent.SavePermissionFlagFailure) }
        }
    }

    private fun checkPermissionFlag() {
        launch {
            getPermissionFlagUseCase()
                .onSuccess { permissionFlag -> handlePermissionFlag(permissionFlag) }
                .onFailure { emitEvent(MainUiEvent.GetPermissionFlagFailure) }
        }
    }

    private fun handleCheckRegistrationResult(result: RegisteredMember) {
        if (result.isRegistered) {
            updateState { copy(isLoading = false, isReady = true) }
            emitEvent(MainUiEvent.LoginSuccess(currentState.hasPermissionFlag))
            return
        }
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
                }.onFailure { emitEvent(MainUiEvent.RegisterFailure) }
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
                }.onFailure { exception -> BottariLogger.error(exception.message, exception) }

            updateState { copy(isLoading = false) }
        }
    }

    companion object {
        fun Factory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    MainViewModel(
                        UseCaseProvider.registerMemberUseCase,
                        UseCaseProvider.checkRegisteredMemberUseCase,
                        UseCaseProvider.savePermissionFlagUseCase,
                        UseCaseProvider.getPermissionFlagUseCase,
                        UseCaseProvider.checkForceUpdateUseCase,
                    )
                }
            }
    }
}
