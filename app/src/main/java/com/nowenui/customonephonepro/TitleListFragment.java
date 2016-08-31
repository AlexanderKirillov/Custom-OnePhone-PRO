package com.nowenui.customonephonepro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.nowenui.customonephonepro.HeapSize.HardwareInfo;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TitleListFragment extends Fragment  implements AdapterView.OnItemClickListener{
    private ListView lv;
    private ArrayAdapter<String> adapter;
    private String[] rows;
    private SharedPreferences pref;
    private String BatteryHealth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rows = getActivity().getResources().getStringArray(R.array.List_Titles);
        
        BatteryInfo easyBatteryMod = new BatteryInfo(getContext());
		int battery_health = easyBatteryMod.getBatteryHealth();
		switch (battery_health) {
		case BatteryInfo.HEALTH_GOOD:
			BatteryHealth = "хорошее";
			break;
		case BatteryInfo.HEALTH_HAVING_ISSUES:
			BatteryHealth = "имеются проблемы";
			break;
		default:
			BatteryHealth = "имеются проблемы";
			break;
		}
    }
    

	private String ProcessorInfo() {
		StringBuffer sb = new StringBuffer();
		if (new File("/proc/cpuinfo").exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
				String aLine;
				while ((aLine = br.readLine()) != null) {
					sb.append(aLine + "\n");
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public synchronized String getCPUName() {
		String CPUName = "";

		String[] lines = ProcessorInfo().split("\n");

		for (int i = 0; i < lines.length; i++) {

			String temp = lines[i];

			if (lines[i].contains("Hardware\t:")) {

				CPUName = lines[i].replace("Hardware\t: ", "");
				break;
			}
		}
		return CPUName;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);
        lv = (ListView) v.findViewById(R.id.listTitles);
        
        TextView tv1 = (TextView) v.findViewById(R.id.tv1);
		TextView tv3 = (TextView) v.findViewById(R.id.tv3);
		tv1.setText("Ваше устройство: " + DeviceName.getDeviceName());
		TextView tv2 = (TextView) v.findViewById(R.id.tv2);
		tv2.setText("Версия Android: " + android.os.Build.VERSION.RELEASE);
		
		tv1.setOnTouchListener(new OnTouchListener() {
			long oldTime = 0;
			BatteryInfo easyBatteryMod = new BatteryInfo(getContext());
			DeviceInfo easyDeviceMod = new DeviceInfo(getContext());
			IdInfo easyIdMod = new IdInfo(getContext());
			DisplayInfo easyDisplayMod = new DisplayInfo(getContext());
			MemInfo easyMemoryMod = new MemInfo(getContext());

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (System.currentTimeMillis() - oldTime < 300) {
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
						builder.setTitle("Информация об устройстве")
								.setMessage("----- Основное -----" + "\n" + "• Ваше устройство: "
										+ DeviceName.getDeviceName() + "\n" + "• Модель: " + android.os.Build.MODEL
										+ "\n" + "• Серийный номер: " + android.os.Build.SERIAL + "\n"
										+ "• Кодовое имя устройства: " + easyDeviceMod.getDevice() + "\n" + "\n"
										+ "• Версия Android: " + android.os.Build.VERSION.RELEASE + " "
										+ easyDeviceMod.getOSCodename() + "\n" + "• Версия загрузчика: "
										+ android.os.Build.BOOTLOADER + "\n" + "• Версия радиомодуля: "
										+ easyDeviceMod.getRadioVer() + "\n" + "• Версия прошивки: "
										+ easyDeviceMod.getBuildVersionIncremental() + "\n" + "• Активный язык: " + easyDeviceMod.getLanguage().toUpperCase()
										+ "\n" + "• Android ID: " + easyIdMod.getAndroidID() + "\n" + "• PseudoID: "
										+ easyIdMod.getPseudoUniqueID() + "\n" + "• GSF ID: " + easyIdMod.getGSFID()
										+ "\n" + "• Наличие Root: " + easyDeviceMod.isDeviceRooted() + "\n"
										+ "• Версия SDK: " + easyDeviceMod.getBuildVersionSDK() + "\n" + "\n"
										+ "----- Процессор -----" + "\n" + "• Процессор: " + getCPUName() + "\n"
										+ "• Тип процессора: " + android.os.Build.CPU_ABI + "\n" + "\n"
										+ "----- Память -----" + "\n" + "• RAM: " + easyMemoryMod.getTotalRAM() + " Mb"
										+ "\n" + "• ROM: " + easyMemoryMod.getTotalInternalMemorySize() + " Mb" + "\n"
										+ "• Heap Size: " + HardwareInfo.getHeapSize().trim() + "\n" + "\n"
										+ "----- Батарея -----" + "\n" + "• Здоровье батареи: " + BatteryHealth + "\n"
										+ "• Зарядка подключена: " + easyBatteryMod.isDeviceCharging() + "\n"
										+ "• Тип батареи: " + easyBatteryMod.getBatteryTechnology() + "\n"
										+ "• Напряжение: " + easyBatteryMod.getBatteryVoltage() + " mV" + "\n"
										+ "• Температура батареи: " + easyBatteryMod.getBatteryTemperature() + " °C"
										+ "\n" + "• Текущий заряд: " + easyBatteryMod.getBatteryPercentage() + " %"
										+ "\n" + "\n" + "----- Экран -----" + "\n" + "• Разрешение: "
										+ easyDisplayMod.getResolution() + "\n" + "• DPI: "
										+ easyDisplayMod.getDensity() + "\n")
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

		tv2.setOnTouchListener(new OnTouchListener() {
			long oldTime = 0;
			BatteryInfo easyBatteryMod = new BatteryInfo(getContext());
			DeviceInfo easyDeviceMod = new DeviceInfo(getContext());
			IdInfo easyIdMod = new IdInfo(getContext());
			DisplayInfo easyDisplayMod = new DisplayInfo(getContext());
			MemInfo easyMemoryMod = new MemInfo(getContext());

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (System.currentTimeMillis() - oldTime < 300) {
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
						builder.setTitle("Информация об устройстве")
								.setMessage("----- Основное -----" + "\n" + "• Ваше устройство: "
										+ DeviceName.getDeviceName() + "\n" + "• Модель: " + android.os.Build.MODEL
										+ "\n" + "• Серийный номер: " + android.os.Build.SERIAL + "\n"
										+ "• Кодовое имя устройства: " + easyDeviceMod.getDevice() + "\n" + "\n"
										+ "• Версия Android: " + android.os.Build.VERSION.RELEASE + " "
										+ easyDeviceMod.getOSCodename() + "\n" + "• Версия загрузчика: "
										+ android.os.Build.BOOTLOADER + "\n" + "• Версия радиомодуля: "
										+ easyDeviceMod.getRadioVer() + "\n" + "• Версия прошивки: "
										+ easyDeviceMod.getBuildVersionIncremental() + "\n" +"• Активный язык: " + easyDeviceMod.getLanguage().toUpperCase()
										+ "\n" + "• Android ID: " + easyIdMod.getAndroidID() + "\n" + "• PseudoID: "
										+ easyIdMod.getPseudoUniqueID() + "\n" + "• GSF ID: " + easyIdMod.getGSFID()
										+ "\n" + "• Наличие Root: " + easyDeviceMod.isDeviceRooted() + "\n"
										+ "• Версия SDK: " + easyDeviceMod.getBuildVersionSDK() + "\n" + "\n"
										+ "----- Процессор -----" + "\n" + "• Процессор: " + getCPUName() + "\n"
										+ "• Тип процессора: " + android.os.Build.CPU_ABI + "\n" + "\n"
										+ "----- Память -----" + "\n" + "• RAM: " + easyMemoryMod.getTotalRAM() + " Mb"
										+ "\n" + "• ROM: " + easyMemoryMod.getTotalInternalMemorySize() + " Mb" + "\n"
										+ "• Heap Size: " + HardwareInfo.getHeapSize().trim() + "\n" + "\n"
										+ "----- Батарея -----" + "\n" + "• Здоровье батареи: " + BatteryHealth + "\n"
										+ "• Зарядка подключена: " + easyBatteryMod.isDeviceCharging() + "\n"
										+ "• Тип батареи: " + easyBatteryMod.getBatteryTechnology() + "\n"
										+ "• Напряжение: " + easyBatteryMod.getBatteryVoltage() + " mV" + "\n"
										+ "• Температура батареи: " + easyBatteryMod.getBatteryTemperature() + " °C"
										+ "\n" + "• Текущий заряд: " + easyBatteryMod.getBatteryPercentage() + " %"
										+ "\n" + "\n" + "----- Экран -----" + "\n" + "• Разрешение: "
										+ easyDisplayMod.getResolution() + "\n" + "• DPI: "
										+ easyDisplayMod.getDensity() + "\n")
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

		tv3.setOnTouchListener(new OnTouchListener() {
			long oldTime = 0;
			BatteryInfo easyBatteryMod = new BatteryInfo(getContext());
			DeviceInfo easyDeviceMod = new DeviceInfo(getContext());
			IdInfo easyIdMod = new IdInfo(getContext());
			DisplayInfo easyDisplayMod = new DisplayInfo(getContext());
			MemInfo easyMemoryMod = new MemInfo(getContext());

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (System.currentTimeMillis() - oldTime < 300) {
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
						builder.setTitle("Информация об устройстве")
								.setMessage("----- Основное -----" + "\n" + "• Ваше устройство: "
										+ DeviceName.getDeviceName() + "\n" + "• Модель: " + android.os.Build.MODEL
										+ "\n" + "• Серийный номер: " + android.os.Build.SERIAL + "\n"
										+ "• Кодовое имя устройства: " + easyDeviceMod.getDevice() + "\n" + "\n"
										+ "• Версия Android: " + android.os.Build.VERSION.RELEASE + " "
										+ easyDeviceMod.getOSCodename() + "\n" + "• Версия загрузчика: "
										+ android.os.Build.BOOTLOADER + "\n" + "• Версия радиомодуля: "
										+ easyDeviceMod.getRadioVer() + "\n" + "• Версия прошивки: "
										+ easyDeviceMod.getBuildVersionIncremental() + "\n" + "• Активный язык: " + easyDeviceMod.getLanguage().toUpperCase()
										+ "\n" + "• Android ID: " + easyIdMod.getAndroidID() + "\n" + "• PseudoID: "
										+ easyIdMod.getPseudoUniqueID() + "\n" + "• GSF ID: " + easyIdMod.getGSFID()
										+ "\n" + "• Наличие Root: " + easyDeviceMod.isDeviceRooted() + "\n"
										+ "• Версия SDK: " + easyDeviceMod.getBuildVersionSDK() + "\n" + "\n"
										+ "----- Процессор -----" + "\n" + "• Процессор: " + getCPUName() + "\n"
										+ "• Тип процессора: " + android.os.Build.CPU_ABI + "\n" + "\n"
										+ "----- Память -----" + "\n" + "• RAM: " + easyMemoryMod.getTotalRAM() + " Mb"
										+ "\n" + "• ROM: " + easyMemoryMod.getTotalInternalMemorySize() + " Mb" + "\n"
										+ "• Heap Size: " + HardwareInfo.getHeapSize().trim() + "\n" + "\n"
										+ "----- Батарея -----" + "\n" + "• Здоровье батареи: " + BatteryHealth + "\n"
										+ "• Зарядка подключена: " + easyBatteryMod.isDeviceCharging() + "\n"
										+ "• Тип батареи: " + easyBatteryMod.getBatteryTechnology() + "\n"
										+ "• Напряжение: " + easyBatteryMod.getBatteryVoltage() + " mV" + "\n"
										+ "• Температура батареи: " + easyBatteryMod.getBatteryTemperature() + " °C"
										+ "\n" + "• Текущий заряд: " + easyBatteryMod.getBatteryPercentage() + " %"
										+ "\n" + "\n" + "----- Экран -----" + "\n" + "• Разрешение: "
										+ easyDisplayMod.getResolution() + "\n" + "• DPI: "
										+ easyDisplayMod.getDensity() + "\n")
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
        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item , R.id.list_title, rows);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MainActivity.setFragment(ShowContentFragment.getInstance(position+1, false));
    }
    @Override
    public void onResume() {
        super.onResume();
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        lv.setSelectionFromTop(pref.getInt("index",0), pref.getInt("top",0));
        MainActivity.menu.setSlidingEnabled(true);
        MainActivity.isShowContentOpen = false;
        MainActivity.actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
    }
    @Override
    public void onPause() {   

        int index =lv.getFirstVisiblePosition();
        View v = lv.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - lv.getPaddingTop());

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Editor edit = pref.edit();
        edit.putInt("index", index);
        edit.putInt("top", top);
        edit.apply();

        super.onPause();
    }
}
