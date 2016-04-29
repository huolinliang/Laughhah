package leung.laughhah;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


/**
 * Created by kongqw on 2016/3/7.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private Context adapterContext;
    public TabFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        adapterContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (0 == position) {
            fragment = new FirstTabFragment();
        } else if (1 == position) {
            fragment = new SecondTabFragment();
        } else if (2 == position) {
            fragment = new ThirdTabFragment();
        } else if (3 == position) {
            fragment = new FourTabFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return  adapterContext.getString(R.string.tab_new_joke);
            case 1:
                return adapterContext.getString(R.string.tab_history_joke);
            case 2:
                return adapterContext.getString(R.string.tab_new_pic);
            case 3:
                return adapterContext.getString(R.string.tab_history_pic);
        }
        return null;
    }
}