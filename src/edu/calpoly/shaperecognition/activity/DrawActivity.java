package edu.calpoly.shaperecognition.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.example.shaperecognition.R;

import edu.calpoly.shaperecongition.drawingcanvas.DrawingPanel;

public class DrawActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new DrawingPanel(this));
		//test
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
