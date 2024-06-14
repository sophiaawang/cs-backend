package org.cuair.cloud.util

import org.cuair.cloud.models.exceptions.InvalidGpsLocationException
import org.cuair.cloud.models.geotag.FOV
import org.cuair.cloud.models.geotag.Geotag
import org.cuair.cloud.models.geotag.GpsLocation
import org.cuair.cloud.models.geotag.Radian
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.math.*

object Geotagging {
    private val logger: Logger = LoggerFactory.getLogger(GpsLocation::class.java)

    /** Width of height and image in pixels  */
    var IMAGE_WIDTH = Flags.FRONTEND_IMAGE_WIDTH
    var IMAGE_HEIGHT = Flags.FRONTEND_IMAGE_HEIGHT
    var SENSOR_WIDTH = Flags.CAM_SENSOR_WIDTH
    var SENSOR_HEIGHT = Flags.CAM_SENSOR_HEIGHT

    /** An approximation of the radius of the Earth in meters  */
    private const val radiusEarth = 6371000.0

    /**
     * Uses the inverse haversine function to return new gps corresponding to a
     * translation
     * of given distance and direction from an initial gps reading.
     *
     * @param initLat   The initial latitude
     * @param initLong  The initial longitude
     * @param distance  The distance offset travelled (in meters)
     * @param direction The direction travelled (in radians clockwise from north)
     * @return an array of two doubles, [latitude, longitude] (in degrees)
     */
    private fun inverseHaversine(initLat: Double, initLong: Double, distance: Double, direction: Double): DoubleArray {
        val r: Double = radiusEarth

        // Initialize empty gps array
        val gps = DoubleArray(2)

        // New latitude
        gps[0] = asin(
            sin(initLat) * cos(distance / r)
                    + cos(initLat) * sin(distance / r) * cos(direction)
        )

        // New longitude
        gps[1] = initLong + atan2(
            sin(direction) * sin(distance / r) * cos(initLat),
            cos(distance / r) - sin(initLat) * sin(gps[0])
        )

        // Convert into degrees
        gps[0] = gps[0] * 180 / PI
        gps[1] = gps[1] * 180 / PI
        return gps
    }

    /**
     * Creates a GpsLocation representing the center of the image
     *
     * @param latitude        The latitude of the plane in degrees
     * @param longitude       The longitude of the plane in degrees
     * @param altitude        The altitude of the plane in meters
     * @param fov             The (horizontal, vertical) fov of the camera
     * @param pixelx          The x-coordinate of the pixel center of the tag on the
     * frontend with respect to the image
     * @param pixely          The y-coordinate of the pixel center of the tag on the
     * frontend with respect to the image
     * @param planeYawRadians The yaw of the plane in radians
     */
    fun getPixelCoordinates(
        latitude: Double,
        longitude: Double,
        altitude: Double,
        fov: FOV,
        pixelx: Double,
        pixely: Double,
        planeRollRadians: Double,
        planePitchRadians: Double,
        planeYawRadians: Double
    ): GpsLocation {
        val deltapixelX: Double = pixelx - (IMAGE_WIDTH / 2)
        val deltapixel_y: Double = (IMAGE_HEIGHT / 2) - pixely
        val fPixels: Double = IMAGE_WIDTH / (2 * tan(fov.x / 2))
        val rTargetRelPlaneP = doubleArrayOf(deltapixelX, deltapixel_y, -fPixels)
        val rUnitTargetRelPlaneP: DoubleArray = MatrixUtil.scaleMultiplyVector(
            rTargetRelPlaneP, 1 / sqrt(
                fPixels.pow(2.0) + deltapixelX.pow(2.0) + deltapixel_y.pow(2.0)
            )
        )
        val c2Roll = arrayOf(
            doubleArrayOf(cos(planeRollRadians), 0.0, -sin(planeRollRadians)),
            doubleArrayOf(0.0, 1.0, 0.0),
            doubleArrayOf(
                sin(planeRollRadians), 0.0, cos(planeRollRadians)
            )
        )
        val c1Pitch = arrayOf(
            doubleArrayOf(1.0, 0.0, 0.0),
            doubleArrayOf(0.0, cos(planePitchRadians), sin(planePitchRadians)),
            doubleArrayOf(0.0, -sin(planePitchRadians), cos(planePitchRadians))
        )
        val c3Yaw = arrayOf(
            doubleArrayOf(cos(-planeYawRadians), sin(-planeYawRadians), 0.0),
            doubleArrayOf(-sin(-planeYawRadians), cos(-planeYawRadians), 0.0),
            doubleArrayOf(0.0, 0.0, 1.0)
        )
        val P_C_I: Array<DoubleArray> = MatrixUtil.multiply(MatrixUtil.multiply(c2Roll, c1Pitch), c3Yaw)
        val I_C_P: Array<DoubleArray> = MatrixUtil.transpose(P_C_I)
        val r_unit_target_rel_plane_I: DoubleArray = MatrixUtil.arrFromVec(
            MatrixUtil.transpose(
                MatrixUtil.multiply(
                    I_C_P,
                    MatrixUtil.vecFromArray(rUnitTargetRelPlaneP)
                )
            )
        )
        val r_target_plane_I: DoubleArray = MatrixUtil.scaleMultiplyVector(
            r_unit_target_rel_plane_I, altitude /abs(
                r_unit_target_rel_plane_I[2]
            )
        )
        val target_dx = r_target_plane_I[0]
        val target_dy = r_target_plane_I[1]

        // Use linear approximation
        // Numbers for St Mary's Airport
        val newGps = doubleArrayOf(latitude + target_dy * 3.28084 / 364180, longitude + target_dx * 3.28084 / 286928)
        var gps: GpsLocation? = null
        try {
            gps = GpsLocation(newGps[0], newGps[1])
        } catch (e: InvalidGpsLocationException) {
            logger.error(e.message)
        }
        return gps!!
    }

    /**
     * Calculate the orientation of this geotag as radians from north
     *
     * @param planeYaw       The yaw of the plane in radians, going clockwise from 0
     * = north
     * @param radiansFromTop The radians from the top of the image
     * @return The orientation of this geotag
     */
    fun calculateClockwiseRadiansFromNorth(planeYaw: Double, radiansFromTop: Double): Double {
        return Radian.normalize(planeYaw + radiansFromTop)
    }

    /**
     * Median a variable number of geotags
     *
     * @param geotags The geotag objects to medianed
     * @return The medianed geotag object
     */
    fun median(vararg geotags: Geotag?): Geotag? {
        var filteredGeotags = geotags
            .filterNotNull()
            .filter { it.gpsLocation != null && it.clockwiseRadiansFromNorth != null }

        if (filteredGeotags.isEmpty()) {
            return null
        }

        val locations = filteredGeotags.map { it.gpsLocation!! }.toTypedArray()
        val radians = filteredGeotags.map { it.clockwiseRadiansFromNorth!! }.toTypedArray()

        return Geotag(GpsLocation.median(locations), Radian.median(*radians))
    }
}