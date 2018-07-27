package com.rachitgoyal.dimagichatbot.helper;

public class Constants {

    public interface COMMANDS {
        String HI = "hi";
        String HELP = "help";
        String TIME = "time";
        String HISTORY = "history";
        String CLEAR = "clear";
        String OPEN = "open";
        String PLAY = "play";
        String PLAY_MY_SONG = "playmysong";
        String MY_SONG_IS = "mysongis";
        String SAVE_NOTE = "savenote";
        String FETCH_NOTES = "fetchnotes";
        String FETCH_ALL_NOTES = "fetchallnotes";
    }

    public interface MESSAGE_TYPE {
        String INPUT = "INPUT";
        String TEXT_OUTPUT = "TEXT_OUTPUT";
        String URL_OUTPUT = "URL_OUTPUT";
    }

    public interface PREFERENCES {
        String SONG_URL = "SONG_URL";
    }
}
