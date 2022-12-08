package com.firework.example.trafficaggregator

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.firework.example.trafficaggregator.databinding.ActivityMainBinding
import com.firework.sdk.trafficaggregator.PaymentHandler
import com.firework.sdk.trafficaggregator.PaymentResult

class MainActivity : AppCompatActivity(), PaymentHandler {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.trafficAggregatorView.init(
            aggregatorUrl = URL,
            paymentHandler = this,
            onSuccess = {
                Log.i(TAG, "Traffic Aggregator View initialized successfully")
            },
            onError = { error ->
                Log.e(TAG, "Something went wrong with Traffic Aggregator View initialization: ${error.message}")
                Toast.makeText(this, "View initialization error:\n${error.message}", Toast.LENGTH_LONG).show()
            },
        )
    }

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

    companion object {
        const val TAG = "Firework View"
        private const val URL = "https://alien.boutir.com/p/3"
        private const val SUCCESS_URL = "https://alien.boutir.com/p/success"
        private const val FAILURE_URL = "https://alien.boutir.com/p/failure"
    }
}
