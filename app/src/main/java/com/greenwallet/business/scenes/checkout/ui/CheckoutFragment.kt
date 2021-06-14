package com.greenwallet.business.scenes.checkout.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.greenwallet.business.R
import com.greenwallet.business.databinding.FragmentCheckoutBinding
import com.greenwallet.business.helper.shop.getBasketPrices
import com.greenwallet.business.helper.ui.ImageLoaderListener
import com.greenwallet.business.helper.ui.VerticalSpaceItemDecoration
import com.greenwallet.business.network.product.response.CartProduct
import com.greenwallet.business.scenes.basket.model.CheckoutItem

class CheckoutFragment : Fragment(), CheckoutView, CheckoutAdapter.EditClickListener,
    CheckoutAdapter.CheckoutItemChangeListener {

    private lateinit var presenter: CheckoutView.Presenter

    private lateinit var binding: FragmentCheckoutBinding

    private lateinit var checkoutAdapter: CheckoutAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBackButton.setOnClickListener {
            presenter.onBackPressed()
        }
        updateCheckoutDetails()

        initCheckoutItems()
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeView(this)

        updateCheckoutDetails()

        initCheckoutItems()
    }

    override fun onStop() {
        presenter.disposeView(this)
        super.onStop()
    }

    private fun updateCheckoutDetails() {
        val checkoutDetailsItem = presenter.getCheckoutDetails()

        binding.tvGreenCoinsTitle.text =
            context?.getString(R.string.label_coins_number, checkoutDetailsItem.greenCoinsAmount)
        binding.tvGreenCoinsMessage.text = context?.getString(
            R.string.label_redeemed_coins,
            checkoutDetailsItem.redeemedGreenCoins,
            checkoutDetailsItem.redeemedGreenCoins.toFloat().div(100)
        )

        binding.tvPaymentValue.text =
            context?.getString(
                R.string.label_payment_card_end,
                "X"
            )

        binding.tvShippingAddressMessage.text = "Address"

        //summary
        updateSummary()
    }

    private fun updateSummary() {
        val checkoutDetailsItem = presenter.getCheckoutDetails()
        val checkoutPrices = getBasketPrices(ArrayList(presenter.getCheckoutItems())).second

        binding.tvSubtotalValue.text =
            context?.getString(R.string.label_price, checkoutPrices.subtotal.toFloat().div(100))
        binding.tvTotalVatValue.text =
            context?.getString(R.string.label_price, checkoutPrices.vat.toFloat().div(100))
        binding.tvTotalShippingValue.text =
            context?.getString(R.string.label_price, checkoutPrices.shipping.toFloat().div(100))
        binding.tvGreenCoinsRedeemedValue.text =
            context?.getString(
                R.string.label_green_coins_redeemed_value,
                checkoutDetailsItem.redeemedGreenCoins.toFloat().div(100)
            )

        val total: Float =
            checkoutPrices.subtotal.toFloat().div(100)
                .plus(checkoutPrices.vat.toFloat().div(100))
                .plus(checkoutPrices.shipping.toFloat().div(100))
                .minus(checkoutDetailsItem.redeemedGreenCoins.toFloat().div(100))

        binding.tvGrandTotalValue.text =
            context?.getString(R.string.label_price, total)
    }

    private fun initCheckoutItems() {
        val checkoutItems = presenter.getCheckoutItems()

        if (!this::checkoutAdapter.isInitialized) {
            checkoutAdapter = CheckoutAdapter(this, this)

            binding.rvCheckoutProducts.addItemDecoration(
                VerticalSpaceItemDecoration(
                    resources.getDimension(R.dimen.margin_large).toInt()
                )
            )
            binding.rvCheckoutProducts.adapter = checkoutAdapter
        }
        checkoutAdapter.items = checkoutItems
    }

    override fun onEditCheckoutItemClicked(item: CheckoutItem) {
        presenter.onEditCheckoutItemClicked()
    }

    override fun onCheckoutItemCountChanged(cartItem: CartProduct) {
        presenter.updateProductCount(cartItem)
        if (presenter.getCheckoutItems().size == 0) {
            presenter.onBackPressed()
        }

        updateCheckoutDetails()
    }

    override fun onImageLoadListener(
        fileId: String,
        loaderListener: ImageLoaderListener,
        sizes: Pair<Int, Int>?
    ) {
        presenter.fetchImage(fileId, loaderListener, sizes, true)
    }

    override fun onItemEditShippingClicked(productItem: CartProduct) {
        presenter.onItemEditShippingClicked(productItem)
    }

    override fun updateValues() {
        updateCheckoutDetails()

        initCheckoutItems()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CheckoutPresenterProvider) {
            this.presenter = context.getCheckoutPresenter()
        } else {
            throw RuntimeException("$context must implement CheckoutPresenterProvider")
        }
    }

    interface CheckoutPresenterProvider {
        fun getCheckoutPresenter(): CheckoutView.Presenter
    }
}