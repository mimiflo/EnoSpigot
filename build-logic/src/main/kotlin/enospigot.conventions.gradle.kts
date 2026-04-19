plugins {
    java
    `java-library`
}

repositories {
    mavenCentral()

    maven(url = "https://oss.sonatype.org/content/groups/public")
    maven(url = "https://hub.spigotmc.org/nexus/content/groups/public")
}

group = "com.enofight.enospigot"
version = "1.8.8-R0.1-SNAPSHOT"

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    // Reproducible jars — identical source produces identical bytes.
    // Prevents EnoInfrastructure sync from committing no-op jar updates
    // (empty EnoSpigot commit -> identical jar -> sha256 unchanged -> no deploy).
    withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    val prepareTestServerDir by registering {
        doLast {
            mkdir(layout.buildDirectory.dir("test-server").get().asFile)
        }
    }

    test {
        workingDir = layout.buildDirectory.dir("test-server").get().asFile

        dependsOn(prepareTestServerDir)
    }
}
