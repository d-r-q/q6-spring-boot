package pro.azhidkov.q6sb.q6springboot.app.anonymous

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class LoginPageController {

    @GetMapping("/login")
    fun get(): ModelAndView {
        return ModelAndView("login")
    }

}