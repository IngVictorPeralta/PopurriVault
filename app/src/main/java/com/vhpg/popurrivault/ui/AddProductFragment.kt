package com.vhpg.popurrivault.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.application.PopurriVaultBDApp
import com.vhpg.popurrivault.data.ProductRepository
import com.vhpg.popurrivault.data.db.model.ContactEntity
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.databinding.FragmentAddProductBinding
import com.vhpg.popurrivault.util.CameraHelper
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class AddProductFragment: Fragment() {
    private val newProduct: Boolean = true
    private var product: ProductEntity = ProductEntity(
        name = "",
        description = "",
        image ="",
        cost = 0.0,
        price = 0.0,
        stock = 0,
        supplier = null
    )
    var spinnerData = 0
    /*private val updateUI: () -> Unit
    private val message: (Int) -> Unit*/

    //private lateinit var cameraHelper: CameraHelper
    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!

    private var keySupplier: Long? = null

    private var saveButton: Button? = null

    private lateinit var repository: ProductRepository

    private lateinit var supplier: ContactEntity


    private lateinit var cameraHelper: CameraHelper

    private val REQUEST_CAMERA_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 101


    /*private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            //Se concedió el permiso
            startIntentCamera()
        }else{
            if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                //Todavía puedo explicarle la razón
                AlertDialog.Builder(requireActivity())
                    .setTitle("Permiso requerido")
                    .setMessage("Se necesita el permiso para leer los códigos")
                    .setPositiveButton("Entendido"){_ , _ ->
                        updateOrRequestPermissions()
                    }
                    .setNegativeButton("Salir"){ dialog, _ ->
                        dialog.dismiss()
                        requireActivity().finish()
                    }
                    .create()
                    .show()
            }else{
                Toast.makeText(requireContext(),
                    "El permiso de la cámara se ha negado permanentemente",
                    Toast.LENGTH_SHORT
                ).show()

                //findNavController().navigate(R.id.action_scannerFragment_to_mainFragment)
            }
        }
    }*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)



        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_form, menu)
// Obtén la referencia al elemento de menú
        //val menuItem = menu.findItem(R.id.action_save)
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)

            // Cambia el color del icono
            val icon: Drawable? = menuItem.icon
            if (icon != null) {
                DrawableCompat.setTint(icon, ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                menuItem.icon = icon
            }
        }

        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                // Maneja el clic en el botón btAdd aquí
                if(validateFields()){
                    InsertProduct()
                    findNavController().navigateUp()
                }
                //Toast.makeText(requireContext(), "Nuevo producto", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Ahora puedes acceder a las vistas usando binding.<nombreDeLaVista>
        //Toast.makeText(requireContext(),"Holoooo!", Toast.LENGTH_SHORT).show()

        repository = (requireActivity().application as PopurriVaultBDApp).productRepository

        cameraHelper = CameraHelper(requireContext())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }




        binding.apply {

            tietName.setText(product.name)
            tietDesc.setText(product.description)
            tietCost.setText(product.cost.toString())
            tietPrice.setText(product.price.toString())
            tietStock.setText(product.stock.toString())


        }
        binding.ivSupplier.setOnClickListener {
            val dialog = ContactsDialog(
                "SUPPLIER"
            ) { keySupp,supp ->
                linkContact(keySupp,supp)
            }
            dialog.show(parentFragmentManager,"contactsTable")
        }

        binding.ivCameraLaunch.setOnClickListener {
           cameraHelper.openCamera(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onStart() {
        super.onStart()

        binding.tietName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietDesc.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietCost.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietPrice.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.tietStock.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
    }
    private fun InsertProduct(): Boolean {
        product.name = binding.tietName.text.toString()
        product.description = binding.tietDesc.text.toString()
        product.cost = binding.tietCost.text.toString().toDouble()
        product.price = binding.tietPrice.text.toString().toDouble()
        //product.category = spinnerData
        product.stock = binding.tietStock.text.toString().toInt()
        product.supplier = keySupplier
        return try{
            lifecycleScope.launch{
                repository.insertProduct(product)

            }
            true
        }catch(e: IOException){
            e.printStackTrace()
            false
        }
    }
    private fun validateFields(): Boolean{
        var Ok = true
        Ok = Ok && binding.tietName.text.toString().isNotEmpty()
        Ok = Ok && binding.tietDesc.text.toString().isNotEmpty()
        Ok = Ok && binding.tietCost.text.toString().isNotEmpty()
        Ok = Ok && binding.tietPrice.text.toString().isNotEmpty()
        Ok = Ok && binding.tietStock.text.toString().isNotEmpty()
        if(Ok) {
            var priceVsCost = binding.tietCost.text.toString().toInt() < binding.tietPrice.text.toString().toInt()
            if (!priceVsCost) {
                messageDialog(getString(R.string.priceVsCost))
            }
            Ok = Ok && priceVsCost
        }
        return(Ok)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        product.image = "${cameraHelper.getImageUri()}"
        binding.ivProduct.setImageURI(cameraHelper.getImageUri())
        Log.d("CAMERA","requestCode $requestCode")
        Log.d("CAMERA"," resultCode $resultCode")
        Log.d("CAMERA","PackageManager.PERMISSION_GRANTED ${PackageManager.PERMISSION_GRANTED}")
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == PackageManager.PERMISSION_GRANTED) {
            // The photo is saved, you can do something with the imageUri if needed
            // For example, display the image in an ImageView
            binding.ivProduct.setImageURI(cameraHelper.getImageUri())
        }
    }


    private fun messageDialog(text: String){
        Snackbar.make(binding.dial, text, Snackbar.LENGTH_SHORT)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            .show()
    }

    private fun linkContact(keySupp:Long?,contact: ContactEntity){
        supplier = contact
        keySupplier = keySupp
        binding.apply {
            tvNameSupplier.text = supplier.name
        }
    }
}