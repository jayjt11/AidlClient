package com.tesseract.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tesseract.aidlinterface.AidlInterface;

public class AidlClientActivity extends AppCompatActivity  implements View.OnClickListener {

    AidlInterface aidlInterface = null;
    String TAG = AidlClientActivity.class.getSimpleName();
    Button btnGetData;

    TextView txtPitch;
    TextView txtRoll;

    float[] values = null;

    float pitch = 0.00f;
    float roll = 0.00f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        txtPitch = (TextView) findViewById(R.id.txtPitch);
        txtRoll = (TextView) findViewById(R.id.txtRoll);

        btnGetData = (Button) findViewById(R.id.btnGetData);
        btnGetData.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (aidlInterface == null) {

            Intent intent = new Intent("TESSERACT");
            intent.setPackage("com.tesseract.aidlserver");
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {

            aidlInterface = AidlInterface.Stub.asInterface(service);
            Toast.makeText(getApplicationContext(),	"Service Connected", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

            aidlInterface = null;
            Toast.makeText(getApplicationContext(), "Service Disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View view) {

        if (aidlInterface != null) {

            try {
                values = aidlInterface.getOrientationData();
                pitch = values[0];
                roll = values[1];

                txtPitch.setText("Pitch : "+ pitch);
                txtRoll.setText("Roll : "+ roll);
                Log.d(TAG, "Name is pitch " + pitch);
                Log.d(TAG, "Name is roll " + roll );

            } catch (RemoteException e) {
                e.printStackTrace();

            }
        } else {
            Toast.makeText(AidlClientActivity.this, "Service not bind " + aidlInterface, Toast.LENGTH_LONG).show();
        }
    }
}
