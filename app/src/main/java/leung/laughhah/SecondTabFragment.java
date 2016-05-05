package leung.laughhah;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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


public class SecondTabFragment extends Fragment {
    private ListView secondListView;
    private News news_2;
    public static List<News> newsDataList_2 = new ArrayList<News>();
    private NewsListViewAdapter newsListViewAdapter_2;
    protected boolean isVisible;
    private int fragment_id = 2;
    private int currentPage = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("leungadd", "secondtabfragment oncreateview");
        View rootView = inflater.inflate(R.layout.fragment_second, container, false);
        newsListViewAdapter_2 = new NewsListViewAdapter(getContext(), newsDataList_2, R.layout.news_list_item);
        secondListView = (ListView) rootView.findViewById(R.id.frame_listview_news_2);
        secondListView.setAdapter(newsListViewAdapter_2);
        secondListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("leungadd", "secondtab onitemclick position=" +position+1);
                Intent intent = new Intent(view.getContext(),
                        NewsDetail.class);
                intent.putExtra("news_id", position+1);
                intent.putExtra("fragment_id", fragment_id);
                view.getContext().startActivity(intent);
            }
        });
        secondListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                switch (i) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (secondListView.getLastVisiblePosition() == (secondListView
                                .getCount() - 1)) {
                            //Log.d("leungadd", "secondlistview 滑倒底部");
                            currentPage++;
                            lazyLoad();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

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
        Log.d("leungadd", "in secondtab lazyload");
        new SecondTabFragment.RequestHistoryJokeTask().execute("1111");
    }

    @Override
    public void onDestroy() {
        Log.e("leungadd", "SecondFragment onDestroy");
        newsDataList_2.clear();
        super.onDestroy();
    }

    public class RequestHistoryJokeTask extends AsyncTask<String, Void, JSONObject> {
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
                getRequest1();
                return afterReplaceObject;
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }


        protected void onPostExecute(org.json.JSONObject result) {
            Log.d("leungadd", "onpostexcute");
            String revertReplaceString = "";
            boolean noUpdate = false;
            try {
                if (afterReplaceObject != null) {
                    revertReplaceString = afterReplaceObject.toString().replace(".5.5", "\\r\\n");
                    revertReplaceString = revertReplaceString.replaceAll("\\s\\s", "\\\\r\\\\n");
                    finalJsonObject = new org.json.JSONObject(revertReplaceString);
                    JSONArray jsonArray = finalJsonObject.optJSONArray("data");
                    if (newsDataList_2.size() > 0 &&
                            jsonArray.getJSONObject(0).optString("updatetime").equals(newsDataList_2.get(0).getPubDate())) {
                        noUpdate = true;
                        Log.d("leungadd", "there is no update");
                        //Toast.makeText(getContext(), R.string.already_newest, Toast.LENGTH_SHORT).show();
                    } else {
                        //newsDataList_2.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String content = jsonObject.optString("content");
                            String updateTime = jsonObject.optString("updatetime");
                            Log.d("leungadd jsonobject ", jsonObject.toString());
                            news_2 = new News(content.substring(0, 20), updateTime, content);
                            newsDataList_2.add(news_2);
                            // data += "Node"+i+" : \n content= "+ content +" \n updatetime= "+ updateTime +" \n ";
                        }
                    }
                }
                newsListViewAdapter_2.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("leungadd", "getrequest2 exception " + e.toString());
            }
        }

        //2.按更新时间查询笑话
        public void getRequest1() {
            Log.d("leungadd", "we are in getrequest1");
            String result = null;
            String data = "";
            String afterReplaceString = "";
            String url = "http://japi.juhe.cn/joke/content/list.from";//请求接口地址
            Map params = new HashMap();//请求参数
            params.put("sort", "desc");//类型，desc:指定时间之前发布的，asc:指定时间之后发布的
            params.put("page", currentPage);//当前页数,默认1
            params.put("pagesize", "20");//每次返回条数,默认1,最大20
            params.put("time", new Date().getTime() / 1000);//时间戳（10位），如：1418816972
            params.put("key", APPKEY);//您申请的key

            try {
                result = net(url, params, "GET");
                myObject = new org.json.JSONObject(result);
                if (myObject.getInt("error_code") == 0) {
                    System.out.println(myObject.get("result"));
                    afterReplaceString = myObject.get("result").toString().replace("\\r\\n", ".5.5");
                    afterReplaceObject = new org.json.JSONObject(afterReplaceString);
                } else {
                    System.out.println(myObject.get("error_code") + ":" + myObject.get("reason"));
                    Log.d("leungadd", myObject.get("reason").toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("leungadd", "getrequest2 exception " + e.toString());
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
