package zhangfei.xingxiangyi.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.core.BaseData;
import zhangfei.xingxiangyi.core.GanZhiCalendar;
import zhangfei.xingxiangyi.core.Lunar;
import zhangfei.xingxiangyi.core.XingZuo;

/**
 * Created by Phil on 2017/4/24.
 */

public class CalendarFragment extends XingXiangYiFragment {

    private int Year = 0, Month = 0, Day = 0;
    //private final   int Hour = 0;

    private GanZhiCalendar ganzhiCal = new GanZhiCalendar();
    private Map ganzhiMap = null;
    private String nian_ganzhi, yue_ganzhi, ri_ganzhi, shi_ganzhi;

    private TextView textViewGongLi = null, textViewGanZhi = null, textViewJieQi, textViewHours = null;


    private int Minute = 0;
    private int Day_Week = 0;
    private String kongwang;
    private TextView textViewNongLi = null, textViewKongWang = null, textViewXingZuo = null;


    @Override
    public String getTitle() {
        return "万年历";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        textViewGongLi = (TextView) view.findViewById(R.id.wannianli_textViewGongLi);
        textViewNongLi = (TextView) view.findViewById(R.id.textViewNongLi);
        textViewGanZhi = (TextView) view.findViewById(R.id.wannianli_textViewGanZhi);
        textViewJieQi = (TextView) view.findViewById(R.id.wannianli_textViewJieQi);
        textViewHours = (TextView) view.findViewById(R.id.wannianli_textViewHours);
        textViewKongWang = (TextView) view.findViewById(R.id.textViewKongWang);
        textViewXingZuo = (TextView) view.findViewById(R.id.textViewXingZuo);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.wannianli_datePicker);

        Calendar cal = Calendar.getInstance();
        Year = cal.get(Calendar.YEAR);
        Month = cal.get(Calendar.MONTH);
        Day = cal.get(Calendar.DAY_OF_MONTH);
        //Hour = cal.get(Calendar.HOUR_OF_DAY);
        Minute = cal.get(Calendar.MINUTE);

        Day_Week = cal.get(Calendar.DAY_OF_WEEK);

        textViewGongLi.setText("公历：" + Year + "年" + (Month + 1) + "月" + Day + "日");

        ganzhiMap = ganzhiCal.get_Map_GanZhi_NianYueRiShi(Year, Month, Day, 0, true);
        nian_ganzhi = (String) ganzhiMap.get("年");
        yue_ganzhi = (String) ganzhiMap.get("月");
        ri_ganzhi = (String) ganzhiMap.get("日");
        shi_ganzhi = (String) ganzhiMap.get("时");

        textViewGanZhi.setText("干支：" + nian_ganzhi + "年 " + yue_ganzhi + "月 " + ri_ganzhi + "日 ");

        HashMap map = GanZhiCalendar.get_JieQiInMonth(Year, Month, true);
        textViewJieQi.setText(map.get("节气名称") + "：" + map.get("节气日期"));

        textViewHours.setText("时辰：" + get12ShiChen(shi_ganzhi));

        datePicker.init(Year, Month, Day, new DatePickerOnDateChangedListenerImpl());


        setNongLI_KongWang_XingZuo();
    }

    private void setNongLI_KongWang_XingZuo() {
        String nongli = "";
        try {
            Calendar c = Calendar.getInstance();
            c.set(Year, Month, Day);
            Lunar lunar = new Lunar(c);
            nongli = lunar.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        textViewNongLi.setText("农历：" + nongli);
        kongwang = ganzhiCal.getKongWang(ri_ganzhi);
        textViewKongWang.setText("空亡：" + kongwang);
        textViewXingZuo.setText("星座：" + XingZuo.getXingZuoName(Month + 1, Day));
    }

    private class DatePickerOnDateChangedListenerImpl implements DatePicker.OnDateChangedListener {
        private HashMap map;

        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Year = year;
            Month = monthOfYear;
            Day = dayOfMonth;

            textViewGongLi.setText("公历：" + Year + "年" + (Month + 1) + "月" + Day + "日");

            ganzhiMap = ganzhiCal.get_Map_GanZhi_NianYueRiShi(Year, Month, Day, 0, true);
            nian_ganzhi = (String) ganzhiMap.get("年");
            yue_ganzhi = (String) ganzhiMap.get("月");
            ri_ganzhi = (String) ganzhiMap.get("日");
            shi_ganzhi = (String) ganzhiMap.get("时");

            textViewGanZhi.setText("干支：" + nian_ganzhi + "年 " + yue_ganzhi + "月 " + ri_ganzhi + "日 ");
            map = GanZhiCalendar.get_JieQiInMonth(Year, Month, true);
            textViewJieQi.setText(map.get("节气名称") + "：" + map.get("节气日期"));
            textViewHours.setText("时辰：" + get12ShiChen(shi_ganzhi));

            setNongLI_KongWang_XingZuo();
        }
    }

    private String get12ShiChen(String sgz) {
        String rigan = sgz.charAt(0) + "";
        //String rizhi = sgz.charAt(1) + "";

        int G = 0, Z = 0;

        for (int i = 0; i < 10; i++) {
            if (rigan.equals(BaseData.TIANGAN[i])) {
                G = i;
                break;
            }
        }

        for (int i = 0; i < 12; i++) {
            if (rigan.equals(BaseData.DIZHI[i])) {
                Z = i;
                break;
            }
        }

        String ShiChen12 = "", s;
        int start = 0, end = 0;

        for (int i = 0; i < 12; i++) {
            start = ((i * 2 - 1) + 24) % 24;
            end = ((i * 2 + 1) + 24) % 24;

            s = BaseData.TIANGAN[(G + i) % 10] + BaseData.DIZHI[(Z + i) % 12];

            ShiChen12 = ShiChen12 + s + "(" + start + "~" + end + ") ";
        }

        return ShiChen12;
    }
}