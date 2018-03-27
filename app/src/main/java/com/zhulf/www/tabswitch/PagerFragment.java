package com.zhulf.www.tabswitch;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/28.
 */

public class PagerFragment extends Fragment {
    private String tabTitle;

    @Bind(R.id.content_id)
    TextView contentText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            tabTitle = getArguments().getString("tabtitle_key","微信");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_fragment,container,false);
        ButterKnife.bind(this,view);

        contentText.setText(tabTitle);
        return view;
    }
}
