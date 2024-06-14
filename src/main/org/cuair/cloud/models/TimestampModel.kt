package org.cuair.cloud.models

import java.sql.Timestamp
import java.util.Objects
import jakarta.persistence.MappedSuperclass

/** Base class for all models that require a timestamp. */
@MappedSuperclass
abstract class TimestampModel : CUAirModel(), Comparable<Any> {

    /** A timestamp for the data  */
    var timestamp: Timestamp? = null

    /**
     * Compares two TimestampModel instances using their timestamp. Used only for testing. this
     * comparable does not mean that all timeseries are ordered by timestamps in the table.
     *
     * @param other The object to compare
     * @return 0 if equal, -1 if o is less than, 1 if o greater than
     */
    override operator fun compareTo(other: Any): Int {
        other as TimestampModel
        return this.timestamp!!.compareTo(other.timestamp!!)
    }

    /**
     * Returns true if the given object is logically equal to this TimestampModel
     * @param other the object to compare
     * @return true if the object equals this TimestampModel
     */
    override fun equals(other: Any?): Boolean {
        if (!super.equals(other)) return false

        if (other !is TimestampModel) return false
        return Objects.equals(this.timestamp, other.timestamp)
    }
}
