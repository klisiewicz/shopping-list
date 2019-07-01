package pl.karollisiewicz.shopping

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import pl.karollisiewicz.shopping.di.appModule

class ShoppingApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@ShoppingApp)
            modules(appModule)
        }
    }
}