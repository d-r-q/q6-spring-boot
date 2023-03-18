package pro.azhidkov.q6sb.q6springboot.domain

import jakarta.persistence.*
import org.hibernate.annotations.Type
import pro.azhidkov.q6sb.q6springboot.platform.StringArrayType
import java.math.BigDecimal
import java.time.Instant


class InternalTransaction(
    override var id: Long,
    override var createdBy: User,
    override var amount: Long,
    override var dateTime: Instant,
    override var comment: String,
    override var tags: List<String>,
    override var type: TransactionType,

    @ManyToOne
    var sourceAccount: Account,

    @ManyToOne
    var targetAccount: Account,

    var rate: BigDecimal
) : Transaction()