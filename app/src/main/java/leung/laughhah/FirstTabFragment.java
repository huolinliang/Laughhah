package leung.laughhah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import net.sf.json.JSONObject;


import org.json.JSONArray;

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


public class FirstTabFragment extends Fragment {
    private PullToRefreshListView pullToRefreshListView;
    private News news;
    public static List<News> newsDataList = new ArrayList<News>();
    private NewsListViewAdapter newsListViewAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("leungadd", "firsttabfragment oncreateview");
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);
        /*if(newsDataList.size() == 0) {
            this.initNewsData();
            this.testLoadNewsData();
        }*/
        new RequestNewJokeTask().execute("1111");

        newsListViewAdapter = new NewsListViewAdapter(getContext(), newsDataList,R.layout.news_list_item);
        pullToRefreshListView = (PullToRefreshListView)rootView.findViewById(R.id.frame_listview_news);
        pullToRefreshListView.setAdapter(newsListViewAdapter);
        pullToRefreshListView
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent intent = new Intent(view.getContext(),
                                NewsDetail.class);
                        intent.putExtra("news_id", position);
                        view.getContext().startActivity(intent);
                    }
                });
        return rootView;
    }

    @Override
    public void onDestroy() {
        Log.e("leungadd", "FistFragment onDestroy");
        newsDataList.clear();
        super.onDestroy();
    }


    /**
     * 初始化新闻内容(插入20条新闻测试数据)
     */
    private void initNewsData() {
        for (int i = 1; i <= 20; i++) {
            news = new News("简单的新闻列表事例标题" + i, "2014-1-" + i, "新闻内容" + i);
            newsDataList.add(news);
        }
    }
    /**
     * 测试读取新闻列表项中的数据
     */
    private void testLoadNewsData() {
        Log.d("当前DataList中数量为", String.valueOf(newsDataList.size()));
        for (int i = 1; i <= newsDataList.size(); i++) {
            News news = newsDataList.get(i - 1);
            Log.i("第" + i + "条新闻标题", news.getTitle());
            Log.i("第" + i + "条新闻发表日期", news.getPubDate());
            Log.i("第" + i + "条新闻内容", news.getBody());
        }
    }

    public class RequestNewJokeTask extends AsyncTask<String, Void, org.json.JSONObject> {
        private Exception exception;
        public static final String DEF_CHATSET = "UTF-8";
        public static final int DEF_CONN_TIMEOUT = 30000;
        public static final int DEF_READ_TIMEOUT = 30000;
        public static final String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/29.0.1547.66 Safari/537.36";
        //配置您申请的KEY
        public static final String APPKEY ="5c6a5e034defb873b5d4971ba36cfdb4";
        public  org.json.JSONObject myObject = null;
        public org.json.JSONObject afterReplaceObject = null;


        protected org.json.JSONObject doInBackground(String... urls) {
            try {
                getRequest2();
                return afterReplaceObject;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }


        protected void onPostExecute(org.json.JSONObject result) {
            Log.d("leungadd", "onpostexcute");
            try {
                if (afterReplaceObject != null) {
                    JSONArray jsonArray = afterReplaceObject.optJSONArray("data");
                    for(int i=0; i < jsonArray.length(); i++){
                        org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String content = jsonObject.optString("content");
                        String updateTime = jsonObject.optString("updatetime");
                        Log.d("leungadd jsonobject ", jsonObject.toString());
                        news = new News( content.substring(0,20), updateTime, content);
                        newsDataList.add(news);
                       // data += "Node"+i+" : \n content= "+ content +" \n updatetime= "+ updateTime +" \n ";
                    }
                }
                newsListViewAdapter.notifyDataSetChanged();
            }catch (Exception e) {
                    e.printStackTrace();
                    Log.d("leungadd", "getrequest2 exception " +e.toString());
                }


            //execute.setEnabled(true);
        }

        //.最新笑话
        public void getRequest2(){
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
                myObject = new org.json.JSONObject(result);
                if(myObject.getInt("error_code")==0){
                    System.out.println(myObject.get("result"));
                    afterReplaceString = myObject.get("result").toString().replace("\\r\\n",".5.5");
                    afterReplaceObject = new org.json.JSONObject(afterReplaceString);
                    JSONArray jsonArray = afterReplaceObject.optJSONArray("data");
                    for(int i=0; i < jsonArray.length(); i++){
                        org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);


                        String content = jsonObject.optString("content");
                        String updateTime = jsonObject.optString("updatetime");
                        Log.d("leungadd jsonobject222 ", jsonObject.toString());

                        data += "Node"+i+" : \n content= "+ content +" \n updatetime= "+ updateTime +" \n ";
                    }
                    //Log.d("leungadd111", afterReplaceObject.get("result").toString());
                   // Log.d("leungadd", data);
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
        public String net(String strUrl, Map params,String method) throws Exception {
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
        public String urlencode(Map<String,Object>data) {
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


}
