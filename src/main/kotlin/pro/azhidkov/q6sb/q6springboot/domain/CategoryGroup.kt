package pro.azhidkov.q6sb.q6springboot.domain

import jakarta.persistence.*

enum class CategoryType {
    INCOME,
    EXPENSE
}

@Entity
@Table(name = "category_groups")
class CategoryGroup(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,

    var name: String,

    @ManyToOne
    var owner: User,

    @Enumerated(EnumType.STRING)
    var type: CategoryType,

    @OneToMany(mappedBy = "parent")
    var children: Set<CategoryGroup>,

    @ManyToOne
    var parent: CategoryGroup?,

    @OneToMany(mappedBy = "group")
    var categories: Set<Category>
)
