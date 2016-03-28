package com.sincava.tech.android.framework.ui.passcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class KeyboardFragment extends Fragment {

    private static final int DELAY = 500;

    private TextView pass1;
    private TextView pass2;
    private TextView pass3;
    private TextView pass4;

    private int length = 0;

    public interface OnPasscodeEnteredListener {
        void onPasscodeEntered(String pass);
    }

    private OnPasscodeEnteredListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
