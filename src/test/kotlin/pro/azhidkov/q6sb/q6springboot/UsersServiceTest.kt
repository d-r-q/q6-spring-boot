package pro.azhidkov.q6sb.q6springboot

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert
import org.junit.Assume
import org.junit.jupiter.api.Test
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import pro.azhidkov.q6sb.q6springboot.app.RegisterRequest
import pro.azhidkov.q6sb.q6springboot.core.CoreConfig
import pro.azhidkov.q6sb.q6springboot.core.users.USER_REGISTERED_EVENTS_QUEUE
import pro.azhidkov.q6sb.q6springboot.core.users.UserRegisteredEvent
import pro.azhidkov.q6sb.q6springboot.domain.Role
import pro.azhidkov.q6sb.q6springboot.core.users.UsersService
import kotlin.concurrent.thread


@Import(CoreConfig::class)
@Configuration
@EntityScan("pro.azhidkov.q6sb.q6springboot.domain")
class UsersTestConfig

@ContextConfiguration(
    classes = [UsersTestConfig::class],
    initializers = [TestContainerDbContextInitializer::class, TestContainerRmqContextInitializer::class]
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(statements = ["TRUNCATE TABLE users CASCADE;"])
class UsersServiceTest {

    @Autowired
    lateinit var usersService: UsersService

    @Autowired
    lateinit var rabbitAdmin: RabbitAdmin

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Test
    fun `User password should not be extractable after registration`() {
        // Given
        val password = "password"
        val email = "irrelevant"
        val registerUserRequest = RegisterRequest(email, password, email)
        usersService.register(registerUserRequest)

        // When
        val user = usersService.loadUserByUsername(email)
        Assume.assumeNotNull(user)

        // Then
        MatcherAssert.assertThat(user!!.password, CoreMatchers.not(equalTo(password)))
    }

    @Test
    fun `Registered user should have ROLE_USER`() {
        // Given
        val userId = usersService.register(RegisterRequest("irrelevant", "irrelevant", "irrelevant"))

        // When
        val user = usersService.findById(userId)

        // Then
        Assume.assumeNotNull(user)
        assertThat(user?.roles).contains(Role.ROLE_USER)
    }

    @Test
    fun `UsersService should publish domain event about new user registration`() {
        // Given
        val registerRequest = RegisterRequest("irrelevant", "irrelevant", "irrelevant")
        rabbitAdmin.deleteQueue(USER_REGISTERED_EVENTS_QUEUE)
        rabbitAdmin.declareQueue(Queue(USER_REGISTERED_EVENTS_QUEUE, true))
        var msg: UserRegisteredEvent? = null
        val listener = thread {
            msg = rabbitTemplate.receiveAndConvert(
                USER_REGISTERED_EVENTS_QUEUE,
                200,
                ParameterizedTypeReference.forType(UserRegisteredEvent::class.java)
            )
        }

        // When
        val userId = usersService.register(registerRequest)

        // Then
        listener.join()
        MatcherAssert.assertThat(msg, notNullValue())
        MatcherAssert.assertThat(msg!!.userId, equalTo(userId.toString()))
    }

}