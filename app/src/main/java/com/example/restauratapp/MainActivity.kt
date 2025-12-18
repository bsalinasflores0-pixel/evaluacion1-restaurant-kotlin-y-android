package com.example.restauratapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import model.CuentaMesa
import model.ItemMenu
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etCantPastel: EditText
    private lateinit var etCantCazuela: EditText

    private lateinit var tvSubtotalPastel: TextView
    private lateinit var tvSubtotalCazuela: TextView

    private lateinit var tvTotalComida: TextView
    private lateinit var tvMontoPropina: TextView
    private lateinit var tvTotalFinal: TextView

    private lateinit var swPropina: SwitchCompat

    private val formatoCLP = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL")).apply {
        maximumFractionDigits = 0
    }


    private val pastel = ItemMenu("Pastel de Choclo", 12000)
    private val cazuela = ItemMenu("Cazuela", 10000)
    private val cuenta = CuentaMesa(mesa = 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etCantPastel = findViewById(R.id.etCantPastel)
        etCantCazuela = findViewById(R.id.etCantCazuela)

        tvSubtotalPastel = findViewById(R.id.tvSubtotalPastel)
        tvSubtotalCazuela = findViewById(R.id.tvSubtotalCazuela)

        tvTotalComida = findViewById(R.id.tvTotalComida)
        tvMontoPropina = findViewById(R.id.tvMontoPropina)
        tvTotalFinal = findViewById(R.id.tvTotalFinal)

        swPropina = findViewById(R.id.swPropina)

        etCantPastel.addTextChangedListener(textWatcher { actualizarCuentaYVista() })
        etCantCazuela.addTextChangedListener(textWatcher { actualizarCuentaYVista() })

        swPropina.setOnCheckedChangeListener { _, _ ->
            actualizarCuentaYVista()
        }

        actualizarCuentaYVista()
    }

    private fun actualizarCuentaYVista() {
        val cantPastel = leerCantidad(etCantPastel)
        val cantCazuela = leerCantidad(etCantCazuela)

        cuenta.agregarItem(pastel, cantPastel)
        cuenta.agregarItem(cazuela, cantCazuela)
        cuenta.aceptaPropina = swPropina.isChecked

        tvSubtotalPastel.text = formatoCLP.format(cuenta.subtotalDe(pastel))
        tvSubtotalCazuela.text = formatoCLP.format(cuenta.subtotalDe(cazuela))

        // Totales
        val totalComida = cuenta.calcularTotalSinPropina()
        val propina = cuenta.calcularPropina()
        val totalFinal = cuenta.calcularTotalConPropina()

        tvTotalComida.text = formatoCLP.format(totalComida)
        tvMontoPropina.text = formatoCLP.format(propina)
        tvTotalFinal.text = formatoCLP.format(totalFinal)
    }

    private fun leerCantidad(et: EditText): Int {
        val txt = et.text?.toString()?.trim().orEmpty()
        return txt.toIntOrNull() ?: 0
    }

    private fun textWatcher(onChange: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onChange()
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }
}
