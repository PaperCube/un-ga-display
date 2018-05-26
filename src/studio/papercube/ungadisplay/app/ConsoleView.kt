package studio.papercube.ungadisplay.app

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import studio.papercube.ungadisplay.model.GeneralAssemblyDisplay
import studio.papercube.ungadisplay.model.ModeratedCaucus
import studio.papercube.ungadisplay.model.SingleRepresentativeRequest
import studio.papercube.ungadisplay.view.*
import studio.papercube.ungadisplay.view.DataInputDialog.ActionResult
import studio.papercube.ungadisplay.view.DataInputDialog.ActionResult.*
import tornadofx.*
import tornadofx.ValidationTrigger.None

class ConsoleView(
        private val application: DisplayApplication,
        private val displayInterface: GeneralAssemblyDisplay
) : View() {
    private lateinit var buttonPush: Button
    private lateinit var keyPressEventHandler: KeyPressEventHandler
    private val rootPane = borderpane {
        keyPressEventHandler = KeyPressEventHandler(this)
        top = anchorpane {
            togglebutton {
                AnchorPane.setTopAnchor(this, 3.0)
                AnchorPane.setLeftAnchor(this, 3.0)
                text = "Toggle Screen"
                isDisable = true
            }

        }

        center = gridpane {
            padding = Insets(70.0)
            alignment = Pos.CENTER
            vgap = 10.0
            hgap = 20.0

            useMaxWidth = true

            vbox {
                gridpaneConstraints {
                    columnRowIndex(0, 0)
                }
                this@vbox.isFillWidth = true
                this@vbox.spacing = 10.0
                menubutton("动议磋商") {
                    item("有主持核心磋商") {
                        action {
                            val dataInputDialog = DataInputDialog(ModeratedCaucus(), this@ConsoleView)
                            dataInputDialog
                                    .bind("代表团", ModeratedCaucus::representativeGroup)
                                    .bind("主题", ModeratedCaucus::topic)
                                    .bind("总时间", ModeratedCaucus::totalTime)
                                    .bind("单位时间", ModeratedCaucus::unitTime)
                                    .bindCheckbox("通过", ModeratedCaucus::accepted)
                                    .validate { moderatedCaucus ->
                                        when {
                                            moderatedCaucus.unitTime > moderatedCaucus.totalTime -> "时间无效"
                                            else -> null
                                        }
                                    }
                                    .onComplete { m ->
                                        displayInterface.newModeratedCaucus(m)
                                        CLOSE_DIALOG
                                    }
                                    .dialog("新建有主持核心磋商")
                        }
                    }
                }


                menubutton("主发言名单") {
                    item("开启") {
                        action {
                            toggleSpeakerListState(true)
                        }
                    }
                    item("关闭") {
                        action {
                            toggleSpeakerListState(false)
                        }
                    }
                }

                menubutton("动议辩论") {
                    item("自由辩论") {
                        action {
                            QuickInputDialogs.generateTimedRequestDialog(this@ConsoleView)
                                    .onComplete { obj ->
                                        displayInterface.newFreeDebate(obj)
                                        CLOSE_DIALOG
                                    }
                                    .dialog("新建自由辩论")

                        }
                    }
                    item("有主题辩论") {
                        action {
                            QuickInputDialogs.generateModeratedRequestDialog(this@ConsoleView)
                                    .onComplete { obj->
                                        displayInterface.newModeratedDebate(obj)
                                        CLOSE_DIALOG
                                    }
                                    .dialog("新建有主题辩论")
                        }
                    }
                    item("结束辩论") {
                        action {
                            displayInterface.newDebateClosure(SingleRepresentativeRequest())
                        }
                    }
                }

                button("动议休会") {
                    action {
//                        QuickInputDialogs.
                    }
                }

                childrenUseMaxWidth()
            }

            separator {
                gridpaneConstraints {
                    columnRowIndex(0, 1)
                }
            }

            vbox {
                spacing = 10.0
                gridpaneConstraints {
                    columnRowIndex(0, 2)
                }

                val validationContext = ValidationContext()
                val textAreaPush = textarea {
                    validationContext.addValidator(this, textProperty(), None) { str: String? ->
                        when {
                            str == null -> error("Unexpected null value")
                            str.isEmpty() -> error("请输入内容")
                            else -> null
                        }
                    }
                }

                hbox {
                    spacing = 6.0
                    buttonPush = button("推送") {
                        tooltip("推送到主屏幕 (Ctrl + Enter)")
                        action {
                            if (validationContext.validate()) {
                                displayInterface.pushMessage(textAreaPush.text)
                                textAreaPush.clear()
                            }
                        }

                        keyPressEventHandler.registerEvent(this, "Ctrl+Enter")
                    }

                    button("清空演示记录") {
                        textFill = Color.RED
                        action {
                            displayInterface.clearLog()
                        }
                    }
                    childrenUseMaxWidth()
                    childrenHgrow(Priority.ALWAYS)
                }

                childrenUseMaxWidth()
            }
        }
    }

    private fun toggleSpeakerListState(on: Boolean) {
        val textRequestedState = if (on) "开启" else "关闭"
        val dialog = DataInputDialog(SingleRepresentativeRequest(), this)
        dialog.bind("主席团", SingleRepresentativeRequest::from)
                .bindCheckbox("通过", SingleRepresentativeRequest::accepted)
                .onComplete { m ->
                    displayInterface.toggleSpeakerListState(on, m)
                    CLOSE_DIALOG
                }
                .dialog(textRequestedState + "主发言名单")
    }

    override val root: Parent
        get() = rootPane
}