package org.cuair.cloud.models

import jakarta.persistence.ManyToOne
import jakarta.persistence.MappedSuperclass
import java.util.*

/** Model to represent any model that can be created by a ground server client  */
@MappedSuperclass
abstract class ClientCreatable(creator: ODLCUser?) : CUAirModel() {
    /** Designates who created the model  */
    @ManyToOne
    private var creator: ODLCUser

    /**
     * Creates a ClientCreatable
     *
     * @param creator the ODLCUser creator of the model
     */
    init {
        this.creator = creator!!
    }

    /**
     * Gets the ODLCUser creator of this model
     *
     * @return the ODLCUser creator of this model
     */
    fun getCreator(): ODLCUser {
        return creator
    }

    /**
     * Sets the ODLCUser creator of this model
     *
     * @param creator new ODLCUser creator of this model
     */
    fun setCreator(creator: ODLCUser) {
        this.creator = creator
    }

    /**
     * Determines if the given object is logically equal to this model
     *
     * @param o The object to compare
     * @return True if the object equals this model
     */
    override fun equals(o: Any?): Boolean {
        val other = o as ClientCreatable?
        return Objects.deepEquals(creator, other!!.getCreator())
    }
}