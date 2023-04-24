package rakshit.sample.controller.web

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import rakshit.sample.config.JasyptConfig
import rakshit.sample.model.Users
import rakshit.sample.repository.UserRepository
import javax.servlet.http.HttpSession

@Controller
class WebController(private val usersRepo: UserRepository) : ErrorController {

    @GetMapping("/greeting")
    fun greeting(
        @RequestParam(name = "name", required = false, defaultValue = "World") name: String?,
        model: Model,
        session: HttpSession
    ): String? {

        val user = session.getAttribute("USER_DETAILS") as Users

        println("NAME >>>>>>>>> ${user.name}")

        model.addAttribute("name", user.name)
        return "greetings"
    }

    @GetMapping("/login")
    fun loginPage(session: HttpSession): String? {
        return if (session.getAttribute("USER_DETAILS") != null) {
            "greetings"
        } else "login"
    }

    @PostMapping("/login")
    fun userLogin(
        @RequestParam("email") email: String,
        @RequestParam("password") password: String,
        model: Model,
        session: HttpSession
    ): String? {

        if (email.isEmpty() && password.isEmpty()) {
            model.addAttribute("loginError", "Email and password can not be empty");
            return "login"
        }

        if (email.isEmpty()) {
            model.addAttribute("loginError", "Email can not be empty");
            return "login"
        }

        if (password.isEmpty()) {
            model.addAttribute("loginError", "Password can not be empty");
            return "login"
        }

        //check if entered email available or not
        if (usersRepo.existsByEmail(email)) {
            try {

                val userDetails = usersRepo.findUser(email)
                val decryptedPassword = decryptKey(userDetails.password)

                if (password == decryptedPassword) {
                    userDetails.password = ""

                    session.setAttribute("USER_DETAILS", userDetails)

                    model.addAttribute("name", userDetails.name)
                    return "greetings"
                } else {
                    model.addAttribute("loginError", "Invalid credentials entered, Try again");
                }

            } catch (e: Exception) {
                model.addAttribute("loginError", "Invalid credentials entered, Try again");
            }
            return "login"
        } else {
            model.addAttribute("loginError", "Email not available in our database, Try again");
            return "login"
        }
    }


    /**
     * Decryption of user password
     */
    private fun decryptKey(encryptedKey: String): String {
        val jsp = JasyptConfig()
        val pbeConfig: SimpleStringPBEConfig = jsp.getSimpleStringPBEConfig()!!
        val pbeStringEncryptor = PooledPBEStringEncryptor()
        pbeStringEncryptor.setConfig(pbeConfig)
        return pbeStringEncryptor.decrypt(encryptedKey)
    }


}