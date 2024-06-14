package org.cuair.cloud.models.geotag

import jakarta.persistence.Entity
import org.cuair.cloud.models.CUAirModel
import org.cuair.cloud.util.Flags
import kotlin.math.atan

/** Represents the field of view (FOV) of the camera during image capture in radians.   */
@Entity
class FOV(
    /** The horizontal (x) field of view.  */
    val x: Double,
    /** The vertical (y) field of view.  */
    val y: Double
) : CUAirModel() {
    companion object {
        // Expects focalLength in mm, returns an FOV in radians
        fun fromFocalLength(focalLength: Double): FOV {
            val CAM_SENSOR_WIDTH: Double = Flags.CAM_SENSOR_WIDTH
            val CAM_SENSOR_HEIGHT: Double = Flags.CAM_SENSOR_HEIGHT
            val fovHoriz = 2 * atan(CAM_SENSOR_WIDTH / (2 * focalLength))
            val fovVert = 2 * atan(CAM_SENSOR_HEIGHT / (2 * focalLength))
            return FOV(fovHoriz, fovVert)
        }
    }
}