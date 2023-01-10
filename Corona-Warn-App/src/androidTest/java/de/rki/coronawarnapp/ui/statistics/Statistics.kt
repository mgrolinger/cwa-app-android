package de.rki.coronawarnapp.ui.statistics

import android.content.Context
import android.content.SharedPreferences
import com.google.common.collect.Ordering
import de.rki.coronawarnapp.datadonation.analytics.common.Districts
import de.rki.coronawarnapp.environment.EnvironmentSetup
import de.rki.coronawarnapp.environment.download.DownloadCDNModule
import de.rki.coronawarnapp.http.HttpModule
import de.rki.coronawarnapp.statistics.AddStatsItem
import de.rki.coronawarnapp.statistics.LocalStatisticsData
import de.rki.coronawarnapp.statistics.StatisticsData
import de.rki.coronawarnapp.statistics.StatisticsModule
import de.rki.coronawarnapp.statistics.StatsSequenceItem
import de.rki.coronawarnapp.statistics.local.FederalStateToPackageId
import de.rki.coronawarnapp.statistics.local.source.LocalStatisticsParser
import de.rki.coronawarnapp.statistics.local.source.LocalStatisticsServer
import de.rki.coronawarnapp.statistics.local.storage.LocalStatisticsConfigStorage
import de.rki.coronawarnapp.statistics.local.storage.SelectedLocations
import de.rki.coronawarnapp.statistics.local.storage.SelectedStatisticsLocation
import de.rki.coronawarnapp.statistics.source.StatisticsParser
import de.rki.coronawarnapp.statistics.source.StatisticsServer
import de.rki.coronawarnapp.util.security.SignatureValidation
import de.rki.coronawarnapp.util.serialization.SerializationModule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import java.time.Instant
import retrofit2.converter.jackson.JacksonConverterFactory
import timber.log.Timber

object Statistics {

    private fun loadRealStatisticsData(): StatisticsData {
        val globalStatisticsData = loadGlobalStatisticsData()
        val globalStats = globalStatisticsData?.items ?: emptyList()
        val localStats = loadLocalStatisticsData()?.items ?: emptyList()

        val cardIdSequence = globalStatisticsData?.cardIdSequence.orEmpty()
        val ordering = Ordering.explicit(cardIdSequence.toList())
        val stats = globalStats.plus(localStats)
            .filterIsInstance<StatsSequenceItem>()
            .filter { it.cardType.id in cardIdSequence }
            .sortedWith { a, b -> ordering.compare(a.cardType.id, b.cardType.id) }

        return StatisticsData(
            items = setOf(AddStatsItem(canAddItem = true, isInternetAvailable = true)) + stats
        )
    }

    private fun loadLocalStatisticsData(): LocalStatisticsData? {
        val localStatisticsConfigStorage = mockk<LocalStatisticsConfigStorage>()
        val context = mockk<Context>(relaxed = true)
        val preferences = mockk<SharedPreferences>(relaxed = true)
        val cache = mockk<Cache>(relaxed = true)

        every { preferences.getString(any(), any()) } returns null
        every { context.getSharedPreferences(any(), any()) } returns preferences
        every { localStatisticsConfigStorage.activeSelections } returns flowOf(
            SelectedLocations(
                setOf(
                    SelectedStatisticsLocation.SelectedDistrict(
                        district = Districts.District(
                            districtName = "Potsdam",
                            districtShortName = "P",
                            districtId = 11012054,
                            federalStateName = "Brandenburg",
                            federalStateShortName = "BB",
                            federalStateId = 13000012
                        ),
                        addedAt = Instant.EPOCH
                    )
                )
            )
        )

        val cdnModule = DownloadCDNModule()
        val objectMapper = SerializationModule().jacksonObjectMapper()
        val environmentSetup = EnvironmentSetup(context = context, objectMapper = objectMapper)
        val httpClient = HttpModule().defaultHttpClient()
        val cdnClient = cdnModule.cdnHttpClient(httpClient)
        val url = cdnModule.provideDownloadServerUrl(environmentSetup)
        val signatureValidation = SignatureValidation(environmentSetup)
        val jacksonFactory = JacksonConverterFactory.create()

        val statisticsServer = LocalStatisticsServer(
            api = {
                StatisticsModule.localApi(
                    client = cdnClient,
                    url = url,
                    jacksonConverterFactory = jacksonFactory,
                    cache = cache
                )
            },
            cache = cache,
            signatureValidation = signatureValidation
        )

        return runBlocking {
            try {
                val rawData = statisticsServer.getRawLocalStatistics(FederalStateToPackageId.BB)
                LocalStatisticsParser(localStatisticsConfigStorage).parse(rawData)
            } catch (e: Exception) {
                Timber.e(e, "Can't download local statistics data. Check your internet connection.")
                null
            }
        }
    }

    private fun loadGlobalStatisticsData(): StatisticsData? {
        val context = mockk<Context>(relaxed = true)
        val preferences = mockk<SharedPreferences>(relaxed = true)
        val cache = mockk<Cache>(relaxed = true)

        every { preferences.getString(any(), any()) } returns null
        every { context.getSharedPreferences(any(), any()) } returns preferences

        val cdnModule = DownloadCDNModule()
        val objectMapper = SerializationModule().jacksonObjectMapper()
        val environmentSetup = EnvironmentSetup(context = context, objectMapper = objectMapper)
        val httpClient = HttpModule().defaultHttpClient()
        val cdnClient = cdnModule.cdnHttpClient(httpClient)
        val url = cdnModule.provideDownloadServerUrl(environmentSetup)
        val signatureValidation = SignatureValidation(environmentSetup)
        val jacksonFactory = JacksonConverterFactory.create()

        val statisticsServer = StatisticsServer(
            api = {
                StatisticsModule.api(
                    client = cdnClient,
                    url = url,
                    jacksonConverterFactory = jacksonFactory,
                    cache = cache
                )
            },
            cache = cache,
            signatureValidation = signatureValidation
        )

        return runBlocking {
            try {
                val rawData = statisticsServer.getRawStatistics()
                StatisticsParser().parse(rawData)
            } catch (e: Exception) {
                Timber.e(e, "Can't download global statistics data. Check your internet connection.")
                null
            }
        }
    }

    val statisticsData: StatisticsData = loadRealStatisticsData()
}
