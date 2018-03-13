package com.vvygulyarniy.l2.gameserver.auth

import com.google.common.eventbus.Subscribe
import com.vvygulyarniy.l2.gameserver.account.AccountRepository
import com.vvygulyarniy.l2.gameserver.domain.AccountId
import com.vvygulyarniy.l2.gameserver.events.AuthRequested
import com.vvygulyarniy.l2.gameserver.events.LobbyEnterRequested
import com.vvygulyarniy.l2.gameserver.events.UserEventBus
import com.vvygulyarniy.l2.gameserver.session.SessionManager


class AuthManager(private val sessionManager: SessionManager,
                  private val accountRepository: AccountRepository,
                  private val eventBus: UserEventBus) {
    init {
        eventBus.register(this)
    }

    @Subscribe
    fun handleAuthRequest(event: AuthRequested) {
        val session = sessionManager.getSession(event.sessionId)

        if (session != null) {
            //TODO this is fake accountId
            val account = accountRepository.find(AccountId(1))
            if (account != null) {
                session.accountId = account.id
                eventBus.post(LobbyEnterRequested(session.sessionId, account.id))
            }

        }
    }
}