package pro.azhidkov.q6sb.q6springboot

import io.github.ulfs.assertj.jsoup.Assertions
import org.jsoup.Jsoup
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithUserDetails
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@ContextConfiguration(
    classes = [Q6SpringBootApplication::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class AuthenticationCases {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `When unauthenticated user opens login page, login page should be returned`() {
        val bodyStr = mockMvc.get("/login")
            .andExpect {
                status { is2xxSuccessful() }
            }
            .andReturn().response.contentAsString
        val body = Jsoup.parse(bodyStr)
        Assertions.assertThatSpec(body) {
            node("#loginForm") { exists() }
        }
    }

    @Test
    fun `When unauthenticated user opens restricted page, he should be redirected to login page`() {
        mockMvc.get("/app/main")
            .andExpect {
                status { isFound() }
                header { string("Location", "http://localhost/login") }
            }
    }

    @Test
    @WithUserDetails("asergeev@ya.ru")
    @Sql("/db/insert-user.sql")
    fun `When authenticated user opens restricted page, it should be returned`() {
        val bodyStr = mockMvc.get("/app/main")
            .andExpect {
                status { is2xxSuccessful() }
            }
            .andReturn().response.contentAsString

        val body = Jsoup.parse(bodyStr)
        Assertions.assertThatSpec(body) {
            this.node("#overview") { containsText("Обзор финансовых дел") }
        }
    }

    @Test
    @WithUserDetails("asergeev@ya.ru")
    @Sql("/db/insert-user.sql")
    fun `When authenticated user opens login page, login page should be returned`() {
        val bodyStr = mockMvc.get("/login")
            .andExpect {
                status { is2xxSuccessful() }
            }
            .andReturn().response.contentAsString

        val body = Jsoup.parse(bodyStr)
        Assertions.assertThatSpec(body) {
            node("#loginForm") { exists() }
        }
    }

}