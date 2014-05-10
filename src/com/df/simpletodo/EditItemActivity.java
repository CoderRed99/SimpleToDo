package com.df.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditItemActivity extends Activity 
{
	private String originalItem;
	private EditText etEditItem;
	private int itemPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		Toast.makeText(this, getString(R.string.toast2), Toast.LENGTH_LONG).show();
		
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		
				// Extract the bundled extras
		originalItem = getIntent().getStringExtra(TodoActivity.EXTRA_ITEM_TO_EDIT);
		itemPos = getIntent().getIntExtra(TodoActivity.EXTRA_POSITION, -1);
		
		etEditItem.setText(originalItem);
		etEditItem.setSelection(etEditItem.length());
	}

	// @Override
	public void onSubmit(View v) 
	{		// Prepares intent, passes edited data back to TodoActivity along with/result code and finishes
	  Intent editedItem = new Intent();
	  editedItem.putExtra(TodoActivity.EXTRA_ITEM_TO_EDIT, etEditItem.getText().toString());
	  editedItem.putExtra(TodoActivity.EXTRA_POSITION, itemPos);
	  setResult(RESULT_OK, editedItem); 		// set result code and bundle data for response
	  String message = (originalItem + " changed to: " + etEditItem.getText().toString());
	  Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	  finish(); 								// closes the activity, pass data back to parent
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{		/*		Handle action bar item clicks here. The action bar will
					automatically handle clicks on the Home/Up button, so long
					as you specify a parent activity in AndroidManifest.xml. */
		int id = item.getItemId();
		if (id == R.id.action_settings) 
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
