package zhangfei.xingxiangyi.core;


import java.util.Iterator;
import org.json.*;

public class GuaFormat {
    public static final int COLUMN_LiuShen = 0;
    public static final int COLUMN_FuShen = 1;
    public static final int COLUMN_BenGuaYao = 2;
    public static final int COLUMN_BenGuaYinYang = 3;
    public static final int COLUMN_BenGuaShiYing = 4;
    public static final int COLUMN_DongYao = 5;

    public static final int COLUMN_BianGuaYao = 6;
    public static final int COLUMN_BianGuaYinYang = 7;
    public static final int COLUMN_BianGuaShiYing = 8;


    public StringBuffer format(JSONObject ALL) throws Exception {
        StringBuffer STRBUF = new StringBuffer();


        String[][] display = new String[8][9];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++)
                display[i][j] = null;
        }
        //System.out.println();
        //STRBUF.append("\n");


        JSONObject jo_bengua = ALL.getJSONObject("本卦");


        JSONObject jo = null;
        JSONArray ja = null;

        String gongli = ALL.getString("公历");
        //System.out.println("公历:"+gongli+"  "+ALL.getString("星期"));
        STRBUF.append("公历:" + gongli + "  " + ALL.getString("星期") + "\n");

        ja = ALL.getJSONArray("干支");
        String ganzhi = ja.getString(0) + "年 " + ja.getString(1) + "月 " + ja.getString(2) + "日 " + ja.getString(3) + "时";
        //System.out.println("干支:"+ganzhi+"  (空亡："+ALL.getString("空亡")+")");
        STRBUF.append("干支:" + ganzhi + "  (空亡：" + ALL.getString("空亡") + ")" + "\n");


        //System.out.print("神煞:");
        STRBUF.append("神煞:");
        ja = ALL.getJSONArray("神煞");
        for (int i = 0; i < ja.length(); i++) {
            jo = ja.getJSONObject(i);
            String names = jo.names().get(0) + "";
            //System.out.print(names[0]+"-"+jo.getString(names[0])+" ");
            STRBUF.append(names + "-" + jo.getString(names) + " ");
        }
        STRBUF.append("\n\n");/**换行后开始输出本变卦*/


        display[1][COLUMN_LiuShen] = "六神";
        ja = ALL.getJSONArray("六神");
        for (int i = 7; i > 1; i--)
            display[i][COLUMN_LiuShen] = ja.getString(7 - i);

        display[1][COLUMN_FuShen] = "伏神";
        ja = ALL.getJSONArray("伏神");
        for (int i = 0; i < ja.length(); i++) {
            jo = ja.getJSONObject(i);
            Iterator it = jo.keys();

            String key = "";
            String value = "";
            while (it.hasNext()) {
                key = (String) (it.next());
                value = jo.getString(key);
                int idx = 7 - Integer.parseInt(key);
                display[idx][COLUMN_FuShen] = value;
            }
        }

        String bengua_gongming = jo_bengua.getString("宫名");
        String bengua_guaming = jo_bengua.getString("卦名");
        String bengua_hechong = jo_bengua.getString("合冲");
        String bengua_guihunyouhun = null;
        if (jo_bengua.has("归魂游魂"))
            bengua_guihunyouhun = jo_bengua.getString("归魂游魂");

        display[0][COLUMN_BenGuaYao] = " " + bengua_gongming + ":" + bengua_guaming;
        if (!bengua_hechong.equals(""))
            display[0][COLUMN_BenGuaYao] = display[0][COLUMN_BenGuaYao] + "(" + bengua_hechong + ")";
        if (bengua_guihunyouhun != null)
            display[0][COLUMN_BenGuaYao] = display[0][COLUMN_BenGuaYao] + "(" + bengua_guihunyouhun + ")";

        display[1][COLUMN_BenGuaYao] = "【本   卦】";

        ja = jo_bengua.getJSONArray("卦爻");
        for (int i = 0; i < ja.length(); i++)
            display[7 - i][COLUMN_BenGuaYao] = ja.getString(i);

        String bengua_yinyangyao = ALL.getString("本卦阴阳爻");
        for (int i = 0; i < bengua_yinyangyao.length(); i++) {
            if ((bengua_yinyangyao.charAt(i) + "").equals("1"))
                display[7 - i][COLUMN_BenGuaYinYang] = BaseData.YANG;//▅▅
            if ((bengua_yinyangyao.charAt(i) + "").equals("0"))
                display[7 - i][COLUMN_BenGuaYinYang] = BaseData.YING;//▅ ▅
        }

        int bengua_shiwei = jo_bengua.getInt("世");
        int bengua_yingwei = (bengua_shiwei + 3) % 6;
        display[7 - bengua_shiwei][COLUMN_BenGuaShiYing] = "世";
        display[7 - bengua_yingwei][COLUMN_BenGuaShiYing] = "应";

        ja = ALL.getJSONArray("动爻");
        for (int i = 0; i < ja.length(); i++) {
            jo = ja.getJSONObject(i);
            Iterator it = jo.keys();

            String key = "";
            String value = "";
            while (it.hasNext()) {
                key = (String) (it.next());
                value = jo.getString(key);
                int idx = 7 - Integer.parseInt(key);
                display[idx][COLUMN_DongYao] = value;
            }
        }
        /**本卦卦爻、本卦阴阳爻、本卦世应、变爻部分结束*/


        /**开始排版变卦*/
        JSONObject jo_biangua = null;
        if (ALL.has("变卦")) {
            jo_biangua = ALL.getJSONObject("变卦");

            String biangua_gongming = jo_biangua.getString("宫名");
            String biangua_guaming = jo_biangua.getString("卦名");
            String biangua_hechong = jo_biangua.getString("合冲");
            String biangua_guihunyouhun = null;
            if (jo_biangua.has("归魂游魂"))
                biangua_guihunyouhun = jo_biangua.getString("归魂游魂");
            display[0][COLUMN_BianGuaYao] = biangua_gongming + ":" + biangua_guaming;
            if (!biangua_hechong.equals(""))
                display[0][COLUMN_BianGuaYao] = display[0][COLUMN_BianGuaYao] + "(" + biangua_hechong + ")";
            if (biangua_guihunyouhun != null)
                display[0][COLUMN_BianGuaYao] = display[0][6] + "(" + biangua_guihunyouhun + ")";

            display[1][COLUMN_BianGuaYao] = "【变   卦】";
            ja = jo_biangua.getJSONArray("卦爻");
            for (int i = 0; i < ja.length(); i++)
                display[7 - i][COLUMN_BianGuaYao] = ja.getString(i);

            String biangua_yinyangyao = ALL.getString("变卦阴阳爻");
            for (int i = 0; i < biangua_yinyangyao.length(); i++) {
                if ((biangua_yinyangyao.charAt(i) + "").equals("1"))
                    display[7 - i][COLUMN_BianGuaYinYang] = BaseData.YANG;
                if ((biangua_yinyangyao.charAt(i) + "").equals("0"))
                    display[7 - i][COLUMN_BianGuaYinYang] = BaseData.YING;
            }


            int biangua_shiwei = jo_biangua.getInt("世");
            int biangua_yingwei = (biangua_shiwei + 3) % 6;
            display[7 - biangua_shiwei][COLUMN_BianGuaShiYing] = "世";
            display[7 - biangua_yingwei][COLUMN_BianGuaShiYing] = "应";
        }


        STRBUF.append(String.format("%38s", display[0][COLUMN_BenGuaYao]));

        if (jo_biangua != null)
            STRBUF.append(String.format("%28s", display[0][COLUMN_BianGuaYao])).append("\n");
        else
            STRBUF.append("\n");


        STRBUF.append(display[1][COLUMN_LiuShen] + String.format("%8s", display[1][COLUMN_FuShen]) + String.format("%20s", display[1][COLUMN_BenGuaYao]));
        if (jo_biangua != null)
            STRBUF.append(String.format("%35s", display[1][COLUMN_BianGuaYao]) + "\n");
        else
            STRBUF.append("\n");


        int threshold = 9;
        if (jo_biangua == null)
            threshold = 5;

        for (int i = 2; i < 8; i++) {
            for (int j = 0; j < threshold; j++) {
                if (j == COLUMN_LiuShen) {
                    //STRBUF.append(String.format("%-3s",display[i][j]));
                    STRBUF.append(ToDBC(display[i][j] + " "));
                }
                if (j == COLUMN_FuShen) {
                    if (display[i][j] == null)
                        STRBUF.append(ToDBC(String.format("%21s", " ")));
                        //STRBUF.append(ToDBC("                      "));
                        //STRBUF.append("               \t\t\t");
                    else
                        STRBUF.append(ToDBC(display[i][j] + " "));
                }
                if (j == COLUMN_BenGuaYao) {
                    //STRBUF.append(ToDBC(String.format("%6s",display[i][j])+" "));
                    STRBUF.append(ToDBC(display[i][j] + " "));
                    //STRBUF.append(display[i][j]);
                }
                if (j == COLUMN_BenGuaYinYang) {
                    STRBUF.append(ToDBC(display[i][j] + "\t"));
                    //STRBUF.append(display[i][j]);
                }
                if (j == COLUMN_BenGuaShiYing) {
                    if (display[i][j] == null)
                        STRBUF.append(ToDBC("     "));
                        //STRBUF.append(ToDBC("\t"));
                    else
                        STRBUF.append(ToDBC(display[i][j] + " "));
                }
                if (j == COLUMN_DongYao) {
                    if (display[i][j] == null)
                        //STRBUF.append(String.format("%-4s"," ")+"\t");
                        STRBUF.append(ToDBC("    \t"));
                    else
                        //STRBUF.append(String.format("%-4s",display[i][j])+"\t");
                        STRBUF.append(ToDBC(display[i][j] + "  \t"));
                }

                if (j == COLUMN_BianGuaYao) {
                    //STRBUF.append(String.format("%8s",display[i][j])+" ");
                    STRBUF.append(ToDBC(display[i][j] + " "));
                }
                if (j == COLUMN_BianGuaYinYang) {
                    STRBUF.append(ToDBC(display[i][j] + "\t"));
                }
                if (j == COLUMN_BianGuaShiYing) {
                    if (display[i][j] == null)
                        STRBUF.append(ToDBC("\t"));
                    else
                        STRBUF.append(ToDBC(display[i][j]));
                }
            }

            STRBUF.append("\n");
        }

        return STRBUF;
    }


    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }

        return new String(c);
    }
}
