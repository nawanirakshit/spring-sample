package rakshit.sample.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "Tbl_Product")
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val product_id: Long = 0,
    var product_name: String = "",
    var product_image: String = "",
    var product_type: String = "",
    var user_id: String = "",
    var price: String = "",
    var price_type: String = "",
    var timestamp: String = "",
    var update_timestamp: String = "",
    var min_buy: String = ""
) {
    @Override
    override fun toString(): String {
        return this::class.simpleName + "(product_id = $product_id , product_name = $product_name , product_image = $product_image , product_type = $product_type , user_id = $user_id , price = $price , price_type = $price_type , timestamp = $timestamp , update_timestamp = $update_timestamp , min_buy = $min_buy )"
    }
}

data class ProductSuccess(
    val status: Int = 200, val message: String = "Product added successfully", val product: Product
)

data class ProductListSuccess(
    val status: Int = 200, val message: String = "Product fetched", val product: List<Product>
)