package com.dekatruong.testdb;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.dekatruong.testdb.dao.FeedDao;
import com.dekatruong.testdb.dao.FeedReaderDbHelper;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabAdd;
    private ListView listViewFeed;

    private FeedDao mFeedDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        listViewFeed = (ListView) findViewById(R.id.listViewFeed);

        //Dependency
        mFeedDao = new FeedDao(this);

        ///////////
        //Fill Data: to do: if it load data immediately, use AsynTask
        CursorAdapter dataAdapter = new SimpleCursorAdapter(
                this, R.layout.feed_element,
                //Cursor of getting data
                mFeedDao.getAllToCursor(),
                // from_columns The desired columns to be bound
                new String[] {
                        FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TITLE,
                        FeedReaderDbHelper.FeedEntry.COLUMN_NAME_SUBTITLE,
                },
                // to_view the XML defined views which the data will be bound to
                new int[] {
                        R.id.textViewTitle,
                        R.id.textViewSubTitle,
                },
                0);

        listViewFeed.setAdapter(dataAdapter);
        //
        listViewFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                Long feed_id =
                        cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderDbHelper.FeedEntry._ID));

                Toast.makeText(getApplicationContext(),
                        "id "+feed_id, Toast.LENGTH_SHORT).show();
                //
                //to edit page
                //Log.i("MyApp", "send Feed id: " + sending_data.get(FeedProcessingActivity.EXTRA_KEY_FEED_ID));
                startActivity(
                        new Intent(getApplicationContext(), FeedProcessingActivity.class)
                                .putExtra(FeedProcessingActivity.EXTRA_KEY_FEED_ID, feed_id)
                );
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick_fabAdd(View view) {

        //go to with CREATE mode
        this.startActivity(new Intent(this, FeedProcessingActivity.class));

    }
}
