package me.whichapp.nsddemo;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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


    private List<String> list = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceName = (TextView) findViewById(R.id.device_name);
        deviceList = (ListView) findViewById(R.id.listView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED)
            {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, RQ_PERM);
            } else
            {
                flag = true;
            }
        } else
        {
            flag = true;
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
                    flag = true;
                }
        }
    }

    public void advertise(View v)
    {
        // Register service
        if (flag)
            MyIntentService.registerService(this);
    }

    public void connect(View v)
    {
        MyIntentService.requestUpdate(this);
    }


    private class ResponseReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            ArrayList<String> newList = intent.getStringArrayListExtra(MyIntentService.LIST);
            list.clear();
            list.addAll(newList);
            adapter.notifyDataSetChanged();
        }
    }
}
