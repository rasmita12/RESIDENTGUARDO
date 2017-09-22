package android.stalwartgroup.residentguardo.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.stalwartgroup.residentguardo.Interface.OTPListener;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by mobileapplication on 9/22/17.
 */

public class OtpReader extends BroadcastReceiver {
    /**
     * Constant TAG for logging key.
     */
    private static final String TAG = "OtpReader";

    /**
     * The bound OTP Listener that will be trigerred on receiving message.
     */
    private static OTPListener otpListener;

    /**
     * The Sender number string.
     */
    private static String receiverString;

    /**
     * Binds the sender string and listener for callback.
     *
     * @param listener
     */
  /*  public static void bind(OTPListener listener, String sender) {
        otpListener = listener;
        receiverString = sender;
    }*/
    public static void bind(OTPListener listener) {
        otpListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {

            final Object[] pdusArr = (Object[]) bundle.get("pdus");

            for (int i = 0; i < pdusArr.length; i++) {

                SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusArr[i]);
                String senderNum = currentMessage.getDisplayOriginatingAddress();
                String message = currentMessage.getDisplayMessageBody();
                Log.i(TAG, "senderNum: " + senderNum + " message: " + message);

                if (!TextUtils.isEmpty("HP-STLWRT") && senderNum.contains("HP-STLWRT")||
                        !TextUtils.isEmpty("MM-STLWRT") && senderNum.contains("MM-STLWRT")) { //If message received is from required number.
                    //If bound a listener interface, callback the overriden method.
                    if (otpListener != null) {
                        otpListener.otpReceived(message);
                    }
                }
            }
        }
    }
}