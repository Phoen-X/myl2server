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
package com.vvygulyarniy.l2.loginserver;

import com.l2server.network.L2LoginClient;
import com.l2server.network.LoginFail.LoginFailReason;
import com.l2server.network.SessionKey;
import com.l2server.network.util.crypt.ScrambledKeyPair;
import com.vvygulyarniy.l2.loginserver.model.data.AccountInfo;
import com.vvygulyarniy.l2.loginserver.util.Rnd;

import javax.crypto.Cipher;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class LoginController {
    /**
     * Time before kicking the client if he didn't logged yet
     */
    public static final int LOGIN_TIMEOUT = 60 * 1000;
    protected static final Logger _log = Logger.getLogger(LoginController.class.getName());
    private static final int BLOWFISH_KEYS = 20;
    private static LoginController _instance;
    private final Map<InetAddress, Integer> _failedLoginAttemps = new HashMap<>();
    private final Map<InetAddress, Long> _bannedIps = new ConcurrentHashMap<>();
    /**
     * Authed Clients on LoginServer
     */
    protected Map<String, L2LoginClient> _loginServerClients = new ConcurrentHashMap<>();
    protected ScrambledKeyPair[] _keyPairs;
    protected byte[][] _blowfishKeys;

    private LoginController() throws GeneralSecurityException {


        _keyPairs = new ScrambledKeyPair[10];

        KeyPairGenerator keygen = null;

        keygen = KeyPairGenerator.getInstance("RSA");
        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(1024, RSAKeyGenParameterSpec.F4);
        keygen.initialize(spec);

        // generate the initial set of keys
        for (int i = 0; i < 10; i++) {
            _keyPairs[i] = new ScrambledKeyPair(keygen.generateKeyPair());
        }


        testCipher((RSAPrivateKey) _keyPairs[0]._pair.getPrivate());

        // Store keys for blowfish communication
        generateBlowFishKeys();

        Thread purge = new PurgeThread();
        purge.setDaemon(true);
        purge.start();
    }

    public static void load() throws GeneralSecurityException {
        synchronized (LoginController.class) {
            if (_instance == null) {
                _instance = new LoginController();
            } else {
                throw new IllegalStateException("LoginController can only be loaded a single time.");
            }
        }
    }

    public static LoginController getInstance() {
        return _instance;
    }

    /**
     * This is mostly to force the initialization of the Crypto Implementation, avoiding it being done on runtime when its first needed.<BR>
     * In short it avoids the worst-case execution time on runtime by doing it on loading.
     *
     * @param key Any private RSA Key just for testing purposes.
     * @throws GeneralSecurityException if a underlying exception was thrown by the Cipher
     */
    private void testCipher(RSAPrivateKey key) throws GeneralSecurityException {
        // avoid worst-case execution, KenM
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
        rsaCipher.init(Cipher.DECRYPT_MODE, key);
    }

    private void generateBlowFishKeys() {
        _blowfishKeys = new byte[BLOWFISH_KEYS][16];

        for (int i = 0; i < BLOWFISH_KEYS; i++) {
            for (int j = 0; j < _blowfishKeys[i].length; j++) {
                _blowfishKeys[i][j] = (byte) (Rnd.nextInt(255) + 1);
            }
        }

    }

    /**
     * @return Returns a random key
     */
    public byte[] getBlowfishKey() {
        return _blowfishKeys[(int) (Math.random() * BLOWFISH_KEYS)];
    }

    public SessionKey assignSessionKeyToClient(String account, L2LoginClient client) {
        SessionKey key;

        key = new SessionKey(Rnd.nextInt(), Rnd.nextInt(), Rnd.nextInt(), Rnd.nextInt());
        _loginServerClients.put(account, client);
        return key;
    }

    public void removeAuthedLoginClient(String account) {
        if (account == null) {
            return;
        }
        _loginServerClients.remove(account);
    }

    public L2LoginClient getAuthedClient(String account) {
        return _loginServerClients.get(account);
    }

    public AccountInfo retriveAccountInfo(InetAddress clientAddr, String login, String password) {
        return retriveAccountInfo(clientAddr, login, password, true);
    }

    private void recordFailedLoginAttemp(InetAddress addr) {
        // We need to synchronize this!
        // When multiple connections from the same address fail to login at the
        // same time, unexpected behavior can happen.
        Integer failedLoginAttemps;
        synchronized (_failedLoginAttemps) {
            failedLoginAttemps = _failedLoginAttemps.get(addr);
            if (failedLoginAttemps == null) {
                failedLoginAttemps = 1;
            } else {
                ++failedLoginAttemps;
            }

            _failedLoginAttemps.put(addr, failedLoginAttemps);
        }
    }

    private void clearFailedLoginAttemps(InetAddress addr) {
        synchronized (_failedLoginAttemps) {
            _failedLoginAttemps.remove(addr);
        }
    }

    private AccountInfo retriveAccountInfo(InetAddress addr, String login, String password, boolean autoCreateIfEnabled) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] raw = password.getBytes(StandardCharsets.UTF_8);
            String hashBase64 = Base64.getEncoder().encodeToString(md.digest(raw));
            return new AccountInfo(login, password, 1, 0);
        } catch (Exception e) {

            return null;
        }
    }

    public AuthLoginResult tryCheckinAccount(L2LoginClient client, InetAddress address, AccountInfo info) {
        return AuthLoginResult.AUTH_SUCCESS;
    }

    /**
     * Adds the address to the ban list of the login server, with the given end time in milliseconds.
     *
     * @param address    The Address to be banned.
     * @param expiration Timestamp in milliseconds when this ban expires
     * @throws UnknownHostException if the address is invalid.
     */
    public void addBanForAddress(String address, long expiration) throws UnknownHostException {
        _bannedIps.putIfAbsent(InetAddress.getByName(address), expiration);
    }

    /**
     * Adds the address to the ban list of the login server, with the given duration.
     *
     * @param address  The Address to be banned.
     * @param duration is milliseconds
     */
    public void addBanForAddress(InetAddress address, long duration) {
        _bannedIps.putIfAbsent(address, System.currentTimeMillis() + duration);
    }

    public boolean isBannedAddress(InetAddress address) throws UnknownHostException {
        String[] parts = address.getHostAddress().split("\\.");
        Long bi = _bannedIps.get(address);
        if (bi == null) {
            bi = _bannedIps.get(InetAddress.getByName(parts[0] + "." + parts[1] + "." + parts[2] + ".0"));
        }
        if (bi == null) {
            bi = _bannedIps.get(InetAddress.getByName(parts[0] + "." + parts[1] + ".0.0"));
        }
        if (bi == null) {
            bi = _bannedIps.get(InetAddress.getByName(parts[0] + ".0.0.0"));
        }
        if (bi != null) {
            if ((bi > 0) && (bi < System.currentTimeMillis())) {
                _bannedIps.remove(address);

                return false;
            }
            return true;
        }
        return false;
    }

    public Map<InetAddress, Long> getBannedIps() {
        return _bannedIps;
    }

    /**
     * Remove the specified address from the ban list
     *
     * @param address The address to be removed from the ban list
     * @return true if the ban was removed, false if there was no ban for this ip
     */
    public boolean removeBanForAddress(InetAddress address) {
        return _bannedIps.remove(address) != null;
    }

    /**
     * Remove the specified address from the ban list
     *
     * @param address The address to be removed from the ban list
     * @return true if the ban was removed, false if there was no ban for this ip or the address was invalid.
     */
    public boolean removeBanForAddress(String address) {
        try {
            return this.removeBanForAddress(InetAddress.getByName(address));
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public SessionKey getKeyForAccount(String account) {
        L2LoginClient client = _loginServerClients.get(account);
        if (client != null) {
            return client.getSessionKey();
        }
        return null;
    }


    public boolean isLoginPossible(L2LoginClient client, int serverId) {
        return true;
    }

    public void setCharactersOnServer(String account, int charsNum, long[] timeToDel, int serverId) {
        L2LoginClient client = _loginServerClients.get(account);

        if (client == null) {
            return;
        }

        if (charsNum > 0) {
            client.setCharsOnServ(serverId, charsNum);
        }

        if (timeToDel.length > 0) {
            client.serCharsWaitingDelOnServ(serverId, timeToDel);
        }
    }

    /**
     * <p>
     * This method returns one of the cached {@link ScrambledKeyPair ScrambledKeyPairs} for communication with Login Clients.
     * </p>
     *
     * @return a scrambled keypair
     */
    public ScrambledKeyPair getScrambledRSAKeyPair() {
        return _keyPairs[Rnd.nextInt(10)];
    }

    /**
     * @param client  the client
     * @param address client host address
     * @param info    the account info to checkin
     * @return true when ok to checkin, false otherwise
     */
    public boolean canCheckin(L2LoginClient client, InetAddress address, AccountInfo info) {
        return true;
    }

    public boolean isValidIPAddress(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        for (String s : parts) {
            int i = Integer.parseInt(s);
            if ((i < 0) || (i > 255)) {
                return false;
            }
        }
        return true;
    }

    public static enum AuthLoginResult {
        INVALID_PASSWORD,
        ACCOUNT_BANNED,
        ALREADY_ON_LS,
        ALREADY_ON_GS,
        AUTH_SUCCESS
    }

    class PurgeThread extends Thread {
        public PurgeThread() {
            setName("PurgeThread");
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                for (L2LoginClient client : _loginServerClients.values()) {
                    if (client == null) {
                        continue;
                    }
                    if ((client.getConnectionStartTime() + LOGIN_TIMEOUT) < System.currentTimeMillis()) {
                        client.close(LoginFailReason.REASON_ACCESS_FAILED);
                    }
                }

                try {
                    Thread.sleep(LOGIN_TIMEOUT / 2);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
