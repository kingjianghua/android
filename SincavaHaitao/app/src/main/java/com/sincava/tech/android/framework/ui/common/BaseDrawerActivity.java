package com.sincava.tech.android.framework.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;

import com.sincava.tech.android.R;
import com.sincava.tech.android.framework.ui.account.AccountsActivity;
import com.sincava.tech.android.framework.ui.passcode.PasscodeLockActivity;


/**
 * Base activity implementing the navigation drawer, to be extended by all activities requiring one
 * <p>All subclasses should call the {@link #setUpDrawer()} method in {@link #onCreate(Bundle)}, after the
 * activity layout has been set.<br/>
 * The activity layout of the subclass is expected to contain {@code DrawerLayout} and a {@code NavigationView}</p>
 *
 * @author Ngewi Fet <ngewif@gmail.com>
 */
public class BaseDrawerActivity extends PasscodeLockActivity {
    protected DrawerLayout  mDrawerLayout;
    protected NavigationView mNavigationView;

    protected ActionBarDrawerToggle mDrawerToggle;

    private class DrawerItemClickListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            onDrawerMenuItemClicked(menuItem.getItemId());
            return true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Sets up the navigation drawer for this activity.
     *
     * This should be called from the activity's
     * {@link Activity#onCreate(Bundle)} method after calling
     * {@link Activity#setContentView(int)}.
     *
     */
    protected void setUpDrawer() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        mDrawerLayout   = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        mNavigationView.setNavigationItemSelectedListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            if (!mDrawerLayout.isDrawerOpen(mNavigationView))
                mDrawerLayout.openDrawer(mNavigationView);
            else
                mDrawerLayout.closeDrawer(mNavigationView);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the navigation drawer items
     * */
    protected void onDrawerMenuItemClicked(int itemId) {

        switch (itemId){
            case R.id.nav_item_open: { //Open... files
                //AccountsActivity.startXmlFileChooser(this);
            }
            break;

            case R.id.nav_item_favorites: { //favorite accounts
//                Intent intent = new Intent(this, AccountsActivity.class);
//                intent.putExtra(AccountsActivity.EXTRA_TAB_INDEX,
//                        AccountsActivity.INDEX_FAVORITE_ACCOUNTS_FRAGMENT);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
            }
            break;

            case R.id.nav_item_reports: {
//                if (!(this instanceof AccountsActivity) || !(this instanceof ReportsActivity))
//                    this.finish();
//                Intent intent = new Intent(this, ReportsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
            }
            break;

            case R.id.nav_item_scheduled_actions: { //show scheduled transactions
//                Intent intent = new Intent(this, ScheduledActionsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);
            }
            break;

            case R.id.nav_item_export:
                //AccountsActivity.openExportFragment(this);
                break;

            case R.id.nav_item_settings: //Settings activity
                //startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.nav_item_help:
//                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//                prefs.edit().putBoolean(UxArgument.SKIP_PASSCODE_SCREEN, true).apply();
//                UserVoice.launchUserVoice(this);
                break;
        }
        mDrawerLayout.closeDrawer(mNavigationView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED){
            return;
        }

        switch (requestCode) {
            case AccountsActivity.REQUEST_PICK_ACCOUNTS_FILE:
                AccountsActivity.importXmlFileFromIntent(this, data, null);
                break;
        }
    }

}
