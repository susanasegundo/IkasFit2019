package org.morfe.ikasfit19.Ventanas;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;

import org.morfe.ikasfit19.BaseDatos.BaseDatos;
import org.morfe.ikasfit19.Clases.GetCaloriesAsyncTask;
import org.morfe.ikasfit19.R;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Principal extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    private static final int SOLICITUD_PERMISO_CALL_PHONE = 1;
    private Intent intentoLocaliz;
    GoogleApiClient mClient;
    private Button boton;
    private TextView textView;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boton = (Button) findViewById(R.id.button);
        boton.setOnClickListener(this);
        textView = (TextView) findViewById(R.id.textoMostrar);
        setContentView(R.layout.activity_principal);
        buildFitnessClient();
        BaseDatos bd = new BaseDatos();
        bd.guardarUsuario();

///////////////////////////////////////////////
       /*intentoLocaliz = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"652340176"));

        //Si nos concede el permiso, lanza la llamada y lanza el toast
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intentoLocaliz);
            Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT);
        } else {

            explicarUsosPermisos();
            solicitarPermisoLlamada();
        }*/
    }
   /* private void explicarUsosPermisos(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)) {
            Toast.makeText(this, "Explicamos permiso", Toast.LENGTH_SHORT);
            alertDialogBasico();
        }
    }

    private void solicitarPermisoLlamada(){
        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.CALL_PHONE}, SOLICITUD_PERMISO_CALL_PHONE);
        Toast.makeText(this, "Pedimos el permiso", Toast.LENGTH_SHORT);
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==SOLICITUD_PERMISO_CALL_PHONE){
            startActivity(intentoLocaliz);
            Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT);
        }
        else{
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
        }
    }

    public void alertDialogBasico () {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sin el permiso de llamada no podemos realizar");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        });
    }*/

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
