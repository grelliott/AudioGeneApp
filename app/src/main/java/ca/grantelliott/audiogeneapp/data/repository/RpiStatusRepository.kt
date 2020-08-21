package ca.grantelliott.audiogeneapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ca.grantelliott.audiogeneapp.data.api.RpiStatus
import ca.grantelliott.audiogeneapp.data.api.RpiWebservice
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RpiStatusRepository {
    //TODO Assuming I can use some Dependency Injection here...
    private val webservice: RpiWebservice = Retrofit.Builder()
        .baseUrl("http://localhost")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(RpiWebservice::class.java)

    fun getStatus(): LiveData<RpiStatus> {
        val data = MutableLiveData<RpiStatus>()
        webservice.getStatus().enqueue(object: Callback<RpiStatus> {
            override fun onResponse(call: Call<RpiStatus>, response: Response<RpiStatus>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<RpiStatus>, t: Throwable) {
                // set Status to No Connection
                val rpiStatus = RpiStatus("Network Error")
                data.value = rpiStatus
            }
        })
        return data
    }
}