package pro.azhidkov.q6sb.q6springboot.app

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.q6sb.q6springboot.core.users.UsersService

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)

@Controller
class Controller(
    private val usersService: UsersService
) {

    @GetMapping("/login")
    fun get(): ModelAndView {
        return ModelAndView("login")
    }

    @GetMapping("/registration")
    fun getRegistrationPage(): ModelAndView {
        return ModelAndView("register")
    }

    @PostMapping("/register")
    fun register(@ModelAttribute registerRequest: RegisterRequest): String {
        usersService.register(registerRequest)
        return "successful-registration"
    }

    @GetMapping("/app/main")
    fun getMain(): String {
        return "app/main"
    }
}