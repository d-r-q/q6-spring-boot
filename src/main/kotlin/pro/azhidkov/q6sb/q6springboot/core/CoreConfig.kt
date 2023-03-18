package pro.azhidkov.q6sb.q6springboot.core

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@EnableAutoConfiguration
@EnableJpaRepositories
@ComponentScan
@Configuration
class CoreConfig {

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}