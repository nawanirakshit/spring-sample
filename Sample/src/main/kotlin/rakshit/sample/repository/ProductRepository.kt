package rakshit.sample.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import rakshit.sample.model.Product
import java.util.*

@Repository
interface ProductRepository : JpaRepository<Product, Long> {

    @Query("SELECT product from Tbl_Product product where product.product_id = :product_id")
    fun findProduct(product_id: Long): Product


    //    @Query("SELECT product from Tbl_Product product where product.product_id = :product_id")
    override fun findById(product_id: Long): Optional<Product>


    @Query("select product from Tbl_Product product where product.user_id = :user_id")
    fun findUserProducts(user_id: String): List<Product>

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Tbl_Product product set product.product_name =:name, product.product_type =:type, product.price= :price, product.update_timestamp= :updateTime, product.min_buy= :minBuy, product.product_image=:product_image WHERE product.product_id =:product_id")
    fun updateProfile(
        product_id: Long,
        name: String,
        type: String,
        price: String,
        updateTime: String,
        minBuy: String,
        product_image: String
    )
}