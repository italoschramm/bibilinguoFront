package br.com.italoschramm.bibilinguo.client;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import br.com.italoschramm.bibilinguo.config.ServerConfig;
import br.com.italoschramm.bibilinguo.util.Erro;

public class RequestClient {

    public Erro erros = new Erro();
    private WeakReference<RequestClientInter> mCallBack;
    private JSONObject jsonResponse;

    public RequestClient(RequestClientInter callback) {
        this.mCallBack = new WeakReference<>(callback);
    }

    public void requestPost(String jsonString, String urlPath, final Context context) {
        erros.initialize();
        jsonResponse = new JSONObject();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest objectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    urlPath,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            final RequestClientInter callBack = mCallBack.get();
                            if (callBack != null) {
                                callBack.onTaskDone(response);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            setErros(error);

                            final RequestClientInter callBack = mCallBack.get();
                            if (callBack != null) {
                                callBack.onTaskDone(jsonResponse);
                            }

                        }
                    });

            requestQueue.add(objectRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void requestGetString(String urlPath, final Context context) {
        erros.initialize();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlPath,
                new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            final RequestClientInter callBack = mCallBack.get();
                            if (callBack != null) {
                                callBack.onTaskDone(response);
                            }
                        }
                        },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            setErros(error);

                            final RequestClientInter callBack = mCallBack.get();
                            if (callBack != null) {
                                callBack.onTaskDone("");
                            }

                        }
                    });
            requestQueue.add(stringRequest);
    }

    private void setErros(VolleyError error) {
        if (error.networkResponse == null) {
            erros.setErro("Não foi possível conectar ao servidor. Contate o suporte!", 0);
            Log.d(ServerConfig.TAG, "Erro de conexão");
            return;
        }

        if (error.networkResponse.statusCode == 409) {
            erros.setErro("E-mail já cadastrado", 0);
            Log.d(ServerConfig.TAG, "Usuário já existe");
        } else if (error.networkResponse.statusCode == 403) {
            erros.setErro("Acesso negado", 0);
            Log.d(ServerConfig.TAG, "Acesso negado");
        } else
            Log.d(ServerConfig.TAG, String.valueOf(error.networkResponse.statusCode));

        }
}
