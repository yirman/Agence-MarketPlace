package com.agence.marketplace.ui.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.agence.marketplace.R
import com.agence.marketplace.databinding.FragmentProductDetailBinding
import com.agence.marketplace.model.Product
import com.agence.marketplace.ui.viewmodel.ProductDetailViewModel
import com.agence.marketplace.util.createConfirmationProductDialog
import com.agence.marketplace.util.createSuccessBuyDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.example.flatdialoglibrary.dialog.FlatDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductDetailFragment : Fragment(), OnMapReadyCallback, MultiplePermissionsListener {

    private lateinit var binding: FragmentProductDetailBinding
    private var mMap: GoogleMap? = null

    private val viewModel: ProductDetailViewModel by viewModels()
    private val args: ProductDetailFragmentArgs by navArgs()

    var dialog: FlatDialog? = null
    var snackbar: Snackbar? = null

    private lateinit var product: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        viewModel.productDetail.observe(requireActivity()){
            product = it
            binding.name.text = it.name
            binding.price.text = it.price
            binding.description.text = it.description
            Glide.with(binding.root)
                .load(it.thumbnailUrl)
                .placeholder(CircularProgressDrawable(binding.root.context).apply { start() })
                .signature(ObjectKey(it))
                .into(binding.thumbnail.findViewById(R.id.thumbnail))
        }

        viewModel.locationLiveData?.observe(requireActivity()){
            val latLng = LatLng(it!!.latitude, it.longitude)
            mMap?.let { map ->
                map.addMarker(MarkerOptions().position(latLng).title(getString(R.string.me)))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
            }
        }

        val productId = args.productId
        viewModel.requestProductDetail(productId)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.buy.setOnClickListener {
            dialog = createConfirmationProductDialog(
                requireActivity(),
                product.name!!,
                {
                    dialog!!.dismiss()
                    dialog = createSuccessBuyDialog(requireActivity())
                    dialog!!.show()
                    viewModel.startTimer()
                },
                {
                    dialog?.dismiss()
                }
            )
            dialog!!.show()
        }

        viewModel.isElapsedTime.observe(requireActivity()){ isElapsedTime ->
            if(isElapsedTime){
                dialog?.dismiss()
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProductDetailFragment()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Dexter.withContext(requireActivity())
            .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(this)
            .check()
    }

    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
        if(report!!.areAllPermissionsGranted()){
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                viewModel.startLocationService()
            }
        }
        else{
            snackbar = Snackbar.make(
                binding.root,
                getString(R.string.allow_location_permission),
                Snackbar.LENGTH_INDEFINITE
            ).setAction(getString(R.string.allow)) {
                val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", requireActivity().packageName, null)
                startActivity(intent);
            }
            snackbar!!.show()
        }
    }

    override fun onPermissionRationaleShouldBeShown(
        list: MutableList<PermissionRequest>?,
        permissionToken: PermissionToken?
    ) {
        permissionToken?.continuePermissionRequest()
    }

    override fun onDestroy() {
        snackbar?.dismiss()
        super.onDestroy()
    }
}