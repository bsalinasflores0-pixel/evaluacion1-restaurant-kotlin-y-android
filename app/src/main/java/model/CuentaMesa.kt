package model

class CuentaMesa(val mesa: Int) {

    private val items: MutableList<ItemMesa> = mutableListOf()
    var aceptaPropina: Boolean = false

    fun agregarItem(itemMenu: ItemMenu, cantidad: Int) {
        val existente = items.find { it.itemMenu.nombre == itemMenu.nombre }

        if (cantidad <= 0) {

            if (existente != null) items.remove(existente)
            return
        }

        if (existente != null) {
            existente.cantidad = cantidad
        } else {
            items.add(ItemMesa(itemMenu, cantidad))
        }
    }

    fun calcularTotalSinPropina(): Int = items.sumOf { it.calcularSubtotal() }

    fun calcularPropina(): Int {
        return if (aceptaPropina) (calcularTotalSinPropina() * 10) / 100 else 0
    }

    fun calcularTotalConPropina(): Int = calcularTotalSinPropina() + calcularPropina()

    fun subtotalDe(itemMenu: ItemMenu): Int {
        val item = items.find { it.itemMenu.nombre == itemMenu.nombre }
        return item?.calcularSubtotal() ?: 0
    }
}