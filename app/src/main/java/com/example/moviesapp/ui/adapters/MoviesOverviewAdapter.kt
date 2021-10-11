package com.example.moviesapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.R
import com.example.moviesapp.data.MovieOverview
import com.example.moviesapp.data.source.network.IMAGE_BASE_URL

/**
 * RecyclerView adapter for list of movie overviews.
 * @property onClickListener OnClickListener with action on item press
 */
class MoviesOverviewAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<MoviesOverviewAdapter.ViewHolder>() {

    // Movies to show in RecyclerView
    private var movies = emptyList<MovieOverview>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_preview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(movie)
        }
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.item_movie_preview__tv_title)
        private val ivPoster: ImageView = itemView.findViewById(R.id.item_movie_preview__iv_poster)
        private val tvScore: TextView = itemView.findViewById(R.id.item_movie_preview__tv_score)

        fun bind(movie: MovieOverview) {
            tvTitle.text = movie.title
            tvScore.text = movie.vote_average.toString()

            val imgUrl = IMAGE_BASE_URL + movie.poster_path
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(ivPoster.context)
                .load(imgUri)
                .override(500, 500)
                .placeholder(R.drawable.loading_placeholder)
                .fitCenter()
                .into(ivPoster)
        }
    }

    /**
     * Change [movies] with [newMovies].
     * @param newMovies list of movie overviews
     */
    fun setData(newMovies: List<MovieOverview>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    /**
     * Class for processing on click.
     * @param onClickListener function to execute
     */
    class OnClickListener(private val onClickListener: (movie: MovieOverview) -> Unit) {
        fun onClick(movie: MovieOverview) = onClickListener(movie)
    }
}
