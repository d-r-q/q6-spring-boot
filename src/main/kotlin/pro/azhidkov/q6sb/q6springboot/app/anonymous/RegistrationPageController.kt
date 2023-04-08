package pro.azhidkov.q6sb.q6springboot.app.anonymous

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.q6sb.q6springboot.core.users.RegisterRequest
import pro.azhidkov.q6sb.q6springboot.core.users.UsersService

@Controller
class RegistrationPageController(
    private val usersService: UsersService
) {

    @GetMapping("/register")
    fun getRegistrationPage(): ModelAndView {
        return ModelAndView("register")
    }

    @PostMapping("/register")
    fun register(@ModelAttribute registerRequest: RegisterRequest): ResponseEntity<Unit> {
        usersService.register(registerRequest)
        return ResponseEntity.status(HttpStatus.FOUND)
            .header("Location", "successful-registration")
            .build()
    }

    @GetMapping("/successful-registration")
    fun getSuccessfulRegistrationPage(): ModelAndView {
        return ModelAndView("successful-registration")
    }

}