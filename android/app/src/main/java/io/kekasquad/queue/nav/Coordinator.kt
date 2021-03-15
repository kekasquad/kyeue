package io.kekasquad.queue.nav

import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

interface Coordinator {
}

@ActivityScoped
class CoordinatorImpl @Inject constructor(
    @ActivityScoped activity: FragmentActivity
) : Coordinator {
    private val fragmentManager = activity.supportFragmentManager

}