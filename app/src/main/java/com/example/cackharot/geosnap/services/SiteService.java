package com.example.cackharot.geosnap.services;

import android.content.Context;

import com.example.cackharot.geosnap.db.BaseRepository;
import com.example.cackharot.geosnap.lib.ConfigurationHelper;
import com.example.cackharot.geosnap.lib.GsonHelper;
import com.example.cackharot.geosnap.model.Site;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.Collection;

public class SiteService extends BaseService implements IDownloadCallBack {
    private static final String serverUrl = ConfigurationHelper.BaseUrl + "/sites?api_key=" + ConfigurationHelper.Api_key;
    private final BaseRepository<Site> repo;
    private Type siteArrayType = new TypeToken<Collection<Site>>() {
    }.getType();

    public SiteService(Context context) throws MalformedURLException {
        super(serverUrl);
        this.repo = new BaseRepository<Site>(context, Site.class);
    }

    public void GetAll(ISiteDownloadCallback callBack) {
        downloadSiteObjects(callBack);
    }

    private void downloadSiteObjects(ISiteDownloadCallback callBack) {
        downloadJSONString(this, callBack);
    }

    @Override
    public void doPostExecute(String results, Object innerCallback) {
        ISiteDownloadCallback cb = (ISiteDownloadCallback) innerCallback;
        if (results.isEmpty() || results.contains("Unauthorized")) {
            cb.doAfterDownload(null);
        }
        Gson gson = GsonHelper.getGson();
        Collection<Site> lst = gson.fromJson(results, siteArrayType);
        cb.doAfterDownload(lst);
    }
}