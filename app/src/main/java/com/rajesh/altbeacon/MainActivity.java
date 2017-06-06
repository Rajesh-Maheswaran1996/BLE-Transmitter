package com.rajesh.altbeacon;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatCallback;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    AdvertiseData madvertisedata;
    AdvertiseSettings mAdvertiseSettings;
    BluetoothDevice obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.button);


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "ble not supported", Toast.LENGTH_SHORT).show();
            finish();
        }




        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final BluetoothLeAdvertiser mBluetoothLeAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        Toast.makeText(getApplicationContext(),obj.getAddress() ,Toast.LENGTH_SHORT).show();

          setAdvertiseData();
          setAdvertiseSettings();
       final AdvertiseCallback madvertisecallback = new AdvertiseCallback() {
           @Override
           public void onStartSuccess(AdvertiseSettings settingsInEffect) {
              Toast.makeText(getApplicationContext(),"Advertising started",Toast.LENGTH_SHORT).show();
           }
       };

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothLeAdvertiser.startAdvertising(mAdvertiseSettings,madvertisedata,madvertisecallback);
            }
        });


    }





    protected void setAdvertiseData(){
        byte [] bb = new byte[24];

        AdvertiseData.Builder mbuilder = new AdvertiseData.Builder();
        ByteBuffer mManufacturerDatadummy = ByteBuffer.wrap(bb);
        String s = "8492E75F-4FD6-469D-B132-043FE94921D8";
        String s2 = s.replace("-", "");
        UUID uuid = new UUID(
                new BigInteger(s2.substring(0, 16), 16).longValue(),
                new BigInteger(s2.substring(16), 16).longValue());
        mManufacturerDatadummy.putLong(uuid.getMostSignificantBits());
        mManufacturerDatadummy.putLong(uuid.getLeastSignificantBits());

        ByteBuffer mManufacturerData = ByteBuffer.allocate(24);
        byte [] uuid1 = mManufacturerDatadummy.array();
        mManufacturerData.put(0, (byte)0xBE);
        mManufacturerData.put(1, (byte)0xAC);
        for (int i=2; i<=17; i++) {
            mManufacturerData.put(i, uuid1[i-2]); // adding the UUID
        }
        mManufacturerData.put(18, (byte)0x00); // first byte of Major
        mManufacturerData.put(19, (byte)0x09); // second byte of Major
        mManufacturerData.put(20, (byte)0x00); // first minor
        mManufacturerData.put(21, (byte)0x06); // second minor
        mManufacturerData.put(22, (byte)0xB5); // tx-power
        mbuilder.addManufacturerData(224, mManufacturerData.array());
        madvertisedata = mbuilder.build();

    }





    protected void setAdvertiseSettings() {
        AdvertiseSettings.Builder mBuilder = new AdvertiseSettings.Builder();
        mBuilder.setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED);
        mBuilder.setConnectable(false);
        mBuilder.setTimeout(0);
        mBuilder.setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM);
        mAdvertiseSettings = mBuilder.build();

    }




}
