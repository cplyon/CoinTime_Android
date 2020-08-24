package ca.cplyon.cointime

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.cplyon.cointime.data.Coin

class CoinListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<CoinListAdapter.CoinViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var coins = mutableListOf<Coin>()

    init {
        setHasStableIds(true)
    }

    inner class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coinItemView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val currentCoin = coins[position]
        holder.coinItemView.text = currentCoin.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return CoinViewHolder(itemView)
    }

    internal fun setCoins(coins: List<Coin>) {
        this.coins = coins.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = coins.size
}