package futuresky.projects.tracnghiem.chamthitracnghiem.Introduce;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IntroAdapter extends FragmentPagerAdapter {
    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return IntroFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return 6;
    }
}
