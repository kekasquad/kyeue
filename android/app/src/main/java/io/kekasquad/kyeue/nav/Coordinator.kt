package io.kekasquad.kyeue.nav

import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.scopes.ActivityScoped
import io.kekasquad.kyeue.R
import io.kekasquad.kyeue.data.usecase.AuthUseCase
import io.kekasquad.kyeue.ui.queue.QueueFragment
import javax.inject.Inject

interface Coordinator {
    fun navigateToLogin()
    fun navigateToQueues()
    fun navigateToQueue(queueId: String)
    fun start()
    fun pop()
}

@ActivityScoped
class CoordinatorImpl @Inject constructor(
    @ActivityScoped activity: FragmentActivity,
    private val authUseCase: AuthUseCase
) : Coordinator {
    private val fragmentManager = activity.supportFragmentManager

    override fun navigateToLogin() {
//        fragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, LoginFragment())
//            .commitAllowingStateLoss()
    }

    override fun navigateToQueues() {
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, QueueFragment())
            .commitAllowingStateLoss()
    }

    override fun navigateToQueue(queueId: String) {
//        fragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, QueueFragment.newInstance(
//                queueId
//            ))
//            .addToBackStack(queueId)
//            .commitAllowingStateLoss()
    }

    override fun start() {
        if (authUseCase.isTokenExists()) {
            navigateToQueues()
        } else {
            navigateToLogin()
        }
    }

    override fun pop() {
        fragmentManager.popBackStack()
    }

    fun clear() {
        repeat(fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStack()
        }
    }

}