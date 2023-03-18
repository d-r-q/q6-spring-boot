package pro.azhidkov.q6sb.q6springboot.domain

import jakarta.persistence.*
import org.hibernate.annotations.Type
import pro.azhidkov.q6sb.q6springboot.platform.CustomStringArrayType

enum class Role {
    ROLE_USER,
    ROLE_ADMIN
}

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    var email: String,

    var name: String,

    var password: String,

    @Column(columnDefinition = "text[]")
    @Type(value = CustomStringArrayType::class)
    var roles: Array<Role>,

    @ManyToMany
    @JoinTable(
        name = "user_groups",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "group_id")]
    )
    var groups: Set<Group>,
)