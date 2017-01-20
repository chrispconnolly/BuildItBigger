package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.Joker;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;

import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSavedInstanceState = savedInstanceState;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        if(mSavedInstanceState == null) {
            FetchJokeTask fetchJokeTask = new FetchJokeTask();
            fetchJokeTask.execute();
        }
        else
            Toast.makeText(getApplication(), getString(R.string.offline_joke), Toast.LENGTH_LONG).show();
    }


    public void androidLibrary(View view) {
        if(mSavedInstanceState == null) {
            FetchAndroidLibraryJokeTask fetchAndroidLibraryJokeTask = new FetchAndroidLibraryJokeTask();
            fetchAndroidLibraryJokeTask.execute();
        }
        else
            Toast.makeText(getApplication(), getString(R.string.offline_joke), Toast.LENGTH_LONG).show();
    }

    public void gceJoke(View view) {
        if(mSavedInstanceState == null) {
            new EndpointsAsyncTask().execute(this);
        }
        else
            Toast.makeText(getApplication(), getString(R.string.offline_joke), Toast.LENGTH_LONG).show();
    }

    public class FetchJokeTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String joke) {
            try {
                joke = new JSONObject(joke).getJSONObject("value").getString("joke");
                Toast toast = Toast.makeText(getApplication(), joke, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            catch (Exception e)
            {
                Log.e("FetchMoviesTask", "Error getting joke", e);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            Joker joker = new Joker();
            return joker.getJoke();
        }
    }

    public class FetchAndroidLibraryJokeTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String joke) {
            try {
                joke = new JSONObject(joke).getJSONObject("value").getString("joke");
                Intent androidLibraryIntent = new Intent(getApplication(), com.example.androidjokes.MainActivity.class);
                androidLibraryIntent.putExtra("joke", joke);
                startActivity(androidLibraryIntent);
            }
            catch (Exception e)
            {
                Log.e("AndroidLibraryJokeTask", "Error getting joke", e);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            Joker joker = new Joker();
            return joker.getJoke();
        }
    }

    public static class EndpointsAsyncTask extends AsyncTask<Context, Void, String> {
        private static MyApi myApiService = null;
        private Context context;

        @Override
        protected String doInBackground(Context... params) {
            if(myApiService == null) {
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                myApiService = builder.build();
            }
            context = params[0];

            try {
                return myApiService.getJoke().execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String joke) {
            try{
                joke = new JSONObject(joke).getJSONObject("value").getString("joke");
                Toast.makeText(context, joke, Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Log.e("EndpointsAsyncTask", "Error getting joke", e);
            }
        }
    }
}
