package pro.azhidkov.q6sb.q6springboot.domain

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class CategoryGroupShareId(
    var groupId: Long,
    var categoryGroupId: Long
) : Serializable

@Entity
@Table(name = "category_group_shares")
class CategoryGroupShare(
    @EmbeddedId
    var id: CategoryGroupShareId,

    @ManyToOne
    @MapsId("groupId")
    var group: Group,

    @ManyToOne
    @MapsId("categoryGroupId")
    var categoryGroup: CategoryGroup,

    @Enumerated(EnumType.STRING)
    var mode: ShareMode
)