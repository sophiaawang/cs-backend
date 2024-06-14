package org.cuair.cloud.models.plane.target

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.JsonNode
import jakarta.persistence.CascadeType
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.OneToOne
import org.cuair.cloud.models.ClientCreatable
import org.cuair.cloud.models.ODLCUser
import org.cuair.cloud.models.geotag.Geotag
import java.util.*

/** Model to represent the target, which is an object on the field.  */
@MappedSuperclass
abstract class Target(creator: ODLCUser?, geotag: Geotag?, judgeTargetId: Long, thumbnailTsid: Long, airdropId: Long) :
    ClientCreatable(creator) {
    /**
     * Represents the Geotag of this target that records the gps location and the
     * direction that the
     * target is facing
     */
    @OneToOne(cascade = [CascadeType.ALL])
    protected var geotag: Geotag
    /**
     * Gets the id of this target on the competition server
     *
     * @return Long target id
     */
    /** Represents the id for this target on the competition server  */
    @JsonIgnore
    var judgeTargetId: Long
        private set

    /** Id of target sighting used for thumbnail  */ // @Column(name = "thumbnailTSId")
    private var thumbnailTsid: Long
    /**
     * Gets the airdrop id of the target
     *
     * @return int airdrop id
     */
    /** Id of airdrop target from 0-4  */
    val airdropId: Long

    /**
     * Creates a target
     *
     * @param creator       the ODLCUser that created this Target
     * @param geotag        Geotag of this Target
     * @param judgeTargetId Long id of this target on the competition server
     * @param thumbnailTsid Long id of Target Sighting used for thumbnail
     * @param airdropId     Long id of the corresponding airdrop target
     */
    init {
        this.geotag = geotag!!
        this.judgeTargetId = judgeTargetId
        this.thumbnailTsid = thumbnailTsid
        this.airdropId = airdropId
    }

    /**
     * Given another target, it updates all fields of this instance if there are any
     * differences
     *
     * @param other Target containing updated fields
     */
    open fun updateFromTarget(other: Target?) {
        if (other?.getthumbnailTsid() != null) {
            thumbnailTsid = other.getthumbnailTsid()
        }
    }

    /**
     * Converts this object to a Json according to the judges specification
     *
     * @return JsonNode
     */
    abstract fun toJson(): JsonNode?

    /**
     * Returns the class of targetSighting associated with this target
     *
     * @return Class associated with target
     */
    abstract fun fetchAssociatedTargetSightingClass(): Class<out TargetSighting?>?

    /**
     * Sets the id of the target on the competition server. The judge target id
     * should never be changed after initial
     * assignment which is done elsewhere, so this should only be used in tests
     *
     * @param judgeTargetId new judge target id
     */
    fun setJudgeTargetId_CREATION(judgeTargetId: Long) {
        this.judgeTargetId = judgeTargetId
    }

    /**
     * Gets the id of the target sighting used for thumbnail
     *
     * @return Long thumnail target sighting id
     */
    fun getthumbnailTsid(): Long {
        return thumbnailTsid
    }

    /**
     * Sets the id of the target sighting used for thumbnail
     *
     * @param thumbnailTsid thumbnail target sighting id
     */
    fun setthumbnailTsid(thumbnailTsid: Long) {
        this.thumbnailTsid = thumbnailTsid
    }

    /**
     * Gets the String representation of the target type
     *
     * @return String the type
     */
    @get:JsonIgnore
    abstract val typeString: String?

    /**
     * Determines if the given object is logically equal to this Target
     *
     * @param o The object to compare
     * @return True if the object equals this Target
     */
    override fun equals(o: Any?): Boolean {
        val other: Target? =
            o as Target?
        if (!super.equals(other)) return false
        if (!Objects.deepEquals(geotag, other.geotag)) return false
        if (!Objects.deepEquals(judgeTargetId, other.judgeTargetId)) return false
        return if (!Objects.deepEquals(airdropId, other.airdropId)) false else Objects.deepEquals(
            thumbnailTsid, other.getthumbnailTsid()
        )
    }
}