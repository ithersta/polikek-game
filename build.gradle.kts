import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

plugins {
    kotlin("multiplatform") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    application
}

group = "com.ithersta"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    maven("https://plugins.gradle.org/m2/")
    maven("https://jitpack.io")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
            }
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        binaries.executable()
        browser()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.ionspin.kotlin:bignum:0.3.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                implementation("com.ionspin.kotlin:bignum-serialization-kotlinx:0.3.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-netty:2.0.1")
                implementation("io.ktor:ktor-server-content-negotiation:2.0.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.1")
                implementation("io.ktor:ktor-html-builder:2.0.0-eap-278")
                implementation("io.ktor:ktor-server-auth:2.0.1")
                implementation("io.ktor:ktor-server-auth-jwt:2.0.1")
                implementation("com.github.elbekD:kt-telegram-bot:1.4.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
                implementation("org.slf4j:slf4j-simple:1.7.36")
                implementation("io.github.microutils:kotlin-logging-jvm:2.1.20")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-core:2.0.1")
                implementation("io.ktor:ktor-client-content-negotiation:2.0.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.1")
                implementation("io.ktor:ktor-client-auth:2.0.1")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.1.0-pre.334")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.1.0-pre.334")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-css:18.0.0-pre.331-kotlin-1.6.20")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-mui:5.6.4-pre.334")
            }
        }
        val jsTest by getting
    }
}

application {
    mainClass.set("com.ithersta.polikekgame.ServerKt")
}

tasks.named<KotlinWebpack>("jsBrowserProductionWebpack") {
    outputFileName = "static/polikek-game.js"
}

tasks.named<Copy>("jvmProcessResources") {
    val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
    from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
    dependsOn(tasks.named<Jar>("jvmJar"))
    classpath(tasks.named<Jar>("jvmJar"))
}
