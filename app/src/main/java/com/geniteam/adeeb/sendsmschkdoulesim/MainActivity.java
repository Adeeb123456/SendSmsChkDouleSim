package com.geniteam.adeeb.sendsmschkdoulesim;

import android.app.PendingIntent;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getSubscriptionID();
        }
        Telephony telephony = Telephony.getInstance(getApplicationContext());
        for(Sim sim : telephony.getAllSim()){
      Log.d("debug",sim.getSlotId()+"d");
            Log.d("debug","network "+sim.getNetwork());;
         //  sim.sendSMS(getApplicationContext(), "03101783575", "Hello World!");

        }
sendSMS("03101883575","cxcx");

    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void sendSmsOnSingleSim(int subscriptionId,String destinationAddress,String scAddress,String text,PendingIntent pisend,PendingIntent deliveryIntent){
        SmsManager.getSmsManagerForSubscriptionId(subscriptionId).sendTextMessage( destinationAddress, scAddress,  text,pisend,  deliveryIntent);

    }
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
public  ArrayList<SimInfo> getSubscriptionID(){
        SimInfo simInfo;
        ArrayList<SimInfo> simInfosList=new ArrayList<>();
    SubscriptionManager subscriptionManager = SubscriptionManager.from(getApplicationContext());
    List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
    for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
        simInfo=new SimInfo();
        int subscriptionId = subscriptionInfo.getSubscriptionId();
        simInfo.subscriptionId=subscriptionId;
       CharSequence displayname=  subscriptionInfo.getDisplayName();
      CharSequence careername= subscriptionInfo.getCarrierName();
    String country=  subscriptionInfo.getCountryIso();

    simInfo.career_name=careername.toString();
    simInfo.countryIso=country;
    simInfo.display_name=displayname.toString();

        Log.d("debug","subscriptionId:"+subscriptionId);
        Log.d("debug","sim :"+displayname);
        Log.d("debug","sim :"+careername);
        simInfosList.add(simInfo);
    }
    return simInfosList;
}





   public void getSimInfoBelowLollypop(){
       TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);

       String imeiSIM1 = telephonyInfo.getImsiSIM1();
       String imeiSIM2 = telephonyInfo.getImsiSIM2();

       boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
       boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

       boolean isDualSIM = telephonyInfo.isDualSIM();


       Log.d("debug" , " IME1 : " + imeiSIM1 + "\n" +
               " IME2 : " + imeiSIM2 + "\n" +
               " IS DUAL SIM : " + isDualSIM + "\n" +
               " IS SIM1 READY : " + isSIM1Ready + "\n" +
               " IS SIM2 READY : " + isSIM2Ready + "\n");
   }








    public static boolean sendSMS(Context ctx, int simID, String toNum, String centerNum, String smsText, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        String name;

        try {
            if (simID == 0) {
                name = "isms";
                // for model : "Philips T939" name = "isms0"
            } else if (simID == 1) {
                name = "isms2";
            } else {
                throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
            }
            Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
            method.setAccessible(true);
            Object param = method.invoke(null, name);

            method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", IBinder.class);
            method.setAccessible(true);
            Object stubObj = method.invoke(null, param);
            if (Build.VERSION.SDK_INT < 18) {
                method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
                method.invoke(stubObj, toNum, centerNum, smsText, sentIntent, deliveryIntent);
            } else {
                method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
                method.invoke(stubObj, ctx.getPackageName(), toNum, centerNum, smsText, sentIntent, deliveryIntent);
            }

            return true;
        } catch (ClassNotFoundException e) {
            Log.e("apipas", "ClassNotFoundException:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.e("apipas", "NoSuchMethodException:" + e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e("apipas", "InvocationTargetException:" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("apipas", "IllegalAccessException:" + e.getMessage());
        } catch (Exception e) {
            Log.e("apipas", "Exception:" + e.getMessage());
        }
        return false;
    }


    public static boolean sendMultipartTextSMS(Context ctx, int simID, String toNum, String centerNum, ArrayList<String> smsTextlist, ArrayList<PendingIntent> sentIntentList, ArrayList<PendingIntent> deliveryIntentList) {
        String name;
        try {
            if (simID == 0) {
                name = "isms";
                // for model : "Philips T939" name = "isms0"
            } else if (simID == 1) {
                name = "isms2";
            } else {
                throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
            }
            Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
            method.setAccessible(true);
            Object param = method.invoke(null, name);

            method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", IBinder.class);
            method.setAccessible(true);
            Object stubObj = method.invoke(null, param);
            if (Build.VERSION.SDK_INT < 18) {
                method = stubObj.getClass().getMethod("sendMultipartText", String.class, String.class, List.class, List.class, List.class);
                method.invoke(stubObj, toNum, centerNum, smsTextlist, sentIntentList, deliveryIntentList);
            } else {
                method = stubObj.getClass().getMethod("sendMultipartText", String.class, String.class, String.class, List.class, List.class, List.class);
                method.invoke(stubObj, ctx.getPackageName(), toNum, centerNum, smsTextlist, sentIntentList, deliveryIntentList);
            }
            return true;
        } catch (ClassNotFoundException e) {
            Log.e("apipas", "ClassNotFoundException:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.e("apipas", "NoSuchMethodException:" + e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e("apipas", "InvocationTargetException:" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("apipas", "IllegalAccessException:" + e.getMessage());
        } catch (Exception e) {
            Log.e("apipas", "Exception:" + e.getMessage());
        }
        return false;
    }


    public static List<SimInfo> getSIMInfo(Context context) {
        List<SimInfo> simInfoList = new ArrayList<>();
        Uri URI_TELEPHONY = Uri.parse("content://telephony/siminfo/");
        Cursor c = context.getContentResolver().query(URI_TELEPHONY, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(c.getColumnIndex("_id"));
               int slot = c.getInt(c.getColumnIndex("slot"));
                String display_name = c.getString(c.getColumnIndex("display_name"));
                String icc_id = c.getString(c.getColumnIndex("icc_id"));
                SimInfo simInfo = new SimInfo(id, display_name, icc_id, slot);
                Log.d("debug", simInfo.toString());
                simInfoList.add(simInfo);
            } while (c.moveToNext());
        }
        c.close();

        return simInfoList;
    }
}
