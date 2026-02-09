package com.michaelrmossman.aucklandmuseum3api.data

import android.content.Context
import com.michaelrmossman.aucklandmuseum3api.database.MuseumDatabase
import com.michaelrmossman.aucklandmuseum3api.network.MuseumApiService
import com.michaelrmossman.aucklandmuseum3api.util.DEBUG_JSON_ADDITIONAL_MESSAGES
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_DATA_AUTH
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_DATA_TYPE
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_SEARCH_URL
import com.michaelrmossman.aucklandmuseum3api.util.MUSEUM_TYPE_KEY
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface AppContainer {

    val database: MuseumDatabase

    val favesRepository: FavouritesRepository

    val historyRepository: HistoryRepository

    val networkObjectsRepository: NetworkObjectsRepository

    val networkPersonsRepository: NetworkPersonsRepository

    val settingsRepository: SettingsRepository
}

class DefaultAppContainer(
    private val apiKey: String, context: Context
) : AppContainer {

//    val appModule = SerializersModule {
//        polymorphic (Reference::class) {
//            subclass(Reference.Object::class)
//            subclass(Reference.Person::class)
//        }
//    }
    val json = Json {
        // serializersModule = appModule
        // classDiscriminator = MUSEUM_CLASS_DISCRIMINATOR /* "_type" */
        ignoreUnknownKeys = false // TODO
    }
    /* This logging implementation shows logcat messages similar to the
       old Retrofit method, e.g. setLogLevel(RestAdapter.LogLevel.FULL) */
    @Suppress("KotlinConstantConditions")
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        when (DEBUG_JSON_ADDITIONAL_MESSAGES) {
            true -> setLevel(HttpLoggingInterceptor.Level.BODY)
            else -> setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(object: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request = chain.request()
                val urlIntercept = request.url.toString()
                val urlReplaced = urlIntercept
                    .replace("%2B","+")
                    .replace("M%C4%81ori","Māori")
                val newRequest = Request
                    .Builder()
                    .url(urlReplaced)
                    .header(MUSEUM_TYPE_KEY, MUSEUM_DATA_TYPE)
                    .header(MUSEUM_DATA_AUTH, apiKey)
//                    .header(MUSEUM_CONTENT_KEY, MUSEUM_CONTENT_TYPE)
                    .build()
                return chain.proceed(newRequest)
            }
        })
        .addInterceptor(loggingInterceptor)
        .build()
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(
            json.asConverterFactory(
                MUSEUM_DATA_TYPE.toMediaType()
            )
        )
        .baseUrl(MUSEUM_SEARCH_URL)
        .client(client)
        .build()
    private val retrofitService: MuseumApiService by lazy {
        retrofit.create(MuseumApiService::class.java)
    }

    override val database = MuseumDatabase.getDatabase(context)

    override val historyRepository: HistoryRepository by lazy {
        HistoryRepository(database.historyDao())
    }

    override val favesRepository: FavouritesRepository by lazy {
        FavouritesRepository(
            database.favouritesDao(), database.settingsDao()
        )
    }

    override val networkObjectsRepository: NetworkObjectsRepository by lazy {
        NetworkObjectsRepository(retrofitService)
    }

    override val networkPersonsRepository: NetworkPersonsRepository by lazy {
        NetworkPersonsRepository(retrofitService)
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(database.settingsDao())
    }
}