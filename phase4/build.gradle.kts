import de.hhu.cs.dbs.gradle.environments.envfile

plugins {
    id("de.hhu.cs.dbs.propra") version "latest.release"
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
}

tasks.bootRun { environment(envfile(".env")) }

tasks.composeBuild { context.setFrom(tasks.bootJar) }

dependencies {
    specification(libs.specification)
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
}
