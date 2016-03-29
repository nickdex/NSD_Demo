package me.whichapp.nsddemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    private static final String TAG = "MainActivity";
    private static final int RQ_PERM = 20;
    private final int port = 9346;
    TextView deviceName;

    ListView deviceList;

    NSDHelper nsdHelper;

    private List<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceName = (TextView) findViewById(R.id.device_name);
        deviceList = (ListView) findViewById(R.id.listView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, RQ_PERM);
        } else
        {
            nsdHelper = new NSDHelper(this);
            nsdHelper.initializeNsd();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++)
        {
            if (i == PackageManager.PERMISSION_GRANTED)
                if (permissions[i].equals(Manifest.permission.INTERNET))
                {
                    nsdHelper = new NSDHelper(this);
                    nsdHelper.initializeNsd();
                }
        }
    }

    public void advertise(View v)
    {
        // Register service
        nsdHelper.registerService(port);
    }

    public void connect(View v)
    {
        list.clear();
        list.addAll(nsdHelper.getList());
        adapter.notifyDataSetChanged();
//        nsdHelper.discoverServices();
    }

    @Override
    protected void onPause()
    {
        if (nsdHelper != null)
        {
            nsdHelper.stopDiscovery();
        }
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (nsdHelper != null)
        {
            nsdHelper.discoverServices();
        }
    }

    @Override
    protected void onDestroy()
    {
        nsdHelper.tearDown();
        super.onDestroy();
    }
}
