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
import com.vhpg.popurrivault.data.SaleRepository
import com.vhpg.popurrivault.data.db.model.*
import com.vhpg.popurrivault.databinding.FragmentAddOrderBinding
import com.vhpg.popurrivault.databinding.FragmentAddSaleBinding
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class AddSaleFragment: Fragment() {
    private val newSale: Boolean = true
    private val sale: SaleEntity = SaleEntity(
        idClient = null,
        description = "",
        dateArrive = Date().time,
        dateOrder = Date().time,
        price = 0.0,
        arrived = false
    )
    private lateinit var selectedProducts: MutableList<ProductEntity>
    /*private val updateUI: () -> Unit
    private val message: (Int) -> Unit*/

    //private lateinit var cameraHelper: CameraHelper
    private var _binding: FragmentAddSaleBinding? = null
    private val binding get() = _binding!!

    private var keyClient: Long? = null

    private var saveButton: Button? = null

    //private var products: List<ProductEntity> = emptyList()
    private lateinit var repository: SaleRepository

    private lateinit var client: ContactEntity

    private lateinit var datePicker: DatePicker

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddSaleBinding.inflate(inflater, container, false)

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
                    insertSale()
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
        var priceSale = 0.0
        selectedProducts.forEach { product ->
            val df = DecimalFormat("#.##")

            priceSale += df.format(  product.price*product.stock  ).toDouble()
            //priceSale+=product.price*product.stock

        }
        sale.price = priceSale

        Log.d("addOrder","selected products : $selectedProducts")
        repository = (requireActivity().application as PopurriVaultBDApp).saleRepository


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

            tietDesc.setText("${sale.description}")
            tietCost.setText("${sale.price}")
            // Deshabilita el TextInputLayout
            tilCost.isEnabled = false

            // Deshabilita el TextInputEditText
            tietCost.isEnabled = false
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
                "CLIENT"
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
    private fun insertSale(): Boolean{
        val selectedDate = Calendar.getInstance()
        selectedDate.set(Calendar.YEAR, datePicker.year)
        selectedDate.set(Calendar.MONTH, datePicker.month)
        selectedDate.set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)

        // Obtener el valor long en milisegundos desde el DatePicker
        val selectedMilliseconds: Long = selectedDate.timeInMillis

        sale.description = "${binding.tietDesc.text}"
        sale.price = binding.tietCost.text.toString().toDouble()

        sale.idClient = keyClient

        sale.dateArrive = selectedMilliseconds

        sale.arrived = binding.arrived.isChecked
        Log.d("sale","$sale")
        /*return try{
            lifecycleScope.launch{
                repository.createRestock(order,selectedProducts)

            }
            true
        }catch(e: IOException){
            e.printStackTrace()
            false
        }*/

        lifecycleScope.launch(Dispatchers.IO){
            try {
                repository.createSale(sale, selectedProducts)
                // Realizar acciones después de la creación del restock, si es necesario
            } catch (e: IOException) {
                e.printStackTrace()
                // Manejar la excepción aquí
            }
        }
        return true

    }
    /*private fun validateFields(): Boolean{
        var Ok = true

        Ok = Ok && binding.tietDesc.text.toString().isNotEmpty()
        Ok = Ok && binding.tietCost.text.toString().isNotEmpty()


        return(Ok)
    }*/

    private fun validateFields(): Boolean{
        var isOk = true

        // Validar que tietDesc no esté vacío
        isOk = isOk && binding.tietDesc.text.toString().isNotEmpty()

        // Validar que tietCost no esté vacío y sea un Double válido
        val costText = binding.tietCost.text.toString()
        isOk = isOk && costText.isNotEmpty() && isValidDouble(costText)

        return isOk
        /*var Ok = true

        Ok = Ok && binding.tietDesc.text.toString().isNotEmpty()
        Ok = Ok && binding.tietCost.text.toString().isNotEmpty()


        return(Ok)*/
    }

    private fun isValidDouble(input: String): Boolean {
        return try {
            input.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun messageDialog(text: String){
        Snackbar.make(binding.dial, text, Snackbar.LENGTH_SHORT)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            .show()
    }

    private fun linkContact(keyClientBack:Long?,contact: ContactEntity){
        client = contact
        keyClient = keyClientBack
        sale.idClient = keyClient
        binding.apply {
            tvNameSupplier.text = client.name

        }
    }
}