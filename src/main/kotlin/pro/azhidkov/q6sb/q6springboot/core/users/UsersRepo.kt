package pro.azhidkov.q6sb.q6springboot.core.users

import org.springframework.data.jpa.repository.JpaRepository
import pro.azhidkov.q6sb.q6springboot.domain.User


interface UsersRepo: JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

}