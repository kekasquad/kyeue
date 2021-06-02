package io.kekasquad.kyeue.nav

import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.scopes.ActivityScoped
import io.kekasquad.kyeue.R
import io.kekasquad.kyeue.ui.login.LoginFragment
import io.kekasquad.kyeue.ui.queue.QueueFragment
import io.kekasquad.kyeue.ui.queuedetails.QueueDetailsFragment
import javax.inject.Inject

interface Coordinator {
    fun navigateToLogin()
    fun navigateToQueue()
    fun navigateToQueueDetails(queueId: String)
    fun pop()
}

@ActivityScoped
class CoordinatorImpl @Inject constructor(
    @ActivityScoped private val activityContext: FragmentActivity
) : Coordinator {
    private val fragmentManager = activityContext.supportFragmentManager

    override fun navigateToLogin() {
        clear()
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }

    override fun navigateToQueue() {
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, QueueFragment())
            .commit()
    }

    override fun navigateToQueueDetails(queueId: String) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, QueueDetailsFragment.newInstance(queueId))
            .addToBackStack(queueId)
            .commit()
    }

    override fun pop() {
        fragmentManager.popBackStack()
    }

    private fun clear() {
        repeat(fragmentManager.backStackEntryCount) {
            fragmentManager.popBackStack()
        }
    }

}