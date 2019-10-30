package com.deer.retrofitapisample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.deer.retrofitapisample.obj.LogResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        get.setOnClickListener {
            getLoginItem()
        }
    }

    private var loginGetCall: Call<LogResponse>? = null
    private fun getLoginItem() {
        val endpointService
                = EndPointGenerator.getApiEndpoint(EndpointService.LogInService::class.java)
        loginGetCall = endpointService.logItem
        loginGetCall?.enqueue(object : ApiRequestCallBack<LogResponse>() {
            override fun onApiSuccess(call: Call<LogResponse>?, response: LogResponse?) {
                if(response != null && response.data != null)
                    updateViews(response.data)
            }

            override fun onAnyFailure(call: Call<LogResponse>?, t: Throwable?, errorCode: String?) {
                //TODO do the fail catch
            }
        })
    }

    private fun updateViews(data: LogResponse.Data) {
        uuid.text = data.uuid
        time.text = data.timeStamp.toString()
        lat.text = data.lat.toString()
        lng.text = data.lng.toString()
    }
}
