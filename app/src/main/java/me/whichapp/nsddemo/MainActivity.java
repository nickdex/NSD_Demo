package me.whichapp.nsddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";
    TextView deviceName;

    ListView deviceList;

    NSDHelper nsdHelper;
    DeviceConnection deviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceName = (TextView) findViewById(R.id.device_name);
        deviceList = (ListView) findViewById(R.id.listView);

        nsdHelper = new NSDHelper(this);
        nsdHelper.initializeNsd();
    }

    public void registerService(View v)
    {
        // Register service
        if(deviceConnection.getLocalPort() > -1) {
            nsdHelper.registerService(deviceConnection.getLocalPort());
        } else {
            Log.d(TAG, "ServerSocket isn't bound.");
        }
    }

    public void clickDiscover(View v) {
        nsdHelper.discoverServices();
    }
}
