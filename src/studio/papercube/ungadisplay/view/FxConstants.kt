package studio.papercube.ungadisplay.view

import javafx.geometry.Insets
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.paint.Color
import javafx.scene.paint.Color.rgb
import javafx.scene.text.Font

object FxColors {
    val WORD_BLUE = rgb(66, 105, 165)!!
}

object FxFonts {
    val DEFAULT_NAME = Font.getDefault().name!!
    fun String.asFont(size:Int): Font = Font.font(this, size.toDouble())
}

object FxBackgrounds {
    fun fromColor(color: Color, cornerRadii: CornerRadii? = null, insets: Insets = Insets.EMPTY): Background {
        return Background(BackgroundFill(color, cornerRadii, insets))
    }
}