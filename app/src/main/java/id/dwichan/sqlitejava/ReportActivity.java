package id.dwichan.sqlitejava;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import id.dwichan.sqlitejava.databases.AllDataHelper;
import id.dwichan.sqlitejava.databases.AllDataMappingHelper;
import id.dwichan.sqlitejava.models.Data;

interface LoadAllDataCallback {
    void onPreExecute();

    void onPostExecute(ArrayList<Data> dataArrayList);
}

public class ReportActivity extends AppCompatActivity implements LoadAllDataCallback {

    private WebView webReport;
    private ProgressBar progressBar;
    private AllDataHelper allDataHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        webReport = findViewById(R.id.webReport);
        progressBar = findViewById(R.id.progressBar);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        allDataHelper = AllDataHelper.getInstance(this);
        allDataHelper.openDatabase();

        // ambil data
        new LoadDataAsync(allDataHelper, this).execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) super.onBackPressed();
        return true;
    }

    @Override
    public void onPreExecute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // jalankan sebelum asynchronous!
                webReport.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onPostExecute(ArrayList<Data> dataArrayList) {
        // jalankan setelah asynchronous!
        webReport.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        // buat halaman HTML
        StringBuilder html = new StringBuilder("<html>" +
                "<head>" +
                getString(R.string.title_student_score_report) +
                "</head>" +
                "<body>" +
                getString(R.string.table_title_student_score_report) +
                "<table border=\"1\" style=\"width: 100%\">" +
                "<tr>" +
                getString(R.string.row_student_nim) +
                getString(R.string.row_student_name) +
                getString(R.string.row_student_email) +
                getString(R.string.row_student_course) +
                getString(R.string.row_student_score) +
                "</tr>");

        if (dataArrayList.size() > 0) {
            for (int i = 0; i <= dataArrayList.size() - 1; i++) {
                html.append("<tr>")
                        .append("<td>").append(dataArrayList.get(i).getNim()).append("</td>")
                        .append("<td>").append(dataArrayList.get(i).getName()).append("</td>")
                        .append("<td>").append(dataArrayList.get(i).getEmail()).append("</td>")
                        .append("<td>").append(dataArrayList.get(i).getCourses()).append("</td>")
                        .append("<td>").append(dataArrayList.get(i).getScores()).append("</td>")
                        .append("</tr>");
            }
        }

        html.append("</table>")
                .append("</body>")
                .append("</html>");
        // buat halaman HTML selesai

        // muat HTML ke WebView
        webReport.loadData(html.toString(), "text/html", "UTF-8");
        webReport.getSettings().setBuiltInZoomControls(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        allDataHelper.closeDatabase();
    }

    private static class LoadDataAsync extends AsyncTask<Void, Void, ArrayList<Data>> {

        private WeakReference<AllDataHelper> allDataHelperWeakReference;
        private WeakReference<LoadAllDataCallback> loadAllDataCallbackWeakReference;

        public LoadDataAsync(AllDataHelper allDataHelper, LoadAllDataCallback loadAllDataCallback) {
            this.allDataHelperWeakReference = new WeakReference<>(allDataHelper);
            this.loadAllDataCallbackWeakReference = new WeakReference<>(loadAllDataCallback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadAllDataCallbackWeakReference.get().onPreExecute();
        }

        @Override
        protected ArrayList<Data> doInBackground(Void... voids) {
            // Jalankan secara asynchronous!
            Cursor cursor = allDataHelperWeakReference.get().queryAllStudentScore();
            return AllDataMappingHelper.mapCursorToArrayList(cursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Data> data) {
            super.onPostExecute(data);
            loadAllDataCallbackWeakReference.get().onPostExecute(data);
        }
    }
}