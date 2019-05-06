package io.gamov.kafka.connect.hazelcast.config

import org.apache.kafka.common.config.AbstractConfig
import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.common.config.ConfigDef.Importance
import org.apache.kafka.common.config.ConfigDef.Type


class HazelcastSinkConnectorConfig(config: ConfigDef, parsedConfig: Map<String, String>) : AbstractConfig(config, parsedConfig) {

  val my: String
    get() = this.getString(MY_SETTING_CONFIG)

  constructor(parsedConfig: Map<String, String>) : this(conf(), parsedConfig) {}

  companion object {

    val MY_SETTING_CONFIG = "my.setting"
    private val MY_SETTING_DOC = "This is a setting important to my connector."

    @JvmStatic
    fun conf(): ConfigDef {
      return ConfigDef()
              .define(MY_SETTING_CONFIG, Type.STRING, Importance.HIGH, MY_SETTING_DOC)
    }
  }
}
