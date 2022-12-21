package rakshit.sample.controller.products

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import rakshit.sample.filestorage.FileStorage
import rakshit.sample.model.NoDataFound
import rakshit.sample.model.Product
import rakshit.sample.model.ProductListSuccess
import rakshit.sample.model.ProductSuccess
import rakshit.sample.repository.ProductRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RestController
class ProductsController(private val productRepo: ProductRepository) : ErrorController {

    @Autowired
    lateinit var fileStorage: FileStorage

    /**
     * Registration API
     */
    @PostMapping("/product/add")
    @ResponseBody
    fun addNewUser(
        @RequestParam("product_name") name: String?,
        @RequestParam("product_image") image: MultipartFile?,
        @RequestParam("product_type") type: String?,
        @RequestParam("user_id") user_id: String?,
        @RequestParam("price") price: String?,
        @RequestParam("price_type") price_type: String?,
        @RequestParam("timestamp") timestamp: String?,
        @RequestParam("update_timestamp") update_timestamp: String?,
        @RequestParam("min_buy") min_buy: String?,
    ): ResponseEntity<Any> {
        if (name == null || name.isEmpty()) {
            return ResponseEntity(
                NoDataFound(404, "Enter Product Name(product_name) to continue"), HttpStatus.BAD_REQUEST
            )
        }

        if (type == null || type.isEmpty()) {
            return ResponseEntity(
                NoDataFound(404, "Enter Product Type(product_type) to continue"), HttpStatus.FORBIDDEN
            )
        }

        if (user_id == null || user_id.isEmpty()) {
            return ResponseEntity(NoDataFound(404, "Enter Seller ID(user_id) to continue"), HttpStatus.FORBIDDEN)
        }

        if (price == null || price.isEmpty()) {
            return ResponseEntity(NoDataFound(404, "Enter Product Price(price) to continue"), HttpStatus.FORBIDDEN)
        }

        if (price_type == null || price_type.isEmpty()) {
            return ResponseEntity(NoDataFound(404, "Enter Product Type(price_type) to continue"), HttpStatus.FORBIDDEN)
        }

        if (min_buy == null || min_buy.isEmpty()) {
            return ResponseEntity(NoDataFound(404, "Enter Minimum Buy(min_buy) to continue"), HttpStatus.FORBIDDEN)
        }

        var uri = ""
        if (image != null) {
            val imageSave: String = fileStorage.store(image)
            uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(imageSave)
                .toUriString()
        }

        val localDateTime: LocalDateTime = LocalDateTime.now()
        val format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

        val product = Product(
            product_name = name,
            product_image = uri,
            product_type = type,
            user_id = user_id,
            price = price,
            price_type = price_type,
            timestamp = localDateTime.format(format),
            update_timestamp = localDateTime.format(format),
            min_buy = min_buy
        )
        val saveProduct = productRepo.save(product)

        return ResponseEntity(ProductSuccess(product = saveProduct), HttpStatus.OK)
    }

    @PostMapping("/product/user")
    @ResponseBody
    fun addNewUser(
        @RequestParam("user_id") user_id: String?
    ): ResponseEntity<Any> {

        return if (user_id == null || user_id.isEmpty()) {
            ResponseEntity(
                NoDataFound(404, "Enter User ID(user_id) to continue"), HttpStatus.FORBIDDEN
            )
        } else {
            val listProduct = productRepo.findUserProducts(user_id = user_id)

            if (listProduct.isEmpty()) {
                ResponseEntity(
                    ProductListSuccess(
                        message = "No Products available for this user", product = listProduct
                    ), HttpStatus.OK
                )
            } else ResponseEntity(ProductListSuccess(product = listProduct), HttpStatus.OK)
        }
    }

    @PostMapping("/product/update")
    @ResponseBody
    fun updateProduct(
        @RequestParam("product_id") product_id: String?,
        @RequestParam("product_name") product_name: String?,
        @RequestParam("product_type") product_type: String?,
        @RequestParam("product_price") product_price: String?,
        @RequestParam("product_min_buy") product_min_buy: String?,
        @RequestParam("product_image") image: MultipartFile?
    ): ResponseEntity<Any> {
        if (product_id == null || product_id.isEmpty()) {
            return ResponseEntity(
                NoDataFound(404, "Enter User ID(user_id) to continue"), HttpStatus.FORBIDDEN
            )
        } else {
            val product = productRepo.findProduct(product_id.toLong())

            val productName = if (product_name == null || product_name.isEmpty()) product.product_name else product_name
            val type = if (product_type == null || product_type.isEmpty()) product.price_type else product_type
            val price = if (product_price == null || product_price.isEmpty()) product.price else product_price
            val minBuy = if (product_min_buy == null || product_min_buy.isEmpty()) product.min_buy else product_min_buy

            val localDateTime: LocalDateTime = LocalDateTime.now()
            val format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

            var uri = product.product_image
            if (image != null) {
                val imageSave: String = fileStorage.store(image)
                uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(imageSave)
                    .toUriString()
            }

            productRepo.updateProfile(
                product_id = product_id.toLong(),
                name = productName,
                type = type,
                price = price,
                updateTime = localDateTime.format(format),
                minBuy = minBuy,
                product_image = uri
            )

            val prod = Product(
                product_id = product_id.toLong(),
                product_name = productName,
                product_image = uri,
                product_type = type,
                user_id = product.user_id,
                price = price,
                price_type = type,
                timestamp = product.timestamp,
                update_timestamp = localDateTime.format(format),
                min_buy = minBuy
            )

            return ResponseEntity(
                ProductSuccess(
                    message = "Product updated successfully.", product = prod
                ), HttpStatus.OK
            )

        }
    }


}