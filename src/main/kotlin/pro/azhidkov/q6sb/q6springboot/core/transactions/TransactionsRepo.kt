package pro.azhidkov.q6sb.q6springboot.core.transactions

import org.springframework.data.jpa.repository.JpaRepository
import pro.azhidkov.q6sb.q6springboot.domain.Transaction


interface TransactionsRepo : JpaRepository<Transaction, Long>