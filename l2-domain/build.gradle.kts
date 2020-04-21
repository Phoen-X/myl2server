import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "11"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.testng:testng:6.10")
    testImplementation("org.assertj:assertj-core:3.6.2")
}