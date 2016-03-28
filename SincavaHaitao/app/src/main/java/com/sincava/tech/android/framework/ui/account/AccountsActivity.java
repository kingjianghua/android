package com.sincava.tech.android.framework.ui.account;

import android.os.Bundle;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
//
//import com.crashlytics.android.Crashlytics;
//import com.kobakei.ratethisapp.RateThisApp;


import com.sincava.tech.android.R;
import com.sincava.tech.android.framework.ui.common.BaseDrawerActivity;
import com.sincava.tech.android.framework.ui.util.TaskDelegate;
import com.sincava.tech.android.framework.util.OnAccountClickedListener;

public class AccountsActivity extends BaseDrawerActivity implements OnAccountClickedListener {

    /**
     * Request code for GnuCash account structure file to import
     */
    public static final int REQUEST_PICK_ACCOUNTS_FILE = 0x1;

    /**
     * Request code for opening the account to edit
     */
    public static final int REQUEST_EDIT_ACCOUNT = 0x10;

    /**
     * Logging tag
     */
    protected static final String LOG_TAG = "AccountsActivity";

    /**
     * Number of pages to show
     */
    private static final int DEFAULT_NUM_PAGES = 3;

    /**
     * Index for the recent accounts tab
     */
    public static final int INDEX_RECENT_ACCOUNTS_FRAGMENT = 0;

    /**
     * Index of the top level (all) accounts tab
     */
    public static final int INDEX_TOP_LEVEL_ACCOUNTS_FRAGMENT = 1;

    /**
     * Index of the favorite accounts tab
     */
    public static final int INDEX_FAVORITE_ACCOUNTS_FRAGMENT = 2;

    /**
     * Used to save the index of the last open tab and restore the pager to that index
     */
    public static final String LAST_OPEN_TAB_INDEX = "last_open_tab";

    /**
     * Key for putting argument for tab into bundle arguments
     */
    public static final String EXTRA_TAB_INDEX = "org.gnucash.android.extra.TAB_INDEX";
    public static final int REQUEST_PERMISSION_WRITE_SD_CARD = 0xAB;

    private class AccountViewPagerAdapter extends FragmentPagerAdapter {

        public AccountViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            AccountsListFragment currentFragment;
            switch (position){
                case INDEX_RECENT_ACCOUNTS_FRAGMENT:
                    currentFragment = AccountsListFragment.newInstance(AccountsListFragment.DisplayMode.RECENT);
                    break;

                case INDEX_FAVORITE_ACCOUNTS_FRAGMENT:
                    currentFragment = AccountsListFragment.newInstance(AccountsListFragment.DisplayMode.FAVORITES);
                    break;

                case INDEX_TOP_LEVEL_ACCOUNTS_FRAGMENT:
                default:
                    currentFragment = AccountsListFragment.newInstance(AccountsListFragment.DisplayMode.TOP_LEVEL);
                    break;
            }

            mFragmentPageReferenceMap.put(position, currentFragment);
            return currentFragment;
        }

        @Override
        public int getCount() {
            return 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        setUpDrawer();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void accountSelected(String accountUID) {

    }

    public static void importXmlFileFromIntent(Activity context, Intent data, TaskDelegate onFinishTask) {
//        GncXmlExporter.createBackup();
//        new ImportAsyncTask(context, onFinishTask).execute(data.getData());
    }

    public static void startXmlFileChooser(Activity activity) {
        Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
        pickIntent.setType("*/*");
        Intent chooser = Intent.createChooser(pickIntent, "Select GnuCash account file");

        try {
            activity.startActivityForResult(chooser, REQUEST_PICK_ACCOUNTS_FILE);
        } catch (ActivityNotFoundException ex) {
//            Crashlytics.log("No file manager for selecting files available");
//            Crashlytics.logException(ex);
            Toast.makeText(activity, R.string.toast_install_file_manager, Toast.LENGTH_LONG).show();
        }
    }
}
