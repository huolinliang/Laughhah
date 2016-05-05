package leung.laughhah;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class NewsDetail extends Activity {


	private TextView news_detail_title;
	private TextView news_detail_author;
	private TextView news_detail_date;
	private TextView news_detail_commentcount;
	private TextView news_detail_body;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		this.initView();
		this.initData();
		//this.testNewsData();
	}


	private void initData() {

		Intent intent = getIntent();
		intent.getExtras();
		Bundle data = intent.getExtras();
		int position = data.getInt("news_id");
		int fragment_id = data.getInt("fragment_id");
		Log.d("leungadd 接收到的数据", String.valueOf(position));
		Log.d("leungadd", "接收到的fragment id=" + String.valueOf(fragment_id));

		if(fragment_id == 1) {
			News news = FirstTabFragment.newsDataList.get(position - 1);
			news_detail_title.setText(news.getTitle());
			//news_detail_author.setText(news.getAuthor());
			news_detail_date.setText(news.getPubDate());
			//news_detail_commentcount.setText(String.valueOf(news.getCommentCount()));
			news_detail_body.setText(news.getBody());
		}else if(fragment_id == 2) {
			News news = SecondTabFragment.newsDataList_2.get(position - 1);
			news_detail_title.setText(news.getTitle());
			news_detail_date.setText(news.getPubDate());
			news_detail_body.setText(news.getBody());
		}

	}


	private void initView() {
		news_detail_title = (TextView) findViewById(R.id.news_detail_title);
		//news_detail_author = (TextView) findViewById(R.id.news_detail_author);
		//news_detail_commentcount = (TextView) findViewById(R.id.news_detail_commentcount);
		news_detail_date = (TextView) findViewById(R.id.news_detail_date);
		news_detail_body = (TextView) findViewById(R.id.news_detail_body);
	}


	private void testNewsData() {

		Intent intent = getIntent();
		intent.getExtras();
		Bundle data = intent.getExtras();
		int position = data.getInt("news_id");
		Log.d("leungadd 测试接点击列表项位置", String.valueOf(position));
		News news = FirstTabFragment.newsDataList.get(position-1);
		Log.d("leungadd标题", news.getTitle());
		//Log.w("作者", news.getAuthor());
		Log.d("leungadd 发表日期", news.getPubDate());
		//Log.w("评论数", String.valueOf(news.getCommentCount()));
		Log.d("leungadd 内容", news.getBody());

	}
}
