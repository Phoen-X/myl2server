rootProject.name = "myl2server"


pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
        mavenCentral()
    }

    plugins {
        kotlin("jvm") version "1.3.71"
    }
}

include(
        ":l2-crypt",
        ":l2-domain",
        ":l2-gameserver",
        ":l2-loginserver",
        ":l2-mock-client",
        ":l2-network"
)
