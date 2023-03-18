package pro.azhidkov.q6sb.q6springboot.domain

import jakarta.persistence.*


@Entity
@Table(name = "categories")
class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,

    var name: String,

    @ManyToOne
    @JoinColumn(name = "category_group_id")
    var group: CategoryGroup
)