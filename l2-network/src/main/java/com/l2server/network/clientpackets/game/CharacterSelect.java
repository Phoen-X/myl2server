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
package com.l2server.network.clientpackets.game;

import com.l2server.network.GameServerPacketProcessor;
import com.l2server.network.L2GameClient;
import lombok.Getter;

/**
 * This class ...
 *
 * @version $Revision: 1.5.2.1.2.5 $ $Date: 2005/03/27 15:29:30 $
 */
public class CharacterSelect extends L2GameClientPacket {
    // cd
    @Getter
    private int slotId;

    @SuppressWarnings("unused")
    private int unk1; // new in C4
    @SuppressWarnings("unused")
    private int unk2; // new in C4
    @SuppressWarnings("unused")
    private int unk3; // new in C4
    @SuppressWarnings("unused")
    private int unk4; // new in C4

    @Override
    protected void readImpl() {
        slotId = readD();
        unk1 = readH();
        unk2 = readD();
        unk3 = readD();
        unk4 = readD();
    }

   /* @Override
    protected void runImpl() {
        final L2GameClient client = getClient();
        if (!client.getFloodProtectors().getCharacterSelect().tryPerformAction("CharacterSelect")) {
            return;
        }

        if (SecondaryAuthData.getInstance().isEnabled() && !client.getSecondaryAuth().isAuthed()) {
            client.getSecondaryAuth().openDialog();
            return;
        }

        // We should always be able to acquire the lock
        // But if we can't lock then nothing should be done (i.e. repeated packet)
        if (client.getActiveCharLock().tryLock()) {
            try {
                // should always be null
                // but if not then this is repeated packet and nothing should be done here
                if (client.getActiveChar() == null) {
                    final CharSelectInfoPackage info = client.getCharSelection(slotId);
                    if (info == null) {
                        return;
                    }

                    // Banned?
                    if (PunishmentManager.getInstance().hasPunishment(info.getObjectId(), PunishmentAffect.CHARACTER, PunishmentType.BAN) || PunishmentManager.getInstance().hasPunishment(client.getAccountName(), PunishmentAffect.ACCOUNT, PunishmentType.BAN) || PunishmentManager.getInstance().hasPunishment(client.getConnectionAddress().getHostAddress(), PunishmentAffect.IP, PunishmentType.BAN)) {
                        client.close(ServerClose.STATIC_PACKET);
                        return;
                    }

                    // Selected character is banned (compatibility with previous versions).
                    if (info.getAccessLevel() < 0) {
                        client.close(ServerClose.STATIC_PACKET);
                        return;
                    }

                    if ((Config.L2JMOD_DUALBOX_CHECK_MAX_PLAYERS_PER_IP > 0) && !AntiFeedManager.getInstance().tryAddClient(AntiFeedManager.GAME_ID, client, Config.L2JMOD_DUALBOX_CHECK_MAX_PLAYERS_PER_IP)) {
                        final NpcHtmlMessage msg = new NpcHtmlMessage();
                        msg.setFile(info.getHtmlPrefix(), "data/html/mods/IPRestriction.htm");
                        msg.replace("%max%", String.valueOf(AntiFeedManager.getInstance().getLimit(client, Config.L2JMOD_DUALBOX_CHECK_MAX_PLAYERS_PER_IP)));
                        client.sendPacket(msg);
                        return;
                    }

                    // The L2PcInstance must be created here, so that it can be attached to the L2GameClient
                    if (Config.DEBUG) {
                        _log.fine("selected slot:" + slotId);
                    }

                    // load up character from disk
                    final L2PcInstance cha = client.loadCharFromDisk(slotId);
                    if (cha == null) {
                        return; // handled in L2GameClient
                    }
                    L2World.getInstance().addPlayerToWorld(cha);
                    CharNameTable.getInstance().addName(cha);

                    cha.setClient(client);
                    client.setActiveChar(cha);
                    cha.setOnlineStatus(true, true);

                    final TerminateReturn terminate = EventDispatcher.getInstance().notifyEvent(new OnPlayerSelect(cha, cha.getObjectId(), cha.getName(), getClient()), Containers.Players(), TerminateReturn.class);
                    if ((terminate != null) && terminate.terminate()) {
                        cha.deleteMe();
                        return;
                    }

                    sendPacket(new SSQInfo());

                    client.setState(GameClientState.IN_GAME);
                    CharSelected cs = new CharSelected(cha, client.getSessionId().playOkID1);
                    sendPacket(cs);
                }
            } finally {
                client.getActiveCharLock().unlock();
            }

            LogRecord record = new LogRecord(Level.INFO, "Logged in");
            record.setParameters(new Object[]
                    {
                            client
                    });
            _logAccounting.log(record);
        }
    }*/

    @Override
    public void process(GameServerPacketProcessor processor, L2GameClient client) {
        processor.process(this, client);
    }
}
