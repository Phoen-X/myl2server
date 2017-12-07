/*
 * Copyright (C) 2004-2015 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vvygulyarniy.l2.gameserver.network.packet.client;

import com.vvygulyarniy.l2.gameserver.network.L2GameClient;
import com.vvygulyarniy.l2.gameserver.network.packet.L2ClientPacketProcessor;
import lombok.ToString;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.ByteBuffer;

/**
 * This packet manages the trade request.
 */
@ToString
public final class TradeRequest extends L2GameClientPacket {
    private int _objectId;

    public TradeRequest(ByteBuffer buf) {
        super(buf);
    }

    @Override
    protected void readImpl() {
        _objectId = readD();
    }

    /*@Override
    protected void runImpl() {
        L2PcInstance player = getActiveChar();
        if (player == null) {
            return;
        }

        if (!player.getAccessLevel().allowTransaction()) {
            player.sendMessage("Transactions are disabled for your current Access Level.");
            send(ActionFailed.STATIC_PACKET);
            return;
        }

        BuffInfo info = player.getEffectList().getBuffInfoByAbnormalType(AbnormalType.BOT_PENALTY);
        if (info != null) {
            for (AbstractEffect effect : info.getEffects()) {
                if (!effect.checkCondition(BotReportTable.TRADE_ACTION_BLOCK_ID)) {
                    player.send(SystemMessageId.YOU_HAVE_BEEN_REPORTED_SO_ACTIONS_NOT_ALLOWED);
                    player.send(ActionFailed.STATIC_PACKET);
                    return;
                }
            }
        }

        final L2Object target = L2World.getInstance().findObject(_objectId);
        // If there is no target, target is far away or
        // they are in different instances (except multiverse)
        // trade request is ignored and there is no system message.
        if ((target == null) || !player.getKnownList().knowsObject(target) || ((target.getInstanceId() != player.getInstanceId()) && (player.getInstanceId() != -1))) {
            return;
        }

        // If target and acting player are the same, trade request is ignored
        // and the following system message is sent to acting player.
        if (target.getObjectId() == player.getObjectId()) {
            player.send(SystemMessageId.TARGET_IS_INCORRECT);
            return;
        }

        if (!target.isPlayer()) {
            player.send(SystemMessageId.INCORRECT_TARGET);
            return;
        }

        final L2PcInstance partner = target.getActingPlayer();
        if (partner.isInOlympiadMode() || player.isInOlympiadMode()) {
            player.sendMessage("A user currently participating in the Olympiad cannot accept or request a trade.");
            return;
        }

        info = partner.getEffectList().getBuffInfoByAbnormalType(AbnormalType.BOT_PENALTY);
        if (info != null) {
            for (AbstractEffect effect : info.getEffects()) {
                if (!effect.checkCondition(BotReportTable.TRADE_ACTION_BLOCK_ID)) {
                    final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_REPORTED_AND_IS_BEING_INVESTIGATED);
                    sm.addCharName(partner);
                    player.send(sm);
                    player.send(ActionFailed.STATIC_PACKET);
                    return;
                }
            }
        }

        // L2J Customs: Karma punishment
        if (!Config.ALT_GAME_KARMA_PLAYER_CAN_TRADE && (player.getKarma() > 0)) {
            player.sendMessage("You cannot trade while you are in a chaotic state.");
            return;
        }

        if (!Config.ALT_GAME_KARMA_PLAYER_CAN_TRADE && (partner.getKarma() > 0)) {
            player.sendMessage("You cannot request a trade while your target is in a chaotic state.");
            return;
        }

        if (Config.JAIL_DISABLE_TRANSACTION && (player.isJailed() || partner.isJailed())) {
            player.sendMessage("You cannot trade while you are in in Jail.");
            return;
        }

        if ((player.getPrivateStoreType() != PrivateStoreType.NONE) || (partner.getPrivateStoreType() != PrivateStoreType.NONE)) {
            player.send(SystemMessageId.CANNOT_TRADE_DISCARD_DROP_ITEM_WHILE_IN_SHOPMODE);
            return;
        }

        if (player.isProcessingTransaction()) {
            if (Config.DEBUG) {
                _log.fine("Already trading with someone else.");
            }
            player.send(SystemMessageId.ALREADY_TRADING);
            return;
        }

        SystemMessage sm;
        if (partner.isProcessingRequest() || partner.isProcessingTransaction()) {
            if (Config.DEBUG) {
                _log.info("Transaction already in progress.");
            }
            sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_BUSY_TRY_LATER);
            sm.addString(partner.getName());
            player.send(sm);
            return;
        }

        if (partner.getTradeRefusal()) {
            player.sendMessage("That person is in trade refusal mode.");
            return;
        }

        if (BlockList.isBlocked(partner, player)) {
            sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ADDED_YOU_TO_IGNORE_LIST);
            sm.addCharName(partner);
            player.send(sm);
            return;
        }

        if (player.calculateDistance(partner, true, false) > 150) {
            player.send(SystemMessageId.TARGET_TOO_FAR);
            return;
        }

        player.onTransactionRequest(partner);
        partner.send(new SendTradeRequest(player.getObjectId()));
        sm = SystemMessage.getSystemMessage(SystemMessageId.REQUEST_C1_FOR_TRADE);
        sm.addString(partner.getName());
        player.send(sm);
    }
    */
    @Override
    public void process(L2ClientPacketProcessor processor, L2GameClient client) {
        throw new NotImplementedException();
    }
}
