package studio.papercube.ungadisplay.view

import javafx.beans.property.Property
import javafx.beans.property.SimpleStringProperty
import javafx.event.EventTarget
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.TextInputControl
import javafx.scene.layout.Pane
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import tornadofx.*


fun Pane.childrenUseMaxWidth() {
    children.forEach { node ->
        if (node is Region) {
            node.useMaxWidth = true
        }
    }
}

fun Pane.childrenHgrow(priority: Priority) {
    children.forEach { node ->
        node.hgrow = Priority.ALWAYS
    }
}

fun TextArea.appendEmptyLines(lineCnt: Int) = apply {
    for (i in 1..lineCnt) {
        appendln("")
    }
}

fun TextArea.separateMsgs() = appendEmptyLines(2)

fun TextArea.appendln(string: String) = apply {
    appendText(string)
    appendText("\r\n")
}

fun TextInputControl.requiredWhenFired() {
    required(ValidationTrigger.None)
}

fun ViewModel.bindSimpleStringProperty() =
        bind { SimpleStringProperty() }

fun EventTarget.bindStringPropertiesToLabeledTextFields(properties: Array<Property<String>>,
                                                        vararg fieldNames: String,
                                                        op: TextField.() -> Unit = {}): Array<Field> {
    return fieldNames.mapIndexed { index, fieldName ->
        field(fieldName) {
            textfield(properties[index]) {
                op()
            }
        }
    }.toTypedArray()
}