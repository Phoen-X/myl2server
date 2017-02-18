package com.vvygulyarniy.l2.loginserver.logic;

import com.l2server.network.*;
import com.l2server.network.ServerList.ServerData;
import com.l2server.network.clientpackets.*;
import com.l2server.network.gameserverpackets.ServerStatus;
import com.vvygulyarniy.l2.loginserver.GameServerTable;
import com.vvygulyarniy.l2.loginserver.LoginController;
import com.vvygulyarniy.l2.loginserver.model.data.AccountInfo;

import javax.crypto.Cipher;
import java.net.InetAddress;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import static com.l2server.network.AccountKicked.AccountKickedReason.REASON_PERMANENTLY_BANNED;
import static com.l2server.network.L2LoginClient.LoginClientState.AUTHED_LOGIN;
import static com.l2server.network.LoginFail.LoginFailReason.*;
import static com.l2server.network.PlayFail.PlayFailReason.REASON_SERVER_OVERLOADED;

/**
 * Created by Phoen-X on 16.02.2017.
 */
public class LoginPacketsProcessor implements ClientPacketProcessor {
    private final GameServerTable gameServerTable;

    public LoginPacketsProcessor(GameServerTable gameServerTable) {
        this.gameServerTable = gameServerTable;
    }

    @Override
    public void process(RequestServerList packet, L2LoginClient client) {
        if (client.getSessionKey().checkLoginPair(packet.getSessionKey1(), packet.getSessionKey2())) {

            List<ServerData> servers = Collections.singletonList(new ServerData(client, 1, ServerStatus.STATUS_GOOD, 9999, false, 1, 100, 0));
            client.sendPacket(new ServerList(client, servers));
        } else {
            client.close(REASON_ACCESS_FAILED);
        }
    }

    @Override
    public void process(ProtocolVersion packet, L2LoginClient client) {

    }

    @Override
    public void process(RequestAuthLogin packet, L2LoginClient client) {
        byte[] decrypted = null;
        try {
            final Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
            rsaCipher.init(Cipher.DECRYPT_MODE, client.getRSAPrivateKey());
            decrypted = rsaCipher.doFinal(packet.getRaw(), 0x00, 0x80);
        } catch (GeneralSecurityException e) {

            return;
        }

        try {
            packet.setUser(new String(decrypted, 0x5E, 14).trim().toLowerCase());
            packet.setPassword(new String(decrypted, 0x6C, 16).trim());
        } catch (Exception e) {

            return;
        }

        InetAddress clientAddr = client.getConnection().getInetAddress();

        final LoginController lc = LoginController.getInstance();
        AccountInfo info = lc.retriveAccountInfo(clientAddr, packet.getUser(), packet.getPassword());
        if (info == null) {
            // user or pass wrong
            client.close(REASON_USER_OR_PASS_WRONG);
            return;
        }

        LoginController.AuthLoginResult result = lc.tryCheckinAccount(client, clientAddr, info);
        switch (result) {
            case AUTH_SUCCESS:
                client.setAccount(info.getLogin());
                client.setState(AUTHED_LOGIN);
                client.setSessionKey(lc.assignSessionKeyToClient(info.getLogin(), client));
                List<ServerData> servers = Collections.singletonList(new ServerData(client, 1, ServerStatus.STATUS_GOOD, 9999, false, 1, 100, 0));
                client.sendPacket(new ServerList(client, servers));
                break;
            case INVALID_PASSWORD:
                client.close(REASON_USER_OR_PASS_WRONG);
                break;
            case ACCOUNT_BANNED:
                client.close(new AccountKicked(REASON_PERMANENTLY_BANNED));
                return;
            case ALREADY_ON_LS:
                L2LoginClient oldClient = lc.getAuthedClient(info.getLogin());
                if (oldClient != null) {
                    // kick the other client
                    oldClient.close(REASON_ACCOUNT_IN_USE);
                    lc.removeAuthedLoginClient(info.getLogin());
                }
                // kick also current client
                client.close(REASON_ACCOUNT_IN_USE);
                break;
        }
    }

    @Override
    public void process(RequestServerLogin packet, L2LoginClient client) {
        SessionKey sk = client.getSessionKey();

        // if we didnt showed the license we cant check these values
        if (sk.checkLoginPair(packet.getSessionKey1(), packet.getSessionKey2())) {
            if (LoginController.getInstance().isLoginPossible(client, packet.getServerID())) {
                client.setJoinedGS(true);
                client.sendPacket(new PlayOk(sk));
            } else {
                client.close(REASON_SERVER_OVERLOADED);
            }
        } else {
            client.close(REASON_ACCESS_FAILED);
        }
    }

    @Override
    public void process(AuthGameGuard packet, L2LoginClient client) {
        if (packet.getSessionId() == client.getSessionId()) {
            client.setState(L2LoginClient.LoginClientState.AUTHED_GG);
            client.sendPacket(new GGAuth(client.getSessionId()));
        } else {
            client.close(REASON_ACCESS_FAILED);
        }
    }
}
