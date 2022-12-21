package rakshit.sample.controller.user

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import rakshit.sample.config.JasyptConfig
import rakshit.sample.filestorage.FileStorage
import rakshit.sample.model.*
import rakshit.sample.repository.UserRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
class UserDetailController(private val usersRepo: UserRepository) : ErrorController {

    @Autowired
    lateinit var fileStorage: FileStorage

    @RequestMapping("/error")
    fun error(): ResponseEntity<NoAPIFound> = ResponseEntity(NoAPIFound(), HttpStatus.NOT_FOUND)

    /**
     * Registration API
     */
    @PostMapping("/user/register")
    @ResponseBody
    fun addNewUser(
        @RequestParam("email") email: String?,
        @RequestParam("name") name: String?,
        @RequestParam("mobile") mobile: String?,
        @RequestParam(required = false, name = "address") address: String?,
        @RequestParam(required = false, name = "bio") bio: String?,
        @RequestParam("password") password: String?,
        @RequestParam(required = false, name = "profile_image") file: MultipartFile?
    ): ResponseEntity<Any> {
        if (email == null || email.isEmpty()) {
            return ResponseEntity(NoDataFound(404, "Enter email to continue"), HttpStatus.FORBIDDEN)
        } else if (usersRepo.existsByEmail(email)) {
            return ResponseEntity(
                NoDataFound(404, "Email already available in database, Login instead"), HttpStatus.FORBIDDEN
            )
        }

        if (password == null || password.isEmpty()) {
            return ResponseEntity(NoDataFound(404, "Enter password to continue"), HttpStatus.FORBIDDEN)
        }

        if (name == null || name.isEmpty()) {
            return ResponseEntity(NoDataFound(404, "Enter name to continue"), HttpStatus.FORBIDDEN)
        }

        if (mobile == null || mobile.isEmpty()) {
            return ResponseEntity(NoDataFound(404, "Enter mobile to continue"), HttpStatus.FORBIDDEN)
        } else if (usersRepo.existsByMobile(mobile)) {
            return ResponseEntity(
                NoDataFound(404, "Mobile already available in database, Login instead"), HttpStatus.FORBIDDEN
            )
        }

        var uri = ""
        if (file != null) {
            val imageSave: String = fileStorage.store(file)
            uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(imageSave)
                .toUriString()
        }

        val userAddress = address ?: ""
        val userBio = bio ?: ""
        val encryptedPassword: String = encryptKey(password)

        val localDateTime: LocalDateTime = LocalDateTime.now()
        val format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

        val user = Users(
            email = email,
            name = name,
            mobile = mobile,
            address = userAddress,
            bio = userBio,
            user_image = uri,
            password = encryptedPassword,
            createdAt = localDateTime.format(format),
            updatedAt = localDateTime.format(format)
        )
        val saved = usersRepo.save(user)
        saved.password = ""
        return ResponseEntity(RegisterSuccess(user = saved), HttpStatus.OK)
    }

    @PostMapping("/user/check_encrypt")
    fun checkEncryption(@RequestParam("password") password: String): ResponseEntity<Any> {
        return ResponseEntity(
            encryptKey(password), HttpStatus.OK
        )
    }

    @PostMapping("/user/check_decrypt")
    fun checkDecryption(@RequestParam("password") password: String): ResponseEntity<Any> {
        return ResponseEntity(
            decryptKey(password), HttpStatus.OK
        )
    }

