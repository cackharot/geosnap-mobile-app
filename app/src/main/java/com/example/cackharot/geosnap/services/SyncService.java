package com.example.cackharot.geosnap.services;

import java.net.MalformedURLException;

public class SyncService extends BaseService {
    private static String api_key = "TEtORDg5JiUjQCFOREZITEtE";
    private static final String serverUrl = "http://192.168.1.3:5500/api/users?api_key=" + api_key;

    public SyncService() {
        super(serverUrl);
    }

    public void GetUsers(DownloadCallBack callBack) throws MalformedURLException {
        downloadJSONString(callBack, "");
    }
}
