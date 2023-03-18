package pro.azhidkov.q6sb.q6springboot.domain

import jakarta.persistence.*
import org.hibernate.annotations.Type
import pro.azhidkov.q6sb.q6springboot.platform.StringArrayType
import java.time.Instant


@Entity
@Table(name = "planned_transactions")
class PlannedTransaction (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    @ManyToOne
    var createdBy: User,
    var amount: Long,
    var dateTime: Instant,
    var comment: String,
    @Column(columnDefinition = "varchar[]")
    @Type(value = StringArrayType::class)
    var tags: List<String>,

    @ManyToOne
    var sourceAccount: Account,

    @ManyToOne
    var category: Category,
)