package org.cuair.cloud.models.plane.target

import jakarta.persistence.CascadeType
import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.OneToOne
import org.cuair.cloud.models.Assignment
import org.cuair.cloud.models.ClientCreatable
import org.cuair.cloud.models.Confidence
import org.cuair.cloud.models.Image
import org.cuair.cloud.models.ODLCUser
import org.cuair.cloud.models.geotag.Geotag
import org.cuair.cloud.util.Flags
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*
import javax.imageio.ImageIO

/**
 * Model to represent a target sighting. The target sighting is the sighting of
 * a target in a
 * specific image.
 */
@MappedSuperclass
abstract class TargetSighting(
    creator: ODLCUser?,
    /**
     * The x pixel coordinate of the center of the target sighting in the specific
     * Image
     */
    protected var pixelx: Int?,
    /**
     * The y pixel coordinate of the center of the target sighting in the specific
     * Image
     */
    protected var pixely: Int?,
    width: Int?,
    height: Int?,
    geotag: Geotag?,
    radiansFromTop: Double?,
    orientationConfidence: Double?,
    mdlcClassConf: Confidence?,
    assignment: Assignment?
) : ClientCreatable(creator) {
    /**
     * Represents the Geotag of this target sighting that records the gps location
     * and the direction
     * that the target sighting is facing
     */
    @OneToOne(cascade = [CascadeType.ALL])
    var geotag: Geotag?

    /**
     * Gets the width of the TargetSighting in pixels
     *
     * @return width Integer pixel width
     */
    /** The horizontal pixel width of the target sighting in the specific image  */
    var width: Int?
        protected set
    /**
     * Gets the height of the TargetSighting in pixels
     *
     * @return height Integer pixel height
     */
    /** The vertical pixel height of the target sighting in the specific image  */
    var height: Int?
        protected set

    /** The confidence MDLC taggers have in the classification accuracy  */
    protected var mdlcClassConf: Confidence?

    /**
     * The assignment from which this target sighting was created (contains the
     * image that this target
     * sighting was tagged in)
     */
    @ManyToOne
    var assignment: Assignment?
    /**
     * Gets the orientation of the target sighting from the top of the image
     *
     * @return Double representing the orientation of the sighting
     */
    /**
     * The orientation of the target sighting with respect to the top of the image.
     * This means that
     * the vector below is 0 and the radians increase in a counterclockwise fashion.
     */
    val radiansFromTop: Double
    /**
     * Gets the confidence the vision system has in the target orientation
     * classification
     *
     * @return Double representing the target orientation confidence
     */
    /**
     * Sets the confidence the vision system has in the target orientation
     * classification
     *
     * @param orientationConfidence Double representing the target orientation
     * confidence
     */
    /**
     * The confidence the vision system has in the target orientation identification
     */
    var orientationConfidence: Double?

    /**
     * Creates a TargetSighting
     *
     * @param creator               the ODLCUser that created this Target Sighting
     * @param geotag                Geotag of this Target Sighting
     * @param pixelx                Integer x pixel coordinate of the center of the
     * target sighting in the specific Image
     * @param pixely                Integer y pixel coordinate of the center of the
     * target sighting in the specific Image
     * @param radiansFromTop        the orientation of the Target Sighting
     * @param orientationConfidence the confidence the vision system has in the
     * target orientation identification
     * @param mdlcClassConf         the confidence MDLC taggers have in the target
     * classification
     * @param assignment            the assignment that created this TargetSighting
     * @param width                 the horizontal pixel width of the TargetSighting
     * @param height                the vertical pixel height of the TargetSighting
     */
    init {
        pixely = pixely
        this.width = width
        this.height = height
        this.geotag = geotag
        this.radiansFromTop = radiansFromTop!!
        this.orientationConfidence = orientationConfidence
        this.mdlcClassConf = mdlcClassConf
        this.assignment = assignment
    }

    /**
     * Given another target sighting, it updates all fields of this instance if
     * there are any
     * differences
     *
     * @param other TargetSighting containing updated fields
     */
    open fun updateFromTargetSighting(other: TargetSighting) {
        assert(assignment != null)
        if (other.getpixelx() != null) {
            pixelx = other.getpixelx()
        }
        if (other.getpixely() != null) {
            pixely = other.getpixely()
        }
        if (other.width != null) {
            width = other.width
        }
        if (other.height != null) {
            height = other.height
        }
        if (other.geotag != null) {
            geotag = other.geotag
        }
        if (other.orientationConfidence != null) {
            orientationConfidence = other.orientationConfidence
        }
        if (other.mdlcClassConf != null) {
            mdlcClassConf = other.mdlcClassConf
        }
        if (other.assignment != null) {
            assignment = other.assignment
        }
    }

    /** Sets this target to be null  */
    abstract fun makeAssociatedTargetNull()

    /**
     * Returns the raw content of the thumbnail corresponding to this target
     * sighting, for
     * submission to interop.
     */
    @Throws(IOException::class)
    fun thumbnailImage(): ByteArray {
        val logger = LoggerFactory.getLogger(TargetSighting::class.java)
        val image: Image = assignment?.image!!
        val imgPathLocal: String = image.localImageUrl!!
        logger.info(imgPathLocal)
        // InputStream in = getClass().getResourceAsStream(imgPathLocal);
        // assert in != null;
        val initialImage = ImageIO.read(File(imgPathLocal))

        // Produce cropped thumbnail - need to scale up as values are from compressed
        // frontend image
        val scaleUpW: Double = Flags.RAW_IMAGE_WIDTH / Flags.FRONTEND_IMAGE_WIDTH
        val scaleUpH: Double = Flags.RAW_IMAGE_HEIGHT / Flags.FRONTEND_IMAGE_HEIGHT
        val croppedThumb = initialImage.getSubimage(
            (scaleUpW * (pixelx!! - width!! / 2)).toInt(),
            (scaleUpH * (pixely!! - height!! / 2)).toInt(),
            (scaleUpW * width!!).toInt(),
            (scaleUpH * height!!).toInt()
        )
        val baos = ByteArrayOutputStream()
        ImageIO.write(croppedThumb, "jpg", baos)
        return baos.toByteArray()
    }

    /**
     * Gets the target
     *
     * @return Target
     */
    abstract val target: Target?

    /**
     * Gets the x pixel coordinate of the TargetSighting
     *
     * @return pixelx Integer x pixel coordinate
     */
    fun getpixelx(): Int? {
        return pixelx
    }

    /**
     * Gets the y pixel coordinate of the TargetSighting
     *
     * @return Integer y pixel coordinate
     */
    fun getpixely(): Int? {
        return pixely
    }

    /**
     * Determines if the given object is logically equal to this
     * AlphanumTargetSighting
     *
     * @param o The object to compare
     * @return True if the object equals this AlphanumTargetSighting
     */
    override fun equals(o: Any?): Boolean {
        val other = o as TargetSighting?
        if (!super.equals(o)) return false
        if (!Objects.deepEquals(geotag, other!!.geotag)) return false
        if (!Objects.deepEquals(pixelx, other.getpixelx())) return false
        if (!Objects.deepEquals(pixely, other.getpixely())) return false
        if (!Objects.deepEquals(width, other.width)) return false
        if (!Objects.deepEquals(height, other.height)) return false
        if (!Objects.deepEquals(radiansFromTop, other.radiansFromTop)) return false
        if (!Objects.deepEquals(orientationConfidence, other.orientationConfidence)) {
            return false
        }
        return if (!Objects.deepEquals(mdlcClassConf, other.mdlcClassConf)) false else Objects.deepEquals(
            assignment, other.assignment
        )
    }
}