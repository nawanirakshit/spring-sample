package rakshit.sample.controller.file

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import rakshit.sample.filestorage.FileStorage
import rakshit.sample.model.FileUploadResponse
import rakshit.sample.model.NoUploadDataFound

@RestController
class UploadFileController {

    @Autowired
    lateinit var fileStorage: FileStorage

    @GetMapping("/")
    fun index(): String {
        return "multipartfile/uploadform.html"
    }

    @PostMapping("/upload-file")
    fun uploadMultipartFile(@RequestParam("uploadfile") file: MultipartFile): ResponseEntity<Any> {

        val name: String = fileStorage.store(file)
        if (name.isEmpty()) {
            return ResponseEntity(NoUploadDataFound(), HttpStatus.NOT_FOUND)
        }

        val uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/").path(name).toUriString()

        val response = FileUploadResponse(200, name, uri, file.contentType!!, file.size)
        return ResponseEntity(response, HttpStatus.OK)
    }
}