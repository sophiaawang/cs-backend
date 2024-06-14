package org.cuair.cloud.models

import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne

import org.cuair.cloud.models.geotag.FOV
import org.cuair.cloud.models.geotag.GpsLocation
import org.cuair.cloud.models.geotag.Telemetry
import org.cuair.cloud.util.Geotagging

@Entity
class ROI : ClientCreatable {
    /**
     * Returns the assignment for this ROI
     *
     * @return the assignment set for this ROI
     */
    /**
     * Sets the assignment for this ROI
     *
     * @param assignment the assignment to set for this ROI
     */
    /**
     * The assignment from which this ROI was created (contains the image that this
     * ROI was tagged in)
     */
    @ManyToOne
    var assignment: Assignment? = null
    /**
     * Returns the associated pixel location on the x axis of this ROI
     *
     * @return The pixel location on the x axis
     */
    /** The x pixel coordinate of the center of the ROI in the specific Image  */
    var pixelx: Int? = null
        private set
    /**
     * Returns the associated pixel location on the y axis of this ROI
     *
     * @return The pixel location on the y axis
     */
    /** The y pixel coordinate of the center of the ROI in the specific Image  */
    var pixely: Int? = null
        private set

    /** The GPS location of this ROI  */
    @Embedded
    private var gpsLocation: GpsLocation? = null
    /**
     * Returns whether this ROI is an averaged ROI or not
     *
     * @return True if averaged, false otherwise
     */
    /**
     * A boolean value representing if this ROI was the result of averaging a
     * cluster
     */
    var isAveraged: Boolean
        private set

    /**
     * Creates a new non-averaged ROI
     *
     * @param creator    The ODLCUser of the ROI
     * @param pixelx     Integer x pixel coordinate of the center of the ROI in the
     * specific Image
     * @param pixely     Integer y pixel coordinate of the center of the ROI in the
     * specific Image
     * @param assignment The assignment that created this ROI
     */
    constructor(
        creator: ODLCUser?,
        pixelx: Int?,
        pixely: Int?,
        assignment: Assignment?
    ) : super(creator) {
        this.assignment = assignment
        this.pixelx = pixelx
        this.pixely = pixely
        isAveraged = false
    }

    /**
     * Creates a new averaged ROI with an averaged GpsLocation
     *
     * @param creator The ODLCUser of the ROI
     * @param gps     The averaged GpsLocation for this ROI
     */
    constructor(creator: ODLCUser?, gps: GpsLocation?) : super(creator) {
        gpsLocation = gps
        isAveraged = true
    }

    /**
     * Returns the associated GpsLocation of this ROI
     *
     * @return The ROI's GpsLocation
     */
    fun getGpsLocation(): GpsLocation? {
        if (gpsLocation == null) {
            gpsLocation = calcGpsLocation()
        }
        return gpsLocation
    }

    /**
     * Calculates the GpsLocation of this ROI
     *
     * @return A GpsLocation of the ROI or null if assignment, pixelx, or pixely is
     * null
     */
    private fun calcGpsLocation(): GpsLocation? {
        if (assignment == null || pixelx == null || pixely == null) {
            return null
        }
        val i: Image = assignment!!.image ?: return null
        val telemetry: Telemetry = assignment!!.image!!.telemetry ?: return null
        val assignmentGps: GpsLocation = telemetry.getGps() ?: return null
        val fov: FOV = i.fov
        val latitude: Double = assignmentGps.getLatitude()
        val longitude: Double = assignmentGps.getLongitude()
        val altitude: Double = telemetry.getAltitude()
        val planeRoll: Double = telemetry.getGimOrt().getRoll()
        val planePitch: Double = telemetry.getGimOrt().getPitch()
        val planeYaw: Double = telemetry.getPlaneYaw()
        return if (latitude == null || longitude == null || altitude == null || planeYaw == null) {
            null
        } else Geotagging
            .getPixelCoordinates(
                latitude, longitude, altitude, fov, pixelx!!.toDouble(), pixely!!.toDouble(), planeRoll, planePitch,
                planeYaw
            )
    }
}