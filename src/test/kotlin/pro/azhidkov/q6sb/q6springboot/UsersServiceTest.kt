package pro.azhidkov.q6sb.q6springboot

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Assume
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import pro.azhidkov.q6sb.q6springboot.app.RegisterRequest
import pro.azhidkov.q6sb.q6springboot.core.CoreConfig
import pro.azhidkov.q6sb.q6springboot.domain.Role
import pro.azhidkov.q6sb.q6springboot.core.users.UsersService


@ContextConfiguration(
    classes = [CoreConfig::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(statements = ["TRUNCATE TABLE users CASCADE;"])
class UsersServiceTest {

    @Autowired
    lateinit var usersService: UsersService

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
        MatcherAssert.assertThat(user!!.password, CoreMatchers.not(CoreMatchers.equalTo(password)))
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

}