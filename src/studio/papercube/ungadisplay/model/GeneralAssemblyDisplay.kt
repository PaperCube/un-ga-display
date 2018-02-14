package studio.papercube.ungadisplay.model

import studio.papercube.ungadisplay.model.ModeratedCaucus

interface GeneralAssemblyDisplay {
    fun newModeratedCaucus(moderatedCaucus: ModeratedCaucus)
    fun toggleSpeakerListState()
    fun newFreeDebate()
    fun newModeratedDebate()
    fun newDebateClosure()
    fun newMeetingSuspension()
    fun pushMessage(string: String)
    fun clearLog()
}