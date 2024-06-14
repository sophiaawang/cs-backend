package org.cuair.cloud.models.plane.target

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.ebean.Ebean
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import org.cuair.cloud.models.Assignment
import org.cuair.cloud.models.Color
import org.cuair.cloud.models.Confidence
import org.cuair.cloud.models.ODLCUser
import org.cuair.cloud.models.Point
import org.cuair.cloud.models.Shape
import org.cuair.cloud.models.geotag.Geotag
import java.util.*

/** Alphanumeric Target Sighting that has features associated with alphanmuerics.  */
@Entity
class AlphanumTargetSighting @JsonCreator constructor(
    @JsonProperty("creator") creator: ODLCUser?,
    @JsonProperty("shape") shape: Shape?,
    @JsonProperty("shapeColor") shapeColor: Color?,
    @JsonProperty("alpha") alpha: String?,
    @JsonProperty("alphaColor") alphaColor: Color?,
    @JsonProperty("offaxis") offaxis: Boolean?,
    @JsonProperty("pixelx") pixelx: Int?,
    @JsonProperty("pixely") pixely: Int?,
    @JsonProperty("width") width: Int?,
    @JsonProperty("height") height: Int?,
    @JsonProperty("geotag") geotag: Geotag?,
    @JsonProperty("target") target: AlphanumTarget?,
    @JsonProperty("radiansFromTop") radiansFromTop: Double?,
    @JsonProperty("assignment") assignment: Assignment?,
    @JsonProperty("shapeConfidence") shapeConfidence: Double?,
    @JsonProperty("shapeColorConfidence") shapeColorConfidence: Double?,
    @JsonProperty("alphaConfidence") alphaConfidence: Double?,
    @JsonProperty("alphaColorConfidence") alphaColorConfidence: Double?,
    @JsonProperty("orientationConfidence") orientationConfidence: Double?,
    @JsonProperty("points") points: List<Point?>?,
    @JsonProperty("mdlcClassConf") mdlcClassConf: Confidence?
) : TargetSighting(
    creator,
    pixelx,
    pixely,
    width,
    height,
    geotag,
    radiansFromTop,
    orientationConfidence,
    mdlcClassConf,
    assignment
) {
    /**
     * Gets the target
     *
     * @return Target
     */
    /**
     * Sets the target
     *
     * @param target new Target
     */
    /** The target of this target sighting  */
    @ManyToOne
    override var target: AlphanumTarget? = null

    /** The list of points outlining a target shape  */ // @OneToMany(mappedBy="alphanumTargetSighting")
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    protected var points: List<Point?>?

    /** A description of the shape of the target  */
    private var shape: Shape?

    /** The confidence the vision system has in the shape identification  */
    private var shapeConfidence: Double?

    /** The color of the shape  */
    private var shapeColor: Color?

    /** The confidence the vision system has in the shape color identification  */
    private var shapeColorConfidence: Double?
    /**
     * Gets the alphanumeric
     *
     * @return String alphanumeric
     */
    /**
     * Sets the alphanumeric
     *
     * @param alpha String alphanumeric
     */
    /** The alphanumeric of this target sighting  */
    var alpha: String?

    /** The confidence the vision system has in the alphanumeric identification  */
    private var alphaConfidence: Double?

    /** The color of the alphanumeric  */
    private var alphaColor: Color?
    /**
     * Gets the confidence the vision system has in the alphanumeric color classification
     *
     * @return Double representing the alphanumeric color confidence
     */
    /** The confidence the vision system has in the alphanumeric color identification  */
    var alphaColorConfidence: Double?
        private set

    /** The overall confidence the vision system has in classification  */
    @JsonIgnore
    private var adlcClassConf: Double? = null
    /**
     * Gets whether sighting is offaxis
     *
     * @return Boolean true if sighting is offaxis, false otherwise
     */
    /**
     * Sets whether sighting is offaxis
     *
     * @param offaxis Boolean true if sighting is offaxis, false otherwise
     */
    /** True if the target sighting is of the off axis target, false otherwise  */
    var isOffaxis: Boolean?

    /**
     * Creates an Alphanumeric Target Sighting
     *
     * @param creator        the ODLCUser that created this Target Sighting
     * @param shape          String description of the shape of the Target Sighting
     * @param shapeColor     String color of the shape
     * @param alpha          String alphanumeric of this Target Sighting
     * @param alphaColor     String color of the alphanumeric
     * @param offaxis        Boolean of whether the target is off-axis
     * @param geotag         Geotag of this Target Sighting
     * @param pixelx         Integer x pixel coordinate of the center of the target sighting in the specific
     * Image
     * @param pixely         Integer y pixel coordinate of the center of the target sighting in the specific
     * Image
     * @param target         the Target of this Target Sighting
     * @param radiansFromTop the orientation of the Target Sighting
     * @param assignment     the assignment that created this TargetSighting
     * @param width          the horizontal pixel width of the TargetSighting
     * @param height         the vertical pixel height of the TargetSighting
     */
    init {
        this.shape = shape
        this.shapeColor = shapeColor
        this.alpha = alpha
        this.alphaColor = alphaColor
        this.target = target
        this.shapeConfidence = shapeConfidence
        this.shapeColorConfidence = shapeColorConfidence
        this.alphaConfidence = alphaConfidence
        this.alphaColorConfidence = alphaColorConfidence
        isOffaxis = offaxis
        this.points = points
        updateAdlcClassConf()
    }

    override fun makeAssociatedTargetNull() {
        target = null
    }

    /**
     * Given another target sighting, it updates all fields of this instance if there are any
     * differences
     *
     * @param other AlphanumTargetSighting containing updated fields
     */
    override fun updateFromTargetSighting(other: TargetSighting) {
        var updateConf = (other.orientationConfidence != null
                && !Objects.deepEquals(
            other.orientationConfidence, this.orientationConfidence
        ))
        super.updateFromTargetSighting(other)
        val alphaSighting: AlphanumTargetSighting
        assert(other is AlphanumTargetSighting)
        alphaSighting = other as AlphanumTargetSighting
        if (alphaSighting.getShape() != null) {
            shape = alphaSighting.getShape()
        }
        if (alphaSighting.getShapeColor() != null) {
            shapeColor = alphaSighting.getShapeColor()
        }
        if (alphaSighting.alpha != null) {
            alpha = alphaSighting.alpha
        }
        if (alphaSighting.getAlphaColor() != null) {
            alphaColor = alphaSighting.getAlphaColor()
        }
        if (alphaSighting.isOffaxis != null) {
            isOffaxis = alphaSighting.isOffaxis
        }
        if (alphaSighting.getShapeConfidence() != null) {
            if (!updateConf && alphaSighting.getShapeConfidence() != shapeConfidence) {
                updateConf = true
            }
            shapeConfidence = alphaSighting.getShapeConfidence()
        }
        if (alphaSighting.getShapeColorConfidence() != null) {
            if (!updateConf
                && alphaSighting.getShapeColorConfidence() != shapeColorConfidence
            ) {
                updateConf = true
            }
            shapeColorConfidence = alphaSighting.getShapeColorConfidence()
        }
        if (alphaSighting.getAlphaConfidence() != null) {
            if (!updateConf && alphaSighting.getAlphaConfidence() != alphaConfidence) {
                updateConf = true
            }
            alphaConfidence = alphaSighting.getAlphaConfidence()
        }
        if (alphaSighting.alphaColorConfidence != null) {
            if (!updateConf
                && alphaSighting.alphaColorConfidence != alphaColorConfidence
            ) {
                updateConf = true
            }
            alphaColorConfidence = alphaSighting.alphaColorConfidence
        }
        if (alphaSighting.points != null) {
            // https://github.com/ebean-orm/ebean/issues/2127 ???
            Ebean.deleteAll(points)
            points = alphaSighting.points
        }
        if (updateConf) {
            updateAdlcClassConf()
        }
        target = alphaSighting.target
    }

    /**
     * Updates `adlcClassConf` to be the product of the maximum 3 confidence values if the
     * sighting is ADLC and 2 or fewer confidence values are null. Otherwise, sets `adlcClassConf` to null.
     */
    private fun updateAdlcClassConf() {
        val sortedConf = arrayOf<Double?>(
            alphaConfidence,
            alphaColorConfidence,
            shapeConfidence,
            shapeColorConfidence,
            orientationConfidence
        )

        // Double comparator where null is always lowest
        val confCmp = Comparator { o1: Double?, o2: Double? ->
            if (o1 == null) {
                return@Comparator -1
            }
            if (o2 == null) {
                return@Comparator 1
            }
            o1.compareTo(o2)
        }
        Arrays.sort(sortedConf, confCmp)
        adlcClassConf = if (sortedConf[4] == null || sortedConf[3] == null || sortedConf[2] == null) {
            null
        } else {
            sortedConf[4]!! * sortedConf[3]!! * sortedConf[2]!!
        }
    }

    /**
     * Gets the shape
     *
     * @return Shape description of the shape
     */
    fun getShape(): Shape? {
        return shape
    }

    /**
     * Sets the shape
     *
     * @param shape Shape description of the shape
     */
    fun setShape(shape: Shape?) {
        this.shape = shape
    }

    /**
     * Gets the shape color
     *
     * @return Color of the shape
     */
    fun getShapeColor(): Color? {
        return shapeColor
    }

    /**
     * Sets the color of the shape
     *
     * @param shapeColor Color of the shape
     */
    fun setShapeColor(shapeColor: Color?) {
        this.shapeColor = shapeColor
    }

    /**
     * Gets the color of the alphanumeric
     *
     * @return Color of the alphanumeric
     */
    fun getAlphaColor(): Color? {
        return alphaColor
    }

    /**
     * Sets the color of the alphanumeric
     *
     * @param alphaColor Color of the alphanumeric
     */
    fun setAlphaColor(alphaColor: Color?) {
        this.alphaColor = alphaColor
    }

    /**
     * Gets the confidence the vision system has in the shape classification
     *
     * @return Double representing the shape confidence
     */
    fun getShapeConfidence(): Double? {
        return shapeConfidence
    }

    /**
     * Sets the confidence the vision system has in the shape classification
     *
     * @param shapeConfidence Double representing the shape confidence
     */
    fun setShapeConfidence(shapeConfidence: Double?) {
        this.shapeConfidence = shapeConfidence
        updateAdlcClassConf()
    }

    /**
     * Gets the confidence the vision system has in the shape color classification
     *
     * @return Double representing the shape color confidence
     */
    fun getShapeColorConfidence(): Double? {
        return shapeColorConfidence
    }

    /**
     * Sets the confidence the vision system has in the shape color classification
     *
     * @param shapeColorConfidence Double representing the shape color confidence
     */
    fun setShapeColorConfidence(shapeColorConfidence: Double?) {
        this.shapeColorConfidence = shapeColorConfidence
        updateAdlcClassConf()
    }

    /**
     * Gets the confidence the vision system has in the alphanumeric classification
     *
     * @return Double representing the alphanumeric confidence
     */
    fun getAlphaConfidence(): Double? {
        return alphaConfidence
    }

    /**
     * Sets the confidence the vision system has in the alphanumeric classification
     *
     * @param alphaConfidence Double representing the alphanumeric confidence
     */
    fun setAlphaConfidence(alphaConfidence: Double?) {
        this.alphaConfidence = alphaConfidence
        updateAdlcClassConf()
    }

    fun setOrientationConfidence(orientationConfidence: Double) {
        super.orientationConfidence = orientationConfidence
        updateAdlcClassConf()
    }

    /**
     * Determines if the given object is logically equal to this AlphanumTargetSighting
     *
     * @param o The object to compare
     * @return True if the object equals this AlphanumTargetSighting
     */
    override fun equals(o: Any?): Boolean {
        if (!super.equals(o)) return false
        val other = o as AlphanumTargetSighting
        if (!Objects.deepEquals(shape, other.getShape())) return false
        if (!Objects.deepEquals(shapeColor, other.getShapeColor())) return false
        if (!Objects.deepEquals(alpha, other.alpha)) return false
        if (!Objects.deepEquals(alphaColor, other.getAlphaColor())) return false
        if (!Objects.deepEquals(isOffaxis, other.isOffaxis)) return false
        if (!Objects.deepEquals(target, other.target)) return false
        if (!Objects.deepEquals(shapeConfidence, other.getShapeConfidence())) return false
        if (!Objects.deepEquals(shapeColorConfidence, other.getShapeColorConfidence())) {
            return false
        }
        if (!Objects.deepEquals(alphaConfidence, other.getAlphaConfidence())) return false
        return if (!Objects.deepEquals(points, other.points)) false else Objects.deepEquals(
            alphaColorConfidence, other.alphaColorConfidence
        )
    }
}