package com.example.cackharot.geosnap.services;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.cackharot.geosnap.model.BaseModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.HashMap;

public abstract class BaseService<T> {
    protected void doHttpRequest(String baseUrl, HashMap<String, String> queryArgs, String post_data,
                                 IDownloadCallBack<T> callback, IEntityDownloadCallback<T> innerCallback) {
        Uri.Builder b = Uri.parse(baseUrl).buildUpon();
        if (queryArgs != null && !queryArgs.isEmpty()) {
            for (String key : queryArgs.keySet()) {
                b.appendQueryParameter(key, queryArgs.get(key));
            }
        }
        Uri url = b.build();
        HttpService httpService = new HttpService(url, post_data, callback, innerCallback);
        httpService.execute();
    }

    private class HttpService extends AsyncTask<Void, Void, String> {
        private final IDownloadCallBack<T> downloadCallBack;
        private final IEntityDownloadCallback<T> innerCallback;
        private HttpRequestBase httpRequest;

        public HttpService(Uri url, String jsonData, IDownloadCallBack<T> callBack, IEntityDownloadCallback<T> innerCallback) {
            this.downloadCallBack = callBack;
            this.innerCallback = innerCallback;
            if (jsonData == null || jsonData.isEmpty()) {
                httpRequest = new HttpGet(url.toString());
            } else {
                StringEntity input = null;
                try {
                    input = new StringEntity(jsonData);
                    input.setContentType("application/json;charset=UTF-8");
                    input.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8"));

                    HttpPost postRequest = new HttpPost(url.toString());
                    postRequest.setHeader("Accept", "application/json");
                    postRequest.setEntity(input);

                    httpRequest = postRequest;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            String content = null;
            try {

                HttpResponse response = httpClient.execute(httpRequest, localContext);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK
                        || statusCode == HttpStatus.SC_NO_CONTENT
                        || statusCode == HttpStatus.SC_CREATED) {
                    HttpEntity entity = response.getEntity();
                    content = getASCIIContentFromEntity(entity);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return content;
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