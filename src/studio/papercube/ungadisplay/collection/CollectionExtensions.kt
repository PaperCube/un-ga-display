package studio.papercube.ungadisplay.collection

class ModifyList<E>(private val collection: MutableList<E>) {
    inline fun modify(modifier: ModifyList<E>.() -> Unit) {
        modifier()
    }

    operator fun E.unaryPlus() {
        collection += this
    }
}