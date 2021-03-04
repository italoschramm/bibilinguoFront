package br.com.italoschramm.bibilinguo.client;

import org.json.JSONArray;
import org.json.JSONObject;

public interface RequestClientInter {

    void onTaskDone(JSONObject jsonObject);
    void onTaskDone(JSONArray jsonObject);

}
