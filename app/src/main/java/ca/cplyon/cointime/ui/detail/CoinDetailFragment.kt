package ca.cplyon.cointime.ui.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ca.cplyon.cointime.CoinTimeApplication
import ca.cplyon.cointime.MainActivity
import ca.cplyon.cointime.R
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.databinding.DetailFragmentBinding
import ca.cplyon.cointime.ui.main.CoinViewModel
import ca.cplyon.cointime.ui.main.CoinViewModelFactory

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
    private lateinit var deleteButton: MenuItem
    private lateinit var editButton: MenuItem
    private lateinit var saveButton: MenuItem
    private lateinit var binding: DetailFragmentBinding
    private var editMode = false

    private val viewModel by activityViewModels<CoinViewModel> {
        CoinViewModelFactory((requireContext().applicationContext as CoinTimeApplication).coinRepository)
    }

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

        binding = DetailFragmentBinding.inflate(layoutInflater, container, false)

        val c = coin
        if (c != null) {
            binding.coinCountry.setText(c.country)
            binding.coinDenomination.setText(c.denomination)
            binding.coinYear.setText(c.year.toString())
            binding.coinMintMark.setText(c.mintMark)
            binding.coinNotes.setText(c.notes)
        } else {
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
        deleteButton = menu.findItem(R.id.action_delete)
        editButton = menu.findItem(R.id.action_edit)
        saveButton = menu.findItem(R.id.action_save)
        setEditMode(editMode)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_delete -> {
            val c = coin
            if (c != null) {
                viewModel.deleteCoin(c)
                (context as MainActivity).onBackPressed()
            }
            true
        }

        R.id.action_edit -> {
            setEditMode(true)
            true
        }

        R.id.action_save -> {
            setEditMode(false)
            val yearText = binding.coinYear.text.toString()
            val year = if (yearText.isBlank()) 0 else yearText.toInt()
            val c = coin
            if (c != null) {
                c.country = binding.coinCountry.text.toString()
                c.denomination = binding.coinDenomination.text.toString()
                c.year = year
                c.mintMark = binding.coinMintMark.text.toString()
                c.notes = binding.coinNotes.text.toString()
                viewModel.updateCoin(c)
            } else {
                // no coin selected, so create a new one
                viewModel.addCoin(
                    Coin(
                        binding.coinCountry.text.toString(),
                        binding.coinDenomination.text.toString(),
                        year,
                        binding.coinMintMark.text.toString(),
                        binding.coinNotes.text.toString()
                    )
                )
            }
            true
        }

        else -> {
            false
        }
    }

    private fun setEditMode(enabled: Boolean) {

        // hide/disable these controls in edit mode
        editButton.isVisible = !enabled
        deleteButton.isEnabled = !enabled

        // show/enable these controls in edit mode
        editMode = enabled
        saveButton.isVisible = enabled
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