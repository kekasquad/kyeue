package io.kekasquad.kyeue.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import io.kekasquad.kyeue.R
import io.kekasquad.kyeue.base.BaseActivity
import io.kekasquad.kyeue.nav.Coordinator
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BaseActivity<MainActivityViewState, MainActivityIntent, MainActivityNavigationEvent>() {
    override val layoutResourceId: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var coordinator: Coordinator

    override fun backStackIntent(): MainActivityIntent =
        MainActivityIntent.MainActivityNothingIntent

    override fun initialIntent(): MainActivityIntent = MainActivityIntent.InitialIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun initViews() {

    }

    override fun render(viewState: MainActivityViewState) {

    }

    override fun navigator(navigationEvent: MainActivityNavigationEvent) {
        when (navigationEvent) {
            MainActivityNavigationEvent.NavigateToLoginEvent -> coordinator.navigateToLogin()
            MainActivityNavigationEvent.NavigateToQueueEvent -> coordinator.navigateToQueue()
        }
    }
}