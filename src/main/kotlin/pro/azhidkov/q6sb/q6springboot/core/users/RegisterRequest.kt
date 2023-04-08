package pro.azhidkov.q6sb.q6springboot.core.users

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)