package itocorp.ibms.bibliamontesiao;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import de.greenrobot.event.EventBus;
import itocorp.ibms.bibliamontesiao.view.FragmentBible;
import itocorp.ibms.bibliamontesiao.view.FragmentSplash;

public class MainActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks, ActionBar.OnNavigationListener {
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Fragment mFragment;
    private int mFragmentPos = 0;
    private boolean mHasFocus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

//        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
//        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        getSupportActionBar().hide();

        mFragment = new FragmentSplash();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
    }


    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return false;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mFragmentPos = position;
        switch (position) {
            case 0:
                ArrayAdapter<String> aAddpt = new ArrayAdapter<String>(this,
                        R.layout.item_livro,
                        R.id.title,
                        getResources().getStringArray(R.array.livros_list));
                /*
                ActionBar actionBar = getSupportActionBar();
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
                actionBar.setListNavigationCallbacks(aAddpt, this);
                */
                mFragment = new FragmentBible();
                break;

            default:
                mFragment = new FragmentBible();
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
    }

    public static class OpenBibleScreenEvent{}
    public void onEventMainThread(OpenBibleScreenEvent event) {
        if (mHasFocus) {
            mFragment = new FragmentBible();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHasFocus = true;
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        mHasFocus = false;
        EventBus.getDefault().unregister(this);

        super.onPause();
    }
}