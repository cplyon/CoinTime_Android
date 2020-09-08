package ca.cplyon.cointime.ui.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import ca.cplyon.cointime.MainActivity
import ca.cplyon.cointime.R
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.databinding.DetailFragmentBinding

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
const val ARG_PARAM1 = "coin"

/**
 * A simple [Fragment] subclass.
 * Use the [CoinDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CoinDetailFragment : Fragment() {
    private var coin: Coin? = null
    private var fragmentBinding: DetailFragmentBinding? = null
    private lateinit var editButton: MenuItem
    private lateinit var saveButton: MenuItem
    private var editMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            coin = it.getSerializable(ARG_PARAM1) as Coin?
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val binding = DetailFragmentBinding.inflate(layoutInflater, container, false)

        val c = coin
        if (c != null) {
            binding.coinCountry.setText(c.country)
            binding.coinDenomination.setText(c.denomination)
            binding.coinYear.setText(c.year.toString())
            binding.coinMintMark.setText(c.mintMark)
            binding.coinNotes.setText(c.notes)
        } else {
            // TODO: enter new coin details
            editMode = true
        }

        fragmentBinding = binding
        return binding.root
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
        editButton = menu.findItem(R.id.action_edit)
        saveButton = menu.findItem(R.id.action_save)
        setEditMode(editMode)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_delete -> {
            true
        }

        R.id.action_edit -> {
            setEditMode(true)
            true
        }

        R.id.action_save -> {
            setEditMode(false)
            true
        }

        else -> {
            false
        }
    }

    private fun setEditMode(enabled: Boolean) {
        editMode = enabled
        saveButton.isVisible = enabled
        editButton.isVisible = !enabled

        fragmentBinding?.let {
            it.coinCountry.isEnabled = enabled
            it.coinDenomination.isEnabled = enabled
            it.coinYear.isEnabled = enabled
            it.coinMintMark.isEnabled = enabled
            it.coinNotes.isEnabled = enabled
        }

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