package com.vhpg.popurrivault.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.databinding.FragmentConfigBinding

class ConfigFragment : Fragment() {
    private var _binding: FragmentConfigBinding? = null
    private val binding get() = _binding!!

    private val PREFS_NAME = "MisPreferencias"
    private val NOTIFICACIONES_CHECKBOX_KEY = "notificaciones_checkbox"
    private val MODO_AVANZADO_CHECKBOX_KEY = "modo_avanzado_checkbox"


    private lateinit var notificacionesCheckBox: CheckBox
    private lateinit var modoAvanzadoCheckBox: CheckBox

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfigBinding.inflate(inflater, container, false)
        notificacionesCheckBox = binding.notificacionesCheckbox
        modoAvanzadoCheckBox = binding.modoAvanzadoCheckbox

        // Cargar el estado de los CheckBox desde las SharedPreferences
        val prefs = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        notificacionesCheckBox.isChecked = prefs?.getBoolean(NOTIFICACIONES_CHECKBOX_KEY, false) ?: false
        modoAvanzadoCheckBox.isChecked = prefs?.getBoolean(MODO_AVANZADO_CHECKBOX_KEY, false) ?: false

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ahora puedes acceder a las vistas usando binding.<nombreDeLaVista>
        //binding.tvPrueba.text = "Exito!!!"
    }
    override fun onPause() {
        super.onPause()

        // Guardar el estado actual de los CheckBox en las SharedPreferences
        val editor = activity?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)?.edit()
        editor?.putBoolean(NOTIFICACIONES_CHECKBOX_KEY, notificacionesCheckBox.isChecked)
        editor?.putBoolean(MODO_AVANZADO_CHECKBOX_KEY, modoAvanzadoCheckBox.isChecked)
        editor?.apply()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}