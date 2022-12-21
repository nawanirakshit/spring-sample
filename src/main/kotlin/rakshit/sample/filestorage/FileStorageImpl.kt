package rakshit.sample.filestorage

import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream
import javax.annotation.PostConstruct
import org.springframework.util.StringUtils;
import rakshit.sample.exception.StorageException
import java.nio.file.StandardCopyOption

@Service
class FileStorageImpl : FileStorage {

    val log = LoggerFactory.getLogger(this::class.java)
    val rootLocation = Paths.get("filestorage")

    override fun store(file: MultipartFile): String {
        val filename: String = StringUtils.cleanPath(file.originalFilename!!)

        try {
            if (file.isEmpty) {
                return ""
//                throw StorageException("Failed to store empty file $filename");
//                return ""
            }
            if (filename.contains("..")) {
                throw StorageException(
                    "Cannot store file with relative path outside current directory $filename"
                )
            }

            Files.copy(
                file.inputStream,
                this.rootLocation.resolve(file.originalFilename!!),
                StandardCopyOption.REPLACE_EXISTING
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return filename
    }

    override fun loadFile(filename: String): Resource {
        val file = rootLocation.resolve(filename)
        val resource = UrlResource(file.toUri())

        if (resource.exists() || resource.isReadable) {
            return resource
        } else {
            throw RuntimeException("FAIL!")
        }
    }

    override fun deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile())
    }

    @PostConstruct
    override fun init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectory(rootLocation)
            }
        } catch (e: IOException) {
            throw StorageException("Could not initialize storage location", e)
        }
    }

    override fun loadFiles(): Stream<Path> {
        return Files.walk(this.rootLocation, 1).filter { path -> !path.equals(this.rootLocation) }
            .map(this.rootLocation::relativize)
    }
}