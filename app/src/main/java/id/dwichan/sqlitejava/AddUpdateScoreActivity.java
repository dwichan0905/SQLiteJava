package id.dwichan.sqlitejava;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import id.dwichan.sqlitejava.databases.scores.ScoresContract;
import id.dwichan.sqlitejava.databases.scores.ScoresHelper;
import id.dwichan.sqlitejava.models.Score;

public class AddUpdateScoreActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_IS_EDITING = "extra_is_editing";
    public final static String EXTRA_COURSE = "extra_course";
    public final static int REQUEST_SAVE = 101;
    public final static int REQUEST_UPDATE = 102;
    public final static int RESULT_DELETED = 201;

    private Score score;
    private Spinner spCourse;
    private EditText edtScore;
    private ScoresHelper scoresHelper;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_score);

        Toolbar toolbar = findViewById(R.id.toolbar);
        spCourse = findViewById(R.id.spCourses);
        edtScore = findViewById(R.id.edtScore);
        TextView textView = findViewById(R.id.textView);
        TextView tvKet = findViewById(R.id.tvKet);
        final MaterialButton btnSave = findViewById(R.id.btnSave);

        scoresHelper = ScoresHelper.getInstance(this);
        scoresHelper.openDatabase();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isEdit = bundle.getBoolean(EXTRA_IS_EDITING);
            score = bundle.getParcelable(EXTRA_COURSE);
            assert score != null;

            if (isEdit) {
                textView.setText(R.string.change_student_score);
                String courseName = score.getCourseName();
                String courseScore = score.getScore();
                String[] coursesList = getResources().getStringArray(R.array.courses_list);

                for (int i = 0; i < coursesList.length; i++) {
                    if (courseName.equals(coursesList[i])) {
                        spCourse.setSelection(i);
                        break;
                    }
                }
                edtScore.setText(courseScore);
                btnSave.setText(R.string.update);
            }
            String msg = getResources().getString(R.string.add_update_score_message) + score.getNim();
            tvKet.setText(msg);
        }

        edtScore.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    btnSave.performClick();
                }
                return true;
            }
        });

        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSave) {
            String courseName = spCourse.getSelectedItem().toString();
            String courseScore = edtScore.getText().toString();
            ContentValues values = new ContentValues();
            if (isEdit) {
                values.put(ScoresContract.ScoresColumn.COURSES, courseName);
                values.put(ScoresContract.ScoresColumn.SCORE, courseScore);

                long result = scoresHelper.update(score.getId(), values);
                if (result > 0) {
                    setResult(RESULT_OK);
                    finish();
                }
            } else {
                values.put(ScoresContract.ScoresColumn.NIM, score.getNim());
                values.put(ScoresContract.ScoresColumn.COURSES, courseName);
                values.put(ScoresContract.ScoresColumn.SCORE, courseScore);

                long result = scoresHelper.insertData(values);
                if (result > 0) {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scoresHelper.closeDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) getMenuInflater().inflate(R.menu.menu_students_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.menuDelete:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(R.string.sure);
                alert.setMessage(getString(R.string.confirm_delete_score) + score.getCourseName() + "" +
                        getString(R.string.confirm_delete_score_for_nim) + score.getNim() + "?");
                alert.setPositiveButton(R.string.delete_score_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long result = scoresHelper.deleteById(score.getId());
                        if (result > 0) {
                            setResult(RESULT_DELETED);
                            finish();
                        }
                    }
                });
                alert.setNegativeButton(R.string.delete_score_no, null);
                alert.setCancelable(false);

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                break;
        }
        return true;
    }
}