package pro.azhidkov.q6sb.q6springboot.platform.errors


open class DomainException(msg: String? = null, cause: Throwable? = null) : RuntimeException(msg, cause)