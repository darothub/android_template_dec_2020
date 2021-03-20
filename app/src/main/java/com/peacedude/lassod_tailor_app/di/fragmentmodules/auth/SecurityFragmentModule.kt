package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.profile.SecurityFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SecurityFragmentModule {
    /**
     * A abstract function inject Security Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideSecurityFragment(): SecurityFragment
}
