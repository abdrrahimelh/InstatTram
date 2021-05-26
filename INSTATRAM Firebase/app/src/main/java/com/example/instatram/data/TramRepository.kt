package com.example.instatram.data
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.instatram.ui.main.WEB_SERVICE_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
class TramRepository(val app: Application) {
    val TramData = MutableLiveData<List<Tram>>()
    // 3. Appeler la méthode callWebService() en background en mode nonbloquant (via Coroutines)
    init {
        CoroutineScope(Dispatchers.IO).launch {
            callWebService()
        }
    }

    @WorkerThread
    suspend fun callWebService() {
        if (networkAvailable()) {
            val retrofit = Retrofit.Builder()
                .baseUrl(WEB_SERVICE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            val service = retrofit.create(TramService::class.java)
            val serviceData = service.getTramData().body() ?:
            emptyList()
            TramData.postValue(serviceData)
        }
    }
    // 1. Vérifier si la connexion Internet est en marche ou pas
    @Suppress("DEPRECATION")
    private fun networkAvailable(): Boolean {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }
}
