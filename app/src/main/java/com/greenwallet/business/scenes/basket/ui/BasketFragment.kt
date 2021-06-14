package com.greenwallet.business.scenes.basket.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.greenwallet.business.databinding.FragmentBasketBinding
import com.greenwallet.business.helper.ui.SwipeToDeleteCallback

class BasketFragment : Fragment(), BasketFeatureView {

    private lateinit var presenter: BasketFeatureView.Presenter

    private lateinit var binding: FragmentBasketBinding

    private lateinit var adapter: BasketAdapter

    lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBackButton.setOnClickListener { presenter.onBackPressed() }
        binding.btnProceedCheckout.setOnClickListener { presenter.onProceedCheckoutClicked() }
        initBasketProducts()
    }

    private fun initBasketProducts() {
        val cartProducts = presenter.getCartProductList()

        if (cartProducts.size > 0) {
            binding.rvBasketProducts.visibility = View.VISIBLE
            binding.clNoItemContainer.visibility = View.GONE
            binding.clBottomContainer.visibility = View.VISIBLE

            if (binding.rvBasketProducts.adapter == null) {
                adapter = BasketAdapter().apply {
                    items = cartProducts
                    itemClickListener = {
                        //todo
                    }
                    imageLoaderListener = { id, listener, sizes ->
                        presenter.fetchImage(id, listener, sizes, true)
                    }

                    deleteListener = {
                        presenter.deleteAnItem(it)

                        if (cartProducts.size > 0) {
                            binding.rvBasketProducts.visibility = View.VISIBLE
                            binding.clNoItemContainer.visibility = View.GONE
                            binding.clBottomContainer.visibility = View.VISIBLE
                        } else {
                            binding.rvBasketProducts.visibility = View.GONE
                            binding.clNoItemContainer.visibility = View.VISIBLE
                            binding.clBottomContainer.visibility = View.GONE
                        }
                    }
                }
                binding.rvBasketProducts.adapter = adapter
            } else {
                (binding.rvBasketProducts.adapter as BasketAdapter).items = cartProducts
            }

            enableSwipeToDelete()
        } else {
            binding.rvBasketProducts.visibility = View.GONE
            binding.clNoItemContainer.visibility = View.VISIBLE
            binding.clBottomContainer.visibility = View.GONE
        }
    }

    private fun enableSwipeToDelete() {
        if (!this::itemTouchHelper.isInitialized) {
            itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter, requireContext()))
            itemTouchHelper.attachToRecyclerView(binding.rvBasketProducts)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BasketFeatureProvider) {
            this.presenter = context.getBasketFeatureProvider()
        } else {
            throw RuntimeException("$context must implement BasketFeatureProvider")
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeView(this)
        initBasketProducts()
    }

    override fun onStop() {
        presenter.disposeView(this)
        super.onStop()
    }

    interface BasketFeatureProvider {
        fun getBasketFeatureProvider(): BasketFeatureView.Presenter
    }
}