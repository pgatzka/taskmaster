import org.testcontainers.postgresql.PostgreSQLContainer

plugins {
    id("java")
    id("org.flywaydb.flyway") version "12.4.0"
    id("org.openapi.generator") version "7.25.0"
    id("org.springframework.boot") version "4.1.1"
    id("org.jooq.jooq-codegen-gradle") version "3.21.7"
    id("io.spring.dependency-management") version "1.1.7"
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
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.flywaydb:flyway-database-postgresql")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
    testImplementation("org.springframework.boot:spring-boot-starter-jooq-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-postgresql")
    testCompileOnly("org.projectlombok:lombok")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testAnnotationProcessor("org.projectlombok:lombok")
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
        dependsOn(jooqCodegen, openApiGenerate)
    }
    withType<Test> {
        useJUnitPlatform()
    }
    bootRun {
        args("--spring.profiles.active=development")
    }
}

jooq {
    configuration {
        generator {
            database {
                inputSchema = "public"
                excludes = "flyway_schema_history"
            }
            target {
                packageName = "io.github.pgatzka.taskmaster.generated.jooq"
            }
        }
    }
}


val openApiSpec = layout.projectDirectory.file("openapi.json")

openApiValidate {
    inputSpec = openApiSpec
}

val generatedRoot = layout.buildDirectory.dir("generated-sources/openapi")

openApiGenerate {
    inputSpec = openApiSpec
    generatorName.set("spring")
    quiet.set(true)

    outputDir.set(generatedRoot)
    apiPackage.set("io.github.pgatzka.taskmaster.generated.openapi.api")
    modelPackage.set("io.github.pgatzka.taskmaster.generated.openapi.model")

    schemaMappings.apply {
        put("ProblemDetail", "org.springframework.http.ProblemDetail")
    }

    configOptions.apply {
        put("sourceFolder", "")
        put("interfaceOnly", "true")
        put("skipDefaultInterface", "true")
        put("useJackson3", "true")
        put("useJakartaEe", "true")
        put("documentationProvider", "none")
        put("annotationLibrary", "none")
        put("useSpringBoot4", "true")
        put("useJspecify", "true")
        put("openApiNullable", "false")
        put("generateJsonIncludeAnnotations", "false")
        put("generateJsonSetterNullsAnnotations", "false")
    }

    globalProperties.apply {
        put("apis", "")
        put("models", "")
        put("supportingFiles", "false")
    }
}

sourceSets {
    main {
        java.srcDir(generatedRoot)
    }
}














