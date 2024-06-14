package org.cuair.cloud.repositories

import org.cuair.cloud.models.plane.target.AlphanumTarget
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AlphanumTargetRepository : CrudRepository<AlphanumTarget, Long>
