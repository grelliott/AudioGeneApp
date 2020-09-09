package ca.grantelliott.audiogeneapp

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class AudiogeneApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(MaskNoisyLibsTree())
    }
}

class MaskNoisyLibsTree : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (tag!! == "Activator" && priority <= Log.DEBUG) {
            return;
        }
        super.log(priority, tag, message, t)
    }
}