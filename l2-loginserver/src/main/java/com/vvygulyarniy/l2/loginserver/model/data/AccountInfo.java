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
package com.vvygulyarniy.l2.loginserver.model.data;

import java.util.Objects;

/**
 * @author HorridoJoho
 */
public final class AccountInfo {
    private final String login;
    private final String passHash;
    private final int accessLevel;
    private final int lastServerId;

    public AccountInfo(final String login, final String passHash, final int accessLevel, final int lastServerId) {
        Objects.requireNonNull(login, "login");
        Objects.requireNonNull(passHash, "passHash");

        if (login.isEmpty()) {
            throw new IllegalArgumentException("login");
        }
        if (passHash.isEmpty()) {
            throw new IllegalArgumentException("passHash");
        }

        this.login = login.toLowerCase();
        this.passHash = passHash;
        this.accessLevel = accessLevel;
        this.lastServerId = lastServerId;
    }

    public boolean checkPassHash(final String passHash) {
        return this.passHash.equals(passHash);
    }

    public String getLogin() {
        return login;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public int getLastServerId() {
        return lastServerId;
    }
}
