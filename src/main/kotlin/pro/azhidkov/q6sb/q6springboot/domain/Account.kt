package pro.azhidkov.q6sb.q6springboot.domain

import jakarta.persistence.*
import java.math.BigDecimal

enum class AccountType {
    CURRENT,
    SAVINGS
}

@Entity
@Table(name = "accounts")
class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    var name: String,

    @ManyToOne
    var owner: User,

    @ManyToOne
    @JoinColumn(name = "currency_id")
    var currency: Currency,

    @Enumerated(EnumType.STRING)
    var type: AccountType,

    var balance: Long
)
