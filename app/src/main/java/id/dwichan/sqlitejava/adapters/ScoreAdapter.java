package id.dwichan.sqlitejava.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import id.dwichan.sqlitejava.AddUpdateScoreActivity;
import id.dwichan.sqlitejava.R;
import id.dwichan.sqlitejava.models.Score;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {

    private ArrayList<Score> score = new ArrayList<>();

    public void setData(ArrayList<Score> score) {
        this.score.clear();
        this.score.addAll(score);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        holder.bind(score.get(position));
    }

    @Override
    public int getItemCount() {
        return this.score.size();
    }

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCourseName;
        private TextView tvScore;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCourseName = itemView.findViewById(R.id.tvCourseName);
            tvScore = itemView.findViewById(R.id.tvScore);
        }

        public void bind(final Score score) {
            String courseName = score.getCourseName();
            String courseScore = score.getScore();
            tvCourseName.setText(courseName);
            tvScore.setText(courseScore);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), AddUpdateScoreActivity.class);
                    i.putExtra(AddUpdateScoreActivity.EXTRA_COURSE, score);
                    i.putExtra(AddUpdateScoreActivity.EXTRA_IS_EDITING, true);
                    ((Activity) itemView.getContext()).startActivityForResult(i, AddUpdateScoreActivity.REQUEST_UPDATE);
                }
            });
        }
    }
}
