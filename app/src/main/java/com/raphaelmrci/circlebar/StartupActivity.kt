package com.raphaelmrci.circlebar

import android.content.Context
import android.content.Intent
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.raphaelmrci.circlebar.network.ApiClient

class StartupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        val circle: ImageView = findViewById(R.id.circleLogo)
        val drop: ImageView = findViewById(R.id.dropLogo)

        val fadeIn: Animation = AnimationUtils.loadAnimation(this@StartupActivity, R.anim.fadein)
        val rotate: Animation = AnimationUtils.loadAnimation(this@StartupActivity, R.anim.rotate)

        drop.startAnimation(fadeIn)
        circle.startAnimation(rotate)

        nsdManager = getSystemService(Context.NSD_SERVICE) as NsdManager

        val serviceType = "_http._tcp." // Adjust the service type according to your Express.js configuration
        nsdManager?.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)

    }

    var nsdManager :NsdManager? = null


    private val discoveryListener = object : NsdManager.DiscoveryListener {
        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            // Handle start discovery failure
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            // Handle stop discovery failure
        }

        override fun onDiscoveryStarted(serviceType: String) {
            // Discovery started
        }

        override fun onDiscoveryStopped(serviceType: String) {
            // Discovery stopped
        }

        override fun onServiceFound(serviceInfo: NsdServiceInfo) {
            // Service found
            if (serviceInfo.serviceType == "_http._tcp.") { // Adjust the service type according to your Express.js configuration
                // Resolve the service to obtain the server address
                nsdManager?.resolveService(serviceInfo, object : NsdManager.ResolveListener {
                    override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                        // Handle service resolve failure
                    }

                    override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                        val serviceName = serviceInfo.serviceName
                        val serviceAddress = serviceInfo.host.hostAddress
                        val serverPort = serviceInfo.port // Obtain the server port

                        if (serviceName == "circlebar") {
                            ApiClient.BASE_URL = "http://$serviceAddress:$serverPort/"
                            val intent = Intent(this@StartupActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        // Use the serverAddress and serverPort to connect to your Express.js server
                        // You can store this information or directly make the API calls from here
                    }
                })
            }
        }

        override fun onServiceLost(serviceInfo: NsdServiceInfo) {
            // Service lost
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        nsdManager?.stopServiceDiscovery(discoveryListener)

    }


    companion object {
        private const val TAG = "STARTUP"
    }
}