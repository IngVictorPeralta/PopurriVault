package com.vhpg.popurrivault.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.databinding.FragmentConfigBinding

class ConfigFragment : Fragment() {
    private var _binding: FragmentConfigBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfigBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ahora puedes acceder a las vistas usando binding.<nombreDeLaVista>
        //binding.tvPrueba.text = "Exito!!!"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}