pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            name = "hhu"
            setUrl("https://git.hhu.de/api/v4/projects/5295/packages/maven")
            credentials(HttpHeaderCredentials::class) {
                name =
                    providers
                        .gradleProperty("de.hhu.git.pat")
                        .map { "Private-Token" }
                        .orElse(
                            providers
                                .environmentVariable("CI")
                                .orElse("false")
                                .map { it.toBoolean() }
                                .flatMap {
                                    if (it) {
                                        providers.provider { "Job-Token" }
                                    } else {
                                        providers.provider { null }
                                    }
                                })
                        .getOrElse("")
                value =
                    providers
                        .gradleProperty("de.hhu.git.pat")
                        .orElse(
                            providers
                                .environmentVariable("CI")
                                .orElse("false")
                                .map { it.toBoolean() }
                                .flatMap {
                                    if (it) {
                                        providers.environmentVariable("CI_JOB_TOKEN")
                                    } else {
                                        providers.provider { null }
                                    }
                                })
                        .getOrElse("")
            }
            authentication { register("header", HttpHeaderAuthentication::class) }
        }
    }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0" }

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven {
            name = "hhu"
            setUrl("https://git.hhu.de/api/v4/projects/5295/packages/maven")
            credentials(HttpHeaderCredentials::class) {
                name =
                    providers
                        .gradleProperty("de.hhu.git.pat")
                        .map { "Private-Token" }
                        .orElse(
                            providers
                                .environmentVariable("CI")
                                .orElse("false")
                                .map { it.toBoolean() }
                                .flatMap {
                                    if (it) {
                                        providers.provider { "Job-Token" }
                                    } else {
                                        providers.provider { null }
                                    }
                                })
                        .getOrElse("")
                value =
                    providers
                        .gradleProperty("de.hhu.git.pat")
                        .orElse(
                            providers
                                .environmentVariable("CI")
                                .orElse("false")
                                .map { it.toBoolean() }
                                .flatMap {
                                    if (it) {
                                        providers.environmentVariable("CI_JOB_TOKEN")
                                    } else {
                                        providers.provider { null }
                                    }
                                })
                        .getOrElse("")
            }
            authentication { register("header", HttpHeaderAuthentication::class) }
        }
    }

    versionCatalogs {
        register("libs") { from("de.hhu.cs.dbs.propra:versioncatalog:latest.integration") }
    }
}

rootProject.name = "implementation"
