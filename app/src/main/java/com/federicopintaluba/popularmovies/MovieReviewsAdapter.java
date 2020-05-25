package com.federicopintaluba.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.federicopintaluba.popularmovies.model.MovieReview;

import java.util.List;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewViewHolder> {

    private Context context;
    private List<MovieReview> dataSource;

    MovieReviewsAdapter(Context context, List<MovieReview> dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_movie_review, parent, false);

        return new MovieReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewViewHolder holder, int position) {
        if (dataSource != null && dataSource.get(position) != null) {
            holder.bind(dataSource.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return dataSource != null ? dataSource.size() : 0;
    }

    void updateDataSource(List<MovieReview> movieReviews) {
        this.dataSource = movieReviews;
        notifyDataSetChanged();
    }

    class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        final TextView reviewAuthor;
        final TextView reviewContent;

        MovieReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewAuthor = itemView.findViewById(R.id.movie_review_author);
            reviewContent = itemView.findViewById(R.id.movie_review_content);
        }

        void bind(MovieReview movieReview) {
            reviewAuthor.setText(movieReview.getAuthor());
            reviewContent.setText(movieReview.getContent());
        }
    }
}
