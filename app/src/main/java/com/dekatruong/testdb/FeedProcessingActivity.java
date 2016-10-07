package com.dekatruong.testdb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dekatruong.testdb.dao.FeedDao;
import com.dekatruong.testdb.dto.Feed;

public class FeedProcessingActivity extends AppCompatActivity {

    public static final java.lang.String EXTRA_KEY_FEED_ID = "feed_id";

    private class ProcessMode {
        public static final int CREATE = 1;
        public static final int EDIT = 2;
    }

    private EditText editTextTitle;
    private EditText editTextSubTitle;

    private FeedDao mFeedDao;

    private int currentProcessMode = ProcessMode.CREATE;
    private Feed currentModel;
    private long currentModelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_processing);
        //View
        editTextTitle = (EditText) this.findViewById(R.id.editTextTitle);
        editTextSubTitle = (EditText) this.findViewById(R.id.editTextSubTitle);
        Button buttonSave = (Button) this.findViewById(R.id.buttonSave);
        //Dependency
        mFeedDao = new FeedDao(this);
        //////////////////////////////////////////////////
        //Process Income Intent
        //what mode: give feed-id mean EDIT mode
        Bundle income_extras = this.getIntent().getExtras();
        if(null != income_extras) {
            long feed_id = income_extras.getLong(EXTRA_KEY_FEED_ID); //to do: process when feed-id is wrong
            //Log.i("MyApp", "receive Feed id: " + feed_id);
            this.currentProcessMode =  ProcessMode.EDIT;
            this.currentModelId = feed_id;

            Feed data = mFeedDao.getById(feed_id);
            Log.i("MyApp", data.getId() + " " + data.title);
            updateViewData(data);
        }
    }

    private void updateViewData(Feed model) {
        this.editTextTitle.setText(model.title);
        this.editTextSubTitle.setText(model.subtitle);
    }

    public void onClick_buttonSave(View view) {
        String new_title = String.valueOf(this.editTextTitle.getText());
        String new_subtitle = String.valueOf(this.editTextSubTitle.getText());

        Feed new_data = new Feed(new_title, new_subtitle);

        //check mode and process. To do: use pattern
        switch (this.currentProcessMode) {
            case ProcessMode.CREATE:
                long new_id = mFeedDao.create(new_data); //to do: work with DB-Id
                break;
            case ProcessMode.EDIT:
                mFeedDao.updateById(currentModelId, new_data); //to do: work with DB-Id
                break;
        }

        //return list page
        this.startActivity(new Intent(this, MainActivity.class));
    }

}


