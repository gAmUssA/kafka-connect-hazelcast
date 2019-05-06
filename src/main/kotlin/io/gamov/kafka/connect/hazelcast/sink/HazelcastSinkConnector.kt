package io.gamov.kafka.connect.hazelcast.sink

import io.gamov.kafka.connect.hazelcast.VersionUtil
import io.gamov.kafka.connect.hazelcast.config.HazelcastSinkConnectorConfig
import org.apache.kafka.common.config.ConfigDef
import org.apache.kafka.connect.connector.Task
import org.apache.kafka.connect.sink.SinkConnector
import org.slf4j.LoggerFactory

class HazelcastSinkConnector : SinkConnector() {
  private var config: HazelcastSinkConnectorConfig? = null

  override fun version(): String {
    return VersionUtil.version
  }

  override fun start(map: Map<String, String>) {
    config = HazelcastSinkConnectorConfig(map)

    //TODO: Add things you need to do to setup your connector.

    /**
     * This will be executed once per connector. 
     * This can be used to handle connector level setup.
     */

  }

  override fun taskClass(): Class<out Task> {
    //TODO: Return your task implementation.
    return HazelcastSinkTask::class.java
  }

  override fun taskConfigs(maxTasks: Int): List<Map<String, String>> {
    //TODO: Define the individual task configurations that will be executed.

    /**
     * This is used to schedule the number of tasks that will be running.
     * This should not exceed maxTasks.
     */

    throw UnsupportedOperationException("This has not been implemented.")
  }

  override fun stop() {
    //TODO: Do things that are necessary to stop your connector.
  }

  override fun config(): ConfigDef {
    return HazelcastSinkConnectorConfig.conf()
  }

  companion object {
    private val log = LoggerFactory.getLogger(HazelcastSinkConnector::class.java)
  }
}
