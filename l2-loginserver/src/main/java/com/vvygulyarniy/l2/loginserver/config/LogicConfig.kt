package com.vvygulyarniy.l2.loginserver.config

import com.google.common.eventbus.EventBus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@Configuration
@ComponentScan("com.vvygulyarniy.l2.loginserver")
open class LogicConfig {

    @Bean open fun eventBus(): EventBus {
        return EventBus()
    }
}