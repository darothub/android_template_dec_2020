package com.peacedude.lassod_tailor_app.di.fragmentmodules.customer

import com.peacedude.lassod_tailor_app.ui.*
import com.peacedude.lassod_tailor_app.ui.customer.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SearchFragmentModule {
    /**
     * A abstract function inject Search Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideSearchFragment(): SearchFragment
}
