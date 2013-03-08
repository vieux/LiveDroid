package com.victorvieux.livedroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.victorvieux.livedroid.R;
import com.victorvieux.livedroid.data.Player;
import com.victorvieux.livedroid.fragments.OnTapListener;
import com.victorvieux.livedroid.fragments.ProfileFragment;
import com.victorvieux.livedroid.fragments.TrophiesFragment;
import com.victorvieux.livedroid.fragments.WallFragment;

/**
 * This demonstrates how you can implement switching between the tabs of a
 * TabHost through fragments.  It uses a trick (see the code below) to allow
 * the tabs to switch between fragments instead of simple views.
 */
public class MainActivity extends SherlockFragmentActivity {
    private Player mPlayer;
   
    public Player getPlayer() {
		return mPlayer;
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayer = (Player) getIntent().getExtras().getSerializable("player");

        setContentView(R.layout.main);
        ActionBar bar = getSupportActionBar();

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.Tab tabA = bar.newTab().setIcon(R.drawable.ic_action_list);
        ActionBar.Tab tabB = bar.newTab().setIcon(R.drawable.ic_action_wall);
        ActionBar.Tab tabC = bar.newTab().setIcon(R.drawable.ic_action_trophy);
        
        tabA.setTabListener(new MyTabsListener(new ProfileFragment()));
        tabB.setTabListener(new MyTabsListener(new WallFragment()));
        tabC.setTabListener(new MyTabsListener(new TrophiesFragment()));

        bar.addTab(tabA);
        bar.addTab(tabB);
        bar.addTab(tabC);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    } 
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.carousel: 
            {
                Intent intent = new Intent(this, CarouselActivity.class);
                intent.putExtra("player", getPlayer());
                startActivity(intent);
                return true;
            }
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
        	((OnTapListener) fragment).onTap();
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            ft.replace(R.id.fragment_container, fragment);

        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            ft.remove(fragment);
        }

    }
}
