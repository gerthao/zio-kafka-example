import pureconfig.*
import pureconfig.error.ConfigReaderFailures
import zio.kafka.consumer.ConsumerSettings

object ConsumerSettingsConfig:

  def get(at: String): Either[ConfigReaderFailures, ConsumerSettings] =
    val cs = ConfigSource.default.at(at)
    for
      bootstrapServers <- cs.at("boostrap-servers").load[Set[String]]
      groupId          <- cs.at("groupId").load[String]
    yield ConsumerSettings(bootstrapServers.toList).withGroupId(groupId)
