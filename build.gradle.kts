import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
  id("org.gradle.java-library")
  id("io.freefair.lombok") version "6.6.1"
  id("com.github.johnrengelman.shadow") version "7.1.2"

  id("io.papermc.paperweight.userdev") version "1.5.0"
  id("xyz.jpenilla.run-paper") version "2.0.1"
  id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

group = "ml.empee"
version = "1.0.0-SNAPSHOT"

java {
  // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
  maven("https://jitpack.io")
}

dependencies {
  paperweight.paperDevBundle("1.19.3-R0.1-SNAPSHOT")

  implementation("com.github.Mr-EmPee:SimpleIoC:1.5.0")
  implementation("com.github.Mr-EmPee:ImperialEdicta:1.0.0")
  implementation("com.github.Mr-EmPee:JsonPersistence:2.0.0")
  implementation("com.github.Mr-EmPee:SimpleLectorem:1.0.0")
  implementation("com.github.Mr-EmPee:SimpleHeraut:1.0.1")
  implementation("com.github.Mr-EmPee:ItemBuilder:1.0.0")
  implementation("io.github.rysefoxx.inventory:RyseInventory-Plugin:1.5.6.2")
}

tasks {
  compileJava {
    options.encoding = Charsets.UTF_8.name()

    // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
    // See https://openjdk.java.net/jeps/247 for more information.
    options.release.set(17)
  }

  javadoc {
    options.encoding = Charsets.UTF_8.name()
  }

  processResources {
    filteringCharset = Charsets.UTF_8.name()
  }

  shadowJar {
    fun reloc(pkg: String) = relocate(pkg, "ml.empee.templateplugin.relocations.$pkg")

    reloc("ml.empee.commandsManager")
    reloc("ml.empee.configurator")
    reloc("ml.empee.ioc")
    reloc("ml.empee.itembuilder")
    reloc("ml.empee.json")
    reloc("ml.empee.notifier")

    reloc("io.github.rysefoxx.inventory")
  }
}

// Configure plugin.yml generation
bukkit {
  load = BukkitPluginDescription.PluginLoadOrder.STARTUP
  main = "ml.empee.templateplugin.TemplatePlugin"
  apiVersion = "1.19"
  authors = listOf("Mr. EmPee")
}
