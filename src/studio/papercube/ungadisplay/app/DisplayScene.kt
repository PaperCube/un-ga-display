package studio.papercube.ungadisplay.app

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Scene
import javafx.scene.control.TextArea
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import studio.papercube.ungadisplay.model.*
import studio.papercube.ungadisplay.view.*
import studio.papercube.ungadisplay.view.FxFonts.asFont

class DisplayScene(displayApplication: DisplayApplication) :
        Scene(DisplayPane()), GeneralAssemblyDisplay {
    val displayPane: DisplayPane = root as DisplayPane

    val defaultInsets = Insets(20.0)

    val textPrimaryCounter = Text("00:00")

    val textSecondaryCounter = Text("00:00")
    val textTitle = Text("Title").apply {
        font = Font.font(FxFonts.DEFAULT_NAME, 40.0)
    }
    val counterView = VBox().apply {
        background = Background(BackgroundFill(Color.WHITE, CornerRadii(18.0), Insets.EMPTY))
        with(textPrimaryCounter) {
            font = Font.font(FxFonts.DEFAULT_NAME, 48.0)
        }

        with(textSecondaryCounter) {
            font = Font.font(FxFonts.DEFAULT_NAME, 24.0)
        }

        alignment = Pos.CENTER

        children.addAll(textPrimaryCounter, textSecondaryCounter)
    }

    val nameListLayout = TextArea().apply {
        //            maxHeight = Double.MAX_VALUE
    }

    val titlePane = FlowPane().apply {
        padding = Insets(20.0)
        background = FxBackgrounds.fromColor(Color.WHITE)
        this.alignment = Pos.CENTER
        textTitle.textAlignment = TextAlignment.CENTER
        children.add(textTitle)
    }

    val loggingArea = TextArea().apply {
        font = FxFonts.DEFAULT_NAME.asFont(30)
    }

    var title: String
        set(value) {
            textTitle.text = value
        }
        get() = textTitle.text

    init {
        displayPane.initComponents()
    }


    private fun DisplayPane.initComponents() {
        alignment = Pos.CENTER
        padding = Insets(60.0)
        background = Background(BackgroundFill(FxColors.WORD_BLUE, CornerRadii(0.0), Insets.EMPTY))
        hgap = 30.0
        vgap = 30.0
        add(counterView, 0, 0, 1, 1)
        add(nameListLayout, 0, 1, 1, 1)
        add(titlePane, 1, 0, 1, 1)
        add(loggingArea, 1, 1, 1, 1)
        maxHeight = Double.MAX_VALUE
        maxWidth = Double.MAX_VALUE
        with(columnConstraints) {
            this += ColumnConstraints(200.0).apply {
                hgrow = Priority.SOMETIMES
            }

            this += ColumnConstraints().apply {
                isFillWidth = true
                hgrow = Priority.ALWAYS
            }
        }

        with(rowConstraints) {
            this += RowConstraints()
            this += RowConstraints(500.0, 500.0, Double.MAX_VALUE, Priority.ALWAYS, VPos.CENTER, true)
        }
    }

    class DisplayPane : GridPane()

    override fun newModeratedCaucus(moderatedCaucus: ModeratedCaucus) {
        with(moderatedCaucus) {
            loggingArea.appendln("[有主持核心磋商]")
                    .appendln("代表团 $representativeGroup 动议一个有主持核心磋商")
                    .appendln("主题为 $topic")
                    .appendln("总时长为 $totalTime ，单位时长为 $unitTime")
            if (accepted) loggingArea.appendln("此动议通过")
            else loggingArea.appendln("此动议未获通过")
        }
    }

    override fun toggleSpeakerListState(on: Boolean, singleRepresentativeRequest: SingleRepresentativeRequest) {
        with(singleRepresentativeRequest) {
            loggingArea.appendln("$from 动议${if(on){"开启"}else{"关闭"}}主发言名单")
            if (accepted) loggingArea.appendln("此动议通过")
            else loggingArea.appendln("此动议未获通过")
        }
    }

    override fun newFreeDebate(timedRequest: TimedRequest) {
        unimplemented()
    }

    override fun newModeratedDebate(moderatedRequest: ModeratedRequest) {
        unimplemented()
    }

    override fun newDebateClosure(singleRepresentativeRequest: SingleRepresentativeRequest) {
        loggingArea.separateMsgs()
                .appendln("")

    }

    override fun newMeetingSuspension(singleRepresentativeRequest: SingleRepresentativeRequest) {
        unimplemented()
    }

    private fun unimplemented() {
        loggingArea.appendln(Thread.currentThread().stackTrace[2].toString() + ": Unsupported")
    }

    override fun pushMessage(string: String) {
        loggingArea.appendln(string)
    }

    override fun clearLog() {
        loggingArea.clear()
    }
}