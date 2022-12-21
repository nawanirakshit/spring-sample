package rakshit.sample.controller.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
class WebController {

    @GetMapping("/home")
    fun homePage(model: Model): String? {
        println("HOME CALLED >>>>")
        model.addAttribute("appName", "MY APPPPPPPPPPPPPPPPPP")
        return "home"
    }

    @GetMapping("/greeting")
    fun greeting(
        @RequestParam(name = "name", required = false, defaultValue = "World") name: String?,
        model: Model
    ): String? {
        println("GGREETINGS CALLED >>>>>>>>>>>>>>>")
        model.addAttribute("name", name)
        return "greetings"
    }
}