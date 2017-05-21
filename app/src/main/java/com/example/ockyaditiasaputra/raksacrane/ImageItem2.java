package com.example.ockyaditiasaputra.raksacrane;

import android.graphics.Bitmap;

/**
 * Created by Ocky Aditia Saputra on 01/03/2016.
 */

public class ImageItem2 {
    private Bitmap image;
    private String title;
    private String id;
    private String statusP;

    public ImageItem2(Bitmap image, String title, String id, String statusP) {
        super();
        this.image = image;
        this.title = title;
        this.id = id;
        this.statusP = statusP;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getStatusP() {
        return statusP;
    }

    public void setStatusP(String statusP) {
        this.statusP = statusP;
    }
}