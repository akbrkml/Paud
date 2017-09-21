package com.kemendikbud.paud.event;

import com.kemendikbud.paud.model.Picture;

import java.util.List;

/**
 * Created by akbar on 20/09/17.
 */

public class PictureEvent {
    private String message;
    private boolean success;
    private List<Picture> pictures;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }
}
