package com.example.cackharot.geosnap.lib;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cackharot.geosnap.R;
import com.example.cackharot.geosnap.model.Site;

import java.io.InputStream;
import java.util.List;

public class SiteArrayAdapter extends ArrayAdapter<Site> {

    Context mContext;
    int layoutResourceId;
    List<Site> data = null;

    public SiteArrayAdapter(Context mContext, int layoutResourceId, List<Site> data) {
        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /*
         * The convertView argument is essentially a "ScrapView" as described is Lucas post 
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
         * It will have a non-null value when ListView is asking you recycle the row layout. 
         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
         */
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        Site item = data.get(position);

        if(item.photos != null && !item.photos.isEmpty()) {
            ImageView siteImg = (ImageView) convertView.findViewById(R.id.imgSite);
            new DownloadImageTask(siteImg)
                    .execute(ConfigurationHelper.SiteImageUrl + item.photos.get(0));
        }

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.txtSiteName);
        textViewItem.setText(item.name);
        textViewItem.setTag(item.sync_status);

        textViewItem = (TextView) convertView.findViewById(R.id.txtSiteAddress);
        textViewItem.setText(item.address);

        textViewItem = (TextView) convertView.findViewById(R.id.txtSiteSqFt);
        textViewItem.setText(String.valueOf(item.square_feet));

        textViewItem = (TextView) convertView.findViewById(R.id.txtSiteLatLng);
        textViewItem.setText(String.valueOf(item.location.latitude) + "," + String.valueOf(item.location.longitude));

        return convertView;
   }
}