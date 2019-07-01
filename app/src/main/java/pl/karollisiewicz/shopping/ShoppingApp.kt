package pl.karollisiewicz.shopping

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import pl.karollisiewicz.shopping.di.appModule
import pl.karollisiewicz.shopping.di.dataModule
import pl.karollisiewicz.shopping.di.databaseModule

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ShoppingApp)
            modules(listOf(appModule, dataModule, databaseModule))
        }
    }
}