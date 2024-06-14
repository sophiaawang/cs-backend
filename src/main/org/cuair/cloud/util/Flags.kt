package org.cuair.cloud.util

object Flags {
    /** Special username  */
    var MDLC_OPERATOR_USERNAME = "operator"
    var INTSYS_TAGGER_USERNAME = "intsys"

    /** Image Directory  */
    var PLANE_IMAGE_DIR = "images/"

    /** Test image directory  */
    var TEST_IMAGE_DIR = "src/test/java/org/cuair/cloud/controllers/test_images/"

    /** Constants  */
    var CAM_SENSOR_WIDTH = 23.2 // in mm
    var CAM_SENSOR_HEIGHT = 15.4 // in mm
    var RAW_IMAGE_WIDTH = 5456.0
    var RAW_IMAGE_HEIGHT = 3632.0
    var FRONTEND_IMAGE_WIDTH: Double = RAW_IMAGE_WIDTH // 1900
    var FRONTEND_IMAGE_HEIGHT: Double = RAW_IMAGE_HEIGHT // 1263
    var DEFAULT_EMERGENT_TARGET_DESC = "A lost hiker with a water bottle."
    var MISSION_NUMBER = 2

    /** Default username if no username specified as part of request  */
    var DEFAULT_USERNAME = "<NO_USER>"

    /** Allow multiple users on a single IP if set to true.  */
    var ENABLE_MULTIPLE_USERS_PER_IP = true

    /**
     * Allows clients to create users if true. Otherwise, all will use default user
     */
    var USERS_ENABLED = true
    //  IP's of different systems: GS: 192.168.1.2 AUTOPILOT: 192.168.1.4 PI: 192.168.1.6
    /** Cloud  */
    var CLOUD_IP = "0.0.0.0"

    /** Autopilot  */
    var AUTOPILOT_GROUND_IP = "192.168.1.4"
    var AUTOPILOT_GROUND_PORT = "8001"
    var AUTOPILOT_COVERAGE = "/ground/api/v3/distributed/geotag"
    var AUTOPILOT_GROUND_MDLC_ROIS = "/ground/api/v3/distributed/mdlc"
    var AUTOPILOT_GROUND_ADLC_ROIS = "/ground/api/v3/distributed/adlc"

    /** Plane servers  */
    var PRINT_CLIENT_LOGS = true
    var OBC_IP = "192.168.0.21"
    var CAMERA_COMMANDS_IP = "192.168.1.6" // IP of the Pi.(hardcoded in the pi)
    var CAM_GIM_PORT = "5000"
    var SET_CAM_GIM_MODE_SETTINGS_ROUTE = "/api/mode"
    var SET_AIRDROP_SETTINGS_ROUTE = "/v1/airdrop/setting"
    var GET_CAM_GIM_MODE_SETTINGS_ROUTE = "/api/state"
    var GET_AIRDROP_SETTINGS_ROUTE = "/v1/airdrop/state"

    /* PS Endpoints */
    var MAIN_CAMERA_COMMANDS_PORT = "4200"
    var SET_FOCAL_LENGTH_ROUTE = "/camera/set-zoom-focal-length"
    var CONTROL_GIMBAL_ROUTE = "/gimbal/control-gimbal"
    var SET_ZOOM_LEVEL_ROUTE = "/camera/set-zoom-level"
    var ZOOM_WIDE_ROUTE = "/camera/zoom-wide"
    var ZOOM_TELE_ROUTE = "/camera/zoom-tele"
    var SET_APERTURE_ROUTE = "/camera/set-aperture"
    var SET_SHUTTER_SPEED_ROUTE = "/camera/set-shutter-speed"
    var SET_EXPOSURE_MODE_ROUTE = "/camera/set-exposure-mode"
    var SET_PANNING_ROUTE = "/modes/set-procedure-pan"
    var SET_CC_ROUTE = "/modes/set-procedure-cc"
    var STOP_PS_MODE_ROUTE = "/modes/stop-search"
    var SET_MANUAL_SEARCH_ROUTE = "/modes/manual-search"
    var SET_DISTANCE_SEARCH_ROUTE = "/modes/distance-search"
    var SET_TIME_SEARCH_ROUTE = "/modes/time-search"
    var CAPTURE_ROUTE = "/camera/capture"
    var GET_STATUS_ROUTE = "/camera/get-status"

    /** Streaming  */
    var STREAM_CLIP_DIR = "src/main/org/cuair/cloud/"
    var MAX_CAMERAS = 5
    var PORT_START = 5000f
    var STREAMING_CAPS =
        " caps = \"application/x-rtp, media=(string)video, clock-rate=(int)90000, encoding-name=(string)H264, payload=(int)96\" ! rtph264depay ! decodebin ! videoconvert ! x264enc tune=zerolatency ! mpegtsmux !"

    /** A constant used in the DBSCAN calculation for clustering ROIs  */
    var CUAIR_CLUSTERING_EPSILON = 0.0003173611111111856
}