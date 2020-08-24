package id.dwichan.sqlitejava;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import id.dwichan.sqlitejava.adapters.StudentsAdapter;
import id.dwichan.sqlitejava.databases.students.StudentsHelper;
import id.dwichan.sqlitejava.databases.students.StudentsMappingHelper;
import id.dwichan.sqlitejava.models.Student;

interface LoadStudentsCallback {
    void preExecute();

    void postExecute(ArrayList<Student> students);
}

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private StudentsAdapter studentsAdapter;
    private StudentsHelper studentsHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialButton btnAddMhs = findViewById(R.id.btnAddMhs);
        MaterialButton btnShowValues = findViewById(R.id.btnShowValues);
        progressBar = findViewById(R.id.progressBar);
        RecyclerView rvData = findViewById(R.id.rvData);
        Toolbar tb = findViewById(R.id.toolbar);

        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        btnAddMhs.setOnClickListener(this);
        btnShowValues.setOnClickListener(this);

        studentsAdapter = new StudentsAdapter();

        rvData.setLayoutManager(new LinearLayoutManager(this));
        rvData.setAdapter(studentsAdapter);
        rvData.setHasFixedSize(true);

        studentsHelper = StudentsHelper.getInstance(this);
        studentsHelper.openDatabase();

        // ambil data
        loadStudents();
    }

    private void loadStudents() {
        new LoadStudentsAsync(studentsHelper, new LoadStudentsCallback() {
            @Override
            public void preExecute() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // sebelum asinkron dijalankan
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void postExecute(ArrayList<Student> students) {
                // setelah asinkron selesai
                progressBar.setVisibility(View.GONE);
                if (students.size() > 0) {
                    studentsAdapter.setData(students);
                } else {
                    studentsAdapter.setData(new ArrayList<Student>());
                }
            }
        }).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //LoadColleges();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == AddUpdateStudentsActivity.REQUEST_SAVE) {
                loadStudents();
            } else if (requestCode == AddUpdateStudentsActivity.REQUEST_UPDATE) {
                loadStudents();
            }
        } else if (resultCode == AddUpdateStudentsActivity.RESULT_DELETED) {
            loadStudents();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        studentsHelper.closeDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            Toast.makeText(this, "Settings showed up!", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddMhs:
                Intent i = new Intent(this, AddUpdateStudentsActivity.class);
                startActivityForResult(i, AddUpdateStudentsActivity.REQUEST_SAVE);
                break;
            case R.id.btnShowValues:
                Intent in = new Intent(this, ReportActivity.class);
                startActivity(in);
                break;
        }
    }

    private static class LoadStudentsAsync extends AsyncTask<Void, Void, ArrayList<Student>> {

        private final WeakReference<StudentsHelper> weakStudentHelper;
        private final WeakReference<LoadStudentsCallback> weakCallback;

        private LoadStudentsAsync(StudentsHelper weakStudentsHelper, LoadStudentsCallback weakCallback) {
            this.weakStudentHelper = new WeakReference<>(weakStudentsHelper);
            this.weakCallback = new WeakReference<>(weakCallback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Student> doInBackground(Void... voids) {
            // disini perintah yang bakal dijalanin secara asinkron
            Cursor cursor = weakStudentHelper.get().queryAll();
            return StudentsMappingHelper.mapCursorToArrayList(cursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Student> students) {
            super.onPostExecute(students);
            weakCallback.get().postExecute(students);
        }
    }
}
