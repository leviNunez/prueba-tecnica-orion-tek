package com.prueba.tinyshopapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.prueba.tinyshopapp.R
import com.prueba.tinyshopapp.data.database.CustomerDatabase
import com.prueba.tinyshopapp.data.repository.CustomerRepositoryImpl
import com.prueba.tinyshopapp.databinding.FragmentCustomerDetailBinding
import com.prueba.tinyshopapp.ui.view.adapters.AddressListAdapter
import com.prueba.tinyshopapp.ui.view.adapters.FooterItemListener
import com.prueba.tinyshopapp.ui.viewmodel.customerdetail.CustomerDetailViewModel
import com.prueba.tinyshopapp.ui.viewmodel.customerdetail.CustomerDetailViewModelFactory
import com.prueba.tinyshopapp.utils.collectLifecycleFlow

class CustomerDetailFragment : Fragment() {
    lateinit var binding: FragmentCustomerDetailBinding
    private lateinit var customerDetailViewModel: CustomerDetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_customer_detail, container, false)

        val application = requireNotNull(this.activity)

        val customerDao = CustomerDatabase.getInstance(application).customerDatabaseDao

        val repository = CustomerRepositoryImpl(customerDao)

        val viewModelFactory = CustomerDetailViewModelFactory(repository)

        customerDetailViewModel =
            ViewModelProvider(this, viewModelFactory)[CustomerDetailViewModel::class.java]

        val adapter = AddressListAdapter(FooterItemListener {
            customerDetailViewModel.onAddNewAddress()
        })

        binding.addressList.adapter = adapter

        customerDetailViewModel.customer.observe(viewLifecycleOwner) { customer ->
            customer?.let {
                adapter.addFooterAndSubmitList(it.addresses)
            }
        }

        collectLifecycleFlow(customerDetailViewModel.navigateToNewAddress) { customerId ->
            this.findNavController()
                .navigate(
                    CustomerDetailFragmentDirections.actionCustomerDetailFragmentToNewAddressFragment(
                        customerId
                    )
                )
        }
        collectLifecycleFlow(customerDetailViewModel.navigateToHome) {
            this.findNavController()
                .navigate(
                    CustomerDetailFragmentDirections.actionCustomerDetailFragmentToHomeFragment()
                )
        }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = customerDetailViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()

    }

    private fun setupMenu() {
        val toolbar = binding.customerDetailToolbar
        toolbar.inflateMenu(R.menu.action_bar_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_delete -> {
                    showDeleteDialog()
                    true
                }
                else -> false

            }
        }
    }

    private fun showDeleteDialog() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(
                    R.string.ok
                ) { dialog, _ ->
                    dialog.dismiss()
                    customerDetailViewModel.onDeleteCustomer()

                }
                setNegativeButton(
                    R.string.cancel
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                setMessage(R.string.delete_dialog_message)
                setTitle(R.string.delete_dialog_title)
            }

            builder.create()
        }
        alertDialog?.show()
    }

}