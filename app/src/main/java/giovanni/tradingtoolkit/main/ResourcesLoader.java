package giovanni.tradingtoolkit.main;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

/**
 * Created by giiio on 17/12/2017.
 */

public class ResourcesLoader {

    public static Drawable getDrawable(Context context, String name) throws Resources.NotFoundException {
        int resourceId;
        Drawable drawable;

        resourceId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        drawable = context.getResources().getDrawable(resourceId);

        return drawable;
    }

    public static Drawable getDrawableFromId(Context context, int resId) throws Resources.NotFoundException {
        Drawable drawable;

        drawable = context.getResources().getDrawable(resId);
        return drawable;
    }

    public static int getColorFromId(Context context, int resId) {
        return context.getResources().getColor(resId);
    }
}
