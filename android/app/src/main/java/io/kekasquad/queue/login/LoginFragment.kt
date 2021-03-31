package io.kekasquad.queue.login

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.kekasquad.queue.R
import io.kekasquad.queue.base.BaseFragment
import io.kekasquad.queue.base.MviViewModel

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginViewState, LoginIntent>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_sign_in
    override val viewModel: LoginViewModel by viewModels()

    override fun backStackIntent(): LoginIntent {
        TODO("Not yet implemented")
    }

    override fun initialIntent(): LoginIntent? {
        TODO("Not yet implemented")
    }

    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun render(viewState: LoginViewState) {
        TODO("Not yet implemented")
    }
}