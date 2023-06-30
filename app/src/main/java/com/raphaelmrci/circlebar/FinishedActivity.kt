package com.raphaelmrci.circlebar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raphaelmrci.circlebar.network.SocketHandler

class FinishedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished)

        val mSocket = SocketHandler.getSocket()

        mSocket.on("glass") { args ->
            val isGlass = args[0] as Int

            if (isGlass == 0) {
                SocketHandler.removeAllSocketEvents()
                finish()
            }
        }

        mSocket.emit("glass")
    }

    override fun onBackPressed() {

    }
}