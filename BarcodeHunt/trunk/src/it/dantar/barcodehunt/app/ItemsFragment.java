package it.dantar.barcodehunt.app;

import it.dantar.gamehunt.HuntItem;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

public class ItemsFragment extends Fragment {

	
	private LinearLayout equipmentLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.items_fragment, container, false);
        ((TextView) rootView.findViewById(R.id.items_fragment_text)).setText("...");
        return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
        equipmentLayout = (LinearLayout)this.getView().findViewById(R.id.items_layout);
        equipmentLayout.removeAllViewsInLayout();
        for (HuntItem item : MainActivity.game.getEquipment()) {
    		equipmentLayout.addView(new EquipmentLayout(this.getView().getContext(), item));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private final class EquipmentLayout extends LinearLayout implements OnClickListener {
		
		HuntItem item;

		private TextView name;
		private ImageView image;

		public EquipmentLayout(Context context, HuntItem item) {
			super(context);
			this.setOrientation(LinearLayout.HORIZONTAL);
			this.item = item;
			ImageView image = new ImageView(context);
	        if (Utils.isExternalStorageReadable()) {
	        	image.setImageBitmap(Utils.loadBitmap(this.item.getIconPath()));
	        } else {
	        	image.setImageResource(R.drawable.icon);
	        }
			this.addView(image);
			this.name = new TextView(context);
			this.name.setText(this.item.getTitle());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 5, 5, 5);
			this.name.setTextSize(30f);
			this.name.setTypeface(Typeface.createFromAsset(context.getAssets(), "kim.ttf"));
			this.name.setTextColor(Color.YELLOW);
			this.name.setBackgroundResource(R.drawable.rounded_box);
			this.name.setClickable(true);
			this.name.setOnClickListener(this);
			this.addView(this.name, params);
		}

		@Override
		public void onClick(View v) {
			MainActivity.currentItem = this.item;
			IntentIntegrator scanIntegrator = new IntentIntegrator((Activity) this.getContext());
			scanIntegrator.initiateScan();
		}
		
	}
	
}
