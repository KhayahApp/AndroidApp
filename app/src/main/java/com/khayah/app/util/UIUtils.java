package com.khayah.app.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by toe-lie on 6/15/2017.
 */

public class UIUtils {

    public static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
