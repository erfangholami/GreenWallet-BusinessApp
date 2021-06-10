package com.greenwallet.business.scenes.productFeatures

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.greenwallet.business.databinding.ActivityProductFeaturesBinding
import com.greenwallet.business.helper.ui.dialog.SuccessDialog
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.productReviews.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.BaseActivity
import com.greenwallet.business.scenes.findMySize.ui.FindMySizeFragment
import com.greenwallet.business.scenes.findMySize.ui.FindMySizeView
import com.greenwallet.business.scenes.productFeatures.ui.aboutThisItem.ProductFeaturesAboutItemFragment
import com.greenwallet.business.scenes.productFeatures.ui.aboutThisItem.ProductFeaturesAboutItemView
import com.greenwallet.business.scenes.productFeatures.ui.details.ProductFeaturesFragment
import com.greenwallet.business.scenes.productFeatures.ui.details.ProductFeaturesFragment.ProductFeaturesProvider
import com.greenwallet.business.scenes.productFeatures.ui.details.ProductFeaturesView
import com.greenwallet.business.scenes.productFeatures.ui.fullscreen.FullScreenPhotoFragment
import com.greenwallet.business.scenes.productFeatures.ui.fullscreen.FullScreenPhotoView

class ProductFeaturesActivity : BaseActivity(),
    ProductFeaturesProcessHandler,
    ProductFeaturesProvider,
    FindMySizeFragment.FindMySizeFeatureProvider,
    ProductFeaturesAboutItemFragment.ProductFeaturesAboutItemPresenterProvider,
    FullScreenPhotoFragment.FullScreenPhotoPresenterProvider {

    companion object {
        const val FRAGMENT_PRODUCT_FEATURES = "fragment_product_features"
        const val FRAGMENT_PRODUCT_FEATURES_ABOUT_ITEM = "fragment_product_features_about_item"
        const val FRAGMENT_FULL_SCREEN_PHOTO = "fragment_full_screen_photo"
        const val FRAGMENT_FIND_MY_SIZE = "fragment_find_my_size"

        const val KEY_PRODUCT = "product"

        fun start(context: Context, product: ProductResponseModel) {
            val intent = Intent(context, ProductFeaturesActivity::class.java)
            intent.putExtra(KEY_PRODUCT, product)

            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityProductFeaturesBinding

    private lateinit var productFeaturesPresenter: ProductFeaturesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        productFeaturesPresenter = ProductFeaturesPresenter(this)

        productFeaturesPresenter.product =
            intent.extras?.getSerializable(KEY_PRODUCT) as ProductResponseModel

        super.onCreate(savedInstanceState)
        binding = ActivityProductFeaturesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        productFeaturesPresenter.subscribeHandler(this)
    }

    override fun onStop() {
        productFeaturesPresenter.disposeHandler()

        super.onStop()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun showProductFeaturesScreen() {
        replaceFragment(
            ProductFeaturesFragment(productFeaturesPresenter.product),
            FRAGMENT_PRODUCT_FEATURES
        )
    }

    override fun showFullScreenPhotoScreen() {
        replaceFragment(FullScreenPhotoFragment(), FRAGMENT_FULL_SCREEN_PHOTO)
    }

    override fun showAboutThisItem(data: String) {
        replaceFragment(
            ProductFeaturesAboutItemFragment.newInstance(data),
            FRAGMENT_PRODUCT_FEATURES_ABOUT_ITEM
        )
    }

    override fun onSeeAllReviewsClicked(
        productID: String,
        reviews: ArrayList<ProductReviewsResponseModel>?
    ) {
        //todo: after adding reviews page
//        ReviewsActivity.start(this, productID, reviews)
    }

    override fun showBasketScreen() {
        //todo: after implementing basket page
//        BasketActivity.start(this)
    }

    override fun showCheckoutScreen() {
        //todo: after implementing checkout page
//        CheckoutActivity.start(this, CheckoutActivity.Mode.BUY_NOW)
    }

    override fun showFindMySizeScreen() {
        //todo after adding FindMy size page
//        replaceFragment(FindMySizeFragment(), FRAGMENT_FIND_MY_SIZE)
    }

    override fun showAddToCartSuccessDialog() {
        SuccessDialog(this).apply {
            hasCloseIcon = true
            title = "Item has been added to your cart."
            explanation = "Review item in your cart then proceed to checkout."
            buttonText = "VIEW MY CART"
            buttonListener = {
                dismiss()
                productFeaturesPresenter.onBasketClicked()
            }
        }.show()
    }

    override fun getProductFeaturesProvider(): ProductFeaturesView.Presenter {
        return productFeaturesPresenter
    }

    override fun getFindMySizeFeatureProvider(): FindMySizeView.Presenter {
        return productFeaturesPresenter
    }

    override fun getFullScreenPhotoProvider(): FullScreenPhotoView.Presenter {
        return productFeaturesPresenter
    }

    override fun getProductFeaturesAboutItemProvider(): ProductFeaturesAboutItemView.Presenter {
        return productFeaturesPresenter
    }
}
