package pro.azhidkov.q6sb.q6springboot.core.categories

import org.springframework.data.jpa.repository.JpaRepository
import pro.azhidkov.q6sb.q6springboot.domain.CategoryGroup


interface CategoryGroupsRepo : JpaRepository<CategoryGroup, Long>