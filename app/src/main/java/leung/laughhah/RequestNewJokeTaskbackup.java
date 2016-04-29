package leung.laughhah;

import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Leung on 2016/4/29.
 */
public class RequestNewJokeTaskbackup extends AsyncTask<String, Void, Object> {
    private Exception exception;
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/29.0.1547.66 Safari/537.36";
    //配置您申请的KEY
    public static final String APPKEY ="5c6a5e034defb873b5d4971ba36cfdb4";
    public static JSONObject myObject = null;


    protected Object doInBackground(String... urls) {
        try {
            getRequest2();
            return myObject;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }
    //.最新笑话
    public static void getRequest2(){
        Log.d("leungadd", "we are in getrequest2");
        String result = null;
        String data = "";
        String afterReplaceString = "";
        String url ="http://japi.juhe.cn/joke/content/text.from";//请求接口地址
        Map params = new HashMap();//请求参数
        params.put("page","1");//当前页数,默认1
        params.put("pagesize","20");//每次返回条数,默认1,最大20
        params.put("key",APPKEY);//您申请的key

        try {
            result =net(url, params, "GET");
            myObject = new JSONObject(result);
            if(myObject.getInt("error_code")==0){
                System.out.println(myObject.get("result"));
                afterReplaceString = myObject.get("result").toString().replace("\\r\\n",".5.5");
                JSONObject tmpObject = new JSONObject(afterReplaceString);
                JSONArray jsonArray = tmpObject.optJSONArray("data");
                for(int i=0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                    String content = jsonObject.optString("content");
                    String updateTime = jsonObject.optString("updatetime");
                    Log.d("leungadd jsonobject ", jsonObject.toString());

                    data += "Node"+i+" : \n content= "+ content +" \n updatetime= "+ updateTime +" \n ";
                }
                Log.d("leungadd", myObject.get("result").toString());
                Log.d("leungadd", data);
            }else{
                System.out.println(myObject.get("error_code")+":"+myObject.get("reason"));
                Log.d("leungadd", myObject.get("reason").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("leungadd", "getrequest2 exception " +e.toString());
        }
    }

    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
