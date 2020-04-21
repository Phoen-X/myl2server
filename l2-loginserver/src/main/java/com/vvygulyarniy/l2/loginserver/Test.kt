package com.vvygulyarniy.l2.loginserver

import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.util.*

/**
 * Phoen-X on 05.03.2017.
 */
object Test {
    @Throws(UnknownHostException::class)
    @JvmStatic fun main(args: Array<String>) {
        val addr = InetAddress.getByName("127.0.0.1")
        println(Arrays.toString(addr.address))

        Socket(addr, 2106).getOutputStream().write(13)
    }
}
