package giovanni.tradingtoolkit.main;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * Created by giiio on 17/12/2017.
 */

public class DrawableLoader {

    public static Drawable getDrawable(Context c, String name) {
        int resourceId;
        Drawable d = null;
        Context context = c;

        resourceId = context.getResources().getIdentifier(name, "drawable", c.getPackageName());

        try {
            d = context.getResources().getDrawable(resourceId);
        } catch (Resources.NotFoundException e) {
            Log.e("ERROR", "Drawable not found");
        }

        return d;
    }
}
