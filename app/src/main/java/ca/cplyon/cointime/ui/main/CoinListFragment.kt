package ca.cplyon.cointime.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ca.cplyon.cointime.CoinTimeApplication
import ca.cplyon.cointime.R
import ca.cplyon.cointime.data.Coin
import ca.cplyon.cointime.databinding.ListFragmentBinding

class CoinListFragment : Fragment(), CoinListAdapter.ContentListener {

    private val viewModel by activityViewModels<CoinListViewModel> {
        CoinListViewModelFactory(
            (requireContext().applicationContext as CoinTimeApplication).coinRepository
        )
    }

    private var fragmentBinding: ListFragmentBinding? = null
    private lateinit var adapter: CoinListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setHasOptionsMenu(true)
        val binding = ListFragmentBinding.inflate(layoutInflater, container, false)
        adapter = CoinListAdapter(this.requireContext(), this, viewModel)

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
                adapter.filter.filter(newText)
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragmentBinding?.addCoinFab?.setOnClickListener {
            // open Coin Detail Fragment with blank values
            findNavController().navigate(CoinListFragmentDirections.nextAction())
        }
    }

    override fun onItemClicked(coin: Coin?) {
        // open Coin Detail Fragment with populated values
        findNavController().navigate(CoinListFragmentDirections.nextAction(coin))
    }
}
