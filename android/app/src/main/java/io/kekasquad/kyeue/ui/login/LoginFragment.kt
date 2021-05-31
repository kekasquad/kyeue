package io.kekasquad.kyeue.ui.login

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.kekasquad.kyeue.base.BaseFragment
import io.kekasquad.kyeue.nav.Coordinator
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewState, LoginIntent, LoginNavigationEvent>() {
    override val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var coordinator: Coordinator

    override fun backStackIntent(): LoginIntent = LoginIntent.LoginNothingIntent

    override fun initialIntent(): LoginIntent = LoginIntent.OpenLoginIntent

    override val render: @Composable (LoginViewState) -> Unit = { viewState ->
        LoginContent(
            viewState = viewState,
            onEnterLoginMode = { _intentLiveData.value = LoginIntent.OpenLoginIntent },
            onEnterSignUpMode = { _intentLiveData.value = LoginIntent.OpenSignUpIntent },
            onUsernameChange = { _intentLiveData.value = LoginIntent.InputUsernameIntent(it) },
            onPasswordChange = { _intentLiveData.value = LoginIntent.InputPasswordIntent(it) },
            onFirstNameChange = {
                _intentLiveData.value = LoginIntent.InputFirstNameIntent(it)
            },
            onLastNameChange = { _intentLiveData.value = LoginIntent.InputLastNameIntent(it) },
            onUserIsTeacherChange = {
                _intentLiveData.value = LoginIntent.ChooseUserIsTeacherIntent(it)
            },
            onLogin = { _intentLiveData.value = LoginIntent.PerformLoginIntent },
            onSignUp = { _intentLiveData.value = LoginIntent.PerformSignUpIntent }
        )
    }

    override fun navigator(navigationEvent: LoginNavigationEvent) {
        when (navigationEvent) {
            LoginNavigationEvent.NavigateToQueue -> coordinator.navigateToQueue()
        }
    }


}