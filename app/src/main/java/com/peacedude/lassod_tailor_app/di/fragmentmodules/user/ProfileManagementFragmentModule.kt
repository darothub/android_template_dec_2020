package com.peacedude.lassod_tailor_app.di.fragmentmodules.user

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.profile.ProfileManagementFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ProfileManagementFragmentModule {
    /**
     * A abstract function inject Profile Management Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideProfileManagementFragmentFragment(): ProfileManagementFragment
}