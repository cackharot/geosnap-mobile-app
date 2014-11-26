package com.example.cackharot.geosnap.model;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class District {
    public ObjectId _id;
    public ObjectId distributor_id;
    public String name;
    public List<String> centers;
    public List<Dealer> dealers;
    public Date created_at;
    public Boolean status;
}
