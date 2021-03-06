package leung.laughhah;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class NewsDetail extends Activity {


	private TextView news_detail_title;
	private TextView news_detail_author;
	private TextView news_detail_date;
	private TextView news_detail_commentcount;
	private TextView news_detail_body;
	private ImageButton copy_button;
    private News news;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		this.initView();
		this.initData();
        copy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager cmb = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(news.getBody().trim());
                Toast.makeText(getApplicationContext(), R.string.copy_content, Toast.LENGTH_SHORT).show();
            }
        });
	}


	private void initData() {

		Intent intent = getIntent();
		intent.getExtras();
		Bundle data = intent.getExtras();
		int position = data.getInt("news_id");
		int fragment_id = data.getInt("fragment_id");

		if(fragment_id == 1) {
			news = FirstTabFragment.newsDataList.get(position - 1);
			news_detail_title.setText(news.getTitle());
			//news_detail_author.setText(news.getAuthor());
			news_detail_date.setText(news.getPubDate());
			//news_detail_commentcount.setText(String.valueOf(news.getCommentCount()));
			news_detail_body.setText(news.getBody());
		}else if(fragment_id == 2) {
			news = SecondTabFragment.newsDataList_2.get(position - 1);
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
		copy_button = (ImageButton) findViewById(R.id.copy_button);
	}


}
