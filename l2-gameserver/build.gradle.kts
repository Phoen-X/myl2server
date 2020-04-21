import org.gradle.api.JavaVersion.VERSION_11
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
    kotlin("jvm")
}

java {
    sourceCompatibility = VERSION_11
    targetCompatibility = VERSION_11
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClassName = "com.vvygulyarniy.l2.gameserver.network.GameServer"
}

dependencies {
    implementation(project(":l2-network", configuration = "default"))
    implementation(project(":l2-crypt", configuration = "default"))
    implementation(project(":l2-domain", configuration = "default"))

    implementation(kotlin("stdlib-jdk8"))

    implementation("ch.qos.logback:logback-classic:1.0.1")

    implementation("com.google.guava:guava:29.0-jre")
    implementation("org.jdom:jdom2:2.0.6")
    implementation("jaxen:jaxen:1.1.6")

    implementation("org.springframework:spring-context:5.2.0.RELEASE")

    compileOnly("org.projectlombok:lombok:1.18.12")
    annotationProcessor("org.projectlombok:lombok:1.18.12")

    testImplementation("org.testng:testng:6.10")
    testImplementation("org.assertj:assertj-core:3.6.2")
    testImplementation("org.mockito:mockito-core:3.3.3")

}