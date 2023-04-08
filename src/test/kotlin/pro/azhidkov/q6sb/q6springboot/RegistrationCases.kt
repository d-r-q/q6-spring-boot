package pro.azhidkov.q6sb.q6springboot

import io.github.ulfs.assertj.jsoup.Assertions
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import pro.azhidkov.q6sb.q6springboot.core.users.RegisterRequest
import pro.azhidkov.q6sb.q6springboot.core.users.UsersService

@ContextConfiguration(
    classes = [Q6SpringBootApplication::class],
    initializers = [TestContainerDbContextInitializer::class, TestContainerRmqContextInitializer::class]
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class RegistrationCases {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var usersService: UsersService

    @Test
    fun `Unauthorized user should have access to registration page`() {
        val bodyStr = mockMvc.get("/register")
            .andExpect {
                status { is2xxSuccessful() }
            }
            .andReturn().response.contentAsString
        val body = Jsoup.parse(bodyStr)
        Assertions.assertThatSpec(body) {
            node("#registerForm") { exists() }
        }
    }

    @Test
    fun `Successful registration page should contains corresponding message`() {
        val bodyStr = mockMvc.get("/successful-registration")
            .andExpect {
                status { is2xxSuccessful() }
            }
            .andReturn().response.contentAsString
        val body = Jsoup.parse(bodyStr)
        Assertions.assertThatSpec(body) {
            node("#registrationSuccess") { exists() }
        }
    }

    @Test
    fun `User should be able to login after registration`() {
        // Given
        val email = "newUser@ya.ru"
        val pass = "password"

        // When
        mockMvc.post("/register") {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("email", email)
            param("password", pass)
            param("name", "Irrelevant")
        }.andExpect {
            status { is3xxRedirection() }
        }

        // And when/then
        mockMvc.post("/login") {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("username", email)
            param("password", pass)
        }.andExpect {
            status { isFound() }
            header { string("Location", "/app/main") }
        }
    }


    @Test
    fun `When registration form with registered email submitted, then fragment with error message should be returned`() {
        // Given
        val theEmail = "test@ya.ru"
        val password = "password"
        val name = "name"
        usersService.register(RegisterRequest(theEmail, password, name))

        // When
        val reregisterResponse = mockMvc.post("/register") {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
            param("email", theEmail)
            param("password", password)
            param("name", name)
        }.andExpect {
            status { is2xxSuccessful() }
        }
            .andReturn().response.contentAsString

        // Then
        Assertions.assertThatSpec(Jsoup.parse(reregisterResponse)) {
            node("input#emailInput.is-invalid") { exists() }
            node("input#nameInput") { attribute("value") { hasText(name) } }
            node("input#emailInput") { attribute("value") { hasText(theEmail) } }
            node("input#passwordInput") { attribute("value") { hasText(password) } }
        }
    }

}