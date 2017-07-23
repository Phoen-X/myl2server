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

import java.security.SecureRandom
import java.util.*

/**
 * @author Forsaiken
 */
object Rnd {
    private val ADDEND = 0xBL
    private val MASK = (1L shl 48) - 1
    private val MULTIPLIER = 0x5DEECE66DL
    private val rnd = newInstance(RandomType.UNSECURE_THREAD_LOCAL)
    @Volatile var SEED_UNIQUIFIER = 8682522807148012L

    fun directRandom(): Random {
        return rnd.directRandom()
    }

    /**
     * Get a random double number from 0 to 1

     * @return A random double number from 0 to 1
     * *
     * @see com.l2jserver.util.Rnd.nextDouble
     */
    fun get(): Double {
        return rnd.nextDouble()
    }

    /**
     * Gets a random integer number from 0(inclusive) to n(exclusive)

     * @param n The superior limit (exclusive)
     * *
     * @return A random integer number from 0 to n-1
     */
    operator fun get(n: Int): Int {
        return rnd[n]
    }

    /**
     * Gets a random integer number from min(inclusive) to max(inclusive)

     * @param min The minimum value
     * *
     * @param max The maximum value
     * *
     * @return A random integer number from min to max
     */
    operator fun get(min: Int, max: Int): Int {
        return rnd[min, max]
    }

    /**
     * Gets a random long number from min(inclusive) to max(inclusive)

     * @param min The minimum value
     * *
     * @param max The maximum value
     * *
     * @return A random long number from min to max
     */
    operator fun get(min: Long, max: Long): Long {
        return rnd[min, max]
    }

    fun newInstance(type: RandomType): RandomContainer {
        when (type) {
            Rnd.RandomType.UNSECURE_ATOMIC -> return RandomContainer(Random())

            Rnd.RandomType.UNSECURE_VOLATILE -> return RandomContainer(NonAtomicRandom())

            Rnd.RandomType.UNSECURE_THREAD_LOCAL -> return RandomContainer(ThreadLocalRandom())

            Rnd.RandomType.SECURE -> return RandomContainer(SecureRandom())
        }
    }

    /**
     * Get a random boolean state (true or false)

     * @return A random boolean state (true or false)
     * *
     * @see Random.nextBoolean
     */
    fun nextBoolean(): Boolean {
        return rnd.nextBoolean()
    }

    /**
     * Fill the given array with random byte numbers from Byte.MIN_VALUE(inclusive) to Byte.MAX_VALUE(inclusive)

     * @param array The array to be filled with random byte numbers
     * *
     * @see Random.nextBytes
     */
    fun nextBytes(array: ByteArray) {
        rnd.nextBytes(array)
    }

    /**
     * Get a random double number from 0 to 1

     * @return A random double number from 0 to 1
     * *
     * @see Random.nextDouble
     */
    fun nextDouble(): Double {
        return rnd.nextDouble()
    }

    /**
     * Get a random float number from 0 to 1

     * @return A random integer number from 0 to 1
     * *
     * @see Random.nextFloat
     */
    fun nextFloat(): Float {
        return rnd.nextFloat()
    }

    /**
     * Get a random gaussian double number from 0 to 1

     * @return A random gaussian double number from 0 to 1
     * *
     * @see Random.nextGaussian
     */
    fun nextGaussian(): Double {
        return rnd.nextGaussian()
    }

    /**
     * Get a random integer number from Integer.MIN_VALUE(inclusive) to Integer.MAX_VALUE(inclusive)

     * @return A random integer number from Integer.MIN_VALUE to Integer.MAX_VALUE
     * *
     * @see Random.nextInt
     */
    fun nextInt(): Int {
        return rnd.nextInt()
    }

    /**
     * @param n
     * *
     * @return
     * *
     * @see com.l2jserver.util.Rnd.get
     */
    fun nextInt(n: Int): Int {
        return get(n)
    }

    /**
     * Get a random long number from Long.MIN_VALUE(inclusive) to Long.MAX_VALUE(inclusive)

     * @return A random integer number from Long.MIN_VALUE to Long.MAX_VALUE
     * *
     * @see Random.nextLong
     */
    fun nextLong(): Long {
        return rnd.nextLong()
    }

    /**
     * @author Forsaiken
     */
    enum class RandomType {
        /**
         * For best random quality.

         * @see SecureRandom
         */
        SECURE,

        /**
         * For average random quality.

         * @see Random
         */
        UNSECURE_ATOMIC,

        /**
         * Like [com.l2jserver.util.Rnd.RandomType.UNSECURE_ATOMIC].<br></br>
         * Each thread has it`s own random instance.<br></br>
         * Provides best parallel access speed.

         * @see com.l2jserver.util.Rnd.ThreadLocalRandom
         */
        UNSECURE_THREAD_LOCAL,

        /**
         * Like [com.l2jserver.util.Rnd.RandomType.UNSECURE_ATOMIC].<br></br>
         * Provides much faster parallel access speed.

         * @see com.l2jserver.util.Rnd.NonAtomicRandom
         */
        UNSECURE_VOLATILE
    }

    /**
     * This class extends [Random] but do not compare and store atomically.<br></br>
     * Instead it`s using a simple volatile flag to ensure reading and storing the whole 64bit seed chunk.<br></br>
     * This implementation is much faster on parallel access, but may generate the same seed for 2 threads.

     * @author Forsaiken
     * *
     * @see Random
     */
    class NonAtomicRandom @JvmOverloads constructor(seed: Long = ++SEED_UNIQUIFIER + System.nanoTime()) : Random() {
        @Volatile private var _seed: Long = 0

