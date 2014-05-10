package com.df.simpletodo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TodoActivity extends Activity 
{		// Create array list, adapter and list view
	private ArrayList<String> todoItems;
	private ArrayAdapter<String> todoAdapter;
	private ListView lvItems;
	private EditText etNewItem;
	
	public final static String EXTRA_ITEM_TO_EDIT = "com.df.simpletodo.ITEM_TO_EDIT";
	public final static String EXTRA_POSITION = "com.df.simpletodo.POSITION";
	private int REQUEST_CODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		
		etNewItem = (EditText) findViewById(R.id.etNewItem);
		lvItems = (ListView)findViewById(R.id.lvItems);
		
			/* Get any existing items, construct the adapter, populate the list view
				and attach the adapter */
		readItems();
		todoAdapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_list_item_1, todoItems);
		lvItems.setAdapter(todoAdapter);
		Toast.makeText(this, getString(R.string.toast1), Toast.LENGTH_LONG).show();
		
		setupListViewListener();
	}

	private void setupListViewListener() 
	{    		/* Listener is attached to the listView item, an anonymous class is constructed
					allowing the listener interface to be used. The item is removed from 
					the array list and the adapter is notified that a change in the data 
					has occurred, which refreshes the list (saves the data and re-populates the view) */
		 		
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item,
					int pos, long id) 
			{
				String itemToRemove = todoAdapter.getItem(pos).toString();
				todoItems.remove(pos);

				String message = (itemToRemove + getString(R.string.removed));
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
				Toast.makeText(getApplicationContext(), getString(R.string.toast2), Toast.LENGTH_LONG).show();
				saveItems();	
				return true;
			}
		});
		
		lvItems.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> adapter, View item, 
					int pos, long id) 
			{		// Construct the intent
			    Intent i = new Intent(getApplicationContext(), EditItemActivity.class);
			  
			    	// put "extras" into the bundle for access in the Edit Item Activity
			    String itemToEdit = todoAdapter.getItem(pos).toString();
			    i.putExtra(EXTRA_ITEM_TO_EDIT, itemToEdit);
			    i.putExtra(EXTRA_POSITION, pos); 
			  
					// The Edit Item Activity is passed the item info
				startActivityForResult(i, REQUEST_CODE);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent editedItem) 
	{
	  if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) 
	  {					     // Extract edited item values from result extras
	     String updatedItem = editedItem.getExtras().getString(TodoActivity.EXTRA_ITEM_TO_EDIT);
	     int updatedItemPos = editedItem.getExtras().getInt(TodoActivity.EXTRA_POSITION);	     
	     
	     		//   Persists the updated data by saving back to the file
	     todoItems.set(updatedItemPos, updatedItem);
	     saveItems();
		 Toast.makeText(this, getString(R.string.toast4), Toast.LENGTH_LONG).show();
	  }
	} 

	public void onAddedItem(View v) 
	{			/* get a handle to the item text, add the text to the 
				adapter and clear the text field   */
		String itemText = etNewItem.getText().toString();
		todoAdapter.add(itemText);
		etNewItem.setText("");
		saveItems();
		
		String message = (itemText + getString(R.string.added));
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		Toast.makeText(this, getString(R.string.toast3), Toast.LENGTH_LONG).show();
	}
	
    public void readItems() 
    {			/* obtains the path for the file and reads the file, or creates a file
    				from a blank array, if the file didn't exist */
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
    	try 
    	{
    		todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
    	} catch (IOException e) 
    	{
    		todoItems = new ArrayList<String>();
    		e.printStackTrace();
    	}
    }
    
    public void saveItems() 
    {   		// notifies the adapter of the data change and saves to the file
    	todoAdapter.notifyDataSetChanged();
    	File filesDir = getFilesDir();
    	File todoFile = new File(filesDir, "todo.txt");
		
    	try 
    	{
    		FileUtils.writeLines(todoFile, todoItems);
    	} catch (IOException e) 
    	{
    		e.printStackTrace();
    	}
    }   

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{			// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo, menu);
		return true;
	}
}
