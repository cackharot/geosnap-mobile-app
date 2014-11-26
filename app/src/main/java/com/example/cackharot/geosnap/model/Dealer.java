package com.example.cackharot.geosnap.model;

import org.bson.types.ObjectId;

import java.util.Date;

public class Dealer {
    public ObjectId _id;
    public ObjectId district_id;
    public String name;
    public String contact_name;
    public String phone;
    public String email;
    public String address;
    public String center;
    public Date created_at;
    public Boolean status;
}
