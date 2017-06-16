package com.example.yoindy.liveat500px.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yoindy.liveat500px.R;
import com.example.yoindy.liveat500px.dao.PhotoItemDao;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class PhotoInfoFragment extends Fragment {

    ImageView ivImg;
    TextView ivLike;
    TextView tvCaption;
    TextView tvUser;
    TextView tvProfilePicture;
    TextView tvCreatedTime;
    TextView tvCamera;

    PhotoItemDao dao;

    public PhotoInfoFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static PhotoInfoFragment newInstance(PhotoItemDao dao) {
        PhotoInfoFragment fragment = new PhotoInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("dao",dao);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        dao = getArguments().getParcelable("dao");

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info_summary, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {
        // Init 'View' instance(s) with rootView.findViewById here
        ivLike = (TextView) rootView.findViewById(R.id.ivLike);
        tvCaption = (TextView) rootView.findViewById(R.id.tvCaption);
        tvUser = (TextView) rootView.findViewById(R.id.tvUser);
        tvProfilePicture = (TextView) rootView.findViewById(R.id.tvProfilePicture);
        tvCreatedTime = (TextView) rootView.findViewById(R.id.tvCreatedTime);
        tvCamera = (TextView) rootView.findViewById(R.id.tvCamera);
        ivImg = (ImageView) rootView.findViewById(R.id.ivImg);

        ivLike.setText(dao.getLike());
        tvCaption.setText(dao.getCaption());
        tvUser.setText(dao.getUserName());
        tvProfilePicture.setText(dao.getProfilePicture());
//        tvCreatedTime.setText((CharSequence) dao.getCreatedTime());
        tvCamera.setText(dao.getCamera());
        Glide.with(PhotoInfoFragment.this)
                .load(dao.getImageUrl())
                .placeholder(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImg);
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
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

}
