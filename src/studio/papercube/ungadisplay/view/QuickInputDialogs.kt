package studio.papercube.ungadisplay.view

import studio.papercube.ungadisplay.model.ModeratedRequest
import studio.papercube.ungadisplay.model.SingleRepresentativeRequest
import studio.papercube.ungadisplay.model.TimedRequest
import tornadofx.UIComponent

object QuickInputDialogs {
    fun generateTimedRequestDialog(uiComponent: UIComponent): DataInputDialog<TimedRequest> {
        val dialog = DataInputDialog(TimedRequest(), uiComponent)
        return dialog.bind("代表团", SingleRepresentativeRequest::from)
                .bind("总时间", TimedRequest::totalTime)
                .bindCheckbox("", SingleRepresentativeRequest::accepted)
    }

    fun generateModeratedRequestDialog(uiComponent: UIComponent): DataInputDialog<ModeratedRequest> {
        val d = DataInputDialog(ModeratedRequest(), uiComponent)
        return d.bind("代表团", SingleRepresentativeRequest::from)
                .bind("", ModeratedRequest::topic)
                .bind("", TimedRequest::totalTime)
    }

    fun generateSingleRepresentativeDialog(uiComponent: UIComponent): DataInputDialog<SingleRepresentativeRequest> {
        val d = DataInputDialog(SingleRepresentativeRequest(), uiComponent)
        return d.bind("代表团", SingleRepresentativeRequest::from)
    }
}