package com.firework.example.trafficaggregator

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.firework.sdk.trafficaggregator.FwTrafficAggregatorSdk
import com.firework.sdk.trafficaggregator.FwTrafficAggregatorSdkConfig

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val sdkConfig = FwTrafficAggregatorSdkConfig.Builder(context = this)
            .setClientId(BuildConfig.FW_CLIENT_ID)
            .setHostUserId("Mohsen") // This parameter is optional.
            .build()

        FwTrafficAggregatorSdk.init(
            sdkConfig = sdkConfig,
            onSuccess = {
                Log.i(TAG, "Traffic Aggregator SDK initialized successfully")
            },
            onError = { error ->
                Log.e(TAG, "Something went wrong with Traffic Aggregator SDK initialization: ${error.message}")
                Toast.makeText(this, "SDK initialization error:\n${error.message}", Toast.LENGTH_LONG).show()
            },
        )
    }

    companion object {
        const val TAG = "Firework SDK"
    }
}
