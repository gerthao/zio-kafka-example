import pureconfig.error.ConfigReaderFailures
import zio.*
import zio.kafka.consumer.*
import zio.kafka.producer.{Producer, ProducerSettings}
import zio.kafka.serde.*
import zio.stream.ZStream
import util.Extensions.*

object ZioKafkaApp extends ZIOAppDefault:

  val producer: ZStream[Producer, Throwable, Nothing] = ZStream
    .repeatZIO(Random.nextIntBetween(0, Int.MaxValue))
    .schedule(Schedule.fixed(2.seconds))
    .mapZIO { random =>
      Producer.produce[Any, Long, String](
        topic = "random",
        key = random % 4,
        value = random.toString,
        keySerializer = Serde.long,
        valueSerializer = Serde.string
      )
    }
    .drain

  val consumer: ZStream[Consumer, Throwable, Nothing] = Consumer
    .subscribeAnd(Subscription.topics("random"))
    .plainStream(Serde.long, Serde.string)
    .tap(r => Console.printLine(r.value))
    .map(_.offset)
    .aggregateAsync(Consumer.offsetBatches)
    .mapZIO(_.commit)
    .drain

  def producerLayer = ZLayer.scoped(List(
    "localhost:29092"
  ) |> ProducerSettings.apply
    |> Producer.make)

  def consumerLayer = ZLayer.scoped(
    Consumer.make(ConsumerSettings(List("localhost:29092")).withGroupId("group"))
  )
  override def run = producer
  .merge(consumer)
  .runDrain
  .provide(producerLayer, consumerLayer)
