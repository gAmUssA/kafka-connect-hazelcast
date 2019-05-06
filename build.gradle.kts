import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "io.gamov.kafka.connect"
version = "0.1-SNAPSHOT" // TODO: externalize
description = """
This is a connector for getting data out of Apache Kafka into Hazelcast IMDG.
It is built off of the Kafka Connect framework, and therefore automatically supports plugable encoding converters, single message transforms, and other useful features.
Hazelcast 3.x is required (Was tested with 3.12).
"""

var kafkaConnectVersion by extra { "2.2.0-cp1" }
var hazelcastAllVersion by extra { "3.12" }
var owner by extra { "gamussa" }

repositories {
  jcenter()
  maven(url = "http://packages.confluent.io/maven/")
}

plugins {
  java
  idea
  distribution
  kotlin("jvm") version "1.3.30"
  id("com.avast.gradle.docker-compose") version "0.9.1"
}

// confluent-hub manifest generation
apply(from = "buildscripts/confluent-hub.gradle.kts")

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

dependencies {
  compile(kotlin("stdlib"))
  compile(kotlin("reflect"))

  compileOnly("org.apache.kafka:connect-api:$kafkaConnectVersion")
  compile("org.slf4j:slf4j-api:1.7.25")
  //compile("org.slf4j:slf4j-log4j12:1.7.25")

  testCompile("junit:junit:4.12")
  /*testCompile 'org.mockito:mockito-core:1.10.19'
  testCompile 'ch.qos.logback:logback-classic:1.1.7'
  */

  // Hazelcast dependencies
  compile("com.hazelcast:hazelcast-all:$hazelcastAllVersion")
}

/**
 * Creates distribution according to Component Archive Structure
 *
 * https://docs.confluent.io/current/connect/managing/confluent-hub/component-archive.html#component-archive-structure
 *
 */
distributions {
  val jar: Jar by tasks
  main {
    baseName = "$owner-${rootProject.name}"
    contents {
      from("${project.buildDir}/hub/manifest.json")
      into("doc/") {
        from(project.projectDir) {
          include("LICENSE*")
          include("README*")
          include("NOTICE*")
          include("license/*")
        }
      }
      into("etc/") {
        from("config")
      }
      into("assets/") {

      }
      into("lib/") {
        from(jar)
        from(configurations.runtimeClasspath)
      }
    }
  }
}

/**
 * Tasks dependencies control block
 */
tasks {
  // defined in confluent-hub.gradle.kts file
  val generateHubManifest by existing
  
  distTar {
    dependsOn(generateHubManifest)
  }
  distZip {
    dependsOn(generateHubManifest)
  }

  /**
   * Docker-compose depends on packaging task because it uses custom connect image and it builds during `docker-compose up` command 
   */
  composeUp {
    dependsOn(distZip)
  }
}

dockerCompose {
  useComposeFiles = listOf("docker-compose.yml")
}