package ca.cplyon.cointime.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.cplyon.cointime.R
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.databinding.RecyclerviewItemBinding
import kotlinx.android.synthetic.main.recyclerview_item.view.*
import java.util.*

class CoinListAdapter internal constructor(
    context: Context,
    private val listener: ContentListener
) : RecyclerView.Adapter<CoinListAdapter.CoinViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var coins = Collections.emptyList<Coin>()
    lateinit var binding: RecyclerviewItemBinding

    init {
        setHasStableIds(true)
    }

    interface ContentListener {
        fun onItemClicked(coin: Coin?)
    }

    inner class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val coinItemView = binding.listItem
        private lateinit var currentCoin: Coin

        fun bind(coin: Coin) {
            currentCoin = coin

            // update list view item
            coinItemView.country.text = currentCoin.country
            coinItemView.details.text = buildString {
                append(currentCoin.denomination)
                append(" ")
                append(currentCoin.year)
                append(currentCoin.mintMark)
            }

        }
    }

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
        this.coins = coins
    }

    override fun getItemCount() = coins.size

    override fun getItemViewType(position: Int): Int {
        return R.layout.recyclerview_item
    }

    override fun getItemId(position: Int): Long {
        return coins[position].coinId.toLong()
    }

}