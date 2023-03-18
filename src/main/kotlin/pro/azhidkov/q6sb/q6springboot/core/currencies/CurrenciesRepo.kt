package pro.azhidkov.q6sb.q6springboot.core.currencies

import org.springframework.data.jpa.repository.JpaRepository
import pro.azhidkov.q6sb.q6springboot.domain.Currency


interface CurrenciesRepo : JpaRepository<Currency, String>