package com.example.mkash32.lyricfinder.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Aakash on 13/12/16.
 */

public class LyricsWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new LyricsViewsFactory(this);
    }
}
