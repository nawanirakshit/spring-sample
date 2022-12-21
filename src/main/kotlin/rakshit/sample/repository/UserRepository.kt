package rakshit.sample.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import rakshit.sample.model.Users

@Repository
interface UserRepository : JpaRepository<Users, Long> {

    @Query("select user from Tbl_User user where user.email = :email")
    fun findUser(email: String): Users

    @Query("select user from Tbl_User user where user.id = :user_id")
    fun findUserByID(user_id: Long): Users

    @Query("select user from Tbl_User user where user.email = :email AND user.password = :password")
    fun checkLogin(email: String, password: String): Users

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Tbl_User user set user.bio =:bio, user.address =:address, user.name= :name, user.user_image= :user_image, user.updatedAt=:updatedAt WHERE user.id =:id")
    fun updateProfile(bio: String, address: String, id: Long, name: String, user_image: String, updatedAt: String)

    fun existsByEmail(email: String): Boolean

    fun existsByMobile(phone: String): Boolean

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Tbl_User user set user.password =:password WHERE user.email =:email")
    fun updatePassword(password: String, email: String)
}