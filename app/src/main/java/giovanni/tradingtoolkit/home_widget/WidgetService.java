package giovanni.tradingtoolkit.home_widget;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService
{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        Log.e("REMOTEVIEWSERVICE", "onGetViewFactory: ");
        return (new WidgetRemoteViewsFactory(this.getApplicationContext(), intent));
    }
}