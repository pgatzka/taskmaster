import org.jooq.meta.jaxb.MatcherTransformType
import org.testcontainers.postgresql.PostgreSQLContainer

plugins {
    id("java")
    id("org.flywaydb.flyway") version "12.4.0"
    id("org.jooq.jooq-codegen-gradle") version "3.21.7"
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {

}

buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.7.13")
        classpath("org.testcontainers:testcontainers-postgresql:2.0.5")
        classpath("org.flywaydb:flyway-database-postgresql:12.4.0")
    }
}


abstract class ContainerService : BuildService<BuildServiceParameters.None>, AutoCloseable {

    private val container = PostgreSQLContainer("postgres:18-alpine")

    fun startContainer(): PostgreSQLContainer {
        if (!container.isRunning) container.start()
        return container
    }

    fun stopContainer() {
        if (!container.isRunning) container.stop()
    }

    override fun close() {
        stopContainer()
    }

}

abstract class StartContainerTask : DefaultTask() {

    @get:ServiceReference("containerService")
    abstract val service: Property<ContainerService>

}

val containerService = gradle.sharedServices.registerIfAbsent("containerService", ContainerService::class)

tasks {
    val startContainer = register<StartContainerTask>("startContainer") {
        group = "codegen"
        description = "Starts the postgres codegen container"
        doLast {
            val container = service.get().startContainer()

            System.setProperty("jooq.codegen.jdbc.url", container.jdbcUrl)
            System.setProperty("jooq.codegen.jdbc.username", container.username)
            System.setProperty("jooq.codegen.jdbc.password", container.password)
            System.setProperty("flyway.url", container.jdbcUrl)
            System.setProperty("flyway.user", container.username)
            System.setProperty("flyway.password", container.password)
        }
    }
    flywayMigrate {
        dependsOn(startContainer)
    }
    jooqCodegen {
        dependsOn(flywayMigrate)
    }
    compileJava {
        dependsOn(jooqCodegen)
    }
}

jooq {
    configuration {
        generator {
            database {
                inputSchema = "public"
                excludes = "flyway_schema_history"
            }
        }
    }
}