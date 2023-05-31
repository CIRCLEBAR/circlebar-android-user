package com.raphaelmrci.circlebar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.raphaelmrci.circlebar.network.SocketHandler

class WaitingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        val cancelBtn = findViewById<Button>(R.id.cancelBtn)

        val cocktailID = intent.getIntExtra("cocktailID", -1)

        val mSocket = SocketHandler.getSocket()

        if (!mSocket.connected())
            mSocket.connect()

        cancelBtn.setOnClickListener {
            mSocket.emit("cancel")
            finish()
        }

        mSocket.on("unauthorized") {
            runOnUiThread {
                Toast
                    .makeText(this@WaitingActivity, "Unauthorized command...", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        }

        mSocket.on("unavailable") {
            runOnUiThread {
                Toast
                    .makeText(this@WaitingActivity, "This cocktail is unavailable.", Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        }

        mSocket.on("command") { args ->
            Log.d(TAG, args[0].toString())
        }

        mSocket.on("ready") {
            runOnUiThread {
                val intent = Intent(this, PutGlassActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }
        }

        if (cocktailID == -1) {
            Toast
                .makeText(this@WaitingActivity, "Error while loading the cocktail...", Toast.LENGTH_LONG)
                .show()
            finish()
        } else {
            mSocket.emit("command", cocktailID)
        }
    }

    companion object {
        private const val TAG = "WAITING"
    }
}