package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.SingleChatFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class SingleChatFragmentModule {
    /**
     * A abstract function inject Single chat Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideSingleChatFragment(): SingleChatFragment

}