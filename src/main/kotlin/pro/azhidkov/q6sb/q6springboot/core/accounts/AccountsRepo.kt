package pro.azhidkov.q6sb.q6springboot.core.accounts

import org.springframework.data.jpa.repository.JpaRepository
import pro.azhidkov.q6sb.q6springboot.domain.Account


interface AccountsRepo : JpaRepository<Account, Long>