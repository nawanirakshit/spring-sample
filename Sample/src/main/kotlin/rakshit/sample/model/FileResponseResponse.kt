package rakshit.sample.model

data class FileUploadResponse(
    val status: Int = 200, val name: String, val uri: String, val type: String, val size: Long
) {
    @Override
    override fun toString(): String {
        return this::class.simpleName + "(status = $status , name = $name , uri = $uri , type = $type )"
    }
}