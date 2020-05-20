package com.federicopintaluba.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.federicopintaluba.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    final private MovieItemClickListener movieItemClickListener;
    private Context context;
    private List<Movie> dataSource;

    MovieAdapter(Context context, List<Movie> dataSource, MovieItemClickListener movieItemClickListener) {
        this.context = context;
        this.dataSource = dataSource;
        this.movieItemClickListener = movieItemClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if (dataSource != null && dataSource.get(position) != null) {
            holder.bind(dataSource.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return dataSource != null ? dataSource.size() : 0;
    }

    void updateDataSource(List<Movie> movies) {
        this.dataSource = movies;
        notifyDataSetChanged();
    }

    public interface MovieItemClickListener {

        void onMovieItemClick(Movie movie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView posterImage;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.poster_image);

            itemView.setOnClickListener(this);
        }

        void bind(Movie movie) {
            Picasso.with(context)
                    .load(NetworkUtils.buildMoviePosterPath(movie.getPosterPath()))
                    .into(posterImage);
        }

        @Override
        public void onClick(View v) {
            Movie movie = dataSource.get(getAdapterPosition());
            movieItemClickListener.onMovieItemClick(movie);
        }
    }
}
