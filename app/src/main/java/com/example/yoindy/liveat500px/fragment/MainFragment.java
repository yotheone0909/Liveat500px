package com.example.yoindy.liveat500px.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yoindy.liveat500px.R;
import com.example.yoindy.liveat500px.activity.MoreInfoActivity;
import com.example.yoindy.liveat500px.adapter.PhotoListAdapter;
import com.example.yoindy.liveat500px.dao.PhotoItemCollectionDao;
import com.example.yoindy.liveat500px.dao.PhotoItemDao;
import com.example.yoindy.liveat500px.datatype.MutableInteger;
import com.example.yoindy.liveat500px.manager.HttpManager;
import com.example.yoindy.liveat500px.manager.PhotoListManager;
import com.example.yoindy.liveat500px.view.PhotoListItem;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by nuuneoi on 11/16/2014.
 */
public class MainFragment extends Fragment {

    public  interface FragmentListener {
        void onPhotoItemClicked(PhotoItemDao dao);
    }

    //Variables
    ListView listView;
    PhotoListAdapter ListAdapter;
    Button btnNewPhotos;

    SwipeRefreshLayout swipeRefreshLayout;

    PhotoListManager photoListManager;

    MutableInteger lastPositionInteger;

    /*************
     * Functions
     **********/

    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize Fragment level
        init(savedInstanceState);


        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);//Restore


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView,savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        photoListManager = new PhotoListManager();
        lastPositionInteger = new MutableInteger(-1);



    }

    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        listView = (ListView) rootView.findViewById(R.id.listView);
        ListAdapter = new PhotoListAdapter(lastPositionInteger);
        ListAdapter.setDao(photoListManager.getDao());
        listView.setAdapter(ListAdapter);

        btnNewPhotos = (Button) rootView.findViewById(R.id.btnNewPhotos);

        btnNewPhotos.setOnClickListener(buttonClickListener);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(pullToRefreshListener);

        listView.setOnScrollListener(listViewScrollListener);
        listView.setOnItemClickListener(listViewItemClickListener);

        if (savedInstanceState == null)
            refreshData();
    }

    private void refreshData(){
        if (photoListManager.getCount() == 0)
            reloadData();
        else
            reloadDaraNewer();
    }


    private void reloadDaraNewer() {

        int MaxId = photoListManager.getMaximumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance()
                .getService()
                .loadPhotoListAfterId(MaxId);
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_RELOAD_NEWER));

    }

    boolean isLoadingMore = false;

    private void reloadMoreData() {

        if (isLoadingMore)
            return;
        isLoadingMore = true;
        int MiniId = photoListManager.getMinimumId();
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance()
                .getService()
                .loadPhotoListฺBeforeId(MiniId);
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_LOAD_MORE));

    }

    private void reloadData() {
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoList();
        call.enqueue(new PhotoListLoadCallback(PhotoListLoadCallback.MODE_RELOAD));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here

        outState.putBundle("photoListManager",
                photoListManager.onSaveInstanceState());
        outState.putBundle("lastPositionInteger",
                lastPositionInteger.onSaveInstanceState());

    }
    private void onRestoreInstanceState (Bundle saveInstanceState) {
        //Restore instance state here
        photoListManager.onRestoreInstanceState(
                saveInstanceState.getBundle("photoListManager"));
        lastPositionInteger.onRestoreInstanceState(
                saveInstanceState.getBundle("lastPositionInteger"));
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    private void showButtonNewPhotos(){
        Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.zoom_fade_in);
        btnNewPhotos.startAnimation(anim);
        btnNewPhotos.setVisibility(View.VISIBLE);
    }
    private void hideButtonNewPhotos(){
        Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.zoom_fade_out);
        btnNewPhotos.startAnimation(anim);
        btnNewPhotos.setVisibility(View.GONE);

    }

    private void showToast(String text){
        Toast.makeText(Contextor.getInstance().getContext(),
                text,
                Toast.LENGTH_SHORT)
                .show();
    }
    /*******
     * Listener Zone
     */
    final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            listView.smoothScrollToPosition(0);
            hideButtonNewPhotos();

        }
    };

    final SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshData();
        }
    };

    final AbsListView.OnScrollListener listViewScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view,
                             int firstVisibleItem,
                             int visibleItemCount,
                             int totalItemCount) {

            swipeRefreshLayout.setEnabled(firstVisibleItem == 0);

            if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                if (photoListManager.getCount() > 0) {
                    //Load More
                    reloadMoreData();
                }
            }

        }
    };

    AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position < photoListManager.getCount()) {
                PhotoItemDao dao = photoListManager.getDao().getData().get(position);
                FragmentListener listener = (FragmentListener) getActivity();
                listener.onPhotoItemClicked(dao);
            }
        }
    };

    /*************
     * Inner Class
     */

    class PhotoListLoadCallback implements Callback<PhotoItemCollectionDao> {


        public static final int MODE_RELOAD = 1;
        public static final int MODE_RELOAD_NEWER = 2;
        public static final int MODE_LOAD_MORE = 3;

        int mode;

        public PhotoListLoadCallback(int mode) {
            this.mode = mode;
        }

        @Override
        public void onResponse(Call<PhotoItemCollectionDao> call, Response<PhotoItemCollectionDao> response) {
            swipeRefreshLayout.setRefreshing(false);
            if (response.isSuccessful()){
                PhotoItemCollectionDao dao = response.body();

                int fistVisiblePosition = listView.getFirstVisiblePosition();
                View c = listView.getChildAt(0);
                int top = c == null ? 0 : c.getTop();

                if (mode == MODE_RELOAD_NEWER) {
                    photoListManager.insertDaoAtTopPosition(dao);
                }
                else if (mode == MODE_LOAD_MORE) {
                    photoListManager.appendDaoAtButtonPosition(dao);
                }
                else {
                    photoListManager.setDao(dao);
                }
                cleadLoadingMoreFlagIfCapable(mode);
                ListAdapter.setDao(photoListManager.getDao());
                ListAdapter.notifyDataSetChanged();

                if (mode == MODE_RELOAD_NEWER){
                    int additionalSize =
                            (dao != null && dao.getData() != null) ? dao.getData().size() : 0;
                    ListAdapter.increaseLastPosition(additionalSize);
                    listView.setSelectionFromTop(fistVisiblePosition + additionalSize,
                            top);
                    if (additionalSize > 0)
                        showButtonNewPhotos();
                }else {

                }

                //TODO: Toast
                showToast("Load Completed");
            } else {
                cleadLoadingMoreFlagIfCapable(mode);
                try {
                    showToast(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        @Override
        public void onFailure(Call<PhotoItemCollectionDao> call, Throwable t) {


            cleadLoadingMoreFlagIfCapable(mode);
            swipeRefreshLayout.setRefreshing(false);
            showToast(t.toString());

        }
        private void cleadLoadingMoreFlagIfCapable(int mode){
            if (mode == MODE_LOAD_MORE)
                isLoadingMore = false;
        }

    }

}
