import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This is a general purpose Gradle build.
 * Learn more about Gradle by exploring our Samples at https://docs.gradle.org/8.13/samples
 */

plugins {
    kotlin("multiplatform") version "2.1.20"
    kotlin("plugin.serialization") version "2.1.20"
}

repositories {
    mavenLocal()
    mavenCentral()
}

kotlin {
    js {
        browser {

            commonWebpackConfig {
                outputFileName = "index.js"
                devServer?.let {
                    it.proxy = mutableListOf(
                        KotlinWebpackConfig.DevServer.Proxy(
                            context = mutableListOf("/api/ark"),
                            target = "https://www.moles.top",
                            changeOrigin = true
                        )
                    )
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        jsMain {
            dependencies {
                implementation(kotlinWrappers.react)
                implementation(kotlinWrappers.reactDom)
                implementation(kotlinWrappers.emotion)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.10.1")
                implementation("io.ktor:ktor-client-js:3.1.2")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
            }
        }
    }
}