        init {
            setSeed(seed)
        }

        public override fun next(bits: Int): Int {
            _seed = _seed * MULTIPLIER + ADDEND and MASK
            return _seed.ushr(48 - bits).toInt()
        }

        override fun setSeed(seed: Long) {
            _seed = seed xor MULTIPLIER and MASK
        }

        companion object {
            private val serialVersionUID = 1L
        }
    }

    /**
     * @author Forsaiken
     */
    class RandomContainer(private val _random: Random) {

        fun directRandom(): Random {
            return _random
        }

        /**
         * Get a random double number from 0 to 1

         * @return A random double number from 0 to 1
         * *
         * @see com.l2jserver.util.Rnd.nextDouble
         */
        fun get(): Double {
            return _random.nextDouble()
        }

        /**
         * Gets a random integer number from 0(inclusive) to n(exclusive)

         * @param n The superior limit (exclusive)
         * *
         * @return A random integer number from 0 to n-1
         */
        operator fun get(n: Int): Int {
            return (_random.nextDouble() * n).toInt()
        }

        /**
         * Gets a random integer number from min(inclusive) to max(inclusive)

         * @param min The minimum value
         * *
         * @param max The maximum value
         * *
         * @return A random integer number from min to max
         */
        operator fun get(min: Int, max: Int): Int {
            return min + (_random.nextDouble() * (max - min + 1)).toInt()
        }

        /**
         * Gets a random long number from min(inclusive) to max(inclusive)

         * @param min The minimum value
         * *
         * @param max The maximum value
         * *
         * @return A random long number from min to max
         */
        operator fun get(min: Long, max: Long): Long {
            return min + (_random.nextDouble() * (max - min + 1)).toLong()
        }

        /**
         * Get a random boolean state (true or false)

         * @return A random boolean state (true or false)
         * *
         * @see Random.nextBoolean
         */
        fun nextBoolean(): Boolean {
            return _random.nextBoolean()
        }

        /**
         * Fill the given array with random byte numbers from Byte.MIN_VALUE(inclusive) to Byte.MAX_VALUE(inclusive)

         * @param array The array to be filled with random byte numbers
         * *
         * @see Random.nextBytes
         */
        fun nextBytes(array: ByteArray) {
            _random.nextBytes(array)
        }

        /**
         * Get a random double number from 0 to 1

         * @return A random double number from 0 to 1
         * *
         * @see Random.nextDouble
         */
        fun nextDouble(): Double {
            return _random.nextDouble()
        }

        /**
         * Get a random float number from 0 to 1

         * @return A random integer number from 0 to 1
         * *
         * @see Random.nextFloat
         */
        fun nextFloat(): Float {
            return _random.nextFloat()
        }

        /**
         * Get a random gaussian double number from 0 to 1

         * @return A random gaussian double number from 0 to 1
         * *
         * @see Random.nextGaussian
         */
        fun nextGaussian(): Double {
            return _random.nextGaussian()
        }

        /**
         * Get a random integer number from Integer.MIN_VALUE(inclusive) to Integer.MAX_VALUE(inclusive)

         * @return A random integer number from Integer.MIN_VALUE to Integer.MAX_VALUE
         * *
         * @see Random.nextInt
         */
        fun nextInt(): Int {
            return _random.nextInt()
        }

        /**
         * Get a random long number from Long.MIN_VALUE(inclusive) to Long.MAX_VALUE(inclusive)

         * @return A random integer number from Long.MIN_VALUE to Long.MAX_VALUE
         * *
         * @see Random.nextLong
         */
        fun nextLong(): Long {
            return _random.nextLong()
        }
    }

    /**
     * This class extends [Random] but do not compare and store atomically.<br></br>
     * Instead it`s using thread local ensure reading and storing the whole 64bit seed chunk.<br></br>
     * This implementation is the fastest, never generates the same seed for 2 threads.<br></br>
     * Each thread has it`s own random instance.

     * @author Forsaiken
     * *
     * @see Random
     */
    class ThreadLocalRandom : Random {
        private val _seedLocal: ThreadLocal<Seed>?

        constructor() {
            _seedLocal = object : ThreadLocal<Seed>() {
                public override fun initialValue(): Seed {
                    return Seed(++SEED_UNIQUIFIER + System.nanoTime())
                }
            }
        }

        constructor(seed: Long) {
            _seedLocal = object : ThreadLocal<Seed>() {
                public override fun initialValue(): Seed {
                    return Seed(seed)
                }
            }
        }

        public override fun next(bits: Int): Int {
            return _seedLocal!!.get().next(bits).toInt()
        }

        override fun setSeed(seed: Long) {
            _seedLocal?.get()?.setSeed(seed)
        }

        private class Seed internal constructor(seed: Long) {
            internal var _seed: Long = 0

            init {
                setSeed(seed)
            }

            internal fun next(bits: Int): Long {
                _seed = _seed * MULTIPLIER + ADDEND and MASK
                return (_seed) ushr (48 - bits)
            }

            internal fun setSeed(seed: Long) {
                _seed = seed xor MULTIPLIER and MASK
            }
        }

        companion object {
            private val serialVersionUID = 1L
        }
    }
}
