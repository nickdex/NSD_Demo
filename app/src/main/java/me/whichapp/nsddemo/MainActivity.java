package me.whichapp.nsddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
{

    TextView deviceName;

    ListView deviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceName = (TextView) findViewById(R.id.device_name);
        deviceList = (ListView) findViewById(R.id.listView);


    }
}
