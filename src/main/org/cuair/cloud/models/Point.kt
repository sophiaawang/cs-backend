package org.cuair.cloud.models

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import org.cuair.cloud.models.plane.target.AlphanumTargetSighting

/** Represents the different colors on the Target  */
@Entity
class Point(
    /**
     * Gets the x coordinate
     * @return the x coordinate
     */
    // @JsonValue
    val x: Int,
    /**
     * Gets the y coordinate
     * @return the y coordinate
     */
    // @JsonValue
    var y: Int
) : CUAirModel() {
    @ManyToOne(cascade = [CascadeType.MERGE])
    protected var targetSighting: AlphanumTargetSighting? = null

    /**
     * Creates a Point
     */
    init {
        y = y
    }

}