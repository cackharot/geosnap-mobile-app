package com.example.cackharot.geosnap.services;

import android.content.Context;
import android.util.Log;

import com.example.cackharot.geosnap.db.BaseRepository;
import com.example.cackharot.geosnap.lib.ConfigurationHelper;
import com.example.cackharot.geosnap.lib.GsonHelper;
import com.example.cackharot.geosnap.model.Distributor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collection;

public class DistributorService extends BaseService<Distributor> {
    private static final String fetchUrl = ConfigurationHelper.BaseUrl + "/distributors?load_all=True";
    private final BaseRepository<Distributor> repo;
    private final Gson gson;
    private Type distributorArrayType = new TypeToken<Collection<Distributor>>() {
    }.getType();

    public DistributorService(Context context) {
        super(context);
        this.repo = new BaseRepository<Distributor>(context, Distributor.class);
        this.gson = GsonHelper.getGson();
    }

    public void GetAll(IEntityDownloadCallback<Distributor> callback) {
        doHttpRequest(fetchUrl, null, null, new IDownloadCallBack<Distributor>() {
            @Override
            public void doPostExecute(String results, IEntityDownloadCallback<Distributor> innerCallback) {
                if (results == null) {
                    innerCallback.doAfterGetAll(null);
                }
                try {
                    Collection<Distributor> lst = gson.fromJson(results, distributorArrayType);
                    //ArrayList<Distributor> dbDistributors = repo.GetAll();
                    //lst.addAll(dbSites);
                    innerCallback.doAfterGetAll(lst);
                } catch (Exception ignored) {
                    Log.e("SiteService", ignored.getLocalizedMessage());
                }
            }
        }, callback);
    }
}
