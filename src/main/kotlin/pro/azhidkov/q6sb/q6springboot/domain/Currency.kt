package pro.azhidkov.q6sb.q6springboot.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "currencies")
class Currency(
    @Id
    var code: String
)
