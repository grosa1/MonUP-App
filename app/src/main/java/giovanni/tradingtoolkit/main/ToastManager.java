package giovanni.tradingtoolkit.main;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by giiio on 17/12/2017.
 */

public class ToastManager {

    private static Toast toastMessage;

    public static void create(Context c, String msg) {
        if (toastMessage!= null) {
            toastMessage.cancel();
        }
        toastMessage = Toast.makeText(c, msg, Toast.LENGTH_SHORT);
        toastMessage.show();
    }

    public static void create(Context c, int strResourceId) {
        ToastManager.create(c, c.getResources().getString(strResourceId));
    }

}
