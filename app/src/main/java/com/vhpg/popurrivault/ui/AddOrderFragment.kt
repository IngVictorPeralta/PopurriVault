package com.vhpg.popurrivault.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.vhpg.popurrivault.R
import com.vhpg.popurrivault.application.PopurriVaultBDApp
import com.vhpg.popurrivault.data.ProductRepository
import com.vhpg.popurrivault.data.RestockRepository
import com.vhpg.popurrivault.data.db.model.ContactEntity
import com.vhpg.popurrivault.data.db.model.OrderEntity
import com.vhpg.popurrivault.data.db.model.ProductEntity
import com.vhpg.popurrivault.databinding.FragmentAddOrderBinding

import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class AddOrderFragment: Fragment() {
    private val newOrder: Boolean = true
    private val order: OrderEntity = OrderEntity(
        idSupplier = null,
        description = "",
        dateArrive = Date().time,
        dateOrder = Date().time,
        cost = 0.0,
        arrived = false
    )
    private lateinit var selectedProducts: MutableList<ProductEntity>
    /*private val updateUI: () -> Unit
    private val message: (Int) -> Unit*/

    //private lateinit var cameraHelper: CameraHelper
    private var _binding: FragmentAddOrderBinding? = null
    private val binding get() = _binding!!

    private var keySupplier: Long? = null

    private var saveButton: Button? = null

    //private var products: List<ProductEntity> = emptyList()
    private lateinit var repository: RestockRepository

    private lateinit var supplier: ContactEntity

    private lateinit var datePicker: DatePicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddOrderBinding.inflate(inflater, container, false)

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
                    insertOrder()
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
        val args: AddOrderFragmentArgs by navArgs()
        selectedProducts = args.selectedProducts.toMutableList()
        Log.d("selectedProducts","${selectedProducts.count()}")
        repository = (requireActivity().application as PopurriVaultBDApp).restockRepository


        // Obtener la fecha actual
        val currentDate = Calendar.getInstance()

        // Configurar el DatePicker con la fecha actual
        datePicker = binding.datePicker
        datePicker.init(
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH),
            null
        )







        binding.apply {

            tietDesc.setText("${order.description}")
            tietCost.setText("${order.cost}")

        }

        binding.arrived.setOnCheckedChangeListener { buttonView, isChecked ->
            // Realizar acciones cuando el CheckBox se marca o desmarca
            if (isChecked) {
                // CheckBox marcado
                datePicker.visibility = View.GONE
            } else {
                // CheckBox desmarcado
                datePicker.visibility = View.VISIBLE
            }


        }
        binding.ivSupplier.setOnClickListener {
            val dialog = ContactsDialog(
                "SUPPLIER"
            ) { keySupp,supp ->
                linkContact(keySupp,supp)
            }
            dialog.show(parentFragmentManager,"contactsTable")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onStart() {
        super.onStart()


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

    }
    private fun insertOrder(): Boolean{
        val selectedDate = Calendar.getInstance()
        selectedDate.set(Calendar.YEAR, datePicker.year)
        selectedDate.set(Calendar.MONTH, datePicker.month)
        selectedDate.set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)

        // Obtener el valor long en milisegundos desde el DatePicker
        val selectedMilliseconds: Long = selectedDate.timeInMillis

        order.description = "${binding.tietDesc.text}"
        order.cost = binding.tietCost.text.toString().toDouble()

        order.idSupplier = keySupplier

        order.dateArrive = selectedMilliseconds

        order.arrived = binding.arrived.isChecked
        Log.d("order","$order")
        return try{
            lifecycleScope.launch{
                repository.createRestock(order,selectedProducts)

            }
            true
        }catch(e: IOException){
            e.printStackTrace()
            false
        }
    }
    private fun validateFields(): Boolean{
        var Ok = true

        Ok = Ok && binding.tietDesc.text.toString().isNotEmpty()
        Ok = Ok && binding.tietCost.text.toString().isNotEmpty()


        return(Ok)
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