package com.peacedude.lassod_tailor_app.di.fragmentmodules.customer

import com.peacedude.lassod_tailor_app.ui.customer.FavouritesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FavouriteFragmentModule {
    /**
     * A abstract function inject Favourites Fragment from DaggerGraph
     */
    @ContributesAndroidInjector()
    abstract fun provideFavouritesFragment(): FavouritesFragment

}