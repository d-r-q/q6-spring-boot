package pro.azhidkov.q6sb.q6springboot.app.main

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainPageController {

    @GetMapping("/app/main")
    fun getMain(): String {
        return "app/main"
    }

}