package com.rachitgoyal.dimagichatbot.modules.chat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.rachitgoyal.dimagichatbot.R;
import com.rachitgoyal.dimagichatbot.helper.Constants;
import com.rachitgoyal.dimagichatbot.helper.Helper;
import com.rachitgoyal.dimagichatbot.model.Message;
import com.rachitgoyal.dimagichatbot.model.Note;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements InputMessageViewHolder.OnItemClickListener {

    private Context mContext;

    @BindView(R.id.input_et)
    EditText mInputET;

    @BindView(R.id.input_button)
    Button mInputButton;

    @BindView(R.id.output_rv)
    RecyclerView mOutputRV;

    @BindView(R.id.empty_tv)
    TextView mEmptyTV;

    private ChatAdapter mChatAdapter;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = MainActivity.this;

        mPrefs = getSharedPreferences("MY_PREFS", MODE_PRIVATE);

        List<Message> messages = fetchAllMessages();
        mEmptyTV.setVisibility(messages.isEmpty() ? View.VISIBLE : View.GONE);

        mChatAdapter = new ChatAdapter(messages, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        mOutputRV.setLayoutManager(linearLayoutManager);
        mOutputRV.setAdapter(mChatAdapter);

        mInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmptyTV.setVisibility(View.GONE);
                String input = mInputET.getText().toString();
                mInputET.setText("");

                addMessage(new Message(System.currentTimeMillis(), input,
                        true, Constants.MESSAGE_TYPE.INPUT));

                String commandArray[] = input.split(" ", 3);
                String command = commandArray[0];
                String extraInput = "";
                String noteInput = "";
                if (commandArray.length > 1) {
                    extraInput = commandArray[1];
                }
                if (commandArray.length > 2) {
                    noteInput = commandArray[2];
                }

                switch (command.toLowerCase()) {
                    case Constants.COMMANDS.HI:
                        addMessage(new Message(System.currentTimeMillis(),
                                getString(R.string.hi_message), false,
                                Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        break;
                    case Constants.COMMANDS.HELP:
                        addMessage(new Message(System.currentTimeMillis(),
                                getString(R.string.help_message), false,
                                Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        break;
                    case Constants.COMMANDS.TIME:
                        addMessage(new Message(System.currentTimeMillis(),
                                Helper.convertLongToFormattedTime(System.currentTimeMillis()), false,
                                Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        break;
                    case Constants.COMMANDS.HISTORY:
                        addMessage(new Message(System.currentTimeMillis(),
                                fetchHistory(), false,
                                Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        break;
                    case Constants.COMMANDS.CLEAR:
                        showClearDialog();
                        break;
                    case Constants.COMMANDS.OPEN:
                        if (extraInput.isEmpty()) {
                            addMessage(new Message(System.currentTimeMillis(),
                                    getString(R.string.forgot_url_message), false,
                                    Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        } else if (!URLUtil.isValidUrl(extraInput)) {
                            addMessage(new Message(System.currentTimeMillis(),
                                    getString(R.string.invalid_url) + extraInput, false,
                                    Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        } else {
                            addMessage(new Message(System.currentTimeMillis(),
                                    getString(R.string.opening_url) + extraInput, false, extraInput,
                                    Constants.MESSAGE_TYPE.URL_OUTPUT));
                            openURL(extraInput);
                        }
                        break;
                    case Constants.COMMANDS.PLAY:
                        if (extraInput.isEmpty() && mPrefs.getString(Constants.PREFERENCES.SONG_URL, "").isEmpty()) {
                            addMessage(new Message(System.currentTimeMillis(),
                                    getString(R.string.forgot_url_message), false,
                                    Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        } else if (extraInput.isEmpty() && !mPrefs.getString(Constants.PREFERENCES.SONG_URL, "").isEmpty()) {
                            String favURL = mPrefs.getString(Constants.PREFERENCES.SONG_URL, "");

                            addMessage(new Message(System.currentTimeMillis(),
                                    getString(R.string.playing_fav_song) + favURL, false, favURL,
                                    Constants.MESSAGE_TYPE.URL_OUTPUT));
                            openURL(favURL);

                        } else if (!URLUtil.isValidUrl(extraInput)) {
                            addMessage(new Message(System.currentTimeMillis(),
                                    getString(R.string.invalid_url) + extraInput, false,
                                    Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        } else {
                            addMessage(new Message(System.currentTimeMillis(),
                                    "Opening URL - " + extraInput, false, extraInput,
                                    Constants.MESSAGE_TYPE.URL_OUTPUT));
                            openURL(extraInput);
                        }
                        break;
                    case Constants.COMMANDS.MY_SONG_IS:
                        if (extraInput.isEmpty()) {
                            addMessage(new Message(System.currentTimeMillis(),
                                    getString(R.string.forgot_url_message), false,
                                    Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        } else if (!URLUtil.isValidUrl(extraInput)) {
                            addMessage(new Message(System.currentTimeMillis(),
                                    getString(R.string.invalid_url) + extraInput, false,
                                    Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        } else {
                            mEdit = mPrefs.edit();
                            mEdit.putString(Constants.PREFERENCES.SONG_URL, extraInput);
                            mEdit.apply();
                            addMessage(new Message(System.currentTimeMillis(),
                                    getString(R.string.saved_fav_song_message), false,
                                    Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        }
                        break;
                    case Constants.COMMANDS.PLAY_MY_SONG:
                        if (mPrefs.getString(Constants.PREFERENCES.SONG_URL, "").isEmpty()) {
                            addMessage(new Message(System.currentTimeMillis(),
                                    getString(R.string.no_fav_song), false,
                                    Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        } else {
                            String favURL = mPrefs.getString(Constants.PREFERENCES.SONG_URL, "");
                            addMessage(new Message(System.currentTimeMillis(),
                                    getString(R.string.playing_fav_song) + favURL, false, favURL,
                                    Constants.MESSAGE_TYPE.URL_OUTPUT));
                            openURL(favURL);
                        }
                        break;
                    case Constants.COMMANDS.SAVE_NOTE:
                        if (extraInput.isEmpty() || noteInput.isEmpty()) {
                            addMessage(new Message(System.currentTimeMillis(),
                                    "Please specify Booklet and Message both.", false,
                                    Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        } else {
                            addMessage(new Message(System.currentTimeMillis(),
                                    "Saved Note to Booklet", false,
                                    Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                            Note note = new Note(System.currentTimeMillis(), noteInput, extraInput);
                            SugarRecord.save(note);
                        }
                        break;
                    case Constants.COMMANDS.FETCH_NOTES:
                        if (extraInput.isEmpty()) {
                            addMessage(new Message(System.currentTimeMillis(),
                                    "Please specify a booklet, or FetchAllNotes", false,
                                    Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        } else {
                            String notes = fetchNotes(extraInput);
                            addMessage(new Message(System.currentTimeMillis(),
                                    notes, false,
                                    Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        }
                        break;
                    case Constants.COMMANDS.FETCH_ALL_NOTES:
                        addMessage(new Message(System.currentTimeMillis(),
                                fetchAllNotes(), false,
                                Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        break;
                    default:
                        addMessage(new Message(System.currentTimeMillis(),
                                getString(R.string.input_not_recognized), false,
                                Constants.MESSAGE_TYPE.TEXT_OUTPUT));
                        break;
                }
            }
        });
    }

    private void showClearDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(mContext);
        }
        builder.setTitle("Clear chat history?")
                .setMessage("Are you sure you want to clear all your chat history? This cannot be reversed.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mChatAdapter.clear();
                        SugarRecord.deleteAll(Message.class);
                        mEmptyTV.setVisibility(View.VISIBLE);
                        mEdit = mPrefs.edit();
                        mEdit.clear();
                        mEdit.apply();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void openURL(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    private String fetchHistory() {
        String history = "Your last 5 inputs were: \n\n";
        List<Message> messages = Select.from(Message.class).where(Condition.prop("type").eq(Constants.MESSAGE_TYPE.INPUT)).
                limit("5").orderBy("timestamp").list();
        Collections.reverse(messages);
        for (Message message : messages) {
            history = history.concat(message.message).concat("\n");
        }
        return history.trim();
    }

    private List<Message> fetchAllMessages() {
        List<Message> messages = Select.from(Message.class).orderBy("timestamp").list();
        Collections.reverse(messages);
        return messages;
    }

    private void addMessage(Message message) {
        SugarRecord.save(message);
        mChatAdapter.addMessage(message);
        mChatAdapter.notifyDataSetChanged();
    }

    private String fetchAllNotes() {
        List<Note> noteList = Select.from(Note.class).groupBy("booklet").orderBy("timestamp").list();
        Collections.reverse(noteList);

        if (noteList.isEmpty()) {
            return "No notes saved so far.";
        }

        String notes = "Here is a list of all your notes: \n\n";

        String currentBooklet = "";
        for (Note note : noteList) {
            if (!currentBooklet.equals(note.getBooklet())) {
                currentBooklet = note.getBooklet();
                notes = notes.concat("Booklet: " + currentBooklet + "\n");
            }
            notes = notes.concat(Helper.convertLongToFormattedTime(note.getTimestamp())).concat(": ").
                    concat(note.getText()).concat("\n\n");
        }
        return notes.trim();
    }

    private String fetchNotes(String booklet) {
        List<Note> noteList = Select.from(Note.class).where(Condition.prop("booklet").eq(booklet)).orderBy("timestamp").list();
        Collections.reverse(noteList);

        if (noteList.isEmpty()) {
            return "No notes found in this booklet";
        }

        String notes = "Your notes for the booklet - " + booklet + "\n\n";
        for (Note note : noteList) {
            notes = notes.concat(Helper.convertLongToFormattedTime(note.getTimestamp())).concat(": ").
                    concat(note.getText()).concat("\n\n");
        }
        return notes.trim();
    }

    @Override
    public void onItemClick(Message message) {

    }
}
