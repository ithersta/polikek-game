import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpack

val koinVersion = "3.2.0"

plugins {
    kotlin("multiplatform") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev703"
    application
}

group = "com.ithersta"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
}

val resourcesDir = "$buildDir/resources/static/"
val skikoWasm by configurations.creating

dependencies {
    skikoWasm("org.jetbrains.skiko:skiko-js-wasm-runtime:0.7.16")
}

val unzipTask = tasks.register("unzipWasm", Copy::class) {
    destinationDir = file(resourcesDir)
    from(skikoWasm.map { zipTree(it) })
}

tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile>().configureEach {
    dependsOn(unzipTask)
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
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
                implementation("io.insert-koin:koin-core:$koinVersion")
                implementation(compose.runtime)
                implementation("org.jetbrains.skiko:skiko:0.7.16")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                dependsOn(commonMain)
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
                implementation("io.insert-koin:koin-ktor:$koinVersion")
                implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")
            }
        }
        val jvmTest by getting
        val jsMain by getting {
            dependencies {
                dependsOn(commonMain)
                implementation("io.ktor:ktor-client-core:2.0.1")
                implementation("io.ktor:ktor-client-content-negotiation:2.0.1")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.1")
                implementation("io.ktor:ktor-client-auth:2.0.1")
                implementation(compose.web.core)
                implementation(compose.material)
                implementation(compose.runtime)
                resources.setSrcDirs(resources.srcDirs)
                resources.srcDirs(unzipTask.map { it.destinationDir })
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
