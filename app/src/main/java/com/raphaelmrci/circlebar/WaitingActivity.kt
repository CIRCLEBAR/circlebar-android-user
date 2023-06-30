package com.raphaelmrci.circlebar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.raphaelmrci.circlebar.network.SocketHandler
import io.socket.client.Socket

class WaitingActivity : AppCompatActivity() {

    private lateinit var mSocket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        val cancelBtn = findViewById<Button>(R.id.cancelBtn)

        val cocktailID = intent.getIntExtra("cocktailID", -1)

        mSocket = SocketHandler.getSocket()

        if (!mSocket.connected())
            mSocket.connect()

        cancelBtn.setOnClickListener {
            mSocket.emit("cancel")
            SocketHandler.removeAllSocketEvents()
            finish()
        }

        mSocket.on("unauthorized") {
            runOnUiThread {
                Toast
                    .makeText(this@WaitingActivity, "Unauthorized command...", Toast.LENGTH_LONG)
                    .show()
                SocketHandler.removeAllSocketEvents()
                finish()
            }
        }

        mSocket.on("unavailable") {
            runOnUiThread {
                Toast
                    .makeText(this@WaitingActivity, "This cocktail is unavailable.", Toast.LENGTH_LONG)
                    .show()
                SocketHandler.removeAllSocketEvents()
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
                SocketHandler.removeAllSocketEvents()
                startActivity(intent)
                finish()
            }
        }

        if (cocktailID == -1) {
            Toast
                .makeText(this@WaitingActivity, "Error while loading the cocktail...", Toast.LENGTH_LONG)
                .show()
            SocketHandler.removeAllSocketEvents()
            finish()
        } else {
            mSocket.emit("command", cocktailID)
        }
    }

    override fun onBackPressed() {
        mSocket.emit("cancel")
        SocketHandler.removeAllSocketEvents()
        finish()
    }

    companion object {
        private const val TAG = "WAITING"
    }
}