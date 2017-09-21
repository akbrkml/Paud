package com.kemendikbud.paud.controller;


import com.kemendikbud.paud.App;
import com.kemendikbud.paud.event.PictureEvent;
import com.kemendikbud.paud.model.Picture;
import com.kemendikbud.paud.model.User;
import com.kemendikbud.paud.network.PictureService;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kemendikbud.paud.Main.id_sekolah;

/**
 * Created by akbar on 20/09/17.
 */

public class PictureController {
    private EventBus eventBus = App.getInstance().getEventBus();
    private PictureEvent event = new PictureEvent();
    private PictureService pictureService;

    public void getDataPictures(){
        pictureService = new PictureService();
        pictureService.getPictures(id_sekolah, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                User user = (User) response.body();
                List<Picture> pictures = user.getPictures();
                if (user != null) {
                    if (!user.isError()) {

                        Collections.sort(pictures, new Comparator<Picture>() {
                            @Override
                            public int compare(Picture picture, Picture t1) {
                                return Integer.parseInt(picture.getImageId()) > Integer.parseInt(t1.getImageId()) ? -1 :
                                        (Integer.parseInt(picture.getImageId()) > Integer.parseInt(t1.getImageId() )) ? 1 : 0;
                            }
                        });
                        event.setMessage(user.getMessage());
                        event.setPictures(pictures);

                        if (response.code() == 200){
                            event.setSuccess(true);
                        } else {
                            event.setSuccess(false);
                        }

                        eventBus.post(event);
                    }
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                event.setMessage(t.getMessage());
                event.setSuccess(false);
                eventBus.post(event);
            }
        });
    }
}
