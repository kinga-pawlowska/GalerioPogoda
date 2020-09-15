package kingapawowska.galeriopogoda.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kingapawowska.galeriopogoda.R;

/**
 * Created by Kinga on 2016-08-29.
 */
public class Fragment1 extends Fragment {
    public Fragment1() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);

        return rootView;
    }
}
