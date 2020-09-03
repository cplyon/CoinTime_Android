package ca.cplyon.cointime.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.databinding.FragmentCoinDetailBinding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
const val ARG_PARAM1 = "coin"

/**
 * A simple [Fragment] subclass.
 * Use the [CoinDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CoinDetailFragment : Fragment() {
    private var coin: Coin? = null
    private var fragmentBinding: FragmentCoinDetailBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            coin = it.getSerializable(ARG_PARAM1) as Coin?
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(false)
        val binding = FragmentCoinDetailBinding.inflate(layoutInflater, container, false)
        val c = coin
        if (c != null) {
            binding.coinCountry.text = c.country
            binding.coinDenomination.text = c.denomination
            binding.coinYear.text = c.year.toString()
            binding.coinMintMark.text = c.mintMark
            binding.coinNotes.text = c.notes
        } else {
            // TODO: enter new coin details
        }
        fragmentBinding = binding
        return binding.root
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param coin Parameter 1.
         * @return A new instance of fragment NewCoinFragment.
         */
        @JvmStatic
        fun newInstance(coin: Coin?) =
            CoinDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, coin)
                }
            }
    }
}