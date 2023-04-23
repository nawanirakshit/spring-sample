package rakshit.sample.controller.web

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import rakshit.sample.config.JasyptConfig
import rakshit.sample.model.NoDataFound
import rakshit.sample.model.RegisterSuccess
import rakshit.sample.repository.UserRepository
import javax.validation.Valid

@Controller
class WebController(private val usersRepo: UserRepository) : ErrorController {

    @GetMapping("/home")
    fun homePage(model: Model): String? {
        model.addAttribute("appName", "MY APPPPPPPPPPPPPPPPPP")
        return "home"
    }

    @GetMapping("/greeting")
    fun greeting(
        @RequestParam(name = "name", required = false, defaultValue = "World") name: String?,
        model: Model
    ): String? {
        model.addAttribute("name", name)
        return "greetings"
    }

    @GetMapping("/login")
    fun loginPage(): String? {
        return "login"
    }


    @PostMapping("/user/login.html")
    fun userLogin(
        @RequestParam("email") email: String, @RequestParam("password") password: String
    ): ResponseEntity<Any> {

        //check if entered email available or not
        if (usersRepo.existsByEmail(email)) {
            return try {

                val userDetails = usersRepo.findUser(email)
                val decryptedPassword = decryptKey(userDetails.password)

                if (password == decryptedPassword) {
                    userDetails.password = ""
                    ResponseEntity(
                        RegisterSuccess(message = "Logged in successfully", user = userDetails),
                        HttpStatus.OK
                    )
                } else {
                    ResponseEntity(
                        NoDataFound(message = "Invalid credentials entered, Try again"),
                        HttpStatus.NOT_FOUND
                    )
                }

            } catch (e: Exception) {
                //User not available/ invalid credentials
                ResponseEntity(
                    NoDataFound(message = "Invalid credentials entered, Try again"),
                    HttpStatus.NOT_FOUND
                )
            }
        } else {
            return ResponseEntity(
                NoDataFound(message = "Email not available in our database, Try again"),
                HttpStatus.NOT_FOUND
            )
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