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
package com.vvygulyarniy.l2.loginserver.util

/**
 * @param <E> The enum type
 * *
 * @author HorridoJoho
</E> */
class EnumIntBitmask<E : Enum<E>> : Cloneable {
    private val _enumClass: Class<E>
    var bitmask: Int = 0

    constructor(enumClass: Class<E>, all: Boolean) {
        _enumClass = enumClass

        val values = _enumClass.enumConstants
        if (values.size > 32) {
            throw IllegalArgumentException("Enum too big for an integer bitmask.")
        }

        if (all) {
            setAll()
        } else {
            clear()
        }
    }

    constructor(enumClass: Class<E>, bitmask: Int) {
        _enumClass = enumClass
        this.bitmask = bitmask
    }

    fun setAll() {
        set(*_enumClass.enumConstants)
    }

    fun clear() {
        bitmask = 0
    }

    @SafeVarargs
    fun set(vararg many: E) {
        clear()
        for (one in many) {
            bitmask = bitmask or (1 shl one.ordinal)
        }
    }

    @SafeVarargs
    fun set(first: E, vararg more: E) {
        clear()
        add(first, *more)
    }

    @SafeVarargs
    fun add(first: E, vararg more: E) {
        bitmask = bitmask or (1 shl first.ordinal)
        if (more != null) {
            for (one in more) {
                bitmask = bitmask or (1 shl one.ordinal)
            }
        }
    }

    @SafeVarargs
    fun remove(first: E, vararg more: E) {
        bitmask = bitmask and (1 shl first.ordinal).inv()
        if (more != null) {
            for (one in more) {
                bitmask = bitmask and (1 shl one.ordinal).inv()
            }
        }
    }

    @SafeVarargs
    fun has(first: E, vararg more: E): Boolean {
        if (bitmask and (1 shl first.ordinal) == 0) {
            return false
        }

        for (one in more) {
            if (bitmask and (1 shl one.ordinal) == 0) {
                return false
            }
        }
        return true
    }

    public override fun clone(): EnumIntBitmask<E> {
        return EnumIntBitmask(_enumClass, bitmask)
    }

    companion object {

        fun <E : Enum<E>> getAllBitmask(enumClass: Class<E>): Int {
            var allBitmask = 0
            val values = enumClass.enumConstants
            if (values.size > 32) {
                throw IllegalArgumentException("Enum too big for an integer bitmask.")
            }
            for (value in values) {
                allBitmask = allBitmask or (1 shl value.ordinal)
            }
            return allBitmask
        }
    }
}
