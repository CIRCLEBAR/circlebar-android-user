package com.raphaelmrci.circlebar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

class StartupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        val circle : ImageView = findViewById(R.id.circleLogo)
        val drop : ImageView = findViewById(R.id.dropLogo)

        val fadeIn : Animation = AnimationUtils.loadAnimation(this@StartupActivity, R.anim.fadein)
        val rotate : Animation = AnimationUtils.loadAnimation(this@StartupActivity, R.anim.rotate)
        val bounce : Animation = AnimationUtils.loadAnimation(this@StartupActivity, R.anim.bounce)

        drop.startAnimation(fadeIn)
        circle.startAnimation(rotate)
    }
}