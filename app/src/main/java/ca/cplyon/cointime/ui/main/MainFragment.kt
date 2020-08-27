package ca.cplyon.cointime.ui.main

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import ca.cplyon.cointime.CoinListAdapter
import ca.cplyon.cointime.CoinTimeApplication
import ca.cplyon.cointime.R
import ca.cplyon.cointime.data.source.CoinRepository
import ca.cplyon.cointime.data.source.local.CoinLocalDataSource
import ca.cplyon.cointime.data.source.local.CoinRoomDatabase
import ca.cplyon.cointime.databinding.MainFragmentBinding


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel by viewModels<CoinViewModel>() {
        CoinViewModelFactory(
            CoinRepository((CoinLocalDataSource(
            Room.databaseBuilder(requireContext().applicationContext,
            CoinRoomDatabase::class.java, "Tasks.db")
            .build().coinDao()))))
    }
    private var fragmentBinding: MainFragmentBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        val binding = MainFragmentBinding.inflate(layoutInflater, container, false)
        fragmentBinding = binding
        val adapter = CoinListAdapter(this.requireContext())
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this.requireContext())

        var i = viewModel.items.observe(this.requireActivity(), Observer { coins ->
            coins?.let {adapter.setCoins(it)}
        })


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

}