package com.example.cackharot.geosnap.services;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class BaseService {
    protected final String BaseUrl;

    protected BaseService(String baseUrl) {
        this.BaseUrl = baseUrl;
    }

    protected void downloadJSONString(DownloadCallBack callback, String url) throws MalformedURLException {
        HttpService httpService = new HttpService(callback);
        httpService.execute(new URL(this.BaseUrl + url));
    }

    private class HttpService extends AsyncTask<URL, Void, String> {
        private final DownloadCallBack downloadCallBack;

        public HttpService(DownloadCallBack callBack) {
            this.downloadCallBack = callBack;
        }

        @Override
        protected String doInBackground(URL... urls) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(urls[0].toString());
            String text = null;
            try {

                HttpResponse response = httpClient.execute(httpGet, localContext);

                HttpEntity entity = response.getEntity();

                text = getASCIIContentFromEntity(entity);

            } catch (Exception e) {
                //return e.getLocalizedMessage();
                e.printStackTrace();
                return null;
            }
            return text;
        }

        @Override
        protected void onPostExecute(String results) {
            super.onPostExecute(results);
            if (this.downloadCallBack == null)
                return;
            this.downloadCallBack.doPostExecute(results);
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
    }
}