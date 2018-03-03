package org.netzd.hilosdip.webservices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Alumno12 on 24/02/18.
 */

public class Petition {
    public static final int MODE_POST = 1;
    public static final int MODE_GET = 2;

    private Entity entity;
    private Object beans;
    private HashMap<String, Object> params=null;
    private List<Object> paramFriendly=null;
    private int mode = 0;
    private int timeConnection = 3000;
    private String tag=null;
    private boolean isArrayList=false;

    public Petition(Entity entity) {
        this.entity = entity;
        this.params = new LinkedHashMap<String, Object>();
        this.paramFriendly=new ArrayList<Object>();
    }

    public void setBeans(Object beans) {
        this.beans = beans;
    }

    public Object getBeans() {
        return this.beans;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void addParam(String key, Object value) {
        this.params.put(key, value);
    }

    public String getParamsGet() {
        StringBuilder paramsGet = null;
        if (this.params.size() != 0) {
            paramsGet = new StringBuilder("?");
            if (this.params.size() == 1) {
                for (String key : this.params.keySet()) {
                    paramsGet.append(key);
                    paramsGet.append("=");
                    paramsGet.append(this.params.get(key));
                }
            } else {
                for (String key : this.params.keySet()) {
                    paramsGet.append(key);
                    paramsGet.append("=");
                    paramsGet.append(this.params.get(key));
                    paramsGet.append("&");
                }
                paramsGet.deleteCharAt(paramsGet.length() - 1);
            }
            return paramsGet.toString();
        } else {
            return null;
        }
    }

    public String getParamsPost() {
        JSONObject paramsPostJsonObject = null;
        try {
            if (this.params.size() != 0) {
                paramsPostJsonObject = new JSONObject();
                for (String key : this.params.keySet()) {
                    paramsPostJsonObject.put(key, this.params.get(key));
                }
                return paramsPostJsonObject.toString();
            } else {
                return null;
            }
        }catch (JSONException e){
            return null;
        }
    }

    public void addParamFriendly(Object param){
        this.paramFriendly.add(param);
    }

    public String getParamFriendly(){
        if(!this.paramFriendly.isEmpty()){
            StringBuilder params=new StringBuilder();
            if(this.paramFriendly.size()==1){
                params.append(this.paramFriendly.get(0));
            }else{
                for(int indice=0;indice<this.paramFriendly.size();indice++){
                    if(indice!=(this.paramFriendly.size()-1)){
                        params.append(this.paramFriendly.get(indice));
                        params.append("/");
                    }else{
                        params.append(this.paramFriendly.get(indice));
                    }
                }
            }
            return params.toString();
        }
        return null;
    }

    public void setModePetitition(int mode) {
        this.mode = mode;
    }

    public void setTimeConnection(int timeConnection) {
        this.timeConnection = timeConnection;
    }

    public int getTimeConnection() {
        return timeConnection;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isArrayList() {
        return isArrayList;
    }

    public void setArrayList(boolean arrayList) {
        isArrayList = arrayList;
    }
}