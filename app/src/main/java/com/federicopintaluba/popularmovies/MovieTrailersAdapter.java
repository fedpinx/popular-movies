package com.federicopintaluba.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.federicopintaluba.popularmovies.model.MovieTrailer;

import java.util.List;

public class MovieTrailersAdapter extends RecyclerView.Adapter<MovieTrailersAdapter.MovieTrailerViewHolder> {

    final private MovieTrailerItemClickListener movieTrailerItemClickListener;
    private Context context;
    private List<MovieTrailer> dataSource;

    MovieTrailersAdapter(Context context, List<MovieTrailer> dataSource, MovieTrailerItemClickListener movieTrailerItemClickListener) {
        this.context = context;
        this.dataSource = dataSource;
        this.movieTrailerItemClickListener = movieTrailerItemClickListener;
    }

    @NonNull
    @Override
    public MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_movie_trailer, parent, false);

        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerViewHolder holder, int position) {
        if (dataSource != null && dataSource.get(position) != null) {
            holder.bind(dataSource.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return dataSource != null ? dataSource.size() : 0;
    }

    void updateDataSource(List<MovieTrailer> movieTrailers) {
        this.dataSource = movieTrailers;
        notifyDataSetChanged();
    }

    public interface MovieTrailerItemClickListener {

        void onMovieTrailerItemClick(MovieTrailer movieTrailer);
    }

    class MovieTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView trailerType;
        final TextView trailerName;

        MovieTrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            trailerType = itemView.findViewById(R.id.movie_trailer_type);
            trailerName = itemView.findViewById(R.id.movie_trailer_name);

            itemView.setOnClickListener(this);
        }

        void bind(MovieTrailer movieTrailer) {
            trailerType.setText(movieTrailer.getType());
            trailerName.setText(movieTrailer.getName());
        }

        @Override
        public void onClick(View v) {
            MovieTrailer movieTrailer = dataSource.get(getAdapterPosition());
            movieTrailerItemClickListener.onMovieTrailerItemClick(movieTrailer);
        }
    }
}
