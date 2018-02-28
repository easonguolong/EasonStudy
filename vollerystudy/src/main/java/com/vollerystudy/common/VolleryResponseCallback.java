package com.vollerystudy.common;

import com.android.volley.VolleyError;

/**
 * Created by Administrator on 2017/6/29.
 */

public interface VolleryResponseCallback {
    void onSuccess(String s);
    void onError(VolleyError error);
}
