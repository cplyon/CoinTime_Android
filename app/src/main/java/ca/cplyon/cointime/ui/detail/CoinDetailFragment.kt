package ca.cplyon.cointime.ui.detail

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.cplyon.cointime.CoinTimeApplication
import ca.cplyon.cointime.R
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.databinding.DetailFragmentBinding

class CoinDetailFragment : Fragment() {

    private var obverseUpdated = false
    private var reverseUpdated = false

    private var coin: Coin? = null
    private var fragmentBinding: DetailFragmentBinding? = null
    private lateinit var deleteButton: MenuItem
    private lateinit var editButton: MenuItem
    private lateinit var saveButton: MenuItem
    private lateinit var binding: DetailFragmentBinding
    private var editMode = false

    private val viewModel by activityViewModels<CoinDetailViewModel> {
        CoinDetailViewModelFactory(
            (requireContext().applicationContext as CoinTimeApplication).coinRepository,
            requireContext().applicationContext as CoinTimeApplication
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val safeArgs: CoinDetailFragmentArgs by navArgs()
        coin = safeArgs.coin
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DetailFragmentBinding.inflate(layoutInflater, container, false)

        binding.obverse.setOnClickListener { if (editMode) takePhoto(OBVERSE_IMAGE_CAPTURE) }
        binding.reverse.setOnClickListener { if (editMode) takePhoto(REVERSE_IMAGE_CAPTURE) }

        coin?.let {
            // populate UI using current coin data
            binding.coinCountry.setText(it.country)
            binding.coinDenomination.setText(it.denomination)
            binding.coinYear.setText(it.year.toString())
            binding.coinMintMark.setText(it.mintMark)
            binding.coinNotes.setText(it.notes)
            it.obverse?.let { obverse ->
                val drawable = BitmapDrawable(resources, viewModel.loadImage(obverse))
                binding.obverse.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            }
            it.obverse?.let { reverse ->
                val drawable = BitmapDrawable(resources, viewModel.loadImage(reverse))
                binding.obverse.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null)
            }
            editMode = false
        } ?: run {
            // new coin, just set to edit mode
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
            viewModel.deleteCoin(coin!!)
            findNavController().navigateUp()
            true
        }

        R.id.action_edit -> {
            setEditMode(true)
            true
        }

        R.id.action_save -> {
            val yearText = binding.coinYear.text.toString()
            val year = if (yearText.isBlank()) 0 else yearText.toInt()

            val obversePath = if (obverseUpdated) {
                viewModel.saveObverseImage((binding.obverse.compoundDrawables[1] as BitmapDrawable).bitmap)
            } else {
                null
            }

            val reversePath = if (reverseUpdated) {
                viewModel.saveReverseImage((binding.reverse.compoundDrawables[1] as BitmapDrawable).bitmap)
            } else {
                null
            }

            coin?.let {
                // update current coin
                it.country = binding.coinCountry.text.toString()
                it.denomination = binding.coinDenomination.text.toString()
                it.year = year
                it.mintMark = binding.coinMintMark.text.toString()
                it.notes = binding.coinNotes.text.toString()
                if (obverseUpdated) {
                    it.obverse = obversePath
                }
                if (reverseUpdated) {
                    it.reverse = reversePath
                }

                viewModel.updateCoin(it)

            } ?: run {
                // create a new coin
                val c = Coin(
                    binding.coinCountry.text.toString(),
                    binding.coinDenomination.text.toString(),
                    year,
                    binding.coinMintMark.text.toString(),
                    binding.coinNotes.text.toString()
                )
                c.obverse = obversePath
                c.reverse = reversePath

                viewModel.addCoin(c)
            }
            obverseUpdated = false
            reverseUpdated = false
            findNavController().navigateUp()
            true
        }

        else -> {
            false
        }
    }

    private fun setEditMode(enabled: Boolean) {
        editMode = enabled

        // hide/disable these controls in edit mode
        editButton.isVisible = !enabled
        deleteButton.isEnabled = !enabled

        // show/enable these controls in edit mode
        saveButton.isVisible = enabled
        fragmentBinding?.let {
            it.coinCountry.isEnabled = enabled
            it.coinDenomination.isEnabled = enabled
            it.coinYear.isEnabled = enabled
            it.coinMintMark.isEnabled = enabled
            it.coinNotes.isEnabled = enabled
        }
    }

    private fun takePhoto(requestCode: Int) {
        startActivityForResult(
            Intent(MediaStore.ACTION_IMAGE_CAPTURE),
            requestCode
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OBVERSE_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            binding.obverse.setCompoundDrawablesWithIntrinsicBounds(
                null,
                BitmapDrawable(resources, bitmap),
                null,
                null
            )
            obverseUpdated = true
        } else if (requestCode == REVERSE_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            binding.obverse.setCompoundDrawablesWithIntrinsicBounds(
                null,
                BitmapDrawable(resources, bitmap),
                null,
                null
            )
            reverseUpdated = true
        }
    }

    companion object {
        const val OBVERSE_IMAGE_CAPTURE = 1
        const val REVERSE_IMAGE_CAPTURE = 2
    }

}