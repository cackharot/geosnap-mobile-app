package com.example.cackharot.geosnap.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.cackharot.geosnap.R;
import com.example.cackharot.geosnap.model.Site;
import com.example.cackharot.geosnap.services.ISiteDownloadCallback;
import com.example.cackharot.geosnap.services.SiteService;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;

public class ListSiteFragment extends Fragment implements AbsListView.OnItemClickListener, ISiteDownloadCallback {
    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;
    private ArrayList<Site> sites;


    public ListSiteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sites = new ArrayList<Site>();
        mAdapter = new ArrayAdapter<Site>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, sites);

        try {
            SiteService siteService = new SiteService(getActivity());
            siteService.GetAll(this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void doAfterDownload(Collection<Site> results) {
        sites.clear();
        if (results != null && !results.isEmpty()) {
            sites.addAll(results);
        }
        mAdapter = new ArrayAdapter<Site>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, sites);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String id);
    }
}
