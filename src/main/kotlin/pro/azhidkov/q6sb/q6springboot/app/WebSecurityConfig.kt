package pro.azhidkov.q6sb.q6springboot.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import pro.azhidkov.q6sb.q6springboot.core.CoreConfig
import pro.azhidkov.q6sb.q6springboot.domain.Role

@Import(CoreConfig::class)
@Configuration
class WebSecurityConfig(
    private val userDetailsService: UserDetailsService,
    private val encoder: PasswordEncoder,
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // @formatter:off
        http
            .csrf().disable()
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/app/**").hasAuthority(Role.ROLE_USER.name)
                    .requestMatchers("/**").anonymous()
            }
            .formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/app/main")
        http.authenticationProvider(authenticationProvider())
        // @formatter:on
        return http.build()
    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(encoder)
        return authProvider
    }

}