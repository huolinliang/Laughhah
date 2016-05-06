package leung.laughhah;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;


public class ImgsListViewAdapter extends BaseAdapter {


	private Context context;

	private LayoutInflater listContainer;

	private List<ImgJokes> newsDataList;


	private int itemViewResource;

	static class ListItemView {
		public TextView title;
		public TextView date;
		public ImageView flag;
        public ImageView jokeImgView;
	}


	public ImgsListViewAdapter(Context context, List<ImgJokes> newsDataList, int itemViewResource) {
		this.context = context;
		this.listContainer = LayoutInflater.from(context);
		this.itemViewResource = itemViewResource;
		this.newsDataList = newsDataList;
	}


	public int getCount() {
		return newsDataList.size();
	}

	public Object getItem(int arg0) {
		return null;
	}

	public long getItemId(int arg0) {
		return 0;
	}


	public View getView(int position, View convertView, ViewGroup parent) {


		ListItemView listItemView = null;

		if (convertView == null) {
			

			convertView = listContainer.inflate(this.itemViewResource, null);

			listItemView = new ListItemView();
			listItemView.title = (TextView) convertView
					.findViewById(R.id.news_listitem_title);
			listItemView.date = (TextView) convertView
					.findViewById(R.id.news_listitem_date);
			listItemView.flag = (ImageView) convertView
					.findViewById(R.id.news_listitem_flag);
            listItemView.jokeImgView = (ImageView) convertView
                    .findViewById(R.id.image);


			convertView.setTag(listItemView);
			
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

        Ion.with(listItemView.jokeImgView)
                .centerCrop()
                .load(newsDataList.get(position).getBody());
		ImgJokes imgjokes = newsDataList.get(position);
		listItemView.title.setText(imgjokes.getTitle());
		//listItemView.author.setText(news.getAuthor());
		listItemView.date.setText(imgjokes.getPubDate());
		//listItemView.count.setText(news.getCommentCount() + "");
		listItemView.flag.setVisibility(View.VISIBLE);
        listItemView.jokeImgView.setVisibility(View.VISIBLE);
		return convertView;
	}
}