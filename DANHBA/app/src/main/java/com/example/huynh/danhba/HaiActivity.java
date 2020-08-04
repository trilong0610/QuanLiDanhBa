package com.example.huynh.danhba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class HaiActivity extends BroadcastReceiver {
    Context ct1;
    @Override
    public void onReceive(Context context, Intent intent) {
        ct1 = context;
        // lay TELEPHONY MANAGER de dang ki lang nghe su kien
        TelephonyManager tmgr = (TelephonyManager) ct1
                .getSystemService(Context.TELEPHONY_SERVICE);

//Tao Listner
        MyPhoneStateListener PhoneListener = new MyPhoneStateListener();


// Dang ki listener cho LISTEN_CALL_STATE
        tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

    }
    private class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {

            // state = 1 khi chuong reng
            if (state == 1) {

                String msg = " CÓ CUỘC GỌI ĐẾN VỚI SỐ ĐIỆN THOẠI : "+incomingNumber;
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(ct1, msg, duration);
                toast.show();

            }
        }
    }

}
