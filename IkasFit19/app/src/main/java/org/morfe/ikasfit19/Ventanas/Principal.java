package org.morfe.ikasfit19.Ventanas;


import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;

import org.morfe.ikasfit19.BaseDatos.BaseDatos;
import org.morfe.ikasfit19.Clases.GetCaloriesAsyncTask;
import org.morfe.ikasfit19.R;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Principal extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    GoogleApiClient mClient;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        buildFitnessClient();
        BaseDatos bd = new BaseDatos();
        bd.guardar();
    }


    private void buildFitnessClient() {
        // Create the Google API Client
        mClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addConnectionCallbacks(this)
                .useDefaultAccount().build();
        mClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        fetchUserGoogleFitData("2019-01-15");
        // selectedDate in format yyyy-MM-dd
    }

    @Override
    public void onConnectionSuspended(int i) {

        // Error Connecting to Google Fit
        // Handle your error messaging

    }

    public void fetchUserGoogleFitData(String date) {
        if (mClient != null && mClient.isConnected() && mClient.hasConnectedApi(Fitness.HISTORY_API)) {

           SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date d1 = null;
            try {
                d1 = originalFormat.parse(date);
            } catch (Exception ignored) {

            }
            Calendar calendar = Calendar.getInstance();

            try {
                calendar.setTime(d1);
            } catch (Exception e) {
                calendar.setTime(new Date());
            }

            DataReadRequest readRequest = queryDateFitnessData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            new GetCaloriesAsyncTask(readRequest, mClient).execute();

            Log.d("HistoryAPI", "Connected");
            System.out.println("Conectado");

        }else{

            Log.d("HistoryAPI", "Not connected");
            System.out.println("no Conectado");

        }

    }
    private DataReadRequest queryDateFitnessData(int year, int month, int day_of_Month) {

        Calendar startCalendar = Calendar.getInstance(Locale.getDefault());


        startCalendar.set(Calendar.YEAR, year);
        startCalendar.set(Calendar.MONTH, month);
        startCalendar.set(Calendar.DAY_OF_MONTH, day_of_Month);

        startCalendar.set(Calendar.HOUR_OF_DAY, 23);
        startCalendar.set(Calendar.MINUTE, 59);
        startCalendar.set(Calendar.SECOND, 59);
        startCalendar.set(Calendar.MILLISECOND, 999);
        long endTime = startCalendar.getTimeInMillis();


        startCalendar.set(Calendar.HOUR_OF_DAY, 0);
        startCalendar.set(Calendar.MINUTE, 0);
        startCalendar.set(Calendar.SECOND, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        long startTime = startCalendar.getTimeInMillis();

        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();

        return new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .aggregate(ESTIMATED_STEP_DELTAS, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByActivitySegment(1, TimeUnit.MILLISECONDS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

    }

}
