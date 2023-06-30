package com.raphaelmrci.circlebar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raphaelmrci.circlebar.network.SocketHandler

class PreparingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preparing)

        val mSocket = SocketHandler.getSocket()

        mSocket.on("finished") {
            SocketHandler.removeAllSocketEvents()
            val intent = Intent(this@PreparingActivity, FinishedActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
            finish()
        }

        mSocket.emit("ready")
    }

    override fun onBackPressed() {
    }
}