package org.cuair.cloud.repositories

import org.cuair.cloud.models.Assignment
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AssignmentRepository : CrudRepository<Assignment, Long>
