package com.raphaelmrci.circlebar

import android.content.Context
import android.content.Intent
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.raphaelmrci.circlebar.network.ApiClient
import com.raphaelmrci.circlebar.network.SocketHandler
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
                changeActivity()
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

    private fun changeActivity() {
        drop.clearAnimation()
        circle.clearAnimation()
        val circleAnimation = AnimationSet(false)
        val dropAnimation = AnimationSet(false)
        val rotate1 = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate1.duration = 450
        rotate1.repeatCount = 0
        rotate1.repeatMode = Animation.RESTART
        rotate1.setInterpolator(this@StartupActivity, android.R.anim.linear_interpolator)
        circleAnimation.addAnimation(rotate1)

        val finalRot = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        finalRot.startOffset = 450
        finalRot.duration = 550
        finalRot.setInterpolator(this@StartupActivity, android.R.anim.decelerate_interpolator)
        circleAnimation.addAnimation(finalRot)

        val trans1 = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT,
            0.0f,
            Animation.RELATIVE_TO_PARENT,
            0f,
            Animation.RELATIVE_TO_PARENT,
            0.0f,
            Animation.RELATIVE_TO_PARENT,
            -0.876f
        )
        trans1.duration = 900
        trans1.setInterpolator(this@StartupActivity, android.R.anim.accelerate_decelerate_interpolator)
        circleAnimation.addAnimation(trans1)
        dropAnimation.addAnimation(trans1)

        val scale = ScaleAnimation(
            1f,
            0.45f,
            1f,
            0.45f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        scale.setInterpolator(this@StartupActivity, android.R.anim.accelerate_decelerate_interpolator)
        scale.duration = 900
        circleAnimation.addAnimation(scale)
        dropAnimation.addAnimation(scale)
                Executors.newSingleThreadScheduledExecutor().schedule({
                    val intent = Intent(this@StartupActivity, HomeActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }, circleAnimEnd.duration, TimeUnit.MILLISECONDS)

        circleAnimation.fillAfter = true
        dropAnimation.fillAfter = true
        circle.startAnimation(circleAnimation)
        drop.startAnimation(dropAnimation)
    }

    private fun launchAnimations() {
        Executors.newSingleThreadScheduledExecutor().schedule({
            if (isFound) {
                changeActivity()
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
                            SocketHandler.setSocket(serviceAddress)
                            val mSocket = SocketHandler.getSocket()
                            mSocket.connect()
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