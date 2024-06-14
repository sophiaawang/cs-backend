package org.cuair.cloud.repositories

import org.cuair.cloud.models.plane.target.TargetSighting
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TargetSightingRepository : CrudRepository<TargetSighting, Long>
