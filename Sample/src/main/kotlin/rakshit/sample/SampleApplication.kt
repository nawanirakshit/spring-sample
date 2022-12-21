package rakshit.sample

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableEncryptableProperties
class SampleApplication

fun main(args: Array<String>) {
    runApplication<SampleApplication>(*args)
}
