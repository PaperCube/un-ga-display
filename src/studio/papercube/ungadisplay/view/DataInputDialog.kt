package studio.papercube.ungadisplay.view

import javafx.scene.Node
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import javafx.scene.text.Text
import studio.papercube.ungadisplay.view.DataInputDialog.Messages.MESSAGE_CANNOT_BE_EMPTY
import studio.papercube.ungadisplay.view.DataInputDialog.Messages.MESSAGE_NUMBER_ONLY
import tornadofx.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.jvm.jvmName

open class DataInputDialog<T : Any>(private val obj: T, private val uiComponent: UIComponent) {

    enum class ActionResult {
        KEEP_DIALOG,
        CLOSE_DIALOG;
    }

    private object Messages {
        const val MESSAGE_CANNOT_BE_EMPTY = "不可为空"
        const val MESSAGE_NUMBER_ONLY = "只能是数字"
    }

    private data class InputObject<T : Any>(val fieldDescription: String,
                                            val classMemberKProperty: KMutableProperty<T>,
                                            val objectKClass: KClass<T>,
                                            val isFieldRequired: Boolean,
                                            var finalValidator: (ValidationContext.(String?) -> ValidationMessage)? = null)

    private data class ObjectNodePair<T, N : Node>(val obj: T) {
        lateinit var node: N
        operator fun component2(): N = node
    }

    private val propertyList: MutableCollection<ObjectNodePair<InputObject<*>, TextField>> = ArrayList()
    private val booleanPropertyList = ArrayList<ObjectNodePair<InputObject<Boolean>, CheckBox>>()
    private var actionOnComplete: ((T) -> ActionResult)? = null
    private var dialogLevelDataValidator: ((T) -> String?)? = null
    private var autoClose: Boolean = true
    private val acceptableKClasses = arrayOf(String::class, Int::class, Long::class, Double::class)
    private lateinit var keyPressEventHandler: KeyPressEventHandler
    private lateinit var textDialogLevelDataValidationMsg: Text

    inline fun <reified R : Any> bind(fieldName: String,
                                      property: KMutableProperty<R>,
                                      required: Boolean = true) = apply {
        bind(fieldName, property, R::class, required)
    }

    fun <R : Any> bind(fieldName: String,
                       property: KMutableProperty<R>,
                       objClass: KClass<R>,
                       required: Boolean = true) = apply {
        if (objClass !in acceptableKClasses) throw IllegalArgumentException("$objClass cannot be accepted")
        propertyList += ObjectNodePair<InputObject<*>, TextField>(InputObject(fieldName, property, objClass, required))
    }

    fun bindCheckbox(fieldName: String,
                     property: KMutableProperty<Boolean>) = apply {
        booleanPropertyList += ObjectNodePair(InputObject(fieldName, property, Boolean::class, false))
    }

    fun autoClose(flag: Boolean = true) = apply {
        autoClose = flag
    }

    fun validate(validator: ((T) -> String?)?) = apply {
        dialogLevelDataValidator = validator
    }

    fun onComplete(action: (T) -> ActionResult) {
        actionOnComplete = action
    }

    fun dialog(title: String = "", op: StageAwareFieldset.() -> Unit) {
        uiComponent.dialog(title) {
            keyPressEventHandler = KeyPressEventHandler(this)
            val validationContext = ValidationContext()
            for (property in propertyList) {
                val (desc, _, objClass, required) = property.obj
                field(desc) {
                    textfield {
                        property.node = this
                        validationContext.addValidator(this) { str: String? ->
                            when {
                                objClass == Long::class || objClass == Int::class ->
                                    when (str) {
                                        null -> error(MESSAGE_CANNOT_BE_EMPTY)
                                        else -> try {
                                            str.toLong()
                                            null
                                        } catch (e: NumberFormatException) {
                                            error(MESSAGE_NUMBER_ONLY)
                                        }
                                    }
                                objClass == Double::class ->
                                    when (str) {
                                        null -> error(MESSAGE_CANNOT_BE_EMPTY)
                                        else -> try {
                                            str.toDouble()
                                            null
                                        } catch (e: NumberFormatException) {
                                            error(MESSAGE_NUMBER_ONLY)
                                        }
                                    }
                                required && str.isNullOrBlank() -> error(MESSAGE_CANNOT_BE_EMPTY)
                                else -> null
                            }
                        }
                    }
                }
            }

            for (booleanProp in booleanPropertyList) {
                booleanProp.node = checkbox(booleanProp.obj.fieldDescription)
            }

            op()

            buttonbar {
                textDialogLevelDataValidationMsg = text("").apply {
                    fill = Color.RED
                }
            }

            buttonbar {
                button("完成") {
                    keyPressEventHandler.registerEvent(this, "Enter")
                    action {
                        if (validationContext.validate()) {
                            applyResultsToObject()
                            if (validateDialog()) {
                                val actionResult = this@DataInputDialog.actionOnComplete?.invoke(obj)
                                        ?: if (autoClose) ActionResult.CLOSE_DIALOG else ActionResult.KEEP_DIALOG
                                if (actionResult == ActionResult.CLOSE_DIALOG) close()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun validateDialog(): Boolean {
        val result = dialogLevelDataValidator?.invoke(obj)
        return if (result == null) {
            true
        } else {
            textDialogLevelDataValidationMsg.text = result
            false
        }
    }

    private fun applyResultsToObject() {
        for ((inputObject, textField) in propertyList) {
            val (_, classMemberKProperty, objClass) = inputObject
            val text = textField.text
            val convertedObject: Any = when (objClass) {
                Int::class -> text.toInt()
                Long::class -> text.toLong()
                String::class -> text
                Double::class -> text.toDouble()
                else -> IllegalStateException("Unexpected condition: unexpectedly requested to convert to ${objClass.jvmName}")
            }
            classMemberKProperty.setter.call(obj, convertedObject)
        }

        for ((inputObject, checkBox) in booleanPropertyList) {
            val classMemberBooleanKProperty = inputObject.classMemberKProperty
            classMemberBooleanKProperty.setter.call(obj, checkBox.isSelected)
        }
    }

    fun get(): T {
        return obj
    }
}