package com.mersens.progresslayout;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mersens on 2016/10/2.
 * 用于网络请求中的四种状态之间相互切换
 */

public class ProgressStateLayout extends RelativeLayout {
    private LayoutParams layoutParams;
    private LayoutInflater inflater;
    private List<View> views = null;
    private static final String TAG_LOADING = "loading";
    private static final String TAG_EMPTY = "empty";
    private static final String TAG_ERROR = "error";
    private View viewLoading, viewEmpty, viewError;
    private ReloadListener listener;

    //重新加载按钮的接口，用于监听重新加载按钮的监听回调
    public interface ReloadListener {
        void onClick();
    }

    private enum Type {
        LOADING, EMPTY, CONTENT, ERROR;
    }

    public ProgressStateLayout(Context context) {
        this(context, null);
    }

    public ProgressStateLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressStateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化操作
     */
    public void init() {
        views = new ArrayList<View>();
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(CENTER_IN_PARENT);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        //把ProgressStateView内的子控件内容添加到list集合中，保证不同状态间相互切换内容的隐藏与显示
        if (child.getTag() == null || (!child.getTag().equals(TAG_LOADING) &&
                !child.getTag().equals(TAG_EMPTY) && !child.getTag().equals(TAG_ERROR))) {
            views.add(child);
        }
    }

    /**
     * 改变状态方法
     * @param type
     */
    private void switchState(Type type, int resid, String title, String msg, String btntext) {
        switch (type) {
            case LOADING:
                hideEmptyView();
                hideErrorView();
                setContentView(false);
                setLoadingView();
                break;
            case EMPTY:
                hideErrorView();
                hideLoadingView();
                setContentView(false);
                setEmptyView(resid, title);
                break;
            case ERROR:
                hideEmptyView();
                hideLoadingView();
                setContentView(false);
                setErrorView(resid, title, msg, btntext);
                break;
            case CONTENT:
                hideEmptyView();
                hideLoadingView();
                hideErrorView();
                setContentView(true);
                break;
        }
    }

    private void setLoadingView() {
        if (viewLoading == null) {
            viewLoading = inflater.inflate(R.layout.layout_loading, null);
            viewLoading.setTag(TAG_LOADING);
            viewLoading.requestLayout();
            addView(viewLoading, layoutParams);
        } else {
            viewLoading.setVisibility(View.VISIBLE);
        }
    }

    private void setEmptyView(int resid, String msg) {
        if (viewEmpty == null) {
            viewEmpty = inflater.inflate(R.layout.layout_empty, null);
            if (resid != 0) {
                ImageView imageView = (ImageView) viewEmpty.findViewById(R.id.img_nodata);
                imageView.setImageResource(resid);
            }
            if (!TextUtils.isEmpty(msg)) {
                TextView tv_msg = (TextView) findViewById(R.id.text_nodata_tips);
                tv_msg.setText(msg);
            }
            viewEmpty.setTag(TAG_EMPTY);
            viewEmpty.requestLayout();
            addView(viewEmpty, layoutParams);
        } else {
            viewEmpty.setVisibility(View.VISIBLE);
        }


    }

    private void setErrorView(int resid, String title, String msg, String btntext) {
        if (viewError == null) {
            viewError = inflater.inflate(R.layout.layout_error, null);

            if (resid != 0) {
                ImageView img = (ImageView) findViewById(R.id.img_nodata);
                img.setImageResource(resid);
            }
            if (!TextUtils.isEmpty(title)) {
                TextView tv_title = (TextView) findViewById(R.id.tv_title);
                tv_title.setText(title);

            }
            if (!TextUtils.isEmpty(msg)) {
                TextView tv_msg = (TextView) findViewById(R.id.tv_msg);
                tv_msg.setText(title);
            }
            Button btn_reload = (Button) viewError.findViewById(R.id.btn_reload);
            if (!TextUtils.isEmpty(btntext)) {
                btn_reload.setText(btntext);
            }
            btn_reload.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick();
                    }
                }
            });
            viewError.requestLayout();
            viewError.setTag(TAG_ERROR);
            addView(viewError, layoutParams);
        } else {
            viewError.setVisibility(View.VISIBLE);
        }
    }

    private void hideLoadingView() {
        if (viewLoading != null) {
            viewLoading.setVisibility(View.GONE);
        }
    }

    private void hideEmptyView() {
        if (viewEmpty != null) {
            viewEmpty.setVisibility(View.GONE);
        }
    }

    private void hideErrorView() {
        if (viewError != null) {
            viewError.setVisibility(View.GONE);
        }
    }

    private void setContentView(boolean flag) {
        for (View v : views) {
            v.setVisibility(flag ? View.VISIBLE : View.GONE);
        }
    }

    public void showLoading() {
        switchState(Type.LOADING,
                0, null, null, null);
    }

    public void showError(int resid, String title, String msg,
                          String btntext, ReloadListener listener) {
        this.listener = listener;
        switchState(Type.ERROR, resid, title, msg, btntext);
    }

    public void showEmpty(int resid, String msg) {
        switchState(Type.EMPTY, resid, msg, null, null);
    }

    public void showContent() {
        switchState(Type.CONTENT, 0, null, null, null);
    }

    public void showError(ReloadListener listener) {
        this.listener = listener;
        switchState(Type.ERROR, 0, null, null, null);
    }

    public void showEmpty() {
        switchState(
                Type.EMPTY, 0, null, null, null);
    }
}
