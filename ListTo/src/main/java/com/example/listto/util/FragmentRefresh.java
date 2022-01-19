package com.example.listto.util;

public class FragmentRefresh {
    private FragmentRefreshListener fragmentRefreshListener;
    private static FragmentRefresh instance=null;
    public FragmentRefresh getInstance(){
        if(instance==null){
            instance=new FragmentRefresh();
        }
        return instance;
    }
    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }
    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    public interface FragmentRefreshListener {
        void onRefresh();
    }

}
