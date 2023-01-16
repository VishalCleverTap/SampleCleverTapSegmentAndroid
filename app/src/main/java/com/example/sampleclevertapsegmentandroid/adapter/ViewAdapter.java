package com.example.sampleclevertapsegmentandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnit;
import com.clevertap.android.sdk.displayunits.model.CleverTapDisplayUnitContent;
import com.example.sampleclevertapsegmentandroid.CleverTapSegmentApplication;
import com.example.sampleclevertapsegmentandroid.R;

import java.util.ArrayList;

public class ViewAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    ArrayList<CleverTapDisplayUnitContent> cleverTapDisplayUnitContents = new ArrayList<>();
    ArrayList<CleverTapDisplayUnit> cleverTapDisplayUnits = new ArrayList<>();
    private CleverTapAPI cleverTapAPI;

    public ViewAdapter(Context context,CleverTapAPI cleverTapAPI) {
        this.context = context;
        this.cleverTapAPI = cleverTapAPI;
    }

    public void setImages(ArrayList<CleverTapDisplayUnit> cleverTapDisplayUnits, ArrayList<CleverTapDisplayUnitContent> cleverTapDisplayUnitContents){
        this.cleverTapDisplayUnits = cleverTapDisplayUnits;
        this.cleverTapDisplayUnitContents = cleverTapDisplayUnitContents;
    }

    @Override
    public int getCount() {
        return cleverTapDisplayUnitContents.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
        View view = layoutInflater.inflate(R.layout.item, null);
        ImageView imageView = view.findViewById(R.id.image_view);
        Glide.with(context).load(cleverTapDisplayUnitContents.get(position).getMedia()).into(imageView);
        //imageView.setImageResource(Integer.parseInt(images.get(position)));
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You clicked "+cleverTapDisplayUnitContents.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                cleverTapAPI.pushDisplayUnitClickedEventForID(cleverTapDisplayUnits.get(position).getUnitID());
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
