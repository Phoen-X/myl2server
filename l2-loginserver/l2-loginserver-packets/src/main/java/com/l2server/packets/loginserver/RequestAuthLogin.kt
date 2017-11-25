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
package com.l2server.packets.loginserver


import com.l2server.network.login.L2LoginClient

/**
 * <pre>
 * Format: x
 * 0 (a leading null)
 * x: the rsa encrypted block with the login an password.

 * <pre>
</pre></pre> */
class RequestAuthLogin : L2LoginClientPacket() {
    val raw = ByteArray(128)
    var user: String? = null
    var password: String? = null
    val oneTimePassword: Int = 0

    public override fun readImpl(): Boolean {
        if (super.buffer!!.remaining() >= 128) {
            readB(raw)
            return true
        }
        return false
    }

    override fun process(processor: ClientPacketProcessor, client: L2LoginClient) {
        processor.process(this, client)
    }

    /*@Override
    public void run() {
        byte[] decrypted = null;
        final L2LoginClient client = getClient();
        try {
            final Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
            rsaCipher.init(Cipher.DECRYPT_MODE, client.getRSAPrivateKey());
            decrypted = rsaCipher.doFinal(raw, 0x00, 0x80);
        } catch (GeneralSecurityException e) {

            return;
        }

        try {
            user = new String(decrypted, 0x5E, 14).trim().toLowerCase();
            password = new String(decrypted, 0x6C, 16).trim();
            _ncotp = decrypted[0x7c];
            _ncotp |= decrypted[0x7d] << 8;
            _ncotp |= decrypted[0x7e] << 16;
            _ncotp |= decrypted[0x7f] << 24;
        } catch (Exception e) {

            return;
        }

        InetAddress clientAddr = getClient().getConnection().getInetAddress();

        final LoginController lc = LoginController.getInstance();
        AccountInfo info = lc.retriveAccountInfo(clientAddr, user, password);
        if (info == null) {
            // user or pass wrong
            client.close(LoginFail.LoginFailReason.REASON_USER_OR_PASS_WRONG);
            return;
        }

        LoginController.AuthLoginResult result = lc.tryCheckinAccount(client, clientAddr, info);
        switch (result) {
            case AUTH_SUCCESS:
                client.setAccount(info.getLogin());
                client.setState(L2LoginClient.LoginClientState.AUTHED_LOGIN);
                client.setSessionKey(lc.assignSessionKeyToClient(info.getLogin(), client));
                getClient().sendPacket(new ServerList(getClient()));
                break;
            case INVALID_PASSWORD:
                client.close(LoginFail.LoginFailReason.REASON_USER_OR_PASS_WRONG);
                break;
            case ACCOUNT_BANNED:
                client.close(new AccountKicked(AccountKicked.AccountKickedReason.REASON_PERMANENTLY_BANNED));
                return;
            case ALREADY_ON_LS:
                L2LoginClient oldClient = lc.getAuthedClient(info.getLogin());
                if (oldClient != null) {
                    // kick the other client
                    oldClient.close(LoginFail.LoginFailReason.REASON_ACCOUNT_IN_USE);
                    lc.removeAuthedLoginClient(info.getLogin());
                }
                // kick also current client
                client.close(LoginFail.LoginFailReason.REASON_ACCOUNT_IN_USE);
                break;
            case ALREADY_ON_GS:
                *//*GameServerTable.GameServerInfo gsi = lc.getAccountOnGameServer(info.getLogin());
                if (gsi != null) {
                    client.close(LoginFail.LoginFailReason.REASON_ACCOUNT_IN_USE);

                    // kick from there
                    if (gsi.isAuthed()) {
                        *//**//*gsi.getGameServerThread().kickPlayer(info.getLogin());*//**//*
                    }
                }*//*
                break;
        }
    }*/
}
