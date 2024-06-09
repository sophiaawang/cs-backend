package org.cuair.cloud.models;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/** Base class for all models that require a timestamp. */
@MappedSuperclass
public abstract class TimestampModel extends CUAirModel<TimestampModel> {
  /** A timestamp for the data */
  private Timestamp timestamp;

  public Timestamp getTimestamp() { return this.timestamp; }

  public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

  /**
   * Compares two TimestampModel instances using their timestamp. Used only for testing. this
   * comparable does not mean that all timeseries are ordered by timestamps in the table.
   *
   * @param other The object to compare
   * @return 0 if equal, -1 if o is less than, 1 if o greater than
   */
  @Override
  public int compareTo(TimestampModel other) {
    return this.timestamp.compareTo(other.timestamp);
  }

  /**
   * Returns true if the given object is logically equal to this TimestampModel
   * @param other the object to compare
   * @return true if the object equals this TimestampModel
   */
  @Override
  public boolean equals(Object other) {
    if (!super.equals(other)) return false;

    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;

    TimestampModel that = (TimestampModel) other;
    return Objects.equals(timestamp, that.timestamp);
  }
}