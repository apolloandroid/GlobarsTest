package com.example.globarstest.di

import com.example.globarstest.ui.authorization.AuthorizationFragment
import com.example.globarstest.ui.map.MapFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectAuthorizationFragment(authorizationFragment: AuthorizationFragment)
    fun injectMapsFragment(mapFragment: MapFragment)
}