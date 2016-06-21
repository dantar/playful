package it.dantar.barcodehunt.app;

import java.io.File;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import it.dantar.barcodehunt.persistence.DbContract;
import it.dantar.barcodehunt.persistence.DbHelper;
import it.dantar.gamehunt.HuntEventsLogger;
import it.dantar.gamehunt.HuntGame;
import it.dantar.gamehunt.HuntItem;
import it.dantar.gamehunt.HuntObject;
import it.dantar.gamehunt.finder.FileFinder;
import it.dantar.gamehunt.finder.HuntFinder;
import it.dantar.gamehunt.rules.HuntConsequence;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	
	HuntPageAdapter huntPageAdapter;
	ViewPager viewPager;
	
	public static HuntFinder finder;
	public static HuntGame game;
	public static HuntObject currentItem;
	
    /** Called when the activity is first created. */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (game == null) {
        	game = new HuntGame();
        	finder = new FileFinder(new File("/mnt/sdcard/matteo2014"));
        	game.setFinder(finder);
			if (ResponseActivity.observer == null) {
				ResponseActivity.observer = new HuntEventsLogger();
				game.registerObserver(ResponseActivity.observer);
			}
			game.resetGame();
        }

        this.refreshGui();

    }

	private void refreshGui() {
		setContentView(R.layout.main);
        huntPageAdapter = new HuntPageAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(huntPageAdapter);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null && scanningResult.getContents() != null) {
			String scanContent = scanningResult.getContents();
			ResponseActivity.observer.flushHuntEvents();
			String itemName = currentItem.getName();
			Log.d("Game", String.format("Trigger attivato: %s + %s", itemName, scanContent));
			if (itemName != null)
				for (HuntConsequence con: game.listConsequences(itemName, scanContent)) {
					con.runConsequence(game);
				};
			findViewById(R.id.items_fragment_text).invalidate();
			if (ResponseActivity.observer.getHuntEvents().size()>0) {
				Intent fire = new Intent(this, ResponseActivity.class);
				fire.putExtra("placeName", scanningResult.getContents());
				fire.putExtra("content", scanningResult.getFormatName());
				this.startActivity(fire);
			}
		}
		else{
		    Toast toast = Toast.makeText(this, "No scan data received!", Toast.LENGTH_SHORT);
		    toast.show();
		}
	}
    
	@Override
	protected void onPause() {
		Log.d("Persistence", "Called onPause");
		super.onPause();
//		saveToDb();
		Log.d("Persistence", "onPause finished!");
	}

	private void saveToDb() {
		DbHelper dbHelper = new DbHelper(this);
		SQLiteDatabase wdb = dbHelper.getWritableDatabase();
		wdb.delete(DbContract.EquipmentEntry.TABLE_NAME, null, null);
		for (HuntItem item : game.getEquipment()) {
			ContentValues values = new ContentValues();
			values.put(DbContract.EquipmentEntry.C_NAME, item.getName());
			long rowId = wdb.insert(DbContract.EquipmentEntry.TABLE_NAME, null, values);
			Log.d("Persistence", String.format("Wrote", item.getName(), rowId));
		}
	}

    private void restoreFromDb() {
		DbHelper dbHelper = new DbHelper(this);
		SQLiteDatabase rdb = dbHelper.getReadableDatabase();
		String[] columns = {DbContract.EquipmentEntry.C_NAME};
		String[] args = {};
		Cursor r = rdb.query(DbContract.EquipmentEntry.TABLE_NAME, columns, null, args, DbContract.EquipmentEntry.C_NAME, null, null);
		boolean goon = r.moveToFirst();
		while (goon) {
			this.game.gainItem(r.getString(0));
			r.moveToNext();
		}
	}

}