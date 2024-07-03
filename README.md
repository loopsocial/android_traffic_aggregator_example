# Android Traffic Aggregator SDK example app
This repository holds Firework Android Traffic Aggregator SDK Example Application designed for an easier integration experience.
This document provides step-by-step guidance for Android Traffic Aggregator SDK integration.

## Download
The SDK is available on Jitpack repository. You have to first add this repository to your project if you don't have it already:
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
```

and then add this dependency to your application gradle:
```kotlin
// Firework Android Traffic Aggregator SDK
val fireworkSdkVersion = "1.0.0-beta.6"
implementation("com.firework.embed:traffic-aggregator:$fireworkSdkVersion")
```

## Min requirements
The min Android API supported by the SDK is 21 and it is targeting API 33.
```groovy
compileSdk = 31
minSdk = 19
targetSdk = 31
```

## SDK initialization
The SDK has an initialization phase that should be added to a place like the app Application class.
```kotlin
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
            .setUserId(BuildConfig.FW_USER_ID)
            .build()

        FwTrafficAggregatorSdk.init(
            sdkConfig = sdkConfig,
            onSuccess = {
                Log.i(TAG, "Firework Traffic Aggregator SDK initialized successfully")
            },
            onError = { error ->
                Log.e(TAG, "Something went wrong with Firework Traffic Aggregator SDK initialization: ${error.message}")
                Toast.makeText(this, "SDK initialization error:\n${error.message}", Toast.LENGTH_LONG).show()
            },
        )
    }

    companion object {
        const val TAG = "Firework SDK"
    }
}
```

The `FwTrafficAggregatorSdk` init method requires a configuration that can be generated using `FwTrafficAggregatorSdkConfig.Builder`.

You should set your `clientId` and optional `hostUserID`. The `hostUserID` is the user ID from your app which can be used for fetching the OAuth authentication token. You can also update the `hostUserId` at runtime in case of user login or logout using `FwTrafficAggregatorSdk.updateHostUserId` method.

The `FwTrafficAggregatorSdk.init` function gets two callbacks for success and error cases. They can be used for debugging, error handling, and for informing the host app about successful SDK initialization.

## View initialization
Whenever you are ready to open the Boutir URL, you can initialize this view and add it to your layout programmatically or make it visible if you already had it in your XML layout:
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.firework.sdk.trafficaggregator.FwTrafficAggregatorView
        android:id="@+id/trafficAggregatorView"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</androidx.constraintlayout.widget.ConstraintLayout>

```

This view requires initialization and it will inform success/error cases for error handling.
```kotlin
binding.trafficAggregatorView.init(
    aggregatorUrl = URL,
    paymentHandler = this,
    onSuccess = {
        Log.i(TAG, "Firework Traffic Aggregator View initialized successfully")
    },
    onError = { error ->
        Log.e(TAG, "Something went wrong with Firework Traffic Aggregator View initialization: ${error.message}")
        Toast.makeText(this, "View initialization error:\n${error.message}", Toast.LENGTH_LONG).show()
    },
)
```

The `TrafficAggregatorView.init` method receives the aggregator URL, a payment handler callback, and success/error callbacks for debugging and error handling.

## Payment callback
Whenever it is time for the Octopus API to handle the payment, the `TrafficAggregatorView` will call the `handlePayment` method of the `PaymentHandler` callback and provide the payment URL.
Also, this method provides `onPaymentDone` function which should be called as soon as the payment is done, with a `PaymentResult` holding a confirmation URL which is later shown on the `TrafficAggregatorView`.
```kotlin
override fun handlePayment(paymentUrl: String, onPaymentDone: (PaymentResult) -> Unit) {
    // handle the payment and call [onPaymentDone] with proper URL
    AlertDialog.Builder(this)
        .setTitle(R.string.payment_result_title)
        .setMessage(R.string.payment_result_message)
        .setPositiveButton(R.string.yes) { _, _ ->
            onPaymentDone(PaymentResult(confirmationUrl = SUCCESS_URL))
        }
        .setNegativeButton(R.string.no) { _, _ ->
            onPaymentDone(PaymentResult(confirmationUrl = FAILURE_URL))
        }
        .show()
}
```

## Proguard
The SDK contains a consumer Gradle rule which makes sure everything works fine after the host app minification and obfuscation, and no extra rules is required.
