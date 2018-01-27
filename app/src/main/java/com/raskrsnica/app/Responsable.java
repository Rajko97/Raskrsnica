package com.raskrsnica.app;

import com.android..volley.VolleyError;
import org.json.JSONObject;

/**
 * Created by Milan on 27-Jan-18.
 */

public interface Responsable {
    public void successResponse(res:JSONObject);
    public void errorResponse(err: JSONObject);
}
