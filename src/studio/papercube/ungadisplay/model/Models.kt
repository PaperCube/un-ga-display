package studio.papercube.ungadisplay.model

open class ModeratedCaucus {
    lateinit var representativeGroup: String
    lateinit var topic: String
    var totalTime: Long = -1
    var unitTime: Long = -1
    var accepted: Boolean = true
}

open class SingleRepresentativeRequest {
    lateinit var from: String
    var accepted = true
}

open class TimedRequest : SingleRepresentativeRequest() {
    var totalTime: Int = -1
}

open class ModeratedRequest : TimedRequest() {
    lateinit var topic: String
}