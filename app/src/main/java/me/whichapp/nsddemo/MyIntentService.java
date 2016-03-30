package me.whichapp.nsddemo;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

public class MyIntentService extends IntentService
{
    public static final String TAG = "MyIntentService";

    private static final String ACTION_INIT_NSD = "me.whichapp.nsddemo.action.initialiseNSD";
    private static final String ACTION_CONNECT = "me.whichapp.nsddemo.action.connect";
    private static final String BROADCAST_ACTION = "me.whichapp.nsddemo.BROADCAST";

    public static final String LIST = "me.whichapp.nsddemo.LIST";


    public MyIntentService()
    {
        super("MyIntentService");
    }

    private NSDHelper nsdHelper;
    private final int port = 9346;


    public static void registerService(Context context)
    {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_INIT_NSD);
        context.startService(intent);
    }

    public static void requestUpdate(Context context)
    {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_CONNECT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            final String action = intent.getAction();
            if (ACTION_INIT_NSD.equals(action))
            {
                initialiseNSD();
            } else if (ACTION_CONNECT.equals(action))
            {
                broadcastList();
            }
        }
    }

    private void initialiseNSD()
    {
        nsdHelper = new NSDHelper(this);
        nsdHelper.initializeNsd();
        Log.v(TAG, "NSDHelper initialised");

        nsdHelper.registerService(port);
        Log.d(TAG, "Service registered on " + port);
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

            Log.d(TAG, "List with updated names sent");
            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        }

    }
}
