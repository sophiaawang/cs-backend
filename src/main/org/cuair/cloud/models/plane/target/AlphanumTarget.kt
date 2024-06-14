package org.cuair.cloud.models.plane.target

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.persistence.Entity
import org.cuair.cloud.models.Color
import org.cuair.cloud.models.ODLCUser
import org.cuair.cloud.models.Shape
import org.cuair.cloud.models.geotag.CardinalDirection
import org.cuair.cloud.models.geotag.Geotag
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Alphanum Target is target that is associated with Alphanumeric Target
 * Sightings.
 */
@Entity
class AlphanumTarget(
    creator: ODLCUser?,
    shape: Shape?,
    shapeColor: Color?,
    alpha: String?,
    alphaColor: Color?,
    geotag: Geotag?,
    judgeTargetId: Long?,
    thumbnailTsid: Long?,
    airdropId: Long?
) : Target(creator, geotag, judgeTargetId!!, thumbnailTsid!!, airdropId!!) {
    /** A description of the shape of the target  */
    private var shape: Shape?

    /** The color of the shape  */
    private var shapeColor: Color?
    /**
     * Gets the alphanumeric of the Target
     *
     * @return Character alphanumeric
     */
    /**
     * Sets the alphanumeric of the Target
     *
     * @param alpha Character new alphanumeric of the Target
     */
    /** The alphanumeric of this target.  */
    var alpha: String?

    /** The color of the alphanumeric  */
    private var alphaColor: Color?
    //  /** True if the target is the off axis target, false otherwise */
    //  private Boolean offaxis;
    /**
     * Creates an AlphanumTarget
     *
     * @param creator       the Creator of the Target
     * @param shape         String description of the shape of the Target
     * @param shapeColor    String color of the shape
     * @param alpha         Character alphanumeric of this Target
     * @param alphaColor    String color of the alphanumeric
     * @param geotag        Geotag of this Target
     * @param judgeTargetId Long id of this Target on the competition server
     * @param thumbnailTsid Long id of Target Sighting used for thumbnail
     * @param airdropId     Long id of this Target's airdrop
     */
    init {
        this.shape = shape
        this.shapeColor = shapeColor
        this.alpha = alpha
        this.alphaColor = alphaColor
    }

    /**
     * Given another target, it updates all fields of this instance if there are any
     * differences
     *
     * @param other Target containing updated fields
     */
    override fun updateFromTarget(other: Target?) {
        super.updateFromTarget(other!!)
        var alphaTarget: AlphanumTarget? = null
        if (other !is AlphanumTarget) {
            return
        }
        alphaTarget = other
        if (alphaTarget!!.getShape() != null) {
            shape = alphaTarget.getShape()
        }
        if (alphaTarget.getShapeColor() != null) {
            shapeColor = alphaTarget.getShapeColor()
        }
        if (alphaTarget.alpha != null) {
            alpha = alphaTarget.alpha
        }
        if (alphaTarget.getAlphaColor() != null) {
            alphaColor = alphaTarget.getAlphaColor()
        }


        /*
     * if (alphaTarget.getGeotag() != null) {
     * this.geotag = alphaTarget.getGeotag();
     * }
     */
    }

    /** Returns class associated with this target  */
    override fun fetchAssociatedTargetSightingClass(): Class<out TargetSighting?> {
        return AlphanumTargetSighting::class.java
    }

    /**
     * Gets the description of the shape of this Target
     *
     * @return String description of the shape
     */
    fun getShape(): Shape? {
        return shape
    }

    /**
     * Sets the description of the shape of this Target
     *
     * @param shape String new description of the shape
     */
    fun setShape(shape: Shape?) {
        this.shape = shape
    }

    /**
     * Gets the color of the shape
     *
     * @return String color of the shape
     */
    fun getShapeColor(): Color? {
        return shapeColor
    }

    /**
     * Sets the color of the shape
     *
     * @param shapeColor String new color of the shape
     */
    fun setShapeColor(shapeColor: Color?) {
        this.shapeColor = shapeColor
    }

    /**
     * Gets the color of the alphanumeric
     *
     * @return String color of the alphanumeric
     */
    fun getAlphaColor(): Color? {
        return alphaColor
    }

    /**
     * Sets the color of the alphanumeric
     *
     * @param alphaColor String new color of the alphanumeric
     */
    fun setAlphaColor(alphaColor: Color?) {
        this.alphaColor = alphaColor
    }

    override fun equals(o: Any?): Boolean {
        if (o == null || !super.equals(o)) {
            return false
        }
        val other = o as AlphanumTarget
        if (!Objects.deepEquals(shape, other.getShape())) {
            return false
        }
        if (!Objects.deepEquals(shapeColor, other.getShapeColor())) {
            return false
        }
        if (!Objects.deepEquals(alpha, other.alpha)) {
            return false
        }
        return if (!Objects.deepEquals(alphaColor, other.getAlphaColor())) {
            false
        } else true
    }

    @get:JsonIgnore
    override val typeString: String
        get() = "Alphanum"

    /**
     * Creates json adhering to the judges specification for an AlphanumTarget
     *
     * @return a JsonNode object
     */
    override fun toJson(): JsonNode {
        val rootNode = ObjectMapper().createObjectNode()
        rootNode.put("type", "STANDARD")
        val logger = LoggerFactory.getLogger(AlphanumTarget::class.java)
        logger.info("geotag is " + this.geotag)
        if (this.geotag != null /* && !this.isOffaxis() */) {
            if (this.geotag.gpsLocation != null && this.geotag.gpsLocation!!
                    .getLatitude() != null && !(this.geotag.gpsLocation!!.getLatitude() as Double).isNaN()
            ) {
                rootNode.put("latitude", this.geotag.gpsLocation!!.getLatitude())
            }
            if (this.geotag.gpsLocation != null && this.geotag.gpsLocation!!
                    .getLongitude() != null && !(this.geotag.gpsLocation!!.getLongitude() as Double).isNaN()
            ) {
                rootNode.put("longitude", this.geotag.gpsLocation!!.getLongitude())
            }
            if (this.geotag.clockwiseRadiansFromNorth != null) {
                rootNode.put(
                    "orientation",
                    CardinalDirection.getFromRadians(this.geotag.clockwiseRadiansFromNorth)
                        .getAbbreviation()
                )
            }
        }
        if (shape != null) rootNode.put("shape", shape!!.getName().toUpperCase())
        rootNode.put("alphanumeric", alpha.toString().uppercase(Locale.getDefault()))
        if (shapeColor != null) {
            rootNode.put("shapeColor", shapeColor!!.getName().toUpperCase())
        }
        if (alphaColor != null) {
            rootNode.put("alphanumericColor", alphaColor!!.getName().toUpperCase())
        }
        rootNode.put("autonomous", getCreator().userType === ODLCUser.UserType.ADLC)
        return rootNode
    }
}