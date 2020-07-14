package giovanni.tradingtoolkit.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

    public static void makeAlert(Context context, String title, String msg) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
//                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

}
