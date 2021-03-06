package com.example.cackharot.geosnap.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private final UserSessionManager userSessionManager;
    ImageView bmImage;

    public DownloadImageTask(Context context, ImageView bmImage) {
        this.bmImage = bmImage;
        this.userSessionManager = new UserSessionManager(context);
    }

    protected Bitmap doInBackground(String... urls) {
        String imgUrl = urls[0];
        imgUrl = imgUrl.replaceAll("serverAddress", userSessionManager.getServerAddress());
        imgUrl = imgUrl.replaceAll("serverPort", String.valueOf(userSessionManager.getServerPort()));
        Bitmap bm = null;
        try {
            InputStream in = new java.net.URL(imgUrl).openStream();
            bm = BitmapFactory.decodeStream(in);
            bm = resizeBitmap(bm);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bm;
    }

    public static Bitmap resizeBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) 110) / width;
        float scaleHeight = ((float) 100) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return bm;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
