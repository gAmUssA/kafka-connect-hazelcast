package io.gamov.kafka.connect.hazelcast.sink

import io.gamov.kafka.connect.hazelcast.VersionUtil
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.connect.sink.SinkRecord
import org.apache.kafka.connect.sink.SinkTask
import org.slf4j.LoggerFactory

class HazelcastSinkTask : SinkTask() {

  override fun version(): String {
    return VersionUtil.version
  }

  override fun start(map: Map<String, String>) {
    //TODO: Create resources like database or api connections here.
  }

  override fun put(collection: Collection<SinkRecord>) {

  }

  override fun flush(map: Map<TopicPartition, OffsetAndMetadata>?) {

  }

  override fun stop() {
    //Close resources here.
  }

  companion object {
    private val log = LoggerFactory.getLogger(HazelcastSinkTask::class.java)
  }

}
