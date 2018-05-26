package studio.papercube.ungadisplay.app

import javafx.stage.Stage
import tornadofx.App

class DisplayApplication : App() {
    override fun start(stage: Stage) {
        stage.isMaximized = true
        val displayScene = DisplayScene(this)
        stage.scene = displayScene
        stage.show()

        val secondaryStage = Stage()
        val consoleView = ConsoleView(this, displayScene)
        secondaryStage.setOnCloseRequest {
            stage.close()
        }
//        secondaryStage.scene = consoleView.root.scene
        secondaryStage.scene = createPrimaryScene(consoleView)
        secondaryStage.show()
    }

}