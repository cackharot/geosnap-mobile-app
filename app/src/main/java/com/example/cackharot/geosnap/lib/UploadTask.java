package com.example.cackharot.geosnap.lib;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UploadTask extends AsyncTask<List<Bitmap>, Void, List<String>> {
    private final String Url;
    private final IUploadComplete uploadComplete;

    public UploadTask(String url, IUploadComplete uploadComplete) {
        this.Url = url;
        this.uploadComplete = uploadComplete;
    }

    protected List<String> doInBackground(List<Bitmap>... bitmaps) {
        if (bitmaps[0] == null)
            return null;

        List<String> lst = new ArrayList<String>();

        for (Bitmap img : bitmaps[0]) {
            String filename = doUpload(img);
            lst.add(filename);
        }

        return lst;
    }

    private String doUpload(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // convert Bitmap to ByteArrayOutputStream
        InputStream in = new ByteArrayInputStream(stream.toByteArray()); // convert ByteArrayOutputStream to ByteArrayInputStream
        DefaultHttpClient client = new DefaultHttpClient();

        HttpPost httppost = new HttpPost(this.Url); // server
        MultiformEntity reqEntity = new MultiformEntity();
        reqEntity.addPart("file", System.currentTimeMillis() + ".jpg", in);
        httppost.setEntity(reqEntity);

        HttpResponse response = null;
        try {
            response = client.execute(httppost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response == null)
            return null;

        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String content = getASCIIContentFromEntity(response.getEntity());
            UploadResponse res = GsonHelper.getGson().fromJson(content, UploadResponse.class);
            return res.filename;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
        InputStream in = entity.getContent();
        StringBuilder out = new StringBuilder();
        int n = 1;
        while (n > 0) {
            byte[] b = new byte[4096];
            n = in.read(b);
            if (n > 0) out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(List<String> result) {
        super.onPostExecute(result);
        this.uploadComplete.complete(result);
    }

    class UploadResponse {
        public String status;
        public String filename;
        public String id;
    }

    public interface IUploadComplete {
        void complete(List<String> filename);
    }
}