package com.sp.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

/**
 * Created by Muhd on 8/7/2015.
 */
public class EditTaskActivity extends Activity {

    private Task task;
    private EditText titleEditText;
    private EditText contentEditText;
    private String postTitle;
    private String postContent;
    private Button saveTaskButton;
    private Button deleteTaskButton;
    private String entityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = this.getIntent();

        titleEditText = (EditText) findViewById(R.id.taskTitle);
        contentEditText = (EditText) findViewById(R.id.taskContent);

        if (intent.getExtras() != null) {
            task = new Task(intent.getStringExtra("taskId"), intent.getStringExtra("taskTitle"), intent.getStringExtra("taskContent"));

            titleEditText.setText(task.getTitle());
            contentEditText.setText(task.getContent());
        }

        deleteTaskButton = (Button) findViewById(R.id.deleteTask);
        deleteTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });
        saveTaskButton = (Button) findViewById(R.id.saveTask);
        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            case R.id.action_twitter:
                Intent data = new Intent();
                data.putExtra("title", titleEditText.getText().toString());
                TweetComposer.Builder builder = new TweetComposer.Builder(this)
                        .text("I have a task, " + titleEditText.getText().toString() + " !");
                builder.show();
                break;

            case R.id.action_sms:
                Intent i = new Intent(EditTaskActivity.this, SMSActivity.class);;
                i.putExtra("text", titleEditText.getText().toString());
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTask() {

        postTitle = titleEditText.getText().toString();
        postContent = contentEditText.getText().toString();

        postTitle = postTitle.trim();
        postContent = postContent.trim();

        // If user doesn't enter a title or content, do nothing
        // If user enters title, but no content, save
        // If user enters content with no title, give warning
        // If user enters both title and content, save

        if (!postTitle.isEmpty()) {

            // Check if post is being created or edited

            if (task == null) {
                // create new post

                final ParseObject post = new ParseObject("Post");
                post.put("title", postTitle);
                post.put("content", postContent);
                post.put("author", ParseUser.getCurrentUser());
                setProgressBarIndeterminateVisibility(true);
                post.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        setProgressBarIndeterminateVisibility(false);
                        if (e == null) {
                            // Saved successfully.
                            task = new Task(post.getObjectId(), postTitle, postContent);
                            Toast.makeText(getApplicationContext(), "Task added", Toast.LENGTH_SHORT).show();
                        } else {
                            // The save failed.
                            Toast.makeText(getApplicationContext(), "Failed to add task", Toast.LENGTH_SHORT).show();
                            Log.d(getClass().getSimpleName(), "User update error: " + e);
                        }
                    }
                });

            } else {
                // update post

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

                // Retrieve the object by id
                query.getInBackground(task.getId(), new GetCallback<ParseObject>() {
                    public void done(ParseObject post, ParseException e) {
                        if (e == null) {
                            // Now let's update it with some new data.
                            post.put("title", postTitle);
                            post.put("content", postContent);
                            setProgressBarIndeterminateVisibility(true);
                            post.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    setProgressBarIndeterminateVisibility(false);
                                    if (e == null) {
                                        // Saved successfully.
                                        Toast.makeText(getApplicationContext(), "Task updated", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // The save failed.
                                        Toast.makeText(getApplicationContext(), "Failed to update task", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        } else if (postTitle.isEmpty() && !postContent.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditTaskActivity.this);
            builder.setMessage(R.string.edit_error_message)
                    .setTitle(R.string.edit_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void deleteTask() {
        postTitle = titleEditText.getText().toString();
        postContent = contentEditText.getText().toString();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");

        // Retrieve the object by id
        query.getInBackground(task.getId(), new GetCallback<ParseObject>() {
            public void done(ParseObject post, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data.
                    post.put("title", postTitle);
                    post.put("content", postContent);
                    post.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(getApplicationContext(), "Task deleted", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    public void onToggleClicked(View view) {
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        entityId = task.getId();

        if (on) {

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.note)
                            .setContentTitle("You have remaining tasks")
                            .setContentText(titleEditText.getText().toString())
                            .setOngoing(true);
            notificationManager.notify(entityId,0, mBuilder.build());
        } else {
            NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nMgr.cancel(entityId, 0);

        }
    }
}
