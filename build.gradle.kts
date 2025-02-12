import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("dev.architectury.loom") version "1.7-SNAPSHOT"
    id("architectury-plugin") version "3.4-SNAPSHOT"
    java
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
}

version = project.extra["mod_version"] as String
group = project.extra["maven_group"] as String
val minecraftVersion = project.extra["minecraft_version"] as String
val loaderVersion = project.extra["loader_version"] as String
val fabricKotlinVersion = project.extra["fabric_kotlin_version"] as String
val fabricVersion = project.extra["fabric_version"] as String
val cobblemonVersion = project.extra["cobblemon_version"] as String

base {
    archivesName.set(project.extra["archives_base_name"] as String)
}

architectury {
    minecraft = minecraftVersion
    platformSetupLoomIde()
    fabric()
}

loom {
    silentMojangMappingsLicense()
    log4jConfigs.from(file("log4j-dev.xml"))
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.impactdev.net/repository/development/") }
    maven("https://maven.parchmentmc.org")
}

dependencies {
    minecraft("net.minecraft:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")

    // Cobblemon
    modCompileOnly("com.cobblemon:mod:$cobblemonVersion")
    modRuntimeOnly("com.cobblemon:fabric:$cobblemonVersion")

    // Permissions
    compileOnly("net.luckperms:api:5.4")

    // Tests
    testImplementation(kotlin("test"))
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile> {
    options.release.set(21)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi")
    }
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${project.base.archivesName.get()}" }
    }
}

tasks.test {
    useJUnitPlatform()
}
