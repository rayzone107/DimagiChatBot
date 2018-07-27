package com.rachitgoyal.dimagichatbot.model;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

@Table
public class Message {

    public long timestamp;
    public String message;
    public boolean isInput;
    public int userId;
    public boolean isExternalTask;
    public String url;
    public String type;

    public Message() {
    }

    public Message(long timestamp, String message, boolean isInput, String type) {
        this.timestamp = timestamp;
        this.message = message;
        this.isInput = isInput;
        this.type = type;
    }

    public Message(long timestamp, String message, boolean isInput, String url, String type) {
        this.timestamp = timestamp;
        this.message = message;
        this.isInput = isInput;
        this.url = url;
        this.type = type;
    }

    public Message(long timestamp, String message, boolean isInput, int userId, boolean isExternalTask, String type) {
        this.timestamp = timestamp;
        this.message = message;
        this.isInput = isInput;
        this.userId = userId;
        this.isExternalTask = isExternalTask;
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isInput() {
        return isInput;
    }

    public void setInput(boolean input) {
        isInput = input;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isExternalTask() {
        return isExternalTask;
    }

    public void setExternalTask(boolean externalTask) {
        isExternalTask = externalTask;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
