package giovanni.tradingtoolkit.main;

import android.app.Dialog;
import android.content.Context;

import giovanni.tradingtoolkit.R;

/**
 * Created by giiio on 17/12/2017.
 */

public class ProgressDialogManager {

    private static Dialog progressDialog;

    public static void open(Context c) {
            progressDialog = new Dialog(c); // Context, this, etc.
            progressDialog.setContentView(R.layout.progress_bar);
            progressDialog.setCancelable(false);
            progressDialog.show();
    }

    public static void close() {
        progressDialog.cancel();
    }

}
