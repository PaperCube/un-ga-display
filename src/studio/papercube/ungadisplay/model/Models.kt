package studio.papercube.ungadisplay.model

import tornadofx.ItemViewModel
import tornadofx.toProperty

class ModeratedCaucus {
    lateinit var representativeGroup: String
    lateinit var topic: String
    var totalTime: Long = -1
    var unitTime: Long = -1
    var accepted: Boolean = true
}

class SpeakerListStateToggleRequest{
    lateinit var from:String
}