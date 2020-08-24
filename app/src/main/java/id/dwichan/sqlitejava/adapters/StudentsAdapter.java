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

import id.dwichan.sqlitejava.AddUpdateStudentsActivity;
import id.dwichan.sqlitejava.R;
import id.dwichan.sqlitejava.StudentsScoreDetailActivity;
import id.dwichan.sqlitejava.models.Student;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder> {

    private ArrayList<Student> mData = new ArrayList<>();

    public void setData(ArrayList<Student> student) {
        mData.clear();
        mData.addAll(student);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_students, parent, false);
        return new StudentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class StudentsViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvClass;

        public StudentsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvClass = itemView.findViewById(R.id.tvScoreFor);
        }

        public void bind(final Student student) {
            tvName.setText(student.getName());
            String nimClass = student.getNim() + " - " + student.getEmail();
            tvClass.setText(nimClass);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), StudentsScoreDetailActivity.class);
                    i.putExtra(StudentsScoreDetailActivity.EXTRA_COLLEGE, student);
                    itemView.getContext().startActivity(i);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent i = new Intent(itemView.getContext(), AddUpdateStudentsActivity.class);
                    i.putExtra(AddUpdateStudentsActivity.EXTRA_COLLEGE, student);
                    ((Activity) itemView.getContext()).startActivityForResult(i, AddUpdateStudentsActivity.REQUEST_UPDATE);
                    return true;
                }
            });
        }

    }
}
