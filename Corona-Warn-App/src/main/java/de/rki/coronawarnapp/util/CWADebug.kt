package de.rki.coronawarnapp.util

import android.app.Application
import android.os.Build
import de.rki.coronawarnapp.BuildConfig
import de.rki.coronawarnapp.bugreporting.debuglog.DebugLogger
import de.rki.coronawarnapp.util.di.ApplicationComponent
import timber.log.Timber

object CWADebug {

    fun init(application: Application) {
        if (isDebugBuildOrMode) System.setProperty("kotlinx.coroutines.debug", "on")

        if (isDeviceForTestersBuild) {
            Timber.plant(Timber.DebugTree())
        }

        DebugLogger.init(application)

        logDeviceInfos()
    }

    fun initAfterInjection(component: ApplicationComponent) {
        DebugLogger.setInjectionIsReady(component)
    }

    val isDebugBuildOrMode: Boolean
        get() = BuildConfig.DEBUG || buildFlavor == BuildFlavor.DEVICE_FOR_TESTERS

    val buildFlavor: BuildFlavor
        get() = BuildFlavor.values().single { it.rawValue == BuildConfig.FLAVOR }

    val isDeviceForTestersBuild: Boolean = buildFlavor == BuildFlavor.DEVICE_FOR_TESTERS

    enum class BuildFlavor(val rawValue: String) {
        DEVICE("device"),
        DEVICE_FOR_TESTERS("deviceForTesters")
    }

    val isAUnitTest: Boolean by lazy {
        try {
            Class.forName("testhelpers.IsAUnitTest")
            true
        } catch (e: Exception) {
            false
        }
    }

    fun logDeviceInfos() {
        Timber.i("CWA version: %s (%s)", BuildConfig.VERSION_CODE, BuildConfig.GIT_COMMIT_SHORT_HASH)
        Timber.i("CWA flavor: %s (%s)", BuildConfig.FLAVOR, BuildConfig.BUILD_TYPE)
        Timber.i("Build.FINGERPRINT: %s", Build.FINGERPRINT)
    }
}
