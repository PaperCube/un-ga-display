package studio.papercube.ungadisplay.app

import studio.papercube.ungadisplay.model.ModeratedCaucus

interface GeneralAssemblyDisplay {
    fun newModeratedCaucus(moderatedCaucus: ModeratedCaucus)
    var isSpeakerListOn: Boolean
    fun newFreeDebate()
    fun newModeratedDebate()
    fun newDebateClosure()
    fun newMeetingSuspension()
    fun pushMessage(string: String)
    fun clearLog()
}