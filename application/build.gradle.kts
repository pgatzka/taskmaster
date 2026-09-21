plugins {
    id("java")
    alias(libs.plugins.io.freefair.lombok)
    alias(libs.plugins.org.springframework.boot)
    alias(libs.plugins.io.spring.dependency.management)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.implementation)
    testImplementation(libs.bundles.test.implementation)
    testRuntimeOnly(libs.bundles.test.runtime.only)
    runtimeOnly(libs.bundles.runtime.only)
    developmentOnly(libs.bundles.development.only)
    annotationProcessor(libs.bundles.annotation.processor)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.addAll(
        listOf(
            "-Amapstruct.defaultComponentModel=spring",
            "-Amapstruct.defaultInjectionStrategy=constructor",
            "-Amapstruct.unmappedTargetPolicy=ERROR",
            "-Amapstruct.unmappedSourcePolicy=ERROR",
            "-Amapstruct.suppressGeneratorTimestamp=true"
        )
    )
}
