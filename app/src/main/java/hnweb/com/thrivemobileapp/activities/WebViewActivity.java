package hnweb.com.thrivemobileapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebView;

import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnLoadCompleteListener;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import java.io.File;

import hnweb.com.thrivemobileapp.R;

public class WebViewActivity extends AppCompatActivity implements OnLoadCompleteListener, OnPageChangeListener {
    WebView myWebView;
    String formURL, title;
    int PIC_WIDTH;
    Toolbar toolbar;
    private PDFView pdfview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        try {

            formURL = getIntent().getExtras().getString("PATH");
            Log.e("formURL",formURL);
            pdfview = (PDFView) findViewById(R.id.pdfview);
            File file = new File(formURL);
            pdfview.fromFile(file)
                    .defaultPage(1)
                    .showMinimap(false)

                    .onLoad(this)
                    .onPageChange(this)
                    .load();

            //.enableSwipe(true)

        }catch (Exception e){

            Log.d("Exception PDF", e.getMessage());

        }


    }


    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }
}
