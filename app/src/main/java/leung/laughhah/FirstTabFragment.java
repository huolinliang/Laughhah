package leung.laughhah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;


public class FirstTabFragment extends Fragment {
    private PullToRefreshListView pullToRefreshListView;
    private News news;
    public static List<News> newsDataList = new ArrayList<News>();
    private NewsListViewAdapter newsListViewAdapter;
    protected FragmentActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);
        this.initNewsData();
        this.testLoadNewsData();
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (FragmentActivity) activity;
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
        Log.w("当前newsDataList中新闻数量为", String.valueOf(newsDataList.size()));
        for (int i = 1; i <= newsDataList.size(); i++) {
            News news = newsDataList.get(i - 1);
            Log.i("第" + i + "条新闻标题", news.getTitle());
            Log.i("第" + i + "条新闻发表日期", news.getPubDate());
            Log.i("第" + i + "条新闻内容", news.getBody());
        }
    }
}
