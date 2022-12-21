package rakshit.sample.config

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JasyptConfig {

    @Bean(name = ["jasyptStringEncryptor"])
    fun encryptor(): StringEncryptor {
        val pbeStringEncryptor = PooledPBEStringEncryptor()
        pbeStringEncryptor.setConfig(getSimpleStringPBEConfig())
        return pbeStringEncryptor
    }

    fun getSimpleStringPBEConfig(): SimpleStringPBEConfig {
        val pbeConfig = SimpleStringPBEConfig()
        //can be picked via the environment variablee
        //TODO - hardcoding to be removed
        pbeConfig.password = "javacodegeek" //encryptor private key
        pbeConfig.algorithm = "PBEWithMD5AndDES"
        pbeConfig.setKeyObtentionIterations("1000")
        pbeConfig.setPoolSize("1")
        pbeConfig.providerName = "SunJCE"
        pbeConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator")
        pbeConfig.stringOutputType = "base64"
        return pbeConfig
    }
}