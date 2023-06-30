package com.raphaelmrci.circlebar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.raphaelmrci.circlebar.network.SocketHandler

class PutGlassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_put_glass)

        val letsGoBtn = findViewById<Button>(R.id.letsGoBtn)
        val cancelBtn = findViewById<Button>(R.id.cancelBtn)

        val socket = SocketHandler.getSocket()

        socket.on("glass") { args ->
            val isGlass = args[0] as Int
            runOnUiThread {
                letsGoBtn.isEnabled = isGlass != 0
                if (isGlass == 0)
                    Toast
                        .makeText(this@PutGlassActivity, "Please place your glass in CIRCLEBAR", Toast.LENGTH_SHORT)
                        .show()
            }
        }

        socket.on("preparing") {
            SocketHandler.removeAllSocketEvents()
            val intent = Intent(this@PutGlassActivity, PreparingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)
            finish()
        }

        socket.emit("glass")

        cancelBtn.setOnClickListener {
            socket.emit("cancel")
            SocketHandler.removeAllSocketEvents()
            finish()
        }

        letsGoBtn.setOnClickListener {
            socket.emit("ready")
        }
    }

    override fun onBackPressed() {
    }
}