package ca.cplyon.cointime.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.databinding.RecyclerviewItemBinding
import kotlinx.android.synthetic.main.recyclerview_item.view.*

class CoinListAdapter internal constructor(
    context: Context,
    private val listener: ContentListener
) : RecyclerView.Adapter<CoinListAdapter.CoinViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var coins = mutableListOf<Coin>()
    lateinit var binding: RecyclerviewItemBinding

    init {
        setHasStableIds(true)
    }

    interface ContentListener {
        fun onItemClicked(coin: Coin?)
    }

    inner class CoinViewHolder(itemView: View) :
        RecyclerView.ViewHolder(
            itemView
        ) {
        val coinItemView = binding.listItem
        var currentCoin: Coin? = null
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val currentCoin = coins[position]
        holder.coinItemView.country.text = currentCoin.country
        val detailString = buildString {
            append(currentCoin.denomination)
            append(" ")
            append(currentCoin.year)
            append(currentCoin.mintMark)
        }
        holder.coinItemView.details.text = detailString
        holder.currentCoin = currentCoin

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
        notifyDataSetChanged()
    }

    override fun getItemCount() = coins.size
}