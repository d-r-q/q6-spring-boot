package pro.azhidkov.q6sb.q6springboot.core.users

import pro.azhidkov.q6sb.q6springboot.domain.Role
import pro.azhidkov.q6sb.q6springboot.domain.User

class UserDto(
    var id: Long,
    var email: String,
    var name: String,
    var password: String,
    var roles: Array<Role>
) {

    constructor(user: User) : this(user.id, user.email, user.name, user.password, user.roles)

}
