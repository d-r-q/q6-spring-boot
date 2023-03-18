package pro.azhidkov.q6sb.q6springboot.core.groups

import org.springframework.data.jpa.repository.JpaRepository
import pro.azhidkov.q6sb.q6springboot.domain.Group


interface GroupsRepo : JpaRepository<Group, Long>