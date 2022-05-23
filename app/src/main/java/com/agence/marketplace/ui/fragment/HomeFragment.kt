package com.agence.marketplace.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.agence.marketplace.R
import com.agence.marketplace.databinding.FragmentHomeBinding
import com.agence.marketplace.model.Product
import com.agence.marketplace.ui.activity.LoginActivity
import com.agence.marketplace.ui.activity.MainActivity
import com.agence.marketplace.ui.adapter.ProductAdapter
import com.agence.marketplace.ui.viewmodel.HomeViewModel
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), ProductAdapter.OnProductClickListener {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.recyclerProducts.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }

        homeViewModel.requestProducts()
        homeViewModel.products.observe(requireActivity()){
            binding.swipeRefresh.isRefreshing = false
            binding.recyclerProducts.adapter = ProductAdapter(it, this)
        }

        binding.swipeRefresh.setOnRefreshListener {
            homeViewModel.requestProducts()
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onProductClick(product: Product) {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(product.productId!!)
        )
    }
}