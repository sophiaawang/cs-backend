package org.cuair.cloud.models.exceptions

/** Exception thrown when a GpsLocation with invalid latitude or longitude is created */
class InvalidGpsLocationException(message: String) : Exception(message)
