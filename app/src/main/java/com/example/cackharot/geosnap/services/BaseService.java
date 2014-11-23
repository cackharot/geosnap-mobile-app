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
    protected final URL BaseUrl;

    protected BaseService(String baseUrl) throws MalformedURLException {
        this.BaseUrl = new URL(baseUrl);
    }

    protected void downloadJSONString(IDownloadCallBack callback, Object innerCallback) {
        HttpService httpService = new HttpService(callback, innerCallback);
        httpService.execute(BaseUrl);
    }

    private class HttpService extends AsyncTask<URL, Void, String> {
        private final IDownloadCallBack downloadCallBack;
        private final Object innerCallback;

        public HttpService(IDownloadCallBack callBack, Object innerCallback) {
            this.downloadCallBack = callBack;
            this.innerCallback = innerCallback;
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
            this.downloadCallBack.doPostExecute(results, innerCallback);
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