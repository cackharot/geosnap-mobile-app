package com.example.cackharot.geosnap.services;

import android.content.Context;
import android.util.Log;

import com.example.cackharot.geosnap.db.BaseRepository;
import com.example.cackharot.geosnap.lib.ConfigurationHelper;
import com.example.cackharot.geosnap.lib.GsonHelper;
import com.example.cackharot.geosnap.model.Site;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

public class SiteService extends BaseService {
    private static final String fetchUrl = ConfigurationHelper.BaseUrl + "/sites?api_key=" + ConfigurationHelper.Api_key;
    private static final String createUrl = ConfigurationHelper.BaseUrl + "/site/{0}?api_key=" + ConfigurationHelper.Api_key;
    private final BaseRepository<Site> repo;
    private final Gson gson;
    private Type siteArrayType = new TypeToken<Collection<Site>>() {
    }.getType();

    public SiteService(Context context) throws MalformedURLException {
        this.repo = new BaseRepository<Site>(context, Site.class);
        this.gson = GsonHelper.getGson();
    }

    public void GetAll(ISiteDownloadCallback callBack) {
        downloadSiteObjects(callBack);
    }

    public void Create(final Site entity, ISiteDownloadCallback callback) {
        entity.sync_status = false;
        this.repo.Create(entity);
        String jsonData = gson.toJson(entity);

        MessageFormat fmt = new MessageFormat(createUrl);
        String url = fmt.format(new Object[]{entity.getId().toString()});

        doHttpRequest(url, null, jsonData, new IDownloadCallBack() {
            @Override
            public void doPostExecute(String results, Object innerCallback) {
                ISiteDownloadCallback cb = (ISiteDownloadCallback) innerCallback;
                if (results.isEmpty() || results.startsWith("<") || results.contains("Unauthorized")) {
                    cb.doAfterCreate(null);
                }
                entity.sync_status = true;
                repo.UpdateSync(entity);
                cb.doAfterCreate(entity);
            }
        }, callback);
    }

    private void downloadSiteObjects(ISiteDownloadCallback callBack) {
        doHttpRequest(fetchUrl, null, null, new IDownloadCallBack() {
            @Override
            public void doPostExecute(String results, Object innerCallback) {
                ISiteDownloadCallback cb = (ISiteDownloadCallback) innerCallback;
                if (results.isEmpty() || results.contains("Unauthorized")) {
                    cb.doAfterGetAll(null);
                }
                Collection<Site> lst = gson.fromJson(results, siteArrayType);
                ArrayList<Site> dbSites = repo.GetAll();
                lst.addAll(dbSites);
                cb.doAfterGetAll(lst);
            }
        }, callBack);
    }
}