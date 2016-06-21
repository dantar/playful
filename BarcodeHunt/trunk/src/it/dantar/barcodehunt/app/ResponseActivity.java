package it.dantar.barcodehunt.app;

import it.dantar.gamehunt.HuntEvent;
import it.dantar.gamehunt.HuntEventsLogger;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResponseActivity extends Activity {

	public static HuntEventsLogger observer;
	private LinearLayout eventsLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.response);
        eventsLayout = (LinearLayout)findViewById(R.id.lastlog_layout);
        for (HuntEvent event: observer.getHuntEvents()) {
        	if (event==null) continue;
        	eventsLayout.addView(new HuntEventLayout(this, event));
        }
	}

	private final class HuntEventLayout extends LinearLayout {
		
		HuntEvent item;

		private TextView name;
		private ImageView image;

		public HuntEventLayout(Context context, HuntEvent item) {
			super(context);
			this.setOrientation(LinearLayout.HORIZONTAL);
			this.item = item;
//			image = (ImageView)findViewById(R.id.response_icon);
			image = new ImageView(context);
//			image.setImageResource(R.drawable.icon);
			image.setImageBitmap(Utils.loadBitmap(item.getIconPath()));
			this.addView(image);
			this.name = new TextView(context);
			this.name.setText(this.item.getTitle());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 5, 5, 5);
			this.name.setTextSize(30f);
			this.name.setTypeface(Typeface.createFromAsset(getAssets(), "kim.ttf"));
			this.name.setTextColor(Color.YELLOW);
			this.name.setBackgroundResource(R.drawable.rounded_box);
			this.addView(this.name, params);
		}
		
	}
	
	
}
