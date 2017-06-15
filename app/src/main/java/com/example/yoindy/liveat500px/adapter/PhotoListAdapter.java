package com.example.yoindy.liveat500px.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yoindy.liveat500px.R;
import com.example.yoindy.liveat500px.dao.PhotoItemCollectionDao;
import com.example.yoindy.liveat500px.dao.PhotoItemDao;
import com.example.yoindy.liveat500px.datatype.MutableInteger;
import com.example.yoindy.liveat500px.manager.PhotoListManager;
import com.example.yoindy.liveat500px.view.PhotoListItem;

/**
 * Created by YoInDy on 4/6/2560.
 */

public class PhotoListAdapter extends BaseAdapter{

    PhotoItemCollectionDao dao;

    MutableInteger lastPositionInteger;

    public PhotoListAdapter(MutableInteger lastPositionInteger) {
        this.lastPositionInteger = lastPositionInteger;
    }

    public void setDao(PhotoItemCollectionDao dao) {
        this.dao = dao;
    }

    @Override
    public int getCount() {
        if (dao == null)
            return 1;
        if (dao.getData() == null)
            return 1;
        return dao.getData().size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return dao.getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

//    @Override
//    public int getViewTypeCount() { //รับค่าตามประเภทที่มี
//        return 2;
//    }

//    @Override
//    public int getItemViewType(int position) { //รับค่าที่เป็นค่าของview
//        return position % 2 == 0 ? 0 : 1 ; // ถ้าposition เป็น คู่ ส่งค่า 0 ไป ถ้าเป็น คี่ ส่งค่า 1 ไป
//    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position == getCount() - 1 ? 1 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (position % 2 == 0) {
        if (position == getCount() -1){

            ProgressBar item ;
            if (convertView != null)
                item = (ProgressBar) convertView;
            else
                item = new ProgressBar(parent.getContext());
            return item;
        }
            PhotoListItem item;
            if (convertView != null)
                item = (PhotoListItem) convertView;
            else
                item = new PhotoListItem(parent.getContext());


        PhotoItemDao dao = (PhotoItemDao) getItem(position);
        item.setNameText(dao.getCaption());
        item.setDescriptionText(dao.getUserName()+ "\n" +dao.getCamera());
        item.setImageUrl(dao.getImageUrl());

        if (position> lastPositionInteger.getValue()) {
            Animation anim = AnimationUtils.loadAnimation(parent.getContext(), //แสดงอนิเมชัน ***
                    R.anim.up_from_boottom);
            item.startAnimation(anim);
            lastPositionInteger.setValue(position);
        }
            return item;
//        } else {
//            TextView item;
//            if (convertView != null)
//                item = (TextView) convertView;
//            else
//                item = new TextView(parent.getContext());
//            item.setText("Position: "+ position );
//            return item;
//        }
    }

    public void increaseLastPosition(int amount) {
        lastPositionInteger.setValue(lastPositionInteger.getValue() + amount);
    }
}
