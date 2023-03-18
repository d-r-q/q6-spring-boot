package pro.azhidkov.q6sb.q6springboot.core.budgets

import org.springframework.data.jpa.repository.JpaRepository
import pro.azhidkov.q6sb.q6springboot.domain.PlannedTransaction


interface PlannedTransactionsRepo : JpaRepository<PlannedTransaction, Long>