package ca.cplyon.cointime.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ca.cplyon.cointime.CoinListAdapter
import ca.cplyon.cointime.R
import ca.cplyon.cointime.databinding.MainFragmentBinding


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel by viewModels<CoinViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        val binding = MainFragmentBinding.inflate(layoutInflater, container, false)
        binding.recyclerview.adapter = CoinListAdapter(this.requireContext())
        binding.recyclerview.layoutManager = LinearLayoutManager(this.requireContext())
        return binding.root
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

}