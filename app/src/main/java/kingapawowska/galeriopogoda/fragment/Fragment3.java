package kingapawowska.galeriopogoda.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import kingapawowska.galeriopogoda.R;
import kingapawowska.galeriopogoda.adapter.ViewPagerAdapter;

/**
 * Created by Kinga on 2016-08-29.
 */
public class Fragment3 extends Fragment {
    public Fragment3() {

    }

    Bundle savedInstanceState;
    Context context;

    private ArrayList<Integer> images;
    private BitmapFactory.Options options;
    private ViewPager viewPager;
    private View btnNext, btnPrev;
    private FragmentStatePagerAdapter adapter;
    private LinearLayout thumbnailsContainer;
    private final static int[] resourceIDs = new int[]{R.drawable.a, R.drawable.b,
            R.drawable.c, R.drawable.d, R.drawable.e, R.drawable.f};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.galeria, container, false);

        images = new ArrayList<>();

        //find view by id
        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        thumbnailsContainer = (LinearLayout) rootView.findViewById(R.id.container);
        btnNext = rootView.findViewById(R.id.next);
        btnPrev = rootView.findViewById(R.id.prev);

        btnPrev.setOnClickListener(onClickListener(0));
        btnNext.setOnClickListener(onClickListener(1));

        setImagesData();

        // init viewpager adapter and attach
        adapter = new ViewPagerAdapter(getFragmentManager(), images);
        viewPager.setAdapter(adapter);

        inflateThumbnails();

        return rootView;
    }

    private View.OnClickListener onClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i > 0) {
                    //next page
                    if (viewPager.getCurrentItem() < viewPager.getAdapter().getCount() - 1) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                } else {
                    //previous page
                    if (viewPager.getCurrentItem() > 0) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }
                }
            }
        };
    }

    private void setImagesData() {
        for (int i = 0; i < resourceIDs.length; i++) {
            images.add(resourceIDs[i]);
        }
    }

    private void inflateThumbnails() {
        for (int i = 0; i < images.size(); i++) {
            View imageLayout = getLayoutInflater(savedInstanceState).inflate(R.layout.item_image, null);
            ImageView imageView = (ImageView) imageLayout.findViewById(R.id.img_thumb);
            imageView.setOnClickListener(onChagePageClickListener(i));

            options = new BitmapFactory.Options();
            options.inSampleSize = 3;

            options.inDither = true;
            options.inScaled = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inDither = false;
            options.inPurgeable = true;


            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), images.get(i), options );
            imageView.setImageBitmap(bitmap);
            //set to image view
            imageView.setImageBitmap(bitmap);
            //add imageview
            thumbnailsContainer.addView(imageLayout);
        }
    }

    private View.OnClickListener onChagePageClickListener(final int i) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(i);
            }
        };
    }
}

