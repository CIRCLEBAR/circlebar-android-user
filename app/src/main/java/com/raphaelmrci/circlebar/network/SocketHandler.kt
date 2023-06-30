package com.raphaelmrci.circlebar.network

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {
    lateinit var mSocket: Socket
    private var uid: String? = null

    @Synchronized
    fun setSocket(m: String) {
        try {
            mSocket = IO.socket("http://$m:3000")

            mSocket.on("addr") {
                mSocket.emit("addr", uid)
            }

            mSocket.on("setAddr") { args ->
                uid = args[0] as String
                Log.d("SOCKETCONF", uid!!)
            }
        } catch (e: URISyntaxException) {
            throw e
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun removeAllSocketEvents() {
        mSocket.off()
        mSocket.on("addr") {
            mSocket.emit("addr", uid)
        }

        mSocket.on("setAddr") { args ->
            uid = args[0] as String
            Log.d("SOCKETCONF", uid!!)
        }
    }
}