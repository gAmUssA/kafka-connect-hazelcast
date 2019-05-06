import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.confluent.connect.packaging.manifest.*
import io.confluent.connect.packaging.manifest.DeliveryGuarantee.AT_LEAST_ONCE
import io.confluent.connect.packaging.manifest.OwnerType.USER
import io.confluent.connect.packaging.manifest.PluginType.SINK_CONNECTOR
import java.time.LocalDateTime
import java.time.LocalDateTime.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.*

buildscript {
  dependencies {
    classpath("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")
    classpath("io.confluent:kafka-connect-maven-plugin:0.11.2") {
      isTransitive = false
    }
  }
  repositories {
    jcenter()
  }
}

tasks.register("generateHubManifest") {
  doLast {
    val metadata = PluginMetadata()
            .setName(project.name)
            .setVersion(project.version.toString())
            .setTitle("Kafka Connect Hazelcast")
            .setDescription(project.description?.trimIndent())
            .setLogo("assets/hazelcast.png")
            .setDocumentationUrl("https://hazelcast.org")
            .setSourceUrl("https://github.com/gamussa/kafka-connect-hazelcast")
            .setOwner(Owner()
                    .setUsername("gamussa")
                    .setType(USER)
                    .setName("Viktor Gamov")
                    .setUrl("http://gamov.io")
                    .setLogo("pic"))
            .setSupport(Support()
                    .setProviderName("Github")
                    .setSummary("""
              I provide support for this connector on «best effort basis» - I'm adding features and bugs on my free time.
            """.trimIndent())
                    .setUrl("https://github.com/gamussa/kafka-connect-hazelcast/issues")
                    .setLogo("mypic"))
            .setDockerImage(DockerImage()
                    .setNamespace("gamussa")
                    .setName("kafka-connect-hazelcast")
                    .setTag(project.version.toString())
                    .setRegistries(listOf("hub.docker.io"))
            )
            .setLicenses(listOf(PluginLicense().setName("MIT License")))
            .setPluginTypes(setOf(SINK_CONNECTOR))
            .setTags(setOf("imdg", "cache", "in-memory"))
            .setRequirements(setOf("Hazelcast 3.x"))
            // Must be in the format "yyyy-MM-dd"
            .setReleaseDate(now().format(ISO_DATE))
            .setFeatures(PluginFeatures()
                    .setSupportedEncodings(setOf("any"))
                    .setSingleMessageTransforms(true)
                    .setConfluentControlCenterIntegration(true)
                    .setKafkaConnectApi(true)
                    .setDeliveryGuarantee(setOf(AT_LEAST_ONCE)))
    val mapper = jacksonObjectMapper()
    val file = File("${project.buildDir}/hub/manifest.json")
    file.parentFile.mkdirs()
    val prettyPrinter = mapper.writerWithDefaultPrettyPrinter()
    logger.info("Generating Confluent Hub manifest...\n${prettyPrinter.writeValueAsString(metadata)}")
    prettyPrinter.writeValue(file, metadata)
    //println(writeValue)

  }
}
