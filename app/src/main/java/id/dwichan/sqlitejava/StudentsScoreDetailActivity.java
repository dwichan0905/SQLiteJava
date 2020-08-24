package id.dwichan.sqlitejava;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import id.dwichan.sqlitejava.adapters.ScoreAdapter;
import id.dwichan.sqlitejava.databases.scores.ScoresHelper;
import id.dwichan.sqlitejava.databases.scores.ScoresMappingHelper;
import id.dwichan.sqlitejava.models.Score;
import id.dwichan.sqlitejava.models.Student;

interface LoadScoresCallback {
    void onPreExecute();

    void onPostExecute(ArrayList<Score> score);
}

public class StudentsScoreDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_COLLEGE = "extra_college";
    private ScoresHelper scoresHelper;
    private Student student;

    private ProgressBar progressBar;
    private ScoreAdapter scoreAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_score_detail);

        TextView tvName = findViewById(R.id.tvName);
        TextView tvNimClass = findViewById(R.id.tvScoreFor);
        RecyclerView recView = findViewById(R.id.recView);
        ExtendedFloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        progressBar = findViewById(R.id.progressBar);
        Toolbar tb = findViewById(R.id.toolbar);

        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            student = bundle.getParcelable(EXTRA_COLLEGE);

            assert student != null;
            String name = student.getName();
            String nimClass = student.getNim();

            tvName.setText(name);
            tvNimClass.setText(nimClass);
        }

        scoresHelper = ScoresHelper.getInstance(this);
        scoresHelper.openDatabase();

        scoreAdapter = new ScoreAdapter();
        recView.setLayoutManager(new LinearLayoutManager(this));
        recView.setAdapter(scoreAdapter);
        recView.setHasFixedSize(true);

        btnAdd.setOnClickListener(this);
        loadScores();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scoresHelper.closeDatabase();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAdd) {
            Score score = new Score();
            score.setNim(student.getNim());

            Intent i = new Intent(this, AddUpdateScoreActivity.class);
            i.putExtra(AddUpdateScoreActivity.EXTRA_COURSE, score);
            i.putExtra(AddUpdateScoreActivity.EXTRA_IS_EDITING, false);
            startActivityForResult(i, AddUpdateScoreActivity.REQUEST_SAVE);
        }
    }

    private void loadScores() {
        new LoadScoreAsync(scoresHelper, student.getNim(), new LoadScoresCallback() {
            @Override
            public void onPreExecute() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // sebelum asinkron dijalankan
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onPostExecute(ArrayList<Score> score) {
                // setelah lakukan async
                progressBar.setVisibility(View.GONE);
                if (score.size() > 0) {
                    scoreAdapter.setData(score);
                } else {
                    scoreAdapter.setData(new ArrayList<Score>());
                }
                Log.e("TAG", "Asynchronous is complete with " + score.size() + " data!");
            }
        }).execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == AddUpdateScoreActivity.REQUEST_SAVE) {
                loadScores();
            } else if (requestCode == AddUpdateScoreActivity.REQUEST_UPDATE) {
                loadScores();
            }
        } else if (resultCode == AddUpdateScoreActivity.RESULT_DELETED) {
            loadScores();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }

    private static class LoadScoreAsync extends AsyncTask<Void, Void, ArrayList<Score>> {

        private WeakReference<ScoresHelper> weakScoresHelper;
        private WeakReference<LoadScoresCallback> weakLoadScoresCallback;
        private String nim;

        public LoadScoreAsync(ScoresHelper helper, String nim, LoadScoresCallback callback) {
            this.weakScoresHelper = new WeakReference<>(helper);
            this.nim = nim;
            this.weakLoadScoresCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakLoadScoresCallback.get().onPreExecute();
        }

        @Override
        protected ArrayList<Score> doInBackground(Void... voids) {
            // dijalankan secara asinkron
            Cursor cursor = weakScoresHelper.get().queryAllByNim(this.nim);
            Log.e("TAG", "Asynchronous is running!");
            return ScoresMappingHelper.mapCursorToArrayList(cursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Score> score) {
            super.onPostExecute(score);
            weakLoadScoresCallback.get().onPostExecute(score);
        }
    }
}