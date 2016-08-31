package com.nowenui.customonephonepro;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ShowContentFragment extends Fragment {
    private WebView webView;
    private int folderID;
    private boolean fromMenu;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            folderID = savedInstanceState.getInt("folderID",1);
            fromMenu = savedInstanceState.getBoolean("forMenu", false);
        }else {
            folderID = getArguments().getInt("folderID", -1);
            fromMenu = getArguments().getBoolean("fromMenu");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.webview_fragment, container, false);
        webView = (WebView) v.findViewById(R.id.webview);
        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        webView.getSettings().setJavaScriptEnabled(true); 
        webView.getSettings().setDefaultTextEncodingName("utf-8"); 
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setBackgroundColor(Color.parseColor("#3b3b3b"));
        webView.setWebViewClient(new URLParsel());

        webView.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_MOVE:
                        if(webView.getScrollX()==0) {
                            MainActivity.menu.setSlidingEnabled(true);
                        }else {
                            MainActivity.menu.setSlidingEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        MainActivity.menu.setSlidingEnabled(true);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        if(webView.getScrollX() != 0)
                            MainActivity.menu.setSlidingEnabled(false);
                    break;
                }
                return false;
            }
        });
        if(fromMenu)
            openFile(folderID, "m");
        else
            openFile(folderID, "p");
    }
    public static ShowContentFragment getInstance(int folderID, boolean fromMenu) {
        Bundle args = new Bundle();
        args.putInt("folderID", folderID);
        args.putBoolean("fromMenu", fromMenu);
        ShowContentFragment fragment = new ShowContentFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public void openFile(int folderID, String folder) {
        String path = "file:///android_asset/"+ folder + folderID + "/index.html";
       webView.loadUrl(path);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("folderID", folderID);
        outState.putBoolean("forMenu", fromMenu);
    }
	@Override
    public void onResume()
	{
        super.onResume();
		MainActivity.actionBar.setHomeAsUpIndicator(R.drawable.backs);
        MainActivity.isShowContentOpen = true;
    }
	public class URLParsel extends WebViewClient {
	    @Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	            view.getContext().startActivity(intent);
	        
	        return true;
	    }
	}
}
