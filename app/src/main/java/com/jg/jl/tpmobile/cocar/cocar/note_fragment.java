package com.jg.jl.tpmobile.cocar.cocar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jiimmy on 2015-03-04.
 */
public class note_fragment extends Fragment{
    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.note_layout,container,false);
        return rootView;
    }
}
