package studio.papercube.ungadisplay.model

interface GeneralAssemblyDisplay {
    fun newModeratedCaucus(moderatedCaucus: ModeratedCaucus)
    fun toggleSpeakerListState(on:Boolean, singleRepresentativeRequest: SingleRepresentativeRequest)
    fun newFreeDebate(timedRequest: TimedRequest)
    fun newModeratedDebate(moderatedRequest: ModeratedRequest)
    fun newDebateClosure(singleRepresentativeRequest: SingleRepresentativeRequest)
    fun newMeetingSuspension(singleRepresentativeRequest: SingleRepresentativeRequest)
    fun pushMessage(string: String)
    fun clearLog()
}