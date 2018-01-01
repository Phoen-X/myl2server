package com.vvygulyarniy.l2.loginserver.security

import org.slf4j.LoggerFactory


class LoginServerAuthManager {

    fun authenricate(login: String, password: String): Boolean {
        log.info("Trying to log in user [$login / $password]")
        return true
    }

    companion object {
        val log = LoggerFactory.getLogger("LoginServerAuthManager")!!
    }
}