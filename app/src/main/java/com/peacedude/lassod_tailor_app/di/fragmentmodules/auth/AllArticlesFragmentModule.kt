package com.peacedude.lassod_tailor_app.di.fragmentmodules.auth

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.resources.AllArticlesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AllArticlesFragmentModule {
    /**
     * A abstract function inject All articles Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideAllArticlesFragment(): AllArticlesFragment
}
