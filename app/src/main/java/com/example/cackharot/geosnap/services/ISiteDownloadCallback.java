package com.example.cackharot.geosnap.services;

import com.example.cackharot.geosnap.model.Site;

import java.util.Collection;

public interface ISiteDownloadCallback {
    void doAfterDownload(Collection<Site> results);
}
