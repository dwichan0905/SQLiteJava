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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import id.dwichan.sqlitejava.databases.students.StudentsContract;
import id.dwichan.sqlitejava.databases.students.StudentsHelper;
import id.dwichan.sqlitejava.models.Student;

public class AddUpdateStudentsActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String EXTRA_COLLEGE = "extra_college";
    public final static int REQUEST_SAVE = 100;
    public final static int REQUEST_UPDATE = 101;
    public final static int RESULT_DELETED = 102;
    private boolean isEdit = false;

    private EditText edtNim;
    private EditText edtName;
    private EditText edtClass;
    private Student student;
    private StudentsHelper studentsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_students);

        studentsHelper = StudentsHelper.getInstance(getApplicationContext());
        studentsHelper.openDatabase();

        final MaterialButton btnSave = findViewById(R.id.btnSave);
        edtNim = findViewById(R.id.edtNim);
        edtName = findViewById(R.id.edtName);
        edtClass = findViewById(R.id.edtClass);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView textView = findViewById(R.id.textView);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isEdit = true;
            student = bundle.getParcelable(EXTRA_COLLEGE);

            assert student != null;
            edtNim.setText(student.getNim());
            edtName.setText(student.getName());
            edtClass.setText(student.getEmail());

            btnSave.setText(R.string.update);
            textView.setText(R.string.change_student_data);
        }

        edtNim.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) edtName.requestFocus();
                return true;
            }
        });
        edtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) edtClass.requestFocus();
                return true;
            }
        });
        edtClass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) btnSave.performClick();
                return true;
            }
        });

        btnSave.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        studentsHelper.closeDatabase();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSave) {
            // save
            boolean isError = false;
            if (edtNim.getText().toString().isEmpty()) {
                isError = true;
                edtNim.setError(getResources().getString(R.string.empty_nim));
            }
            if (edtName.getText().toString().isEmpty()) {
                isError = true;
                edtName.setError(getResources().getString(R.string.empty_name));
            }
            if (edtClass.getText().toString().isEmpty()) {
                isError = true;
                edtClass.setError(getResources().getString(R.string.empty_class));
            }
            if (!isError) {
                String nim = edtNim.getText().toString();
                String name = edtName.getText().toString();
                String collegeClass = edtClass.getText().toString();

                ContentValues values = new ContentValues();
                values.put(StudentsContract.StudentsColumn.NIM, nim);
                values.put(StudentsContract.StudentsColumn.NAME, name);
                values.put(StudentsContract.StudentsColumn.EMAIL, collegeClass);

                if (isEdit) {
                    // Update Database
                    long result = studentsHelper.update(student.getNim(), values);
                    if (result > 0) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "Gagal update data!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Insert Database
                    long result = studentsHelper.insertData(values);
                    if (result > 0) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(this, "Gagal menambahkan data!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
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
                alert.setMessage(getString(R.string.delete_student_confirm) + student.getName() + "?");
                alert.setPositiveButton(R.string.delete_student_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long result = studentsHelper.deleteByNim(student.getNim());
                        if (result > 0) {
                            setResult(RESULT_DELETED);
                            finish();
                        }
                    }
                });
                alert.setNegativeButton(R.string.delete_student_no, null);
                alert.setCancelable(false);

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                break;
        }
        return true;
    }
}