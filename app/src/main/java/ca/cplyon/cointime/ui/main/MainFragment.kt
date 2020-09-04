package ca.cplyon.cointime.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ca.cplyon.cointime.CoinTimeApplication
import ca.cplyon.cointime.MainActivity
import ca.cplyon.cointime.R
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.databinding.MainFragmentBinding


class MainFragment : Fragment(), CoinListAdapter.ContentListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel by viewModels<CoinViewModel> {
        CoinViewModelFactory((requireContext().applicationContext as CoinTimeApplication).coinRepository)
    }
    private var fragmentBinding: MainFragmentBinding? = null
    private var inc = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val binding = MainFragmentBinding.inflate(layoutInflater, container, false)

        val adapter = CoinListAdapter(this.requireContext(), this)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        viewModel.items.observe(this.requireActivity(), { coins ->
            coins?.let { adapter.setCoins(it) }
        })

        fragmentBinding = binding
        return binding.root
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragmentBinding?.addCoinFab?.setOnClickListener {

            // TODO: delete this code
            viewModel.addCoin(Coin("Canada", "10 cents", 1990 + inc, "", "Hi"))
            inc++

            // open new Coin fragment
            (context as MainActivity).launchDetailFragment(null, "new_coin")
        }

    }

    override fun onItemClicked(coin: Coin?) {
        (context as MainActivity).launchDetailFragment(coin, "detail_coin")
    }

}