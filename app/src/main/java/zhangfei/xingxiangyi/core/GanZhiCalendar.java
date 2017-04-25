package zhangfei.xingxiangyi.core;


import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;

import org.json.*;

public class GanZhiCalendar {
    private static double[] JIEQI_CONSTANT21 = {5.4055, 3.87, 5.63, 4.81, 5.52, 5.678, 7.108, 7.5, 7.646, 8.318, 7.438, 7.18};
    private static double[] JIEQI_CONSTANT20 = {6.11, 4.15, 5.63, 5.59, 6.318, 6.5, 7.928, 8.35, 8.44, 9.098, 8.218, 7.9};
    private static double D = 0.2422;

    public Map get_Map_GanZhi_NianYueRiShi(int year, int month, int day, int hour, boolean isJavaCalendarStyleMonth) {
        Map<String, String> map = new HashMap<String, String>();

        if (!isJavaCalendarStyleMonth)
            month = month - 1;

        JSONArray ja_ganzhi = get_JSONArray_GanZhi_NianYueRiShi(year, month, day, hour, true);
        try {
            map.put("年", ja_ganzhi.getString(0));
            map.put("月", ja_ganzhi.getString(1));
            map.put("日", ja_ganzhi.getString(2));
            map.put("时", ja_ganzhi.getString(3));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    public JSONArray get_JSONArray_GanZhi_NianYueRiShi(int year, int month, int day, int hour, boolean isJavaCalendarStyleMonth) {
        if (!isJavaCalendarStyleMonth)
            month = month - 1;

        /**需要注意的是Java的Calendar中月是以0为开始表示一月*/

        /**如果进入23点，是中国的子时。属于中国传统日干支新一天即第二天的开始
         *举例，2011年12月31日晚上的23点12分，实际上计算干支所需时间变成2012年1月1日。
         */
        if (hour >= 23) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            c.add(Calendar.DAY_OF_MONTH, 1);

			/*产生新的一天*/
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        Calendar newCal = Calendar.getInstance();
        newCal.set(year, month, day);

        /**取年的后两位*/
        String yearstr = String.valueOf(year);
        int Y = Integer.parseInt(yearstr.substring(yearstr.length() - 2));

		/*取21和20世纪，即1900年~2100年
		*小寒的修正参数排在最前面，接着是立春
		*/
        //double[] JIEQI_CONSTANT21={5.4055,3.87,5.63,4.81,5.52,5.678,7.108,7.5,7.646,8.318,7.438,7.18};
        //double[] JIEQI_CONSTANT20={6.11,4.15,5.63,5.59,6.318,6.5,7.928,8.35,8.44,9.098,8.218,7.9};
        //double	D=0.2422;

        double JIEQI_CONSTANT = 0.0;
        int century = year / 100 + 1;
        if (century == 21)
            JIEQI_CONSTANT = JIEQI_CONSTANT21[month];
        if (century == 20)
            JIEQI_CONSTANT = JIEQI_CONSTANT20[month];

        int k = 0;
        if (month == 0 || month == 1)
            k = 1;

        int JIEQI_DAY = (int) (Y * D + JIEQI_CONSTANT) - (Y - k) / 4;
        /**修正值*/
        if (year == 1982 && month == 0)
            JIEQI_DAY = JIEQI_DAY + 1;
        if (year == 2019 && month == 0)
            JIEQI_DAY = JIEQI_DAY - 1;
        if (year == 1911 && month == 4)
            JIEQI_DAY = JIEQI_DAY + 1;
        if (year == 1902 && month == 5)
            JIEQI_DAY = JIEQI_DAY + 1;
        if ((year == 1925 || year == 2016) && month == 6)
            JIEQI_DAY = JIEQI_DAY + 1;
        if (year == 2002 && month == 7)
            JIEQI_DAY = JIEQI_DAY + 1;
        if (year == 1927 && month == 8)
            JIEQI_DAY = JIEQI_DAY + 1;
        if (year == 2089 && month == 10)
            JIEQI_DAY = JIEQI_DAY + 1;
        if (year == 1954 && month == 11)
            JIEQI_DAY = JIEQI_DAY + 1;


        /**捎带把立春日期也计算出来，后面要用*/
        if (century == 21)
            JIEQI_CONSTANT = JIEQI_CONSTANT21[1];
        if (century == 20)
            JIEQI_CONSTANT = JIEQI_CONSTANT20[1];
        int LICHUNDAY = (int) (Y * D + JIEQI_CONSTANT) - (Y - 1) / 4;
        Calendar LICHUN_CAL = Calendar.getInstance();
        LICHUN_CAL.set(year, 1, LICHUNDAY);


        /**获得该公历月中蕴含的节气日期点*/
        Calendar JIEQI_CAL = Calendar.getInstance();
        JIEQI_CAL.set(year, month, JIEQI_DAY);

        /**
         寻找月支的算法。原理简洁。
         因为通常每年的24节气关乎干支月的转换节气，与公历月份存在对应关系，
         如公历1月对应丑月的小寒，2月对应寅月的立春，3月对应卯月的惊蛰.....以此类推。
         因此，任意给定公历某一个月份的某一天，那么该日期只可能存在2种情况，要么在节气之前，要么在节气之后。
         所以我们就只比较所给定的公历日期（月日）与节气时间点的前后位置，
         在前，则用该节气之前的月支，之后则用该节气的月支。
         */

        String[] YUEZHI = {"丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥", "子"};
        String yue_zhi = "";

        if (newCal.before(JIEQI_CAL))
            yue_zhi = YUEZHI[(month - 1 + 12) % 12];
        else
            yue_zhi = YUEZHI[month];
        /**计算月支结束，已经获得月支*/


        /**计算年干支*/
        int nian = 0;
        if (newCal.before(LICHUN_CAL))
            nian = year - 1;
        else
            nian = year;

        String yearss = String.valueOf(nian);
        int endy = Integer.parseInt(yearss.substring(yearstr.length() - 1));

        /**获得年干和年支*/
        String nian_gan = BaseData.TIANGAN[(endy - 3 - 1 + 10) % 10];
        String nian_zhi = BaseData.DIZHI[(nian - 3 - 1 + 12) % 12];
        /**计算年干支结束已经获得年干支*/


        /**获取月干*/
        String yue_gan = nianShangQiYue(nian_gan, yue_zhi);

        /**获取日干支*/
        String riganzhi = getRiGanZhi(year, month + 1, day);

        /**获取时干支*/
        String shi_zhi = translateHourToZhi(hour);
        String shi_gan = riShangQiShi(riganzhi.charAt(0) + "", shi_zhi);


        JSONArray jarr = null;
        try {
            jarr = new JSONArray();

            jarr.put(nian_gan + nian_zhi);

            jarr.put(yue_gan + yue_zhi);

            jarr.put(riganzhi);

            jarr.put(shi_gan + shi_zhi);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jarr;
    }


    public String nianShangQiYue(String nian_gan, String yue_zhi) {
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            if (nian_gan.equals(BaseData.TIANGAN[i])) {
                if ((i + 5) % 5 == 0)
                    idx = 2;//"丙";
                if ((i + 5) % 5 == 1)
                    idx = 4;//"戊";
                if ((i + 5) % 5 == 2)
                    idx = 6;//"庚";
                if ((i + 5) % 5 == 3)
                    idx = 8;//"壬";
                if ((i + 5) % 5 == 4)
                    idx = 0;//"甲";

                break;
            }
        }

        int cnt = 0;
        for (int i = 2; i < 14; i++) {
            if (yue_zhi.equals(BaseData.DIZHI[i % 12]))
                break;

            cnt++;
        }

        String yue_gan = BaseData.TIANGAN[(idx + cnt) % 10];

        return yue_gan;
    }


    public String riShangQiShi(String ri_gan, String shi_zhi) {
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            if (ri_gan.equals(BaseData.TIANGAN[i])) {
                if ((i + 5) % 5 == 0)
                    idx = 0;//"甲";
                if ((i + 5) % 5 == 1)
                    idx = 2;//"丙";
                if ((i + 5) % 5 == 2)
                    idx = 4;//"戊";
                if ((i + 5) % 5 == 3)
                    idx = 6;//"庚";
                if ((i + 5) % 5 == 4)
                    idx = 8;//"壬";

                break;
            }
        }

        int cnt = 0;
        for (int i = 0; i < 12; i++) {
            if (shi_zhi.equals(BaseData.DIZHI[i]))
                break;

            cnt++;
        }

        String shi_gan = BaseData.TIANGAN[(idx + cnt) % 10];

        return shi_gan;
    }

    private String getRiGanZhi(int year, int month, int day) {
        /**
         从已知日期计算干支纪日的公式和蔡勒公式很相像，如下：

         g = 4C + [C/4] + 5y + [y/4] + [3*(M+1) / 5] + d - 3
         z = 8C + [C/4] + 5y + [y/4] + [3*(M+1) / 5] + d + 7 + i

         (奇数月i=0，偶数月i=6) 其中C是世纪数减一，y是年份后两位，M是月份，d是日数。
         1月和2月按上一年的13月和14月来算。
         (g-1)除以10的余数是天干，(z-1)除以12的余数是地支。

         **/


        int y = 0, M = 0, d = 0, i = 0;

        M = month;
        if (M == 1 || M == 2) {
            year = year - 1;
            M = M + 12;
        }

        if (M % 2 == 0)
            i = 6;
        if (M % 2 == 1)
            i = 0;

        String ys = String.valueOf(year);
        y = Integer.parseInt(ys.substring(ys.length() - 2));

        d = day;

        int C = year / 100 + 1;
        C = C - 1;

        int gan = 4 * C + C / 4 + 5 * y + y / 4 + 3 * (M + 1) / 5 + d - 3;
        int zhi = 8 * C + C / 4 + 5 * y + y / 4 + 3 * (M + 1) / 5 + d + 7 + i;

        int g = (gan - 1) % 10;
        int z = (zhi - 1) % 12;

        return BaseData.TIANGAN[g] + BaseData.DIZHI[z];
    }

    /**
     * 把公历的24小时制的时，转换成中国传统计时的子，丑，寅，卯。。。制时间
     */
    public String translateHourToZhi(int hour) {
        int h = hour % 24;

        String shi_zhi;
        int idx = 0;
        for (int i = 0; i <= 12; i++) {
            idx = 2 * i + 1;
            if (h < idx) {
                return BaseData.DIZHI[((idx - 1) / 2) % 12];
            }
        }

        return null;
    }


    /**
     * 查空亡
     * 根据传递进来的日的天干地支，获得旬空，参数riganzhi形如“甲子”
     */
    public String getKongWang(String riganzhi) {
        String tiangan = riganzhi.charAt(0) + "";
        String dizhi = riganzhi.charAt(1) + "";

        int cnt = 0;
        for (int i = 0; i < 10; i++) {
            if (tiangan.equals(BaseData.TIANGAN[i])) {
                cnt = 10 - i;
                break;
            }
        }

        int base = 0;
        for (int i = 0; i < 12; i++) {
            if (dizhi.equals(BaseData.DIZHI[i])) {
                base = i;
                break;
            }
        }


        int index = (base + cnt) % 12;

        return BaseData.DIZHI[index] + BaseData.DIZHI[index + 1];
    }


    public static HashMap get_JieQiInMonth(int year, int month, boolean isJavaCalendarStyleMonth) {
        if (!isJavaCalendarStyleMonth)
            month = month - 1;

        /**取年的后两位*/
        String yearstr = String.valueOf(year);
        int Y = Integer.parseInt(yearstr.substring(yearstr.length() - 2));

		/*取21和20世纪，即1900年~2100年
		*小寒的修正参数排在最前面，接着是立春
		*/
        //double[] JIEQI_CONSTANT21={5.4055,3.87,5.63,4.81,5.52,5.678,7.108,7.5,7.646,8.318,7.438,7.18};
        //double[] JIEQI_CONSTANT20={6.11,4.15,5.63,5.59,6.318,6.5,7.928,8.35,8.44,9.098,8.218,7.9};
        //double	D=0.2422;

        double JIEQI_CONSTANT = 0.0;
        int century = year / 100 + 1;
        if (century == 21)
            JIEQI_CONSTANT = JIEQI_CONSTANT21[month];
        if (century == 20)
            JIEQI_CONSTANT = JIEQI_CONSTANT20[month];

        int k = 0;
        if (month == 0 || month == 1)
            k = 1;

        int JIEQI_DAY = (int) (Y * D + JIEQI_CONSTANT) - (Y - k) / 4;

        /**修正值*/
        if (year == 1982 && month == 0)
            JIEQI_DAY = JIEQI_DAY + 1;
        if (year == 2019 && month == 0)
            JIEQI_DAY = JIEQI_DAY - 1;
        if (year == 1911 && month == 4)
            JIEQI_DAY = JIEQI_DAY + 1;
        if (year == 1902 && month == 5)
            JIEQI_DAY = JIEQI_DAY + 1;
        if ((year == 1925 || year == 2016) && month == 6)
            JIEQI_DAY = JIEQI_DAY + 1;
        if (year == 2002 && month == 7)
            JIEQI_DAY = JIEQI_DAY + 1;
        if (year == 1927 && month == 8)
            JIEQI_DAY = JIEQI_DAY + 1;
        if (year == 2089 && month == 10)
            JIEQI_DAY = JIEQI_DAY + 1;
        if (year == 1954 && month == 11)
            JIEQI_DAY = JIEQI_DAY + 1;

        //final	String[] JIEQI_NAME={"小寒","立春","惊蛰","清明","立夏","芒种","小暑","立秋","白露","寒露","立冬","大雪"};

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("节气名称", BaseData.JIEQI_NAME[month]);
        map.put("节气日期", year + "年" + (month + 1) + "月" + JIEQI_DAY + "日");

        return map;
    }
}
