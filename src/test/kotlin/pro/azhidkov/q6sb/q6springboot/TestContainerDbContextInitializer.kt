package pro.azhidkov.q6sb.q6springboot

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

val pgContainer: PostgreSQLContainer<*> by lazy {
    PostgreSQLContainer("postgres:15")
        .withExposedPorts(5432)
        .withUsername("q6")
        .withPassword("password")
        .withDatabaseName("postgres")
        .withTmpFs(mapOf("/var" to "rw"))
        .withEnv("PGDATA", "/var/lib/postgresql/data-no-mounted")
        .withCommand("-c max_connections=400")
        .withReuse(true)
        .withInitScript("db/q6-db-init.sql")
        .apply {
            start()
            // Сначала подключаемся к postgres, пересоздаём qyoga для обнуления фикстуры и подключаемся к ней
            this.withDatabaseName("q6")
        }
}

class TestContainerDbContextInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        applicationContext.overrideProperties(
            "spring.datasource.url" to pgContainer.getJdbcUrl(),
            "spring.datasource.username" to "q6",
            "spring.datasource.password" to "password"
        )
    }
}

fun ConfigurableApplicationContext.overrideProperties(vararg properties: Pair<String, String>) {
    TestPropertyValues
        .of(properties.toMap())
        .applyTo(this)
}