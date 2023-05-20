package com.raphaelmrci.circlebar

import android.content.Context
import android.content.Intent
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.raphaelmrci.circlebar.network.ApiClient
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class StartupActivity : AppCompatActivity() {
    private var isFound = false

    private lateinit var dropAnim: Animation
    private lateinit var circleAnim: Animation
    private lateinit  var rotateAnim: Animation
    private lateinit var circleAnimEnd: Animation
    private lateinit var bounceAnim: Animation

    private lateinit var circle: ImageView
    private lateinit var drop: ImageView

    private lateinit var nsdManager :NsdManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        circle = findViewById(R.id.circleLogo)
        drop = findViewById(R.id.dropLogo)

        dropAnim = AnimationUtils.loadAnimation(this@StartupActivity, R.anim.startup_drop)
        circleAnim = AnimationUtils.loadAnimation(this@StartupActivity, R.anim.startup_circle)
        rotateAnim = AnimationUtils.loadAnimation(this@StartupActivity, R.anim.rotate)
        circleAnimEnd = AnimationUtils.loadAnimation(this@StartupActivity, R.anim.startup_circle_end)
        bounceAnim = AnimationUtils.loadAnimation(this@StartupActivity, R.anim.bounce)

        Executors.newSingleThreadScheduledExecutor().schedule({
            if (isFound) {
                circle.startAnimation(circleAnimEnd)
                val intent = Intent(this@StartupActivity, HomeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fadein, R.anim.fadeout)
            } else {
                launchAnimations()
            }
        }, circleAnim.duration, TimeUnit.MILLISECONDS)
        drop.startAnimation(dropAnim)
        circle.startAnimation(circleAnim)

        nsdManager = getSystemService(Context.NSD_SERVICE) as NsdManager

        val serviceType = "_http._tcp."
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)

    }

    private fun launchAnimations() {
        Executors.newSingleThreadScheduledExecutor().schedule({
            if (isFound) {
                Executors.newSingleThreadScheduledExecutor().schedule({
                    val intent = Intent(this@StartupActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }, circleAnimEnd.duration, TimeUnit.MILLISECONDS)
                circle.startAnimation(circleAnimEnd)
            } else {
                launchAnimations()
            }
        }, rotateAnim.duration, TimeUnit.MILLISECONDS)
        circle.startAnimation(rotateAnim)
        drop.startAnimation(bounceAnim)
    }



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
                nsdManager.resolveService(serviceInfo, object : NsdManager.ResolveListener {
                    override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                        // Handle service resolve failure
                    }

                    override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
                        val serviceName = serviceInfo.serviceName
                        val serviceAddress = serviceInfo.host.hostAddress
                        val serverPort = serviceInfo.port // Obtain the server port

                        if (serviceName == "circlebar") {
                            ApiClient.BASE_URL = "http://$serviceAddress:$serverPort/"
                            isFound = true
                        }
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
        nsdManager.stopServiceDiscovery(discoveryListener)

    }


    companion object {
        private const val TAG = "STARTUP"
    }
}