package org.cuair.cloud.repositories

import org.cuair.cloud.models.plane.target.AlphanumTargetSighting
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AlphanumTargetSightingRepository : CrudRepository<AlphanumTargetSighting, Long>
