import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
  id("org.gradle.java-library")

  id("io.freefair.lombok") version "6.6.3"
  id("com.github.johnrengelman.shadow") version "8.1.0"

  id("xyz.jpenilla.run-paper") version "2.0.1"
  id("net.minecrell.plugin-yml.bukkit") version "0.5.3"
}

if (project.hasProperty("tag")) {
  version = project.property("tag")!!
} else {
  version = "develop"
}

var basePackage = "ml.empee.template"

bukkit {
  load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
  main = "${basePackage}.TemplatePlugin"
  apiVersion = "1.13"
  depend = listOf("Vault")
  authors = listOf("Mr. EmPee")
}

repositories {
  maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
  maven("https://oss.sonatype.org/content/repositories/snapshots")
  maven("https://oss.sonatype.org/content/repositories/central")
  
  maven("https://repo.codemc.io/repository/nms/")
  maven("https://jitpack.io")

  mavenCentral()
}

dependencies {
  compileOnly("com.destroystokyo.paper:paper-api:1.13.2-R0.1-SNAPSHOT")

  compileOnly("org.jetbrains:annotations:24.0.1")
  compileOnly("org.xerial:sqlite-jdbc:3.34.0")

  compileOnly("com.github.MilkBowl:VaultAPI:1.7")  { isTransitive = false }

  // Core depends
  implementation("com.github.Mr-EmPee:LightWire:1.2.0")

  implementation("me.lucko:commodore:2.2") {
    exclude("com.mojang", "brigadier")
  }

  implementation("cloud.commandframework:cloud-paper:1.8.3")
  implementation("cloud.commandframework:cloud-annotations:1.8.3")

  // Utilities
  implementation("com.github.Mr-EmPee:SimpleMenu:0.3.4")
  implementation("com.github.Mr-EmPee:ItemBuilder:1.1.3")
  implementation("com.github.cryptomorin:XSeries:9.4.0")

  //implementation("org.cloudburstmc:nbt:3.0.1.Final")
  //implementation("com.github.Mr-EmPee:SimpleHeraut:1.0.1")
}

tasks {
  shadowJar {
    archiveFileName.set("${project.name}.jar")
    isEnableRelocation = project.version != "develop"
    relocationPrefix = "$basePackage.relocations"
  }

  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }

  processResources {
    filteringCharset = Charsets.UTF_8.name()
  }

  compileJava {
    sourceCompatibility = "11"

    options.encoding = Charsets.UTF_8.name()
    options.compilerArgs.add("-parameters")
  }

  runServer {
    version.set("1.13.2")
    args("-o false")

    jvmArgs("-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005")
    systemProperty("paper.playerconnection.keepalive", 10000)
    systemProperty("LetMeReload", true)
  }
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}