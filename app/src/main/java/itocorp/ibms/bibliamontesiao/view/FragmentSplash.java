package itocorp.ibms.bibliamontesiao.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import de.greenrobot.event.EventBus;
import itocorp.ibms.bibliamontesiao.MainActivity;
import itocorp.ibms.bibliamontesiao.R;

public class FragmentSplash extends Fragment {
    public FragmentSplash() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);

        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.splash);
        anim.reset();
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                EventBus.getDefault().post(new MainActivity.OpenBibleScreenEvent());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        View logos = view.findViewById(R.id.splash_icon);
        logos.clearAnimation();
        logos.startAnimation(anim);
        return view;
    }
}
