package studio.papercube.ungadisplay.view

import javafx.scene.Node
import javafx.scene.control.ButtonBase
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent

class KeyPressEventHandler(node: Node) {
    private val operations: MutableMap<KeyCombination, () -> Unit> = HashMap()

    init {
        node.setOnKeyPressed(this::handleEvent)
    }

    private fun handleEvent(keyEvent: KeyEvent) {
        for ((keyCombination, op) in operations) {
            if(keyCombination.match(keyEvent)) op()
        }
    }

    fun registerEvent(button: ButtonBase, keyCombination:String) {
        operations[KeyCombination.valueOf(keyCombination)] = {
            button.fire()
        }
    }
}