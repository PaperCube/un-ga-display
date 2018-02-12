package studio.papercube.ungadisplay.model

import tornadofx.ItemViewModel
import tornadofx.toProperty

data class ModeratedCaucus(val representativeGroup: String,
                           val topic: String,
                           val totalTime: Long,
                           val unitTime: Long,
                           var accepted: Boolean = true){
}

class ModeratedCaucusModel : ItemViewModel<ModeratedCaucus>() { //TODO make use of it
    val representativeGroup = bind { item.representativeGroup.toProperty() }
    val topic = bind { item.totalTime.toProperty() }
    val totalTime = bind { item.totalTime.toProperty() }
    val unitTime = bind { item.unitTime.toProperty() }
}