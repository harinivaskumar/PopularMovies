package com.harinivaskumarrp.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Hari Nivas Kumar R P on 11/27/2015.
 */

public class ImageViewAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mPosterPaths = {
            "/D6e8RJf2qUstnfkTslTXNTUAlT.jpg",
            "/1n9D32o30XOHMdMWuIT4AaA5ruI.jpg",
            "/5JU9ytZJyR3zmClGmVm9q4Geqbd.jpg",
            "/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg",
            "/A7HtCxFe7Ms8H7e7o2zawppbuDT.jpg",
            "/l3tmn2WOAIgLyGP7zcsTYkl5ejH.jpg",
            "/q0R4crx2SehcEEQEkYObktdeFy.jpg",
            "/g23cs30dCMiG4ldaoVNP1ucjs6.jpg",
            "/l3Lb8UWmqfXY9kr9YhJXvnTvf4I.jpg",
            "/vlTPQANjLYTebzFJM1G4KeON0cb.jpg",
            "/cWERd8rgbw7bCMZlwP207HUXxym.jpg",
            "/qey0tdcOp9kCDdEZuJ87yE3crSe.jpg",
            "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg",
            "/coss7RgL0NH6g4fC2s5atvf3dFO.jpg",
            "/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "/69Cz9VNQZy39fUE2g0Ggth6SBTM.jpg",
            "/aAmfIX3TT40zUHGcCKrlOZRKC7u.jpg",
            "/z2sJd1OvAGZLxgjBdSnQoLCfn3M.jpg",
            "/4VmZeT8YkuMI6BrA623mHZDISlN.jpg",
            "/t90Y3G8UGQp0f0DrP60wRu9gfrH.jpg"
    };

    public ImageViewAdapter(Context context) {
        mContext = context;
    }

    public int getCount() {
        return (mPosterPaths.length);
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(0,0,0,0);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext)
                .load("http://image.tmdb.org/t/p/w185/" + mPosterPaths[position])
                .into(imageView);
        return imageView;
    }
}
