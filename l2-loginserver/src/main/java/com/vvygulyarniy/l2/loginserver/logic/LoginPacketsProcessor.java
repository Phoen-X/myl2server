package com.vvygulyarniy.l2.loginserver.logic;

import com.l2server.network.ClientPacketProcessor;
import com.l2server.network.L2LoginClient;
import com.l2server.network.SessionKey;
import com.l2server.network.clientpackets.game.ProtocolVersion;
import com.l2server.network.clientpackets.login.AuthGameGuard;
import com.l2server.network.clientpackets.login.RequestAuthLogin;
import com.l2server.network.clientpackets.login.RequestServerList;
import com.l2server.network.clientpackets.login.RequestServerLogin;
import com.l2server.network.serverpackets.login.*;
import com.l2server.network.serverpackets.login.ServerList.ServerData;
import com.vvygulyarniy.l2.loginserver.GameServerTable;
import com.vvygulyarniy.l2.loginserver.LoginController;
import com.vvygulyarniy.l2.loginserver.model.data.AccountInfo;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.l2server.network.L2LoginClient.LoginClientState.AUTHED_LOGIN;
import static com.l2server.network.serverpackets.login.AccountKicked.AccountKickedReason.REASON_PERMANENTLY_BANNED;
import static com.l2server.network.serverpackets.login.LoginFail.LoginFailReason.*;
import static com.l2server.network.serverpackets.login.PlayFail.PlayFailReason.REASON_SERVER_OVERLOADED;

/**
 * Created by Phoen-X on 16.02.2017.
 */
@Slf4j
public class LoginPacketsProcessor implements ClientPacketProcessor {
    private final GameServerTable gameServerTable;

    public LoginPacketsProcessor(GameServerTable gameServerTable) {
        this.gameServerTable = gameServerTable;
    }

    @Override
    public void process(RequestServerList packet, L2LoginClient client) {
        if (client.getSessionKey().checkLoginPair(packet.getSessionKey1(), packet.getSessionKey2())) {
            try {
                InetAddress gameServerAddr = InetAddress.getByName("l2-gameserver");
                byte[] serverIp = Arrays.copyOf(gameServerAddr.getAddress(), 4);
                log.info("GameServer IP resolved: {}", Arrays.toString(serverIp));
                List<ServerData> servers = Collections.singletonList(new ServerData(serverIp,
                                                                                    1,
                                                                                    ServerStatus.STATUS_GOOD,
                                                                                    9999,
                                                                                    false,
                                                                                    1,
                                                                                    100,
                                                                                    0));
                client.sendPacket(new ServerList(client, servers));
            } catch (UnknownHostException e) {
                log.error("Cannot resolve address of gameserver host", e);
                client.close(REASON_ACCESS_FAILED);
            }

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

        /*InetAddress clientAddr = client.getConnection().getInetAddress();*/

        final LoginController lc = LoginController.getInstance();
        AccountInfo info = lc.retriveAccountInfo(/*clientAddr, */packet.getUser(), packet.getPassword());
        if (info == null) {
            // user or pass wrong
            client.close(REASON_USER_OR_PASS_WRONG);
            return;
        }

        LoginController.AuthLoginResult result = lc.tryCheckinAccount(client, info);
        switch (result) {
            case AUTH_SUCCESS:
                client.setAccount(info.getLogin());
                client.setState(AUTHED_LOGIN);
                client.setSessionKey(lc.assignSessionKeyToClient(info.getLogin(), client));
                try {
                    InetAddress gameServerAddr = InetAddress.getByName("l2-gameserver");
                    byte[] serverIp = Arrays.copyOf(gameServerAddr.getAddress(), 4);
                    log.info("GameServer IP resolved: {}", Arrays.toString(serverIp));
                    List<ServerData> servers = Collections.singletonList(new ServerData(serverIp,
                                                                                        1,
                                                                                        ServerStatus.STATUS_GOOD,
                                                                                        9999,
                                                                                        false,
                                                                                        1,
                                                                                        100,
                                                                                        0));
                    client.sendPacket(new ServerList(client, servers));
                } catch (Exception e) {
                    log.error("Cannot resolve gameserver IP", e);
                }
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
