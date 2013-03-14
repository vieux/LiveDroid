package com.victorvieux.livedroid.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.victorvieux.livedroid.LiveDroidApp;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.data.Player;
import com.victorvieux.livedroid.fragments.CarouselFragment;
import com.victorvieux.livedroid.fragments.ListFragment;
import com.victorvieux.livedroid.fragments.OnTapListener;
import com.victorvieux.livedroid.fragments.TrophiesFragment;
import com.victorvieux.livedroid.fragments.WallFragment;

public class MainActivity extends SherlockFragmentActivity implements OnRefreshListener {
    private Player mPlayer;
   
    public Player getPlayer() {
		return mPlayer;
	}
   
    private MenuItem mMenuRefresh;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = (Player) getIntent().getExtras().getSerializable("player");
        ((LiveDroidApp)  getApplication()).setPlayer(mPlayer);

        setContentView(R.layout.activity_main);
        ActionBar bar = getSupportActionBar();

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab tabA = bar.newTab().setIcon(R.drawable.ic_memory).setText(R.string.List);
        ActionBar.Tab tabB = bar.newTab().setIcon(R.drawable.ic_apps).setText(R.string.wall);
        ActionBar.Tab tabC = bar.newTab().setIcon(R.drawable.ic_action_achievement).setText(R.string.Trophies);
        
        tabA.setTabListener(new MyTabsListener(new ListFragment()));
        tabB.setTabListener(new MyTabsListener(new WallFragment()));
        tabC.setTabListener(new MyTabsListener(new TrophiesFragment()));

        bar.addTab(tabA);
        bar.addTab(tabB);
        bar.addTab(tabC);
        
        if (Build.VERSION.SDK_INT >= 11) {
            ActionBar.Tab tabD = bar.newTab().setIcon(R.drawable.ic_action_carousel).setText(R.string.carousel);
            tabD.setTabListener(new MyTabsListener(new CarouselFragment()));
            bar.addTab(tabD);
        }
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu, menu);
		mMenuRefresh = menu.findItem(R.id.itemRefresh);
        return true;
    } 
    
    @Override
	public boolean onPrepareOptionsMenu (Menu menu) {
		mMenuRefresh = menu.findItem(R.id.itemRefresh);
		if (mMenuRefresh != null)
			menu.findItem(R.id.itemRefresh).setVisible( getSupportActionBar().getSelectedTab().getPosition() == 0);
		return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.settings:
        		startActivity(new Intent(this, SettingsActivity.class));
        		return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	

    protected class MyTabsListener implements ActionBar.TabListener {

        private Fragment fragment;

        public MyTabsListener(Fragment fragment) {
            this.fragment = fragment;
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        	if (fragment instanceof OnTapListener)
        		((OnTapListener) fragment).onTap();
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            ft.replace(R.id.fragment_container, fragment);
            invalidateOptionsMenu();
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
        }

    }


	@Override
	public void setRefresh(boolean refresh) {
			try {
				if (mMenuRefresh != null) mMenuRefresh.setActionView(refresh ? R.layout.refresh_menuitem : null);
			}catch (NullPointerException ex) {invalidateOptionsMenu();};
	}
}
