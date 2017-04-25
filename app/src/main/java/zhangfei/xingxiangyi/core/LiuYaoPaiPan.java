package zhangfei.xingxiangyi.core;

import org.json.*;
//import	java.util.Iterator;

public class LiuYaoPaiPan {
    public JSONObject paiPan(String original_guayao, String yueganzhi, String riganzhi) {
        JSONObject jo = liuYaoPaiPan(original_guayao);
        try {
            ShenSha ss = new ShenSha();

            jo.put("六神", ss.getLiuShen(riganzhi));
            jo.put("神煞", ss.getShenSha(yueganzhi, riganzhi));
            //jo.put("空亡",getKongWang(riganzhi));

            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 对原始的卦爻记录先进行处理，如“02113”，
     * 此时的“0”表示为三个字面，本卦中为“0”，变卦为“1”
     * "3"表示三个背面，本卦为“1”，变卦为“0”
     * "2"表示两个背面，转换后为“0”
     * “1”表示一个背面，转换后为“1”
     */
    public JSONObject liuYaoPaiPan(String original_guayao) {
        JSONObject RESULT = null;

        String benguaguayao = "";
        String bianguaguayao = "";

        try {
            RESULT = new JSONObject();
            RESULT.put("原始卦爻", original_guayao);

            JSONObject joDongYao = new JSONObject();/**记录动爻*/
            JSONArray ja = new JSONArray();

            String s = "";
            for (int i = 0; i < original_guayao.length(); i++) {
                s = original_guayao.charAt(i) + "";

                if (s.equals("0")) {
                    benguaguayao = benguaguayao + "0";
                    bianguaguayao = bianguaguayao + "1";

                    JSONObject jo = new JSONObject();
                    //jo.put((i+1)+"","x");
                    jo.put(i + "", "x");

                    ja.put(jo);
                }
                if (s.equals("1")) {
                    benguaguayao = benguaguayao + "1";
                    bianguaguayao = bianguaguayao + "1";
                }
                if (s.equals("2")) {
                    benguaguayao = benguaguayao + "0";
                    bianguaguayao = bianguaguayao + "0";
                }
                if (s.equals("3")) {
                    benguaguayao = benguaguayao + "1";
                    bianguaguayao = bianguaguayao + "0";

                    JSONObject jo = new JSONObject();
                    //jo.put(i+1+"","o");
                    jo.put(i + "", "o");

                    ja.put(jo);
                }
            }


            RESULT.put("本卦阴阳爻", benguaguayao);
            RESULT.put("变卦阴阳爻", bianguaguayao);
            RESULT.put("伏神", getFuShen(benguaguayao));
            RESULT.put("本卦", getBenGua(benguaguayao));
            RESULT.put("动爻", ja);

            if (!benguaguayao.equals(bianguaguayao)) {
                RESULT.put("变卦", getBianGua(getGong(benguaguayao).getString("宫名"), bianguaguayao));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return RESULT;
    }

    /**
     * 根据卦爻获得本卦
     */
    private JSONObject getBenGua(String guayao) {
        JSONObject jobj = null;
        try {
            jobj = new JSONObject();

            String gongming = getGong(guayao).getString("宫名");
            jobj.put("宫名", gongming);
            jobj.put("合冲", getHeChongGua(guayao));

            JSONObject jo = getGua(guayao);

            int guaxu = jo.getInt("卦序");
            if (guaxu % 8 == 7)
                jobj.put("归魂游魂", BaseData.GUIHUN_YOUHUN[0]);
            if (guaxu % 8 == 0)
                jobj.put("归魂游魂", BaseData.GUIHUN_YOUHUN[1]);

            jobj.put("卦名", jo.getString("卦名"));

            jobj.put("世", getShiWei(guaxu));

            String dizhi6 = jo.getString("地支");

            JSONArray jarrGuaYao = new JSONArray();

            for (int i = 0; i < 6; i++) {
                String tiangan = "";

                if (i < 3)
                    tiangan = jo.getString("天干").charAt(0) + "";
                else
                    tiangan = jo.getString("天干").charAt(1) + "";

                String value = getLiuQin(gongming, dizhi6.charAt(i) + "") + tiangan + dizhi6.charAt(i) + getDiZhiWuXing(dizhi6.charAt(i) + "");
                //int key=i+1;
                //JSONObject obj=new JSONObject();
                //obj.put(key+"",value);

                //jarrGuaYao.put(obj);
                jarrGuaYao.put(value);
            }

            jobj.put("卦爻", jarrGuaYao);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return jobj;
    }


    private JSONObject getBianGua(String benguagongming, String guayao) {
        JSONObject jobj = null;
        try {
            jobj = new JSONObject();

            String bianGuaGongMing = getGong(guayao).getString("宫名");
            jobj.put("宫名", bianGuaGongMing);

            jobj.put("合冲", getHeChongGua(guayao));

            JSONObject jo = getGua(guayao);
            jobj.put("卦名", jo.getString("卦名"));

            int guaxu = jo.getInt("卦序");
            if (guaxu % 8 == 7)
                jobj.put("归魂游魂", BaseData.GUIHUN_YOUHUN[0]);
            if (guaxu % 8 == 0)
                jobj.put("归魂游魂", BaseData.GUIHUN_YOUHUN[1]);

            jobj.put("世", getShiWei(guaxu));

            String dizhi6 = jo.getString("地支");

            JSONArray jarrGuaYao = new JSONArray();

            for (int i = 0; i < 6; i++) {
                String tiangan = "";

                if (i < 3)
                    tiangan = jo.getString("天干").charAt(0) + "";
                else
                    tiangan = jo.getString("天干").charAt(1) + "";

                String value = getLiuQin(benguagongming, dizhi6.charAt(i) + "") + tiangan + dizhi6.charAt(i) + getDiZhiWuXing(dizhi6.charAt(i) + "");
                //int key=i+1;
                //JSONObject obj=new JSONObject();
                //obj.put(key+"",value);

                //jarrGuaYao.put(obj);
                jarrGuaYao.put(value);
            }

            jobj.put("卦爻", jarrGuaYao);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jobj;
    }


    /**
     * 根据传进来的卦爻，找出伏神，然后封装成JSONObject对象，返回
     * 形如：{"妻财甲寅木":4,"官鬼壬午火":3}，4,3分别表示爻位。
     */
    private JSONArray getFuShen(String guayao) {
        String gongming = "";
        String dizhi6 = "";/**本卦的6个地支*/

        try {
            gongming = getGong(guayao).getString("宫名");
            dizhi6 = getGua(guayao).getString("地支");
        } catch (Exception e) {
            e.printStackTrace();
        }


        String[] lq6 = new String[6];/**此为本卦的6个六亲字符串*/
        for (int i = 0; i < 6; i++)
            lq6[i] = getLiuQin(gongming, dizhi6.charAt(i) + "");
        /**lq[6]中已装入本卦的六亲*/

        int cnt = 0;
        String[] queshaodeliuqin = new String[6];/**缺少的六亲*/

        for (int i = 0; i < 5; i++) {
            int j = 0;
            for (j = 0; j < 6; j++) {
                if (BaseData.LIUQIN[i].equals(lq6[j]))
                    break;
            }
            if (j == 6) {
                queshaodeliuqin[cnt] = BaseData.LIUQIN[i];
                cnt++;
            }
        }


        String firstGuaDiZhi6 = "";/**本宫的首卦，第一卦地支*/
        JSONObject firstGuaJO = null;
        try {
            JSONArray jarr = BaseData.JO_GUA64.getJSONArray(gongming);
            int gx = 0;
            for (int i = 0; i < 8; i++) {
                firstGuaJO = jarr.getJSONObject(i);
                gx = firstGuaJO.getInt("卦序");
                if ((gx % 8) == 1) {
                    firstGuaDiZhi6 = firstGuaJO.getString("地支");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        JSONArray jarr = null;

        try {
            jarr = new JSONArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**比较缺少的六亲和本宫第一卦的六亲，相同则记录下地支和爻位，同时补上天干*/
        String value;
        int key;
        for (int i = 0; i < cnt; i++) {
            String fslq = queshaodeliuqin[i];

            String tiangan = "";
            for (int j = 0; j < 6; j++) {
                String s = getLiuQin(gongming, firstGuaDiZhi6.charAt(j) + "");
                if (fslq.equals(s)) {
                    try {
                        if (j < 3)
                            tiangan = firstGuaJO.getString("天干").charAt(0) + "";
                        else
                            tiangan = firstGuaJO.getString("天干").charAt(1) + "";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    value = fslq + tiangan + firstGuaDiZhi6.charAt(j) + getDiZhiWuXing(firstGuaDiZhi6.charAt(j) + "");
                    //key=j+1;
                    key = j;
                    try {
                        JSONObject jo = new JSONObject();
                        jo.put(key + "", value);
                        jarr.put(jo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        return jarr;
    }


    /**
     * 传递进来卦的卦爻，判断该卦是六合或六冲卦
     */
    private String getHeChongGua(String guayao) {
        /**String guayao="011111";*/
        String dizhi = "";
        try {
            dizhi = getGua(guayao).getString("地支");
        } catch (Exception e) {
            e.printStackTrace();
        }


        String first = "", end = "", target_a = "", target_b = "";
        first = dizhi.charAt(0) + "";
        end = dizhi.charAt(3) + "";

        target_a = first + end;
        target_b = end + first;

        for (int j = 0; j < 6; j++) {
            if (target_a.equals(BaseData.LIUHE[j]))
                return "六合";
            if (target_a.equals(BaseData.LIUCHONG[j]))
                return "六冲";

            if (target_b.equals(BaseData.LIUHE[j]))
                return "六合";
            if (target_b.equals(BaseData.LIUCHONG[j]))
                return "六冲";
        }

        return "";
    }


    /**
     * 根据卦爻，获得该卦所属的宫，返回值形如：
     * {'宫名':'离宫','宫序':3,'五行属性':'火'}
     */
    private JSONObject getGong(String guayao) {
        boolean find = false;
        for (int i = 0; i < 8 && (!find); i++) {
            try {
                JSONObject jobj = BaseData.JA_BAGONG.getJSONObject(i);
                String gongming = jobj.getString("宫名");

                JSONArray jarr = BaseData.JO_GUA64.getJSONArray(gongming);

                for (int j = 0; j < 8; j++) {
                    JSONObject jo = jarr.getJSONObject(j);
                    if (jo.getString("卦爻").equals(guayao)) {
                        find = true;

                        return jobj;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * 根据卦爻，找到卦，返回一个JSONObject对象，
     * 形如：{'卦序':1,'卦名':'乾为天','卦爻':'111111','天干':'甲壬','地支':'子寅辰午申戌','世':6}
     */
    public JSONObject getGua(String guayao) {
        boolean find = false;
        for (int i = 0; i < 8 && (!find); i++) {
            try {
                JSONObject jobj = BaseData.JA_BAGONG.getJSONObject(i);
                String gongming = jobj.getString("宫名");

                JSONArray jarr = BaseData.JO_GUA64.getJSONArray(gongming);

                for (int j = 0; j < 8; j++) {
                    JSONObject jo = jarr.getJSONObject(j);
                    if (jo.getString("卦爻").equals(guayao)) {
                        find = true;

                        return jo;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * 当得知所在宫时，传递进来1个地支，根据本卦所在的宫，
     * 获得该地支的六亲属性
     */
    public String getLiuQin(String gong, String dizhi) {
        String gongwuxing = getGongWuXing(gong);
        String dizhiwuxing = getDiZhiWuXing(dizhi);

        int i = 0;
        for (i = 0; i < 5; i++) {
            if (gongwuxing.equals(BaseData.WUXING[i]))
                break;
        }

        int j = 0;
        for (j = 0; j < 5; j++) {
            if (dizhiwuxing.equals(BaseData.WUXING[j]))
                break;
        }

        int len = (j - i + 5) % 5;

        return BaseData.LIUQIN[len];
    }


    /**
     * 根据宫的名字查找该宫的五行属性
     */
    private String getGongWuXing(String gongming) {
        JSONObject jo = null;
        for (int i = 0; i < 8; i++) {
            try {
                jo = BaseData.JA_BAGONG.getJSONObject(i);
                if (jo.getString("宫名").equals(gongming)) {
                    return jo.getString("五行属性");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 根据地支名字，查出该地支的五行属性
     */
    private String getDiZhiWuXing(String dizhi) {
        try {
            JSONObject jo = null;

            for (int i = 0; i < 12; i++) {
                jo = BaseData.JA_DIZHI.getJSONObject(i);
                if (jo.getString("地支").equals(dizhi)) {
                    return jo.getString("五行属性");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 根据卦序判断该卦得世爻所在爻位
     */
    private int getShiWei(int guaxu) {
        int shiwei = 0;

        switch (guaxu % 8) {
            case 0:
                shiwei = 2;
                break;
            case 1:
                shiwei = 5;
                break;
            case 2:
                shiwei = 0;
                break;
            case 3:
                shiwei = 1;
                break;
            case 4:
                shiwei = 2;
                break;
            case 5:
                shiwei = 3;
                break;
            case 6:
                shiwei = 4;
                break;
            case 7:
                shiwei = 3;
                break;
        }

        return shiwei;
    }
}