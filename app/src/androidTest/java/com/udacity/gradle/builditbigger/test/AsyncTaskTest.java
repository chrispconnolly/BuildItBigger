package com.udacity.gradle.builditbigger.test;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.udacity.gradle.builditbigger.MainActivity;
import com.udacity.gradle.builditbigger.MainActivity.EndpointsAsyncTask;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class AsyncTaskTest {

    @Test
    public void testEndpointsAsyncTask() {
        try {
            MainActivity.EndpointsAsyncTask endpointsAsyncTask = new EndpointsAsyncTask();
            String joke = endpointsAsyncTask.execute(new com.example.androidjokes.MainActivity()).get();
            assertTrue(joke.contains("Chuck Norris"));
        }
        catch(Exception ex){
            Log.e("AsyncTaskTest", "Error in AsyncTaskTest");
        }
    }
}