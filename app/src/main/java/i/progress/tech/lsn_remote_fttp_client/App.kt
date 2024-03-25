package i.progress.tech.lsn_remote_fttp_client

import android.app.Application
import android.content.Context

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
    companion object{
        lateinit var appContext: Context
    }
}