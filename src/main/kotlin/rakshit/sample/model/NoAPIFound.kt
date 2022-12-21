package rakshit.sample.model

data class NoAPIFound(
    val status: Int = 404, val message: String = "API not found, check API and try again"
)

data class NoDataFound(
    val status: Int = 404, val message: String = "User not found, try again"
)

data class NoUploadDataFound(
    val status: Int = 404,
    val message: String = "Fill all field to continue",
    val nodata: NoDataFound = NoDataFound(404, "User not fund try again")
)

data class RegisterSuccess(
    val status: Int = 200, val message: String = "Registered successfully", val user: Users
)