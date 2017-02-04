package com.example.stoycho.traveling.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.example.stoycho.traveling.R;

/**
 * Created by stoycho on 8/18/16.
 */
public class ImageViewPagerAdapter extends PagerAdapter{

    private ArrayList<Drawable> images;
    private LayoutInflater inflater;
    private Context context;
    private Fragment fragment;
    private ViewPager viewPager;
    private String logoBackground;
    private boolean hasLogo;

    public ImageViewPagerAdapter(Context context, ArrayList<Drawable> images,Fragment fragment,ViewPager viewPager) {
        this.context = context;
        this.images = images;
        this.fragment = fragment;
        this.viewPager = viewPager;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_image_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        imageView.setImageDrawable(images.get(position));
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(fragment instanceof PlaceInfoFragment || fragment instanceof BrandFragment || fragment instanceof MallInformationFragment)
//                {
//                    GalleryFragment fragment = new GalleryFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("position",viewPager.getCurrentItem());
//                    bundle.putSerializable("images",images);
//                    if(ImageViewPagerAdapter.this.fragment instanceof PlaceInfoFragment)
//                        bundle.putString("place","place");
//                    else if(ImageViewPagerAdapter.this.fragment instanceof BrandFragment)
//                        bundle.putString("brand","brand");
//                    else if(ImageViewPagerAdapter.this.fragment instanceof MallInformationFragment)
//                        bundle.putString("mall","mall");
//                    fragment.setArguments(bundle);
//                    if(context instanceof HomeActivity)
//                        ImageViewPagerAdapter.this.fragment.getFragmentManager().beginTransaction().add(R.id.drawer_layout_left, fragment, "gallery").addToBackStack("gallery").commit();
//                    else if(context instanceof PromotionsActivity)
//                        ImageViewPagerAdapter.this.fragment.getFragmentManager().beginTransaction().add(R.id.content, fragment, "gallery").addToBackStack("gallery").commit();
//                }
//            }
//        });
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    public void setImages(Drawable image)
    {
        images.add(image);
        this.notifyDataSetChanged();
    }
    public List<Drawable> getImages()
    {
        return this.images;
    }
    public void setColorBackground(String color)
    {
        this.logoBackground = color;
    }
    public void setHasLogo(boolean hasLogo)
    {
        this.hasLogo = hasLogo;
    }
}
