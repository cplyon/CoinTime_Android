package ca.cplyon.cointime.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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

    private val viewModel by activityViewModels<CoinViewModel> {
        CoinViewModelFactory((requireContext().applicationContext as CoinTimeApplication).coinRepository)
    }

    private var fragmentBinding: MainFragmentBinding? = null
    private lateinit var adapter: CoinListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        val binding = MainFragmentBinding.inflate(layoutInflater, container, false)

        adapter = CoinListAdapter(this.requireContext(), this)

        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        viewModel.items.observe(viewLifecycleOwner, { coins ->
            coins?.let {
                adapter.setCoins(it)
                adapter.notifyDataSetChanged()
            }
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
        (item.actionView as SearchView).setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
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
            // open Coin Detail Fragment with blank values
            (context as MainActivity).launchDetailFragment(null, "new_coin")
        }

    }

    override fun onItemClicked(coin: Coin?) {
        // open Coin Detail Fragment with populated values
        (context as MainActivity).launchDetailFragment(coin, "detail_coin")
    }

}