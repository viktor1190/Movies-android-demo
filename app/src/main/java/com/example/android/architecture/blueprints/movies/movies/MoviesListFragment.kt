package com.example.android.architecture.blueprints.movies.movies

import android.app.SearchManager
import android.content.Context.INPUT_METHOD_SERVICE
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.android.architecture.blueprints.movies.EventObserver
import com.example.android.architecture.blueprints.movies.R
import com.example.android.architecture.blueprints.movies.data.source.MoviesListSortType
import com.example.android.architecture.blueprints.movies.databinding.FragmentMoviesListBinding
import com.example.android.architecture.blueprints.movies.movies.adapters.GridAutoFitLayoutManager
import com.example.android.architecture.blueprints.movies.movies.adapters.MoviesListAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MoviesListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MoviesListViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentMoviesListBinding

    private lateinit var listAdapter: MoviesListAdapter

    private val suggestionsMap = mutableMapOf<Int, String>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentMoviesListBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupListAdapter()
        setupNavigation()
        setupHintsSearchAdapter()
        setupErrorsHandler()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.movies_menu, menu)
        val searchView: SearchView = menu.findItem(R.id.menu_search).actionView as SearchView
        setupSearchViewMenu(searchView)
    }

    private fun setupSearchViewMenu(searchView: SearchView) {
        searchView.queryHint = this.getString(R.string.search)

        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.text_suggestion_label)
        val cursorAdapter = SimpleCursorAdapter(context, R.layout.hint_row, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        searchView.suggestionsAdapter = cursorAdapter
        searchView.setOnQueryTextListener(onSearchQueryListener(searchView, cursorAdapter))
        searchView.setOnSuggestionListener(onSearchSuggestionListener(searchView))
        searchView.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {

            override fun onViewDetachedFromWindow(v: View?) {
                viewModel.filterByName(null)
            }
            override fun onViewAttachedToWindow(v: View?) {}
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sortType = when (item.itemId) {
            R.id.menu_sortBy_titleAsc -> MoviesListSortType.TITLE_ASC
            R.id.menu_sortBy_titleDesc -> MoviesListSortType.TITLE_DESC
            R.id.menu_sortBy_dateAsc -> MoviesListSortType.DATE_ASC
            R.id.menu_sortBy_dateDesc -> MoviesListSortType.DATE_DESC
            R.id.menu_sortBy_popularityAsc -> MoviesListSortType.POPULARITY_ASC
            R.id.menu_sortBy_popularityDesc -> MoviesListSortType.POPULARITY_DESC
            else -> null
        }
        return if (sortType != null) {
            viewModel.setSortType(sortType)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun setupNavigation() {
        viewModel.openMovieEvent.observe(viewLifecycleOwner, EventObserver {
            openMovieDetails(it)
        })
    }

    private fun openMovieDetails(movieId: Int) {
        lifecycleScope.launch {
            val action = MoviesListFragmentDirections.actionMoviesListFragmentToMovieDetailFragment(movieId)
            findNavController().navigate(action)
            Toast.makeText(requireContext(), "You have selected movie with ID: $movieId", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = MoviesListAdapter(viewModel)
            val moviesList = viewDataBinding.moviesList
            val gridLayoutManager = GridAutoFitLayoutManager(requireActivity(), 200)
            moviesList.adapter = listAdapter
            moviesList.layoutManager = gridLayoutManager
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun setupHintsSearchAdapter() {
        viewModel.searchSuggestions.observe(viewLifecycleOwner, Observer {
            suggestionsMap.putAll(it)
        })
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager = requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setupErrorsHandler() {
        viewModel.errorsMessages.observe(viewLifecycleOwner, Observer {
            Snackbar.make(requireView(), getString(R.string.error_loading_content, it), Snackbar.LENGTH_LONG).show()
        })
    }

    private fun onSearchSuggestionListener(searchView: SearchView): SearchView.OnSuggestionListener {
        return object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(position: Int): Boolean {
                hideKeyboard(searchView)
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))

                // Clear the searchView for future searches
                searchView.setQuery(null, false)

                val keys = suggestionsMap.filterValues { it == selection }.keys
                openMovieDetails(keys.first())
                return true
            }
        }
    }

    private fun onSearchQueryListener(searchView: SearchView, cursorAdapter: SimpleCursorAdapter): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard(searchView)
                viewModel.filterByName(query)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                query?.let {
                    var index = suggestionsMap.size - 1
                    suggestionsMap.forEach { movieId, movieTitle ->
                        if (movieTitle.contains(query, true)) {
                            cursor.addRow(arrayOf(index, movieTitle))
                            index++
                        }
                    }
                }
                cursorAdapter.changeCursor(cursor)
                return false
            }
        }
    }
}
