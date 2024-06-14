package org.cuair.cloud.repositories

import org.cuair.cloud.models.ODLCUser
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ODLCUserRepository : CrudRepository<ODLCUser, Long>
