package org.cuair.cloud.models;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Base class for models in the CUAir system. All models extend from this class, and the model has
 * a unique id.
 */
public abstract class CUAirModel<T extends CUAirModel<T>> implements Comparable<T> {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public Long getId() {
    return id;
  }

  /**
   * Compares two CUAirModel instances via id. Sorts by earliest ID first. (e.g., ID 1 > ID 2)
   *
   * @param other The object to compare
   * @return 0 if equal, -1 if o is less than, 1 if o greater than
   */
  @Override
  public int compareTo(CUAirModel other) {
    return Long.compare(this.id, other.id);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) return true;
    if (other == null || getClass() != other.getClass()) return false;
    CUAirModel that = (CUAirModel) other;
    return Objects.equals(id, that.id);
  }
}

