package com.prabhat.mswipecrm.androidapifetch;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
public class MainActivity extends AppCompatActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.my_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button b= findViewById(R.id.my_button);

        b.setClickable(false);
        new LongRunnningGetIO().execute();
    }

    private class  LongRunnningGetIO extends AsyncTask<Void,Void,String>{
        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {

            InputStream in = entity.getContent();

            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n>0) {
                byte[] b = new byte[4096];

                n =  in.read(b);

                if (n>0) out.append(new String(b, 0, n));

            }

            return out.toString();

        }


        @Override
        protected String doInBackground(Void... voids) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            HttpGet httpGet = new HttpGet("https://vigilant-cider-261407.appspot.com/eventDetails/findAllEvents");

            String text = null;

            try {
                HttpResponse response = httpClient.execute(httpGet,localContext);

                HttpEntity entity = response.getEntity();

                text = getASCIIContentFromEntity(entity);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }

            return text;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result!=null) {

                EditText et = (EditText)findViewById(R.id.my_edit);

                et.setText(result);

            }

            Button b = (Button)findViewById(R.id.my_button);

            b.setClickable(true);

        }
    }

}
