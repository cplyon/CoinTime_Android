package ca.cplyon.cointime.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import ca.cplyon.cointime.R
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.databinding.RecyclerviewItemBinding
import java.util.Collections
import java.util.Locale

class CoinListAdapter internal constructor(
    context: Context,
    private val listener: ContentListener,
    private val viewModel: CoinListViewModel
) : RecyclerView.Adapter<CoinListAdapter.CoinViewHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var coins = Collections.emptyList<Coin>()
    private var allCoins = coins
    lateinit var binding: RecyclerviewItemBinding

    init {
        setHasStableIds(true)
    }

    interface ContentListener {
        fun onItemClicked(coin: Coin?)
    }

    inner class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(coin: Coin) {
            // update list view item
            coin.obverse?.let {
                // fallback to the default image if we can't load the actual image
                viewModel.loadImage(it)?.let { image ->
                    binding.coinImage.setImageBitmap(image)
                }
            }
            binding.country.text = coin.country
            binding.details.text = buildString {
                append(coin.denomination)
                append(" ")
                append(coin.year)
                append(coin.mintMark)
            }
        }
    }

    // TODO: figure out why we're calling onBindViewHolder for a coin even after we've deleted it
    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        // get currently selected coin based on its position in the RecyclerView
        val currentCoin = coins[position]
        holder.bind(currentCoin)

        // delegate onClick to a ContentListener
        binding.listItem.setOnClickListener {
            listener.onItemClicked(currentCoin)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        binding = RecyclerviewItemBinding.inflate(inflater)
        return CoinViewHolder(binding.root)
    }

    internal fun setCoins(coins: List<Coin>) {
        this.coins = coins.toMutableList()
        this.allCoins = coins.toMutableList()
    }

    override fun getItemCount() = coins.size

    override fun getItemViewType(position: Int): Int {
        return R.layout.recyclerview_item
    }

    override fun getItemId(position: Int): Long {
        return coins[position].coinId
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredCoinList = mutableListOf<Coin>()
                if (constraint == null || constraint.toString().isEmpty()) {
                    filteredCoinList.addAll<Coin>(allCoins)
                } else {
                    val newCoins = allCoins
                    filteredCoinList.addAll(
                        newCoins.filter {
                            it.toString().toLowerCase(Locale.ROOT)
                                .contains(constraint.toString().toLowerCase(Locale.ROOT))
                        }
                    )
                }
                val filterResults = FilterResults()
                filterResults.values = filteredCoinList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null) {
                    this@CoinListAdapter.coins.clear()
                    @Suppress("UNCHECKED_CAST")
                    this@CoinListAdapter.coins.addAll(results.values as Collection<Coin>)
                }
                notifyDataSetChanged()
            }
        }
    }
}
