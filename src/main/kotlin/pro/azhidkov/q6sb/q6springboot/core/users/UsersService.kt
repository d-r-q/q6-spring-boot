package pro.azhidkov.q6sb.q6springboot.core.users

import jakarta.transaction.Transactional
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import pro.azhidkov.q6sb.q6springboot.app.RegisterRequest
import pro.azhidkov.q6sb.q6springboot.domain.Role
import pro.azhidkov.q6sb.q6springboot.domain.User


const val USER_REGISTERED_EVENTS_QUEUE = "UserRegistered"

@Service
class UsersService(
    private val usersRepo: UsersRepo,
    private val passwordEncoder: PasswordEncoder,
    private val rabbitTemplate: RabbitTemplate
) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails? {
        val user =
            usersRepo.findByEmail(username) ?: throw UsernameNotFoundException("User with email $username not found")
        return org.springframework.security.core.userdetails.User(
            user.email, user.password, user.roles.map { SimpleGrantedAuthority(it.name) }
        )
    }

    @Transactional
    fun register(registerRequest: RegisterRequest): Long {
        val user = usersRepo.save(
            User(
                0,
                registerRequest.email,
                registerRequest.name,
                passwordEncoder.encode(registerRequest.password),
                arrayOf(Role.ROLE_USER),
                emptySet()
            )
        )

        rabbitTemplate.convertAndSend(USER_REGISTERED_EVENTS_QUEUE, UserRegisteredEvent(user.id.toString()))

        return user.id
    }

    fun findById(userId: Long): UserDto? {
        return usersRepo.findByIdOrNull(userId)?.let { UserDto(it) }
    }

}