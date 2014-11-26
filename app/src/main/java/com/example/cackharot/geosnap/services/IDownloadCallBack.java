package com.example.cackharot.geosnap.services;

import com.example.cackharot.geosnap.model.BaseModel;

public interface IDownloadCallBack<T> {
    void doPostExecute(String results, IEntityDownloadCallback<T> innerCallback);
}
