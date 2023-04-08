package pro.azhidkov.q6sb.q6springboot.platform.kotlin

fun <T> Result<T>.throwIt(): Nothing {
    checkNotNull(this.exceptionOrNull())
    throw this.exceptionOrNull()!!
}
