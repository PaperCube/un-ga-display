package studio.papercube.ungadisplay.app

import javafx.application.Application

fun main(args: Array<String>) {
    try {
        Application.launch(DisplayApplication::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}