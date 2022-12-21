package rakshit.sample.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "Tbl_User")
data class Users(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
    var name: String = "",
    var address: String = "",
    var bio: String = "",
    var email: String = "",
    var mobile: String = "",
    var user_image: String = "",
    var password: String = "",
    var createdAt: String = "",
    var updatedAt: String = ""
) {
    @Override
    override fun toString(): String {
        return this::class.simpleName + "(" + "id = $id , " + "name = $name , " + "address = $address ," + "bio = $bio , " + "email = $email , " + "mobile = $mobile , " + "user_image = $user_image , " + "createdAt = $createdAt , " + "updatedAt = $updatedAt  )"
    }
}