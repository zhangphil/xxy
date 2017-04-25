package zhangfei.xingxiangyi.core;


import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;


public class ShenSha {
    /**
     * 根据月日干支获得神煞
     */
    public JSONArray getShenSha(String yueGanZhi, String riGanZhi) {
        String yueZhi = yueGanZhi.charAt(1) + "";
        String riGan = riGanZhi.charAt(0) + "";
        String riZhi = riGanZhi.charAt(1) + "";

        JSONArray ja_YiMa = null;/**驿马*/
        JSONArray ja_TianXi = null;/**天喜*/
        String[] str_RiLu = {"寅", "卯", "巳", "午", "巳", "午", "申", "酉", "亥", "子"};/**日禄*/
        JSONArray ja_GuiRen = null;/**贵人*/
        JSONArray ja_TaoHua = null;/**桃花*/

        JSONArray ja_ShenSha = null;

        try {
            ja_ShenSha = new JSONArray();

            ja_YiMa = new JSONArray("[{'寅':'申子辰'},{'巳':'亥卯未'},{'申':'寅午戌'},{'亥':'巳酉丑'}]");
            ja_TianXi = new JSONArray("[{'戌':'寅卯辰'},{'丑':'巳午未'},{'辰':'申酉戌'},{'未':'亥子丑'}]");
            ja_TaoHua = new JSONArray("[{'子':'亥卯未'},{'午':'巳酉丑'},{'卯':'寅午戌'},{'酉':'申子辰'}]");
            ja_GuiRen = new JSONArray("[{'丑,未':'甲戊庚'},{'子,申':'乙己'},{'亥,酉':'丙丁'},{'卯,巳':'壬癸'},{'午,寅':'辛'}]");


            /**查找驿马*/
            JSONObject joyima = new JSONObject();
            joyima.put("驿马", findShenShaTool(ja_YiMa, riZhi));
            ja_ShenSha.put(joyima);

            /**查找天喜*/
            JSONObject jotianxi = new JSONObject();
            jotianxi.put("天喜", findShenShaTool(ja_TianXi, yueZhi));
            ja_ShenSha.put(jotianxi);

            /**查找贵人*/
            JSONObject joguiren = new JSONObject();
            joguiren.put("贵人", findShenShaTool(ja_GuiRen, riGan));
            ja_ShenSha.put(joguiren);

            /**寻找日禄神*/
            for (int i = 0; i < BaseData.TIANGAN.length; i++) {
                if (riGan.equals(BaseData.TIANGAN[i])) {
                    JSONObject jo = new JSONObject();
                    jo.put("日禄", str_RiLu[i]);

                    ja_ShenSha.put(jo);
                }
            }

            /**查找桃花*/
            JSONObject jotaohua = new JSONObject();
            jotaohua.put("桃花", findShenShaTool(ja_TaoHua, riZhi));
            ja_ShenSha.put(jotaohua);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ja_ShenSha;
    }

    /**
     * 查找神煞的辅助工具函数
     */
    private String findShenShaTool(JSONArray ja, String ganzhi) {
        try {
            boolean find = false;
            for (int i = 0; (i < ja.length()) && (!find); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Iterator it = jo.keys();
                while (it.hasNext()) {
                    String key = it.next() + "";
                    String value = jo.getString(key);

                    for (int j = 0; j < value.length(); j++) {
                        if ((value.charAt(j) + "").equals(ganzhi)) {
                            find = true;
                            return key;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 根据传递进来的日天干排六神
     */
    public JSONArray getLiuShen(String riganzhi) {
        String riTianGan = riganzhi.charAt(0) + "";

        /**六兽起法：
         *2.8.2之前是：甲乙起青龙.丙丁起朱雀.戊己起勾陈,庚辛起白虎.壬癸起玄武.
         *实际上2.8.2之前，六神起法在己日是错误的。戊日起勾陈没错，但己日应起螣蛇。2.8.2起，此错误得到修正。
         */
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            if (riTianGan.equals(BaseData.TIANGAN[i])) {
                idx = i;
                break;
            }
        }

        String firstLiuShou = BaseData.LIUSHOU[0];
        switch (idx) {
            case 0:
                firstLiuShou = BaseData.LIUSHOU[0];
                break;
            case 1:
                firstLiuShou = BaseData.LIUSHOU[0];
                break;

            case 2:
                firstLiuShou = BaseData.LIUSHOU[1];
                break;
            case 3:
                firstLiuShou = BaseData.LIUSHOU[1];
                break;

            case 4:
                firstLiuShou = BaseData.LIUSHOU[2];
                break;

            case 5:
                firstLiuShou = BaseData.LIUSHOU[3];
                break;

            case 6:
                firstLiuShou = BaseData.LIUSHOU[4];
                break;
            case 7:
                firstLiuShou = BaseData.LIUSHOU[4];
                break;

            case 8:
                firstLiuShou = BaseData.LIUSHOU[5];
                break;
            case 9:
                firstLiuShou = BaseData.LIUSHOU[5];
                break;
        }

        int index = 0;
        for (int i = 0; i < 6; i++) {
            if (firstLiuShou.equals(BaseData.LIUSHOU[i])) {
                index = i;
                break;
            }
        }

        JSONArray jarr = null;
        try {
            jarr = new JSONArray();

            for (int i = 0; i < 6; i++) {
                jarr.put(BaseData.LIUSHOU[index % 6]);

                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jarr;
    }
}