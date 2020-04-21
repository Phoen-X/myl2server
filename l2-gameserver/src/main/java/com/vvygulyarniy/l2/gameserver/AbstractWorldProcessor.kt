package com.vvygulyarniy.l2.gameserver

import com.vvygulyarniy.l2.gameserver.world.time.GameTimeProvider
import org.slf4j.LoggerFactory
import java.time.Duration

abstract class AbstractWorldProcessor(private val clock: GameTimeProvider, processorName: String) : WorldProcessor {
    private val logger = LoggerFactory.getLogger(processorName)

    override fun process() {
        val start = clock.now()
        processTick()
        if (logger.isDebugEnabled) {
            val end = clock.now()
            logger.debug("Tick processing done in {} ms", Duration.between(start, end).toMillis())
        }
    }

    abstract fun processTick(): Unit
}