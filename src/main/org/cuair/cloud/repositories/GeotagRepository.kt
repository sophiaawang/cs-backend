package org.cuair.cloud.repositories

import org.cuair.cloud.models.geotag.Geotag
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GeotagRepository : CrudRepository<Geotag, Long>
