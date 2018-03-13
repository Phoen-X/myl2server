package com.vvygulyarniy.l2.gameserver.session

import com.l2server.network.communication.SessionId
import com.vvygulyarniy.l2.gameserver.domain.AccountId
import java.time.LocalDateTime


data class GameSession(val sessionId: SessionId, val startTime: LocalDateTime, var accountId: AccountId? = null)