package common.di

import org.example.common.Constants
import org.example.presentation.AdminApp
import org.example.presentation.App
import org.example.presentation.AuthApp
import org.example.presentation.MateApp
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<App>(named(Constants.APPS.AUTH_APP)) { AuthApp() }
    single<App>(named(Constants.APPS.ADMIN_APP)) { AdminApp() }
    single<App>(named(Constants.APPS.MATE_APP)) { MateApp() }
}