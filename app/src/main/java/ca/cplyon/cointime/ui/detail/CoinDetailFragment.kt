package ca.cplyon.cointime.ui.detail

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
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
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
const val ARG_PARAM1 = "coin"

/**
 * A simple [Fragment] subclass.
 * Use the [CoinDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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

        binding.obverse.setOnClickListener { if (editMode) takePhoto(OBVERSE_IMAGE_CAPTURE) }
        binding.reverse.setOnClickListener { if (editMode) takePhoto(REVERSE_IMAGE_CAPTURE) }

        val c = coin
        if (c != null) {
            binding.coinCountry.setText(c.country)
            binding.coinDenomination.setText(c.denomination)
            binding.coinYear.setText(c.year.toString())
            binding.coinMintMark.setText(c.mintMark)
            binding.coinNotes.setText(c.notes)
            if (c.obverse != null) {
                binding.obverse.setImageBitmap(BitmapFactory.decodeFile(c.obverse))
            }
            if (c.reverse != null) {
                binding.reverse.setImageBitmap(BitmapFactory.decodeFile(c.reverse))
            }

        } else {
            editMode = true
        }

        fragmentBinding = binding
        return binding.root
    }

    override fun onDestroyView() {
        viewModel.lastCoinId = 0L
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

                if (c.obverse != null) {
                    val file = File(c.obverse!!)
                    file.delete()
                }
                if (c.reverse != null) {
                    val file = File(c.reverse!!)
                    file.delete()
                }

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
            var obversePath: String? = null
            var reversePath: String? = null

            if (obverseUpdated) {
                obversePath =
                    saveImage((binding.obverse.drawable as BitmapDrawable).bitmap, "obverse")
            }
            if (reverseUpdated) {
                reversePath =
                    saveImage((binding.reverse.drawable as BitmapDrawable).bitmap, "reverse")
            }

            var c = coin
            if (c != null) {
                // update current coin
                c.country = binding.coinCountry.text.toString()
                c.denomination = binding.coinDenomination.text.toString()
                c.year = year
                c.mintMark = binding.coinMintMark.text.toString()
                c.notes = binding.coinNotes.text.toString()
                if (obverseUpdated) {
                    c.obverse = obversePath
                }
                if (reverseUpdated) {
                    c.reverse = reversePath
                }

                // synchronize due to race condition setting lastCoinId.
                // TODO: There's got to be a better way!
                runBlocking {
                    viewModel.updateCoin(c!!).join()
                }
            } else {
                // create a new coin
                c = Coin(
                    binding.coinCountry.text.toString(),
                    binding.coinDenomination.text.toString(),
                    year,
                    binding.coinMintMark.text.toString(),
                    binding.coinNotes.text.toString()
                )
                c.obverse = obversePath
                c.reverse = reversePath

                // synchronize due to race condition setting lastCoinId.
                // TODO: There's got to be a better way!
                runBlocking {
                    viewModel.addCoin(c).join()
                }

                // The id for a new coin is 0 until it is entered into the database, where the id is
                // changed to be unique and set in viewModel.lastCoinId. So if we try to delete a
                // coin from this fragment instance, we don't know the id yet, and so are unable to
                // delete it until we relaunch the listview and refetch it from the database.
                if (viewModel.lastCoinId != 0L) {
                    c.coinId = viewModel.lastCoinId
                }
            }
            coin = c
            obverseUpdated = false
            reverseUpdated = false
            true
        }

        else -> {
            false
        }
    }

    private fun saveImage(image: Bitmap, suffix: String): String? {
        val storageDir: File? = requireContext().getDir("coin_images", Context.MODE_PRIVATE)
        val filename: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date()) + "_$suffix.png"
        val path = Paths.get(storageDir.toString(), filename)
        try {
            FileOutputStream(path.toFile()).use { out ->
                image.compress(
                    Bitmap.CompressFormat.PNG,
                    100,
                    out
                )
            }
        } catch (e: IOException) {
            return null
        }
        return path.toString()
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
            binding.obverse.setImageBitmap(bitmap)
            obverseUpdated = true
        } else if (requestCode == REVERSE_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            binding.reverse.setImageBitmap(bitmap)
            reverseUpdated = true
        }
    }


    companion object {

        const val OBVERSE_IMAGE_CAPTURE = 1
        const val REVERSE_IMAGE_CAPTURE = 2

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