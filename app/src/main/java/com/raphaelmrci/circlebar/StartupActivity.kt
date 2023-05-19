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

        val animation : Animation = AnimationUtils.loadAnimation(this@StartupActivity, R.anim.rotate)

        circle.startAnimation(animation)
    }
}