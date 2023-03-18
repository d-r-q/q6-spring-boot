package pro.azhidkov.q6sb.q6springboot.domain

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class AccountShareId(
    var groupId: Long,
    var accountId: Long
) : Serializable

@Entity
@Table(name = "account_shares")
class AccountShare(
    @EmbeddedId
    var id: AccountShareId,

    @ManyToOne
    @MapsId("groupId")
    var group: Group,

    @ManyToOne
    @MapsId("accountId")
    var account: Account,

    @Enumerated(EnumType.STRING)
    var mode: ShareMode
)