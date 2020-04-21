import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
    kotlin("jvm")
}

application {
    mainClassName = "com.vvygulyarniy.l2.loginserver.L2LoginServer"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
}

dependencies {
    implementation(project(":l2-network", configuration = "default"))
    implementation(project(":l2-crypt", configuration = "default"))


    implementation(kotlin("stdlib-jdk8"))

    implementation("ch.qos.logback:logback-classic:1.0.1")

    implementation("com.google.guava:guava:29.0-jre")

    implementation("org.springframework:spring-context:5.2.0.RELEASE")

    compileOnly("org.projectlombok:lombok:1.18.12")
    annotationProcessor("org.projectlombok:lombok:1.18.12")

    testImplementation("org.testng:testng:6.10")
    testImplementation("org.assertj:assertj-core:3.6.2")
    testImplementation("org.mockito:mockito-core:3.3.3")
}