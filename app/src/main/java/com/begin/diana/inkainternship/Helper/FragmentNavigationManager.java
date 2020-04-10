package com.begin.diana.inkainternship.Helper;

import com.begin.diana.inkainternship.BuildConfig;
import com.begin.diana.inkainternship.Fragment.FragmentContent;
import com.begin.diana.inkainternship.Interface.NavigationManager;
import com.begin.diana.inkainternship.Main2Activity;
import com.begin.diana.inkainternship.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentNavigationManager implements NavigationManager {
    private static FragmentNavigationManager mInstance;
    private FragmentManager mFragmentManager;
    private Main2Activity main2Activity;

    public static FragmentNavigationManager getmInstance(Main2Activity main2Activity){
        if(mInstance == null)
            mInstance = new FragmentNavigationManager();
        mInstance.configure(main2Activity);
        return mInstance;
    }

    private void configure(Main2Activity main2Activity){
        main2Activity = main2Activity;
        mFragmentManager = main2Activity.getSupportFragmentManager();
    }

    @Override
    public void showFragment(String title) {
        showFragment(FragmentContent.newInstance(title),false);
    }

    private void showFragment(Fragment fragmentContent, boolean b){
        FragmentManager fm = mFragmentManager;
        FragmentTransaction ft = fm.beginTransaction().replace(R.id.container,fragmentContent);
        ft.addToBackStack(null);
        if(b || !BuildConfig.DEBUG)
            ft.commitAllowingStateLoss();
        else
            ft.commit();
        fm.executePendingTransactions();
    }
}
