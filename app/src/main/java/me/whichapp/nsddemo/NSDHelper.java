package me.whichapp.nsddemo;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dexter on 29-Mar-16.
 */
public class NSDHelper
{
    private Context context;
    private String serviceName = "NSDDemo";

    private static final String TAG = "NSDHelper";
    public static final String SERVICE_TYPE = "_http._tcp.";

    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;

    private NsdServiceInfo nsdServiceInfo;

    ArrayList<String> list = new ArrayList<>();

    public ArrayList<String> getList()
    {
        return list;
    }

    public NSDHelper(Context context) {
        this.context = context;
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }
    
    public void registerService(int port) {
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(serviceName);
        serviceInfo.setServiceType(SERVICE_TYPE);

        mNsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);


    }

    public void initializeNsd() {
        initializeResolveListener();
        initializeDiscoveryListener();
        initializeRegistrationListener();

        //mNsdManager.init(mContext.getMainLooper(), this);
    }

    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

                if (serviceInfo.getServiceName().equals(serviceName)) {
                    Log.d(TAG, "Same IP.");
                    return;
                }
                nsdServiceInfo = serviceInfo;
                String id = "Service name : " + nsdServiceInfo.getServiceName() + "\n" + nsdServiceInfo.getHost().getHostName();
                if (!list.contains(id))
                    list.add(id);
            }
        };
    }

    public void initializeDiscoveryListener() {
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service discovery success " + service);
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(serviceName)) {
                    Log.d(TAG, "Same machine: " + serviceName);
                } else if (service.getServiceName().contains(serviceName)){
                    mNsdManager.resolveService(service, mResolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost" + service);
                if (nsdServiceInfo == service) {
                    nsdServiceInfo = null;
                }
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {

            @Override
            public void onServiceRegistered(NsdServiceInfo nsdServiceInfo)
            {
                Log.v(TAG, "Service Registered " + nsdServiceInfo);
                serviceName = nsdServiceInfo.getServiceName();
            }

            @Override
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
                Log.e(TAG, "Service Registration failed " + arg0);
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                Log.v(TAG, "Service unregistered " + arg0);
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Service Unregistration failed " + serviceInfo);
            }

        };
    }

    public void discoverServices() {
        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }

    public void stopDiscovery() {
        mNsdManager.stopServiceDiscovery(mDiscoveryListener);
    }

    public NsdServiceInfo getChosenServiceInfo() {
        return nsdServiceInfo;
    }

    public void tearDown() {
        mNsdManager.unregisterService(mRegistrationListener);
    }
}
