package org.cuair.cloud.models

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonValue
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne
import org.cuair.cloud.models.geotag.FOV
import org.cuair.cloud.models.geotag.GpsLocation
import org.cuair.cloud.models.geotag.Telemetry
import org.cuair.cloud.util.Geotagging
import java.util.*

/**
 * Represents an image and its corresponding metadata as sent down from the plane
 */
@Entity
class Image : TimestampModel {
    /** The URL where clients can retrieve the image file  */
    var imageUrl: String

    /** The local URL where image file lives on the ground server  */
    var localImageUrl: String? = null

    /** Closest Telemetry for when this Image was taken  */
    @OneToOne(cascade = [CascadeType.ALL])
    var telemetry: Telemetry

    /** The type of this image. Either FIXED, TRACKING, or OFFAXIS  */
    var imgMode: ImgMode

    /** True if has at least one associated MDLC assignment, otherwise false.  */
    @JsonIgnore
    var hasMdlcAssignment = false

    /** True if has at least one associated ADLC assignment, otherwise false.  */
    @JsonIgnore
    var hasAdlcAssignment = false

    /** The field of view of this image.  */
    @OneToOne(cascade = [CascadeType.ALL])
    var fov: FOV

    /** The possible image modes: fixed, tracking, and off-axis  */
    enum class ImgMode(@field:JsonValue var value: String) {
        FIXED("fixed"), TRACKING("tracking"), OFFAXIS("off-axis");
    }

    // Constructor
    constructor(imageUrl: String, telemetry: Telemetry, fov: FOV, imgMode: ImgMode) {
        this.imageUrl = imageUrl
        this.telemetry = telemetry
        this.fov = fov
        this.imgMode = imgMode
    }

    // Constructor corresponding to original Image class (used in tests)
    constructor(
        imageUrl: String, telemetry: Telemetry, imgMode: ImgMode, hasMdlcAssignment: Boolean,
        hasAdlcAssignment: Boolean, fov: Double
    ) {
        this.imageUrl = imageUrl
        this.telemetry = telemetry
        this.fov = FOV(fov, fov)
        this.imgMode = imgMode
        this.hasMdlcAssignment = hasMdlcAssignment
        this.hasAdlcAssignment = hasAdlcAssignment
    }

    /**
     * Internal method for finding geotags corresponding to four corners of image
     */
    @get:JsonIgnore
    val locations: Map<String, Any>
        get() {
            val imageGPS: GpsLocation = telemetry.getGps()
            val centerLat: Double = imageGPS.getLatitude()
            val centerLong: Double = imageGPS.getLongitude()
            val planeRoll: Double = telemetry.getGimOrt().getRoll() * Math.PI / 180
            val planePitch: Double = telemetry.getGimOrt().getPitch() * Math.PI / 180
            val planeYaw: Double = telemetry.getPlaneYaw() * Math.PI / 180
            val altitude: Double = telemetry.getAltitude()
            val topLeft: GpsLocation = Geotagging
                .getPixelCoordinates(centerLat, centerLong, altitude, fov, 0.0, 0.0, planeRoll, planePitch, planeYaw)
            val topRight: GpsLocation = Geotagging
                .getPixelCoordinates(
                    centerLat, centerLong, altitude, fov, Geotagging.IMAGE_WIDTH, 0.0, planeRoll, planePitch,
                    planeYaw
                )
            val bottomLeft: GpsLocation = Geotagging
                .getPixelCoordinates(
                    centerLat, centerLong, altitude, fov, 0.0, Geotagging.IMAGE_HEIGHT, planeRoll, planePitch,
                    planeYaw
                )
            val bottomRight: GpsLocation = Geotagging
                .getPixelCoordinates(
                    centerLat, centerLong, altitude, fov, Geotagging.IMAGE_WIDTH, Geotagging.IMAGE_HEIGHT,
                    planeRoll, planePitch, planeYaw
                )
            val locs: MutableMap<String, Any> = HashMap()
            locs.put("topLeft", topLeft)
            locs.put("topRight", topRight)
            locs.put("bottomLeft", bottomLeft)
            locs.put("bottomRight", bottomRight)
            return locs
        }
}