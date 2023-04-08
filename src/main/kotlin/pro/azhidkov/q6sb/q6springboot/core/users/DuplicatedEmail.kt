package pro.azhidkov.q6sb.q6springboot.core.users

import pro.azhidkov.q6sb.q6springboot.platform.errors.DomainException


class DuplicatedEmail(email: String, cause: Throwable) : DomainException("Duplicated email=$email", cause)
