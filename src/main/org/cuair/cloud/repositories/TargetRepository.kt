package org.cuair.cloud.repositories

import org.cuair.cloud.models.plane.target.Target
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TargetRepository : CrudRepository<Target, Long>
