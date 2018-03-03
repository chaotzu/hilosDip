package org.netzd.hilosdip;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Alumno12 on 24/02/18.
 */


//Saber si el dispositivo esta o no conectado a internet
public class NetworkConnection {
    public static boolean isConnectionAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager!=null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo!=null && networkInfo.isConnected() && networkInfo.isAvailable() && networkInfo.isConnectedOrConnecting()){
                return true;
            }
        }
        return false;
    }
}
