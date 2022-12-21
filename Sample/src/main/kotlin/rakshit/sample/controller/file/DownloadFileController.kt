package rakshit.sample.controller.file

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import rakshit.sample.filestorage.FileStorage;
import rakshit.sample.model.FileInfo;
import org.springframework.web.bind.annotation.RestController

@RestController
class DownloadFileController {

    @Autowired
    lateinit var fileStorage: FileStorage

    /*
     * Retrieve Files' Information
     */
    @GetMapping("/files")
    fun getListFiles(model: Model): ResponseEntity<List<FileInfo>> {

        val fileInfos: List<FileInfo> = fileStorage.loadFiles().map { path ->
            FileInfo(
                path.fileName.toString(), MvcUriComponentsBuilder.fromMethodName(
                    DownloadFileController::class.java, "downloadFile", path.fileName.toString()
                ).build().toString()
            )
        }.collect(Collectors.toList())

        return ResponseEntity(fileInfos, HttpStatus.OK)
  }

    /*
     * Download Files
     */
    @GetMapping("/files/{filename}")
    fun downloadFile(@PathVariable filename: String): ResponseEntity<Resource> {
        val file = fileStorage.loadFile(filename)

        println("URL >>>>> ${file.url}")
        println("ABSOLUTE PATH >>>> %${file.file.absolutePath}")
        println("ABSOLUTE FILE >>>>> ${file.file.absoluteFile}")
        println("CONONICAL PATGH >>>>> ${file.file.canonicalPath}")
        println("PATH >>>>> ${file.file.path}")

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.filename + "\"").body(file);
    }
}