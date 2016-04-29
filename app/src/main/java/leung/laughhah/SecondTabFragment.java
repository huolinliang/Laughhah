package leung.laughhah;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SecondTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("leungadd", "secondtabfragment oncreateview");
        View rootView = inflater.inflate(R.layout.fragment_second, container, false);

        return rootView;
    }
}
