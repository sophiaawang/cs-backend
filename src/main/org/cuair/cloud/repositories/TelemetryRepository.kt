package org.cuair.cloud.repositories

import org.cuair.cloud.models.geotag.Telemetry
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TelemetryRepository : CrudRepository<Telemetry, Long>
