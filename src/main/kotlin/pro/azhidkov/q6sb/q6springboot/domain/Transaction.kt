package pro.azhidkov.q6sb.q6springboot.domain

import jakarta.persistence.*
import org.hibernate.annotations.Type
import pro.azhidkov.q6sb.q6springboot.platform.CustomStringArrayType
import pro.azhidkov.q6sb.q6springboot.platform.StringArrayType
import java.math.BigDecimal
import java.time.Instant

enum class TransactionType {
    INTERNAL,
    EXTERNAL
}

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "actual_transactions")
abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0

    @ManyToOne
    lateinit open var createdBy: User

    open var amount: Long = 0

    lateinit open var dateTime: Instant

    lateinit open var comment: String

    @Column(columnDefinition = "varchar[]")
    @Type(value = StringArrayType::class)
    lateinit open var tags: List<String>

    @Enumerated(EnumType.STRING)
    lateinit open var type: TransactionType
}