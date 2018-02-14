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
import studio.papercube.ungadisplay.model.SpeakerListStateToggleRequest
import studio.papercube.ungadisplay.view.DataInputDialog
import studio.papercube.ungadisplay.view.KeyPressEventHandler
import studio.papercube.ungadisplay.view.childrenHgrow
import studio.papercube.ungadisplay.view.childrenUseMaxWidth
import tornadofx.*
import tornadofx.ValidationTrigger.None

class ConsoleView(
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
                                    .dialog("新建有主持核心磋商") {
                                        dataInputDialog.onComplete { m ->
                                            displayInterface.newModeratedCaucus(m)
                                            return@onComplete DataInputDialog.ActionResult.CLOSE_DIALOG
                                        }
                                    }
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
                            displayInterface.newFreeDebate()
                        }
                    }
                    item("有主题辩论") {
                        action {
                            displayInterface.newModeratedDebate()
                        }
                    }
                    item("结束辩论") {
                        action {
                            displayInterface.newDebateClosure()
                        }
                    }
                }

                button("动议休会") {
                    action {
                        displayInterface.newDebateClosure()
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
        val dialog = DataInputDialog(SpeakerListStateToggleRequest(), this)
//                .bind
    }

    override val root: Parent
        get() = rootPane
}