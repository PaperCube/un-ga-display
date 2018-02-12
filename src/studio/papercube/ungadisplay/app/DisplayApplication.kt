package studio.papercube.ungadisplay.app

import javafx.stage.Stage
import tornadofx.App

class DisplayApplication : App() {
    override fun start(stage: Stage) {
        stage.isMaximized = true
//        primaryStage.isResizable = false
//        primaryStage.isIconified = true
//        primaryStage.minHeight = 720.0
//        primaryStage.minWidth = 1280.0
        val displayScene = DisplayScene()
        stage.scene = displayScene
        stage.show()

        val secondaryStage = Stage()
        val consoleView = ConsoleView(displayScene)
        secondaryStage.setOnCloseRequest {
            stage.close()
        }
//        secondaryStage.scene = consoleView.root.scene
        secondaryStage.scene = createPrimaryScene(consoleView)
        secondaryStage.show()
    }

}