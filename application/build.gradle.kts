plugins {
    id("java")
    alias(libs.plugins.org.springframework.boot)
    alias(libs.plugins.io.spring.dependency.management)
}

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor(libs.bundles.annotationProcessor)
    compileOnly(libs.bundles.compileOnly)
    developmentOnly(libs.bundles.developmentOnly)
    implementation(libs.bundles.implementation)
    runtimeOnly(libs.bundles.runtimeOnly)
    testAnnotationProcessor(libs.bundles.testAnnotationProcessor)
    testCompileOnly(libs.bundles.testCompileOnly)
    testImplementation(libs.bundles.testImplementation)
    testRuntimeOnly(libs.bundles.testRuntimeOnly)
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
