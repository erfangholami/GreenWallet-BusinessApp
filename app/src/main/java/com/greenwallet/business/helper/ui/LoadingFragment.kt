package com.greenwallet.business.helper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.greenwallet.business.R

/**
 * A simple [Fragment] subclass.
 * Use the [LoadingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class LoadingFragment : Fragment() {

    private var text: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            text = arguments!!.getString(ARG_PARAM_TEXT).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_generic_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.loading_textView_message)?.text = text
    }

    companion object {

        private val ARG_PARAM_TEXT = "param_loading_text"
        private val ARG_PARAM_TRANSPARENT = "param_background_transparent"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param loadingText Text to be displayed
         * @return A new instance of fragment LoadingFragment.
         */
        fun newInstance(loadingText: String): LoadingFragment {
            val fragment = LoadingFragment()
            val args = Bundle()
            args.putString(ARG_PARAM_TEXT, loadingText)
            fragment.arguments = args
            return fragment
        }
    }
}
