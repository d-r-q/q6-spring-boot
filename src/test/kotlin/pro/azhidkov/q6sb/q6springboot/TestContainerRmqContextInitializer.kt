package pro.azhidkov.q6sb.q6springboot

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.HttpWaitStrategy
import java.io.IOException
import java.net.ConnectException
import java.net.Socket

val rabbitMqContainer: GenericContainer<*> by lazy {
    GenericContainer("rabbitmq:3-management-alpine")
        .withExposedPorts(5672, 15672)
        .withReuse(true)
        .withEnv(
            mapOf(
                "RABBITMQ_DEFAULT_USER" to "eventbus",
                "RABBITMQ_DEFAULT_PASS" to "password",
                "RABBITMQ_DEFAULT_VHOST" to "eventbus",
            )
        )
        .waitingFor(HttpWaitStrategy().forPort(15672).forPath("/#/").forStatusCode(200))
        .apply { start() }
}

private val rmqPort = if (isOpen(5673)) 5673 else rabbitMqContainer.getMappedPort(5672)

class TestContainerRmqContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        applicationContext.overrideProperties(
            "spring.rabbitmq.port" to rmqPort.toString(),
            "spring.rabbitmq.host" to rabbitMqContainer.host,
            "spring.rabbitmq.password" to "password",
            "spring.rabbitmq.virtual-host" to "eventbus",
            "spring.rabbitmq.username" to "eventbus",
        )
    }

}

private fun isOpen(port: Int): Boolean {
    try {
        Socket("localhost", port).use {
            return true
        }
    } catch (e: ConnectException) {
        return false
    } catch (e: IOException) {
        throw IllegalStateException("Error while trying to check open port", e)
    }
}