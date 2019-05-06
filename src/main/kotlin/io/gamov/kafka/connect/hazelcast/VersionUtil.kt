package io.gamov.kafka.connect.hazelcast

object VersionUtil {

  val version: String
    get() {
      return try {
        VersionUtil::class.java.getPackage().implementationVersion
      } catch (ex: Exception) {
        "0.0.0.0"
      }

    }
}
