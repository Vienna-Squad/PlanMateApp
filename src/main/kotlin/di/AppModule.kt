package di

import org.example.common.APPS.ADMIN_APP
import org.example.common.APPS.AUTH_APP
import org.example.common.APPS.MATE_APP
import org.example.presentation.AdminApp
import org.example.presentation.App
import org.example.presentation.AuthApp
import org.example.presentation.MateApp
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<App>(named(AUTH_APP)) { AuthApp() }
    single<App>(named(ADMIN_APP)) { AdminApp() }
    single<App>(named(MATE_APP)) { MateApp() }
}