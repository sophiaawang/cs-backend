package org.cuair.cloud.models.geotag

import org.cuair.cloud.models.CUAirModel
import org.cuair.cloud.models.plane.target.AlphanumTarget
import org.cuair.cloud.models.plane.target.AlphanumTargetSighting
import org.cuair.cloud.models.plane.target.EmergentTarget
import org.cuair.cloud.models.plane.target.EmergentTargetSighting
import org.cuair.cloud.models.plane.target.TargetSighting
import org.cuair.cloud.models.plane.target.Target
import org.cuair.cloud.util.Geotagging
import org.slf4j.LoggerFactory
import java.util.Objects
import jakarta.persistence.Embedded
import jakarta.persistence.Entity

/** Represents the position and orientation of an object on the ground  */
@Entity
class Geotag(
    /** The GPS coordinates of this geotag  */
    @Embedded
    var gpsLocation: GpsLocation?,
    /** The orientation of this geotag represented as radians from north clockwise (NE is clockwise from N, etc.)  */
    var clockwiseRadiansFromNorth: Double?
) : CUAirModel() {

    /**
     * Creates a new geotag with the target sighting
     *
     * @param sighting The TargetSighting of this geotag
     */
    constructor(sighting: TargetSighting?) : this(null, null) {
        if (sighting == null) {
            return
        }
        val assignment = sighting.assignment ?: return
        val image = assignment.image ?: return
        val telemetry = image.telemetry
        val fov = image.fov
        val gps = telemetry.getGps()
        val altitude = telemetry.getAltitude()
        val pixelx = sighting.getpixelx()!!.toDouble()
        val pixely = sighting.getpixely()!!.toDouble()
        val planeRoll = telemetry.getGimOrt().getRoll()
        val planePitch = telemetry.getGimOrt().getPitch()
        val planeYaw = telemetry.getPlaneYaw()
        val centerLongitude = gps.getLongitude()
        val centerLatitude = gps.getLatitude()
        gpsLocation = Geotagging
            .getPixelCoordinates(centerLatitude, centerLongitude, altitude, fov, pixelx, pixely, planeRoll, planePitch, planeYaw)
        clockwiseRadiansFromNorth = Geotagging.calculateClockwiseRadiansFromNorth(planeYaw, sighting.radiansFromTop)
    }

    /**
     * Determines if the given object is logically equal to this Geotag
     *
     * @param other The object to compare
     * @return True if the object equals this Geotag
     */
    override fun equals(other: Any?): Boolean {
        if (other !is Geotag) return false
        if (Radian.equals(clockwiseRadiansFromNorth, other.clockwiseRadiansFromNorth)) {
            return false
        }
        return Objects.deepEquals(gpsLocation, other.gpsLocation)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(GpsLocation::class.java)
        /**
         * Checks to see if geotag can be set for target sighting
         *
         * @param sighting the target sighting
         */
        private fun canSetGeotag(sighting: TargetSighting): Boolean {
            if (sighting is AlphanumTargetSighting) {
                if (sighting.isOffaxis!!) {
                    return false
                }
            }
            val assignment = sighting.assignment ?: return false
            assignment.image ?: return false
            return true
        }

        /**
         * Checks if geotag from TargetSighting's assignment is valid. If so, sets target sighting's
         * geotag to it.
         *
         * @param ts
         * @return true if geotag was set, false otherwise
         */
        @JvmStatic
        fun attemptSetGeotagForTargetSighting(ts: TargetSighting): Boolean {
            if (!canSetGeotag(ts)) {
                return false
            }
            ts.geotag = Geotag(ts)
            return true
        }
    }
}
