package com.greenwallet.business.scenes.selectShipping.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.greenwallet.business.R
import com.greenwallet.business.databinding.FragmentSelectShippingMethodBinding
import com.greenwallet.business.network.product.response.ProductShipmentsResponseModel
import com.greenwallet.business.network.product.response.ShippingMethod
import com.greenwallet.business.network.product.response.getShippingMethod

class SelectShippingMethodFragment : Fragment(), SelectShippingMethodView {

    private lateinit var presenter: SelectShippingMethodView.Presenter
    private lateinit var binding: FragmentSelectShippingMethodBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectShippingMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeView(this)
    }

    override fun onStop() {
        presenter.disposeView(this)
        super.onStop()
    }

    private fun setup() {

        binding.ivBackButton.setOnClickListener {
            presenter.dismiss()
        }

        val options = presenter.getShippingOptions()
        val selectedOption = presenter.getSelectedShippingOption()

        val standardMethod = options.find { it.getShippingMethod() == ShippingMethod.STANDARD_DELIVERY }
        if (standardMethod != null) {
            binding.clStandardContainer.visibility = VISIBLE
        } else {
            binding.clStandardContainer.visibility = GONE
        }

        val nextDayMethod = options.find { it.getShippingMethod() == ShippingMethod.NEXT_DAY_DELIVERY }
        if (nextDayMethod != null && (nextDayMethod.amount ?: 0) > 0) {
            binding.clNextDayContainer.visibility = VISIBLE
        } else {
            binding.clNextDayContainer.visibility = GONE
        }

        val expressMethod = options.find { it.getShippingMethod() == ShippingMethod.EXPRESS_DELIVERY }
        if (expressMethod != null && (expressMethod.amount ?: 0) > 0) {
            binding.clExpressContainer.visibility = VISIBLE
        } else {
            binding.clExpressContainer.visibility = GONE
        }

        val internationalMethod = options.find { it.getShippingMethod() == ShippingMethod.INTERNATIONAL_DELIVERY }
        if (internationalMethod != null && (internationalMethod.amount ?: 0) > 0) {
            binding.clInternationalContainer.visibility = VISIBLE
        } else {
            binding.clInternationalContainer.visibility = GONE
        }

        for (option in options) {
            when (option.getShippingMethod()) {
                ShippingMethod.STANDARD_DELIVERY -> {
                    val amount = option.amount?.toFloat()!!
                    if (amount > 0) {
                        binding.tvStandardValue.text =
                            context?.getString(R.string.label_price, amount.div(100))
                    } else {
                        binding.tvStandardValue.text = "FREE"
                    }
                }
                ShippingMethod.NEXT_DAY_DELIVERY -> {
                    binding.tvNextDayValue.text =
                        context?.getString(R.string.label_price, option.amount?.toFloat()?.div(100))
                }
                ShippingMethod.EXPRESS_DELIVERY -> {
                    binding.tvExpressValue.text =
                        context?.getString(R.string.label_price, option.amount?.toFloat()?.div(100))
                }
                ShippingMethod.INTERNATIONAL_DELIVERY -> {
                    binding.tvInternationalValue.text =
                        context?.getString(R.string.label_price, option.amount?.toFloat()?.div(100))
                }
                ShippingMethod.NONE -> {}
            }
        }

        if (selectedOption != null) {
            setSelectedOption(selectedOption)
        }

        binding.clStandardContainer.setOnClickListener {
            setSelectedOption(options.find { it.getShippingMethod() == ShippingMethod.STANDARD_DELIVERY })
            presenter.dismiss()
        }

        binding.clNextDayContainer.setOnClickListener {
            setSelectedOption(options.find { it.getShippingMethod() == ShippingMethod.NEXT_DAY_DELIVERY })
            presenter.dismiss()
        }

        binding.clExpressContainer.setOnClickListener {
            setSelectedOption(options.find { it.getShippingMethod() == ShippingMethod.EXPRESS_DELIVERY })
            presenter.dismiss()
        }

        binding.clInternationalContainer.setOnClickListener {
            setSelectedOption(options.find { it.getShippingMethod() == ShippingMethod.INTERNATIONAL_DELIVERY })
            presenter.dismiss()
        }
    }

    private fun setSelectedOption(selected: ProductShipmentsResponseModel?) {

        selected?.let { presenter.setSelectedShippingOption(it) }

        when (selected?.getShippingMethod()) {
            ShippingMethod.STANDARD_DELIVERY -> {
                binding.ibStandard.isSelected = true
                binding.ibNextDay.isSelected = false
                binding.ibExpress.isSelected = false
                binding.ibInternational.isSelected = false
            }
            ShippingMethod.NEXT_DAY_DELIVERY -> {
                binding.ibStandard.isSelected = false
                binding.ibNextDay.isSelected = true
                binding.ibExpress.isSelected = false
                binding.ibInternational.isSelected = false
            }
            ShippingMethod.EXPRESS_DELIVERY -> {
                binding.ibStandard.isSelected = false
                binding.ibNextDay.isSelected = false
                binding.ibExpress.isSelected = true
                binding.ibInternational.isSelected = false
            }
            ShippingMethod.INTERNATIONAL_DELIVERY -> {
                binding.ibStandard.isSelected = false
                binding.ibNextDay.isSelected = false
                binding.ibExpress.isSelected = false
                binding.ibInternational.isSelected = true
            }
            ShippingMethod.NONE -> {}
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is SelectShippingMethodPresenterProvider) {
            this.presenter = context.getSelectShippingMethodPresenter()
        } else {
            throw RuntimeException("$context must implement SelectShippingMethodPresenterProvider")
        }
    }

    interface SelectShippingMethodPresenterProvider {
        fun getSelectShippingMethodPresenter(): SelectShippingMethodView.Presenter
    }
}