package ca.cplyon.cointime

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.databinding.RecyclerviewItemBinding
import ca.cplyon.cointime.ui.detail.CoinDetailFragment

class CoinListAdapter internal constructor(
    private val context: Context
) : RecyclerView.Adapter<CoinListAdapter.CoinViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var coins = mutableListOf<Coin>()
    lateinit var binding: RecyclerviewItemBinding

    init {
        setHasStableIds(true)
    }

    inner class CoinViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(
            itemView
        ), View.OnClickListener {
        val coinItemView = binding.textView
        var currentCoin: Coin? = null

        init {
            coinItemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val foo = (v as TextView).text
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main, CoinDetailFragment.newInstance(currentCoin), "coin_detail")
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val currentCoin = coins[position]
        holder.coinItemView.text = currentCoin.toString()
        holder.currentCoin = currentCoin
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        binding = RecyclerviewItemBinding.inflate(inflater)
        return CoinViewHolder(binding.root, context)
    }

    internal fun setCoins(coins: List<Coin>) {
        this.coins = coins.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = coins.size
}