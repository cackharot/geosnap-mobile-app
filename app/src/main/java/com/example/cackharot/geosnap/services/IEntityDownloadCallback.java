package com.example.cackharot.geosnap.services;

import com.example.cackharot.geosnap.model.Site;

import java.util.Collection;

public interface IEntityDownloadCallback<T> {
    void doAfterGetAll(Collection<T> results);

    void doAfterCreate(T entity);

    void doAfterGet(T item);
}
