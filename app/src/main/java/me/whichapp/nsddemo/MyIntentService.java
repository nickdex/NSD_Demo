package me.whichapp.nsddemo;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class MyIntentService extends Service
{
    public static final String TAG = "MyIntentService";

    private static final String ACTION_INIT_NSD = "me.whichapp.nsddemo.action.initialiseNSD";
    private static final String ACTION_UPDATE = "me.whichapp.nsddemo.action.UPDATE";
    private static final String ACTION_DISCOVER = "me.whichapp.nsddemo.action.DISCOVER";
    private static final String ACTION_REGISTER = "me.whichapp.nsddemo.action.REGISTER";


    public static final String BROADCAST_ACTION = "me.whichapp.nsddemo.BROADCAST";
    public static final String LIST = "me.whichapp.nsddemo.LIST";

    private NSDHelper nsdHelper;
    private final int port = 9346;

    @Override
    public void onCreate()
    {
        super.onCreate();
        initialiseNSD();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        handleIntent(intent);
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
        nsdHelper.tearDown();
        super.onDestroy();
    }

    public static void initialiseNSD(Context context)
    {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_INIT_NSD);
        context.startService(intent);
    }


    public static void registerService(Context context)
    {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_REGISTER);
        context.startService(intent);
    }

    public static void discover(Context context)
    {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_DISCOVER);
        context.startService(intent);
    }

    public static void requestUpdate(Context context)
    {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_UPDATE);
        context.startService(intent);
    }

    protected void handleIntent(Intent intent)
    {
        if (intent != null)
        {
            final String action = intent.getAction();
            switch (action)
            {
                case ACTION_INIT_NSD:
                    initialiseNSD();
                    break;
                case ACTION_REGISTER:
                    registerService();
                    break;
                case ACTION_DISCOVER:
                    discoverServices();
                    break;
                case ACTION_UPDATE:
                    broadcastList();
                    break;
            }
        }
    }

    private void initialiseNSD()
    {
        nsdHelper = new NSDHelper(this);
        nsdHelper.initializeNsd();
        Log.v(TAG, "NSDHelper initialised");
    }

    private void registerService()
    {
        nsdHelper.registerService(port);
        Log.d(TAG, "Service registered on " + port);
    }

    private void discoverServices()
    {
        nsdHelper.discoverServices();
    }


    private void broadcastList()
    {
        if (nsdHelper == null)
        {
            Log.e(TAG, "NSD Helper not initialised");
        } else
        {
            Intent localIntent = new Intent(BROADCAST_ACTION)
                    .putStringArrayListExtra(LIST, nsdHelper.getList());

            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
            Log.d(TAG, "List with updated names sent");
        }

    }
}
