package com.vvygulyarniy.l2.loginserver

import java.net.InetAddress
import java.net.UnknownHostException
import java.util.*

/**
 * Phoen-X on 05.03.2017.
 */
object Test {
    @Throws(UnknownHostException::class)
    @JvmStatic fun main(args: Array<String>) {
        val addr = InetAddress.getByName("localhost")
        println(Arrays.toString(addr.address))
    }
}
