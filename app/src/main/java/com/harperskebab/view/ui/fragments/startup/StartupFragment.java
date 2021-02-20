package com.harperskebab.view.ui.fragments.startup;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.harperskebab.R;
import com.harperskebab.databinding.FragmentStartupBinding;
import com.harperskebab.utils.Constant;
import com.harperskebab.view.adapter.ViewPagerAdapter;
import com.harperskebab.view.adapter.startup.StartupPagerAdapter;
import com.harperskebab.view.ui.activities.HomeActivity;
import com.harperskebab.view.ui.activities.MainActivity;
import com.harperskebab.view.ui.activities.MapActivity;
import com.harperskebab.view.ui.activities.NewBranchActivity;
import com.harperskebab.view.ui.fragments.BaseFragment;
import com.harperskebab.view.ui.fragments.BranchFragment;
import com.harperskebab.viewmodel.SplashViewModel;
import com.harperskebab.viewmodel.ViewModelFactory;

import java.util.Timer;
import java.util.TimerTask;

public class StartupFragment extends BaseFragment {
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;

    private static final String TAG = "StartupFragment";
    private final static int REQUEST_CODE_APPLICATION_UPDATE = 1736;

    private FragmentStartupBinding binding;

    private AppUpdateManager appUpdateManager;

    private StartupPagerAdapter startupPagerAdapter;
    private SplashViewModel splashViewModel;
boolean isType=false;
    private int containerID;

    public StartupFragment() {
    }

    public static StartupFragment newInstance(int containerID) {
        StartupFragment fragment = new StartupFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.Data.CONTAINER_ID, containerID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashViewModel = ViewModelFactory.getInstance(getActivity()).create(SplashViewModel.class);
//        startupPagerAdapter = new StartupPagerAdapter(getActivity().getSupportFragmentManager());
        if (getArguments() != null) {
            containerID = getArguments().getInt(Constant.Data.CONTAINER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStartupBinding.inflate(inflater, container, false);
        checkApplicationUpdate();
        splashViewModel.getSplashBanners().observe(this, splashBanners -> {

            if (splashBanners != null) {
                if(splashBanners.size()>0) {
                    if(splashBanners.get(0).getError().equalsIgnoreCase("0")) {
                        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity(), splashBanners);
                        binding.viewPager.setAdapter(viewPagerAdapter);
                        binding.dotsIndicator.setViewPager(binding.viewPager);
                        final Handler handler = new Handler();
                        final Runnable Update = new Runnable() {
                            public void run() {
                                if (currentPage == splashBanners.size()) {
                                    currentPage = 0;
                                }
                                binding.viewPager.setCurrentItem(currentPage++, true);
                            }
                        };

                        timer = new Timer(); // This will create a new Thread
                        timer.schedule(new TimerTask() { // task to be scheduled
                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, DELAY_MS, PERIOD_MS);

                        binding.layInd.setVisibility(View.VISIBLE);
                    }
                    else{

                        startActivity(new Intent(getActivity(), MapActivity.class));
                        getActivity().finish();
                    }
                }
                else{
                    startActivity(new Intent(getActivity(), MapActivity.class));
                    getActivity().finish();
                    binding.layInd.setVisibility(View.GONE);
                }
            }else {
                startActivity(new Intent(getActivity(), MapActivity.class));
                getActivity().finish();
                binding.layInd.setVisibility(View.GONE);
            }

        });

//        binding.pager.setAdapter(startupPagerAdapter);

        binding.txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MapActivity.class));
                getActivity().finish();
//                if (isType){
//                    startActivity(new Intent(getActivity(), MapActivity.class));
//                    getActivity().finish();
//                }else {
//
//                }
            }
        });
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==2) {
                    startActivity(new Intent(getActivity(), MapActivity.class));
                    getActivity().finish();
                }

//isType=true;
//                    binding.txtSkip.setText("Next");
//                }
//                new Handler().postDelayed(() -> {
//                    if (position == 2) {
//                        startActivity(new Intent(getActivity(), MapActivity.class));
//                        getActivity().finish();
//                    }
//                }, 30);

            }

            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(getActivity(), "sdd", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Toast.makeText(getActivity(), "sdd", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void checkApplicationUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(getActivity());

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(result -> {

            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                Toast.makeText(getActivity(), "Please Update Application", Toast.LENGTH_LONG).show();
                requestUpdate(result);
            } else {
                //todo
            }
        });
    }

    private void requestUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, getActivity(), REQUEST_CODE_APPLICATION_UPDATE);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }


    @Override
    public boolean onBackPressed(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                goBack();
                return true;
            }
        }
        return false;
    }

    public static String getTAG() {
        return TAG;
    }

}
