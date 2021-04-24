package io.kekasquad.queue.login

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.kekasquad.queue.R
import io.kekasquad.queue.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewState, LoginIntent>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModels()

    private var isLastStateLogin: Boolean? = null

    override fun backStackIntent(): LoginIntent = LoginIntent.LoginNothingIntent

    override fun initialIntent(): LoginIntent = LoginIntent.LoginNothingIntent

    override fun initViews() {
    }

    override fun render(viewState: LoginViewState) {
        if (isLastStateLogin != viewState.isLogin) {
            if (viewState.isLogin) {
                btn_sign_in.setOnClickListener {
                    _intentLiveData.value = LoginIntent.LoginUserIntent(
                        username = input_edit_login.text.toString(),
                        password = input_edit_password.text.toString()
                    )
                }
                btn_sign_up.setOnClickListener {
                    _intentLiveData.value = LoginIntent.SwitchToRegisterUserIntent
                }
            } else {
                btn_sign_in.setOnClickListener {
                    _intentLiveData.value = LoginIntent.SwitchToLoginUserIntent
                }
                btn_sign_up.setOnClickListener {
                    _intentLiveData.value = LoginIntent.RegisterUserIntent(
                        username = input_edit_login.text.toString(),
                        password = input_edit_password.text.toString(),
                        firstName = input_edit_first_name.text.toString(),
                        lastName = input_edit_last_name.text.toString(),
                        isTeacher = user_permissions_group.checkedButtonId == R.id.user_permission_teacher
                    )
                }
            }
            input_layout_first_name.isVisible = !viewState.isLogin
            input_layout_last_name.isVisible = !viewState.isLogin
            user_permissions_group.isVisible = !viewState.isLogin
            isLastStateLogin = viewState.isLogin
            
            clearInputs()
        }
    }
    
    private fun clearInputs() {
        input_edit_login.setText("")
        input_edit_password.setText("")
        input_edit_first_name.setText("")
        input_edit_last_name.setText("")
        user_permissions_group.check(R.id.user_permission_student)
    }

}