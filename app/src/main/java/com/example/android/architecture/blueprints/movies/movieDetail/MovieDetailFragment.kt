package com.example.android.architecture.blueprints.movies.movieDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.architecture.blueprints.movies.R
import com.example.android.architecture.blueprints.movies.databinding.FragmentMovieDetailBinding
import com.example.android.architecture.blueprints.movies.movieDetail.adapters.CastListAdapter
import com.example.android.architecture.blueprints.movies.movieDetail.adapters.ReviewsListAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class MovieDetailFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MovieDetailViewModel> { viewModelFactory }

    private lateinit var viewDataBinding: FragmentMovieDetailBinding

    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewDataBinding = FragmentMovieDetailBinding.inflate(inflater, container, false).apply { viewmodel = viewModel }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        viewModel.load(args.movieId)
        setupCastListAdapter()
        setupReviewsListAdapter()
        setupErrorMessagesHandler()
    }

    private fun setupCastListAdapter() {
        val castList = viewDataBinding.recyclerviewCasts
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        castList.adapter = CastListAdapter()
        castList.layoutManager = layoutManager
    }

    private fun setupReviewsListAdapter() {
        viewDataBinding.recyclerviewReviews.adapter = ReviewsListAdapter()
    }

    private fun setupErrorMessagesHandler() {
        viewModel.errorMessageHandler.observe(viewLifecycleOwner, Observer {
            showErrorOnLoadingMovie(it)
        })
    }

    private fun showErrorOnLoadingMovie(message: String) {
        val snack = Snackbar.make(viewDataBinding.root, getString(R.string.error_loading_content, message), Snackbar.LENGTH_INDEFINITE)
        snack.setAction(R.string.refresh_movie_content) {
            viewModel.load(args.movieId)
        }
        snack.show()
    }
}
