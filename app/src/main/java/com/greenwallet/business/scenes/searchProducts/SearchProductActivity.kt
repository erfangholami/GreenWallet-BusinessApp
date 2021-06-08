package com.greenwallet.business.scenes.searchProducts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.greenwallet.business.databinding.ActivitySearchProductsBinding
import com.greenwallet.business.helper.ui.LoadingFragment
import com.greenwallet.business.network.product.response.ProductResponseModel
import com.greenwallet.business.network.product.response.ProductReviewsResponseModel
import com.greenwallet.business.scenes.base.BaseActivity

import com.greenwallet.business.scenes.searchProducts.ui.SearchProductsFragment
import com.greenwallet.business.scenes.searchProducts.ui.SearchProductsFragment.SearchProductsPresenterProvider
import com.greenwallet.business.scenes.searchProducts.ui.SearchProductsView

class SearchProductActivity : BaseActivity(),
    SearchProductsProcessHandler,
    SearchProductsPresenterProvider {

    companion object {
        const val FRAGMENT_SEARCH_PRODUCTS = "fragment_search_products"
        const val FRAGMENT_LOADING = "fragment_loading"

        const val KEY_MODE = "mode_key"
        const val KEY_CATEGORY_NAME = "category_name"
        const val KEY_SEARCH_QUERY = "search_query"

        fun start(
            context: Context,
            mode: SearchProductsPresenter.Mode = SearchProductsPresenter.Mode.HOT_DEALS,
            categoryName: String? = null,
            searchQuery: String? = null
        ) {
            val intent = Intent(context, SearchProductActivity::class.java)
            intent.putExtra(KEY_MODE, mode)

            if (mode == SearchProductsPresenter.Mode.CATEGORY) {
                intent.putExtra(KEY_CATEGORY_NAME, categoryName)
            }

            if (searchQuery != null) {
                intent.putExtra(KEY_SEARCH_QUERY, searchQuery)
            }

            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivitySearchProductsBinding

    lateinit var presenter: SearchProductsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        presenter = SearchProductsPresenter(this)

        if (intent.extras?.getString(KEY_SEARCH_QUERY) != null) {
            presenter.searchQuery =
                intent.extras?.getString(KEY_SEARCH_QUERY)!!
        }

        if (intent.extras?.getSerializable(KEY_MODE) != null) {
            presenter.mode =
                intent.extras?.getSerializable(KEY_MODE) as SearchProductsPresenter.Mode

            if (presenter.mode == SearchProductsPresenter.Mode.CATEGORY) {
                presenter.categoryName = intent.extras?.getString(KEY_CATEGORY_NAME)!!
            }
        }

        super.onCreate(savedInstanceState)
        binding = ActivitySearchProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        presenter.subscribeHandler(this)
    }

    override fun onStop() {
        presenter.disposeHandler()
        super.onStop()
    }

    override fun onBackPressed() {
        actionBackButton()
    }

    override fun showSearchProductsScreen() {
        replaceFragment(SearchProductsFragment(), FRAGMENT_SEARCH_PRODUCTS)
    }

    override fun showLoadingScreen() {
        replaceFragment(
            LoadingFragment.newInstance("Just a moment..."), FRAGMENT_LOADING
        )
    }

    override fun onItemClicked(product: ProductResponseModel) {
        //TODO: Add after adding product details page
//        ProductFeaturesActivity.start(this, product)
    }

    override fun onItemReviewClicked(
        productID: String,
        reviews: ArrayList<ProductReviewsResponseModel>
    ) {
        //TODO: Add after adding reviews
//        ReviewsActivity.start(this, productID, reviews)
    }

    override fun shoeCartScreen() {
        //Todo: Add after adding basket page
//        BasketActivity.start(this)
    }

    private fun actionBackButton() {
        finish()
    }

    override fun getSearchProductsPresenter(): SearchProductsView.Presenter {
        return presenter
    }
}
