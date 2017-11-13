package lk.prasad.crop.common;

import android.app.Activity;
import android.telephony.TelephonyManager;

/**
 * Created by prasad on 3/28/16.
 */
public class ProfileData extends Activity {
    public static String phoneId;



    public static String getPhoneID(TelephonyManager telephonyManager){
        phoneId = telephonyManager.getDeviceId();
        if (phoneId != null){
            return phoneId;
        } else{
            phoneId = telephonyManager.getSimSerialNumber();
            return phoneId;
        }
    }
}