    /**
     * Encryption of user password
     */
    private fun encryptKey(plainKey: String): String {
        val jsp = JasyptConfig()
        val pbeConfig: SimpleStringPBEConfig = jsp.getSimpleStringPBEConfig()!!
        val pbeStringEncryptor = PooledPBEStringEncryptor()
        pbeStringEncryptor.setConfig(pbeConfig)
        return pbeStringEncryptor.encrypt(plainKey)
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

    /**
     * Login API
     */
    @PostMapping("/user/login")
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
                        RegisterSuccess(message = "Logged in successfully", user = userDetails), HttpStatus.OK
                    )
                } else {
                    ResponseEntity(
                        NoDataFound(message = "Invalid credentials entered, Try again"), HttpStatus.NOT_FOUND
                    )
                }

            } catch (e: Exception) {
                //User not available/ invalid credentials
                ResponseEntity(
                    NoDataFound(message = "Invalid credentials entered, Try again"), HttpStatus.NOT_FOUND
                )
            }
        } else {
            return ResponseEntity(
                NoDataFound(message = "Email not available in our database, Try again"), HttpStatus.NOT_FOUND
            )
        }
    }

    @GetMapping("/user/find")
    fun backend(
        @RequestParam("email") s: String
    ): Any {

        return if (usersRepo.existsByEmail(s)) {
            val data = usersRepo.findUser(s)
            data.password = ""
            ResponseEntity(data, HttpStatus.OK)
        } else {
            ResponseEntity(NoDataFound(), HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/user/update/profile")
    fun updateUserProfile(
        @RequestParam("user_id") user_id: String?,
        @RequestParam("bio") bio: String?,
        @RequestParam("address") address: String?,
        @RequestParam("name") name: String?,
        @RequestParam("product_image") image: MultipartFile?
    ): ResponseEntity<Any> {

        if (user_id == null || user_id.isEmpty()) {
            return ResponseEntity(
                NoDataFound(404, "Enter User ID(user_id) to continue"), HttpStatus.FORBIDDEN
            )
        } else {
            //bio: String, address: String, id: Long, name: String
            val user = usersRepo.findUserByID(user_id.toLong())

            val userBio = if (bio == null || bio.isEmpty()) user.bio else bio
            val address = if (address == null || address.isEmpty()) user.address else address
            val name = if (name == null || name.isEmpty()) user.name else name

            val localDateTime: LocalDateTime = LocalDateTime.now()
            val format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

            var uri = user.user_image
            if (image != null) {
                val imageSave: String = fileStorage.store(image)
                uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(imageSave)
                    .toUriString()
            }

            usersRepo.updateProfile(
                bio = userBio,
                address = address,
                id = user_id.toLong(),
                name = name,
                user_image = uri,
                updatedAt = localDateTime.format(format)
            )

            val userResponse = Users(
                id = user_id.toLong(),
                name = name,
                address = address,
                bio = userBio,
                email = user.email,
                mobile = user.mobile,
                user_image = uri,
                password = "",
                createdAt = user.createdAt,
                updatedAt = localDateTime.format(format)
            )

            return ResponseEntity(
                RegisterSuccess(
                    message = "Profile updated successfully.", user = userResponse
                ), HttpStatus.OK
            )
        }
    }

    @GetMapping("/user/change_password")
    fun changePassword(
        @RequestParam("email") email: String?,
        @RequestParam("old_password") old_password: String?,
        @RequestParam("new_password") new_password: String?
    ): ResponseEntity<Any> {

        return if (email == null || email.isEmpty()) {
            ResponseEntity(
                NoDataFound(message = "Email not available in our database, Try again"), HttpStatus.NOT_FOUND
            )
        } else if (old_password == null || old_password.isEmpty()) {
            ResponseEntity(
                NoDataFound(message = "Enter old password and try again"), HttpStatus.NOT_FOUND
            )
        } else if (new_password == null || new_password.isEmpty()) {
            ResponseEntity(
                NoDataFound(message = "Enter new password and try again"), HttpStatus.NOT_FOUND
            )
        } else {
            if (usersRepo.existsByEmail(email)) {
                val data = usersRepo.findUser(email)

                val decryptPassword = decryptKey(data.password)

                if (decryptPassword == old_password) {
                    usersRepo.updatePassword(encryptKey(new_password), email)
                }
                ResponseEntity(
                    NoDataFound(message = "Password Changed Successfully"), HttpStatus.NOT_FOUND
                )
            } else {
                ResponseEntity(NoDataFound(), HttpStatus.NOT_FOUND)
            }
        }
    }
}