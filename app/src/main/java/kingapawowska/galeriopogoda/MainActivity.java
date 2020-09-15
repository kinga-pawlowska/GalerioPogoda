package kingapawowska.galeriopogoda;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kingapawowska.galeriopogoda.adapter.SlidingMenuAdapter;
import kingapawowska.galeriopogoda.fragment.Fragment1;
import kingapawowska.galeriopogoda.fragment.Fragment2;
import kingapawowska.galeriopogoda.fragment.Fragment3;
import kingapawowska.galeriopogoda.model.ItemSlideMenu;

/**
 * Created by Kinga on 2016-08-29.
 */
public class MainActivity extends AppCompatActivity {

    // blokada orientacji ekranu
    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    //private RelativeLayout mainContent;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // init component
        listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //mainContent = (RelativeLayout) findViewById(R.id.main_content);
        listSliding = new ArrayList<>();

        // Add item for sliding list
        listSliding.add(new ItemSlideMenu(R.drawable.ic_action_home, "Home"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_action_pogoda, "Pogoda"));
        listSliding.add(new ItemSlideMenu(R.drawable.ic_action_galeria, "Galeria"));

        listSliding.add(new ItemSlideMenu(R.drawable.ic_action_divider, "_______________________"));

        listSliding.add(new ItemSlideMenu(R.mipmap.ic_launcher, "Element 1"));
        listSliding.add(new ItemSlideMenu(R.mipmap.ic_launcher, "Element 2"));
        listSliding.add(new ItemSlideMenu(R.mipmap.ic_launcher, "Element 3"));

        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        // Display icon to open / close sliding list
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= 16) {
            getSupportActionBar().setHomeAsUpIndicator(
                    getResources().getDrawable(R.drawable.ic_navigation_drawer));
        }

        // set Title
        setTitle(listSliding.get(0).getTitle());
        // item selected
        listViewSliding.setItemChecked(0, true);
        // Close Menu
        drawerLayout.closeDrawer(listViewSliding);

        // Display fragment 1 when start
        replaceFragment(0);

        // Handle on item click
        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Set title
                setTitle(listSliding.get(position).getTitle());
                // item selected
                listViewSliding.setItemChecked(position, true);
                // Replace fragment
                replaceFragment(position);
                // Close menu
                drawerLayout.closeDrawer(listViewSliding);

            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }

    // Create metthod replace fragment

    private void replaceFragment(int pos) {
        Fragment fragment = null;
        switch (pos) {
            case 0:
                fragment = new Fragment1();
                break;
            case 1:
                fragment = new Fragment2();
                break;
            case 2:
                fragment = new Fragment3();
                break;
            default:
                fragment = new Fragment1();
                break;
        }

        if (null != fragment) {
            //FragmentManager fragmentManager = getFragmentManager();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.main_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
