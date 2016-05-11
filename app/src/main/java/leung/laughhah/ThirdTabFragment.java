package leung.laughhah;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.koushikdutta.ion.Ion;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ThirdTabFragment extends Fragment {
    private ImgJokes imgJokes;
    public static List<ImgJokes> newsDataList_3 = new ArrayList<ImgJokes>();
    private ImgsListViewAdapter imgsListViewAdapter;
    private PullToRefreshListView thirdListView;
    protected boolean isVisible;
    private static final String TAG = "leungadd";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_third, container, false);
        imgsListViewAdapter = new ImgsListViewAdapter(getContext(), newsDataList_3, R.layout.jokeimgs_list_item);
        thirdListView = (PullToRefreshListView) rootView.findViewById(R.id.frame_listview_news_3);
        thirdListView.setAdapter(imgsListViewAdapter);
        thirdListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(TAG, "thirdtab onitemclick position=" +position);
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View imgEntryView = inflater.inflate(R.layout.dialog_photo_entry, null);
                final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                ZoomImageView img = (ZoomImageView) imgEntryView.findViewById(R.id.dialog_imageview);
                Ion.with(img)
                        .fitCenter()
                        .load(newsDataList_3.get(position-1).getBody());
                dialog.setView(imgEntryView);
                dialog.show();

            }
        });
        thirdListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "thirdtab onrefresh");
                new ThirdTabFragment.RequestNewGifTask().execute("1111");
            }
        });
        return rootView;
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    //懒加载
    protected void lazyLoad() {
        Log.d(TAG, "in thirdtab lazyload");
        new ThirdTabFragment.RequestNewGifTask().execute("1111");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "ThirdFragment onDestroy");
        newsDataList_3.clear();
        super.onDestroy();
    }

    public class RequestNewGifTask extends AsyncTask<String, Void, JSONObject> {
        private Exception exception;
        public static final String DEF_CHATSET = "UTF-8";
        public static final int DEF_CONN_TIMEOUT = 30000;
        public static final int DEF_READ_TIMEOUT = 30000;
        public static final String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/29.0.1547.66 Safari/537.36";
        //配置您申请的KEY
        public static final String APPKEY = "5c6a5e034defb873b5d4971ba36cfdb4";
        public org.json.JSONObject myObject = null;
        public org.json.JSONObject afterReplaceObject = null;
        public org.json.JSONObject finalJsonObject = null;


        protected org.json.JSONObject doInBackground(String... urls) {
            try {
                getRequest4();
                return afterReplaceObject;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }


        protected void onPostExecute(org.json.JSONObject result) {
            String revertReplaceString = "";
            boolean noUpdate = false;
            try {
                if (afterReplaceObject != null) {
                    revertReplaceString = afterReplaceObject.toString().replace(".5.5", "\\r\\n");
                    revertReplaceString = revertReplaceString.replaceAll("\\s\\s", "\\\\r\\\\n");
                    finalJsonObject = new org.json.JSONObject(revertReplaceString);
                    JSONArray jsonArray = finalJsonObject.optJSONArray("data");
                    if (newsDataList_3.size() > 0 &&
                            jsonArray.getJSONObject(0).optString("updatetime").equals(newsDataList_3.get(0).getPubDate())) {
                        noUpdate = true;
                        Log.d(TAG, "there is no update");
                        //Toast.makeText(getContext(), R.string.already_newest, Toast.LENGTH_SHORT).show();
                    } else {
                        //newsDataList_2.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String title = jsonObject.optString("content");
                            String updateTime = jsonObject.optString("updatetime");
                            String imgUrl = jsonObject.optString("url");
                            imgJokes = new ImgJokes(title, updateTime, imgUrl);
                            newsDataList_3.add(imgJokes);
                        }
                    }
                }
                imgsListViewAdapter.notifyDataSetChanged();
                thirdListView.onRefreshComplete();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "getrequest2 exception " + e.toString());
            }
        }

        //4.最新趣图
        public void getRequest4(){
            String result =null;
            String url ="http://japi.juhe.cn/joke/img/text.from";//请求接口地址
            Map params = new HashMap();//请求参数
            params.put("page","1");//当前页数,默认1
            params.put("pagesize","20");//每次返回条数,默认1,最大20
            params.put("key",APPKEY);//您申请的key
            String afterReplaceString = "";

            try {
                result = net(url, params, "GET");
                myObject = new org.json.JSONObject(result);
                if (myObject.getInt("error_code") == 0) {
                    System.out.println(myObject.get("result"));
                    afterReplaceString = myObject.get("result").toString().replace("\\r\\n", ".5.5");
                    afterReplaceObject = new org.json.JSONObject(afterReplaceString);
                } else {
                    System.out.println(myObject.get("error_code") + ":" + myObject.get("reason"));
                    Log.d(TAG, myObject.get("reason").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "getrequest2 exception " + e.toString());
            }
        }


        /**
         * @param strUrl 请求地址
         * @param params 请求参数
         * @param method 请求方法
         * @return 网络请求字符串
         * @throws Exception
         */
        public String net(String strUrl, Map params, String method) throws Exception {
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            String rs = null;
            try {
                StringBuffer sb = new StringBuffer();
                if (method == null || method.equals("GET")) {
                    strUrl = strUrl + "?" + urlencode(params);
                }
                URL url = new URL(strUrl);
                conn = (HttpURLConnection) url.openConnection();
                if (method == null || method.equals("GET")) {
                    conn.setRequestMethod("GET");
                } else {
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                }
                conn.setRequestProperty("User-agent", userAgent);
                conn.setUseCaches(false);
                conn.setConnectTimeout(DEF_CONN_TIMEOUT);
                conn.setReadTimeout(DEF_READ_TIMEOUT);
                conn.setInstanceFollowRedirects(false);
                conn.connect();
                if (params != null && method.equals("POST")) {
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
        public String urlencode(Map<String, Object> data) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry i : data.entrySet()) {
                try {
                    sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

    }
}
