package com.nowenui.customonephonepro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private String[] titles;
    private String[] icons;
    private MyAdapter adapter;
    private Button exit;
	private ImageView twitter, vk;
	private TextView version;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titles = getActivity().getResources().getStringArray(R.array.Menu_Titles);
        icons = getActivity().getResources().getStringArray(R.array.Menu_imageForTitles);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.menu_list, null);
        exit = (Button) v.findViewById(R.id.exit);
		twitter = (ImageView) v.findViewById(R.id.twitterbutton);
		vk = (ImageView) v.findViewById(R.id.vk);
		version = (TextView) v.findViewById(R.id.textView3);
        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new MyAdapter(getActivity(),titles,icons);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
		twitter.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
            	 	Intent intent = new Intent();
                	Uri address = Uri.parse("https://twitter.com/intent/user?user_id=4771768877");
                	Intent openlink = new Intent(Intent.ACTION_VIEW, address);
                	startActivity(openlink);
				}});
		vk.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
            	 	Intent intent = new Intent();
                	Uri address = Uri.parse("http://vk.com/nowenui_official_group");
                	Intent openlink = new Intent(Intent.ACTION_VIEW, address);
                	startActivity(openlink);
				}});
		version.setOnTouchListener(new OnTouchListener() {
			long oldTime = 0;

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (System.currentTimeMillis() - oldTime < 300) {
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
						builder.setTitle("Доп.информация о программе")
								.setMessage("Версия программы: 15.0.0 Release\n" + "----- Build 85 (17.07.2016) -----"
										+ "\n\nEclipse build environment " + "\n\nИспользованные библиотеки:"
										+ "\n- AppCompact v7 by Google (Android N version)"
										+ "\n- SlidingMenu by jfeinstein10 (version 1.0)"
										+ "\n- AndroidDeviceNames by jaredrummler (1.0.9)"
										+ "\n- Easy DeviceInfo by nisrulz (2.0.2)"
										+ "\n- DeviceInformation by akexorcist (1.3.1)"
										+ "\n- android-support-v4 libs by Google (Android N version)"
										+ "\n\nBuild by NowenUI (Alexander Kirillov) ")
								.setIcon(R.drawable.splash).setCancelable(false)
								.setNegativeButton("ОК", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int id) {
										dialog.cancel();
									}
								});
						AlertDialog alert = builder.create();
						alert.show();
					}
					oldTime = System.currentTimeMillis();

				}
				return true;
			}
		});
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)
            MainActivity.setFragment(new TitleListFragment());
        else
            MainActivity.setFragment(ShowContentFragment.getInstance(position,true));
    }
    protected class MyAdapter extends ArrayAdapter<String> {
        private String[] menuIcons;
        private Context context;
        private LayoutInflater inflater;
        public MyAdapter(Context context, String[] menuTitles, String[] menuIcons) {
            super(context,R.layout.menu_item, menuTitles);
            this.context = context;
            this.menuIcons = menuIcons;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null) {
                convertView = inflater.inflate(R.layout.menu_item, null);
                viewHolder = new ViewHolder();
                viewHolder.rowTitle = (TextView) convertView.findViewById(R.id.menuItem_title);
                viewHolder.rowIcon = (ImageView) convertView.findViewById(R.id.menuItem_icon);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.rowTitle.setText(getItem(position));
            if(!menuIcons[position].equals("null")) {
                int iconID = context.getResources().getIdentifier(menuIcons[position], "drawable", context.getPackageName());
                viewHolder.rowIcon.setBackgroundResource(iconID);
            }
            return convertView;
        }
         class ViewHolder {
            TextView rowTitle;
            ImageView rowIcon;
        }
    }
}
