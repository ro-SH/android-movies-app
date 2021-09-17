package com.example.moviesapp.ui.favourites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.data.MovieOverview

class FavouritesAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<FavouritesAdapter.ViewHolder>() {

    private var favourites = emptyList<MovieOverview>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favourite_movies, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouritesAdapter.ViewHolder, position: Int) {
        val movie = favourites[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(movie)
        }
        holder.bind(movie)
    }

    override fun getItemCount() = favourites.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvTitle: TextView = itemView.findViewById(R.id.item_favourite_movies__tv_title)
        private val tvAverage: TextView = itemView.findViewById(R.id.item_favourite_movies__tv_average)

        fun bind(movie: MovieOverview) {
            tvTitle.text = movie.title
            tvAverage.text = movie.vote_average.toString()
        }
    }

    fun setData(newMovies: List<MovieOverview>) {
        favourites = newMovies
        notifyDataSetChanged()
    }

    class OnClickListener(private val onClickListener: (movie: MovieOverview) -> Unit) {
        fun onClick(movie: MovieOverview) = onClickListener(movie)
    }
}
