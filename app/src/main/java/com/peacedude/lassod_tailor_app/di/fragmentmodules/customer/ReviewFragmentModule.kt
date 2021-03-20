package com.peacedude.lassod_tailor_app.di.fragmentmodules.customer

import com.peacedude.lassod_tailor_app.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ReviewFragmentModule {
    /**
     * A abstract function inject Review Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideReviewFragment(): ReviewFragment
}
