package com.blueocean.stare_app;

import android.content.Context;

/**
 * Created by BlueOcean_hua
 * Date 2017/6/10
 * Nicename 蓝色海洋
 * Desc 分享犹如大海，互联你我他
 */

public class SizeUtils {


    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
}
