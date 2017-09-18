package com.ztesoft.zwfw.moudle.workchat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.moudle.Config;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewActivity extends BaseActivity {

    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        String[] imgIds = getIntent().getStringArrayExtra("imgs");
        List<String> imageUrls = new ArrayList<>();
        for(String imgId : imgIds){
            imageUrls.add(Config.BASE_URL + Config.URL_ATTACHMENT + "/"+imgId);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new ImageAdapter(imageUrls,this));
    }


    public class ImageAdapter extends PagerAdapter {

        private List<String> imageUrls;
        private Context context;

        public ImageAdapter(List<String> imageUrls, Context context) {
            this.imageUrls = imageUrls;
            this.context = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String url = imageUrls.get(position);
            PhotoView photoView = new PhotoView(context);
            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(photoView);
            container.addView(photoView);
            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            return photoView;
        }

        @Override
        public int getCount() {
            return imageUrls != null ? imageUrls.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}
