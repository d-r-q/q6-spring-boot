package pro.azhidkov.q6sb.q6springboot.app.anonymous

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.q6sb.q6springboot.core.users.DuplicatedEmail
import pro.azhidkov.q6sb.q6springboot.core.users.RegisterRequest
import pro.azhidkov.q6sb.q6springboot.core.users.UsersService
import pro.azhidkov.q6sb.q6springboot.platform.kotlin.throwIt

@Controller
class RegistrationPageController(
    private val usersService: UsersService
) {

    @GetMapping("/register")
    fun getRegistrationPage(): ModelAndView {
        return ModelAndView("register")
            .addObject("model", RegisterRequest("", "", ""))
            .addObject("duplicatedEmail", false)
    }

    @PostMapping("/register")
    fun register(@ModelAttribute registerRequest: RegisterRequest): ModelAndView {
        val res = Result.runCatching { usersService.register(registerRequest) }
        return when {
            res.isSuccess -> ModelAndView("redirect:successful-registration")

            res.exceptionOrNull() is DuplicatedEmail -> ModelAndView("register :: form")
                .addObject("model", registerRequest)
                .addObject("duplicatedEmail", true)

            else -> res.throwIt()
        }
    }

    @GetMapping("/successful-registration")
    fun getSuccessfulRegistrationPage(): ModelAndView {
        return ModelAndView("successful-registration")
    }

}