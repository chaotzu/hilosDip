package org.netzd.hilosdip.webservices;

import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.netzd.hilosdip.Video;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alumno12 on 24/02/18.
 */

public class JSONParser {
    private HttpURLConnection connection = null;
    public JSONParser(){

    }

    private String getJSONString(String petitionUri, Petition petition){
        InputStream inputStream = null;
        String jsonResult = null;
        try{
            connection=createConnection(petitionUri, petition);
            if(petition.getEntity()!=Entity.POST){
                inputStream=new BufferedInputStream(connection.getInputStream());
                jsonResult=convertInputStreamToString(inputStream);
            }else{
                if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
                    inputStream = new BufferedInputStream(connection.getInputStream());
                    jsonResult=convertInputStreamToStringPost(inputStream);
                }else{
                    jsonResult=convertInputStreamToString(inputStream);
                }
            }
        }catch (Exception e){
            return  null;
        }
        return jsonResult;
    }

    //Parser de json /*Ejemplo usamos OMDb API  http://www.omdbapi.com/?s=superman&apikey=2b28d307&r=json
    public List<Video> getVideos(String uri, Petition petition){
        List<Video> videos = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(getJSONString(uri,petition));
            if(jsonObject != null){
                //Leemos el arreglo de json
                //JSONArray videosJsonArray = new jsonObject.getJSONArray("Search");
                JSONArray videosJsonArray=jsonObject.getJSONArray("Search");
                if(videosJsonArray != null && videosJsonArray.length() > 0){
                    for (int indice = 0; indice<videosJsonArray.length(); indice++) {
                        JSONObject videoJsonObject = videosJsonArray.getJSONObject(indice);
                        videos.add(new Video(videoJsonObject.getString("imdbID"), videoJsonObject.getString("Title"), videoJsonObject.getString("Year"),videoJsonObject.getString("Type"), videoJsonObject.getString("Poster")));
                    }

                }
            }
        }catch (JSONException e){

        }
        return videos;
    }

    public HttpURLConnection createConnection(String petitionUri, Petition petition){
        HttpURLConnection httpURLConnection = null;
        DataOutputStream dataOutputStream = null;

        try{
            switch (petition.getEntity()){
                case POST:
                    URL url = new URL(petitionUri); //Creamos URL bien formada
                    httpURLConnection=(HttpURLConnection) url.openConnection(); //Abrimos conexion
                    httpURLConnection.setInstanceFollowRedirects(false);//Para no hacer redirecciones
                    httpURLConnection.setConnectTimeout(petition.getTimeConnection());//Tiempo que esperara la respuesta del servidor
                    httpURLConnection.setReadTimeout(petition.getTimeConnection());
                    httpURLConnection.setRequestMethod("POST");//Especificamos tipo de metodo
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");//Cabeceras y tipo de peticion
                    httpURLConnection.setRequestProperty("Accept", "application/json");
                    httpURLConnection.setRequestProperty("Charset", "utf-8");
                    httpURLConnection.setRequestProperty("Accept-Encoding", "");
                    httpURLConnection.setDoInput(true);//Banderas
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);

                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream()); //A traves de la conexion, leemos sus bytes de salida ya que el json se arma con bytes
                    String paramsJSON = petition.getParamsPost(); //Devolvemos la cadena del json object
                    dataOutputStream.write(paramsJSON.getBytes("UTF-8")); //Lee los bytes de la cadena pero los codifica en UTF-8
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    break;
                case GET:
                    String uri = petitionUri;
                    if(petition.getParamsGet()!=null)
                        uri+=petition.getParamsGet();
                    URL urlGet = new URL(uri);
                    httpURLConnection=(HttpURLConnection) urlGet.openConnection(); //Abrimos conexion
                    httpURLConnection.setInstanceFollowRedirects(false);//Para no hacer redirecciones
                    httpURLConnection.setConnectTimeout(petition.getTimeConnection());//Tiempo que esperara la respuesta del servidor
                    httpURLConnection.setReadTimeout(petition.getTimeConnection());
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Charset", "utf-8");
                    httpURLConnection.setUseCaches(false);
                    break;
                case FRIENDLY:
                    String uriFriendly = petitionUri;
                    if(petition.getParamFriendly()!=null)
                        uriFriendly+=petition.getParamFriendly();
                    URL urlFriendly = new URL(uriFriendly);
                    httpURLConnection=(HttpURLConnection) urlFriendly.openConnection(); //Abrimos conexion
                    httpURLConnection.setInstanceFollowRedirects(false);//Para no hacer redirecciones
                    httpURLConnection.setConnectTimeout(petition.getTimeConnection());//Tiempo que esperara la respuesta del servidor
                    httpURLConnection.setReadTimeout(petition.getTimeConnection());
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Charset", "utf-8");
                    httpURLConnection.setUseCaches(false);
                    break;
                default:
                    URL urlNone = new URL(petitionUri);
                    httpURLConnection=(HttpURLConnection) urlNone.openConnection(); //Abrimos conexion
                    httpURLConnection.setInstanceFollowRedirects(false);//Para no hacer redirecciones
                    httpURLConnection.setConnectTimeout(petition.getTimeConnection());//Tiempo que esperara la respuesta del servidor
                    httpURLConnection.setReadTimeout(petition.getTimeConnection());
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Charset", "utf-8");
                    httpURLConnection.setUseCaches(false);
                    break;
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;

        }catch (SocketTimeoutException f){
            f.printStackTrace();
            return null;
        }catch (Exception g){
            g.printStackTrace();
            return null;
        }
        return httpURLConnection;
    }

    //Estos dos metodos reciben la respuesta en bytes y lo convierten a un objeto string
    public static String convertInputStreamToString(InputStream inputStream) {
        String line = new String();
        String result = new String();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String convertInputStreamToStringPost(InputStream inputStream) {
        String line = null;
        StringBuffer output = new StringBuffer("");
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

            line = "";
            while ((line = bufferedReader.readLine()) != null)
                output.append(line);

            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();

    }

}
