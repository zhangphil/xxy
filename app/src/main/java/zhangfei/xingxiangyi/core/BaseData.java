package zhangfei.xingxiangyi.core;

/**
 * Created by Phil on 2017/4/25.
 */

import org.json.JSONArray;
import org.json.JSONObject;


public class BaseData {
    public static final String[] WUXING = {"金", "水", "木", "火", "土"};
    public static final String[] LIUQIN = {"兄弟", "子孙", "妻财", "官鬼", "父母"};
    public static final String[] LIUSHOU = {"青龙", "朱雀", "勾陈", "螣蛇", "白虎", "玄武"};
    /**
     * 六兽顺序
     */
    public static final String[] TIANGAN = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    public static final String[] DIZHI = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    public static final String[] GUIHUN_YOUHUN = {"游魂", "归魂"};
    public static final String[] DaysOfWeek = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    public static final String[] JIEQI_NAME = {"小寒", "立春", "惊蛰", "清明", "立夏", "芒种", "小暑", "立秋", "白露", "寒露", "立冬", "大雪"};


    public static JSONArray JA_DIZHI = null;
    public static JSONArray JA_BAGONG = null;
    public static JSONObject JO_GUA64 = null;
    public static final String[] LIUHE = {"子丑", "寅亥", "卯戌", "辰酉", "巳申", "午未"};
    public static final String[] LIUCHONG = {"子午", "寅申", "卯酉", "辰戌", "巳亥", "丑未"};

    public static final String YANG = "▅▅▅▅▅", YING = "▅▅　  ▅▅";

    public static void  init(){
        BaseData baseData=new BaseData();
    }

    public BaseData() {
        try {
            String dz = "[" +
                    "{'地支':'子','地支序':1,'五行属性':'水'}," +
                    "{'地支':'丑','地支序':2,'五行属性':'土'}," +
                    "{'地支':'寅','地支序':3,'五行属性':'木'}," +
                    "{'地支':'卯','地支序':4,'五行属性':'木'}," +
                    "{'地支':'辰','地支序':5,'五行属性':'土'}," +
                    "{'地支':'巳','地支序':6,'五行属性':'火'}," +
                    "{'地支':'午','地支序':7,'五行属性':'火'}," +
                    "{'地支':'未','地支序':8,'五行属性':'土'}," +
                    "{'地支':'申','地支序':9,'五行属性':'金'}," +
                    "{'地支':'酉','地支序':10,'五行属性':'金'}," +
                    "{'地支':'戌','地支序':11,'五行属性':'土'}," +
                    "{'地支':'亥','地支序':12,'五行属性':'水'}," +
                    "]";

            JA_DIZHI = new JSONArray(dz);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            String bg = "[" +
                    "{'宫名':'乾宫','宫序':1,'五行属性':'金'}," +
                    "{'宫名':'兑宫','宫序':2,'五行属性':'金'}," +
                    "{'宫名':'离宫','宫序':3,'五行属性':'火'}," +
                    "{'宫名':'震宫','宫序':4,'五行属性':'木'}," +
                    "{'宫名':'巽宫','宫序':5,'五行属性':'木'}," +
                    "{'宫名':'坎宫','宫序':6,'五行属性':'水'}," +
                    "{'宫名':'艮宫','宫序':7,'五行属性':'土'}," +
                    "{'宫名':'坤宫','宫序':8,'五行属性':'土'}," +
                    "]";

            JA_BAGONG = new JSONArray(bg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String gua = "{" +
                    "'乾宫':[" +
                    "{'卦序':1,'卦名':'乾为天','卦爻':'111111','天干':'甲壬','地支':'子寅辰午申戌'}," +
                    "{'卦序':2,'卦名':'天风姤','卦爻':'011111','天干':'辛壬','地支':'丑亥酉午申戌'}," +
                    "{'卦序':3,'卦名':'天山遁','卦爻':'001111','天干':'丙壬','地支':'辰午申午申戌'}," +
                    "{'卦序':4,'卦名':'天地否','卦爻':'000111','天干':'乙壬','地支':'未巳卯午申戌'}," +
                    "{'卦序':5,'卦名':'风地观','卦爻':'000011','天干':'乙辛','地支':'未巳卯未巳卯'}," +
                    "{'卦序':6,'卦名':'山地剥','卦爻':'000001','天干':'乙丙','地支':'未巳卯戌子寅'}," +
                    "{'卦序':7,'卦名':'火地晋','卦爻':'000101','天干':'乙己','地支':'未巳卯酉未巳'}," +
                    "{'卦序':8,'卦名':'火天大有','卦爻':'111101','天干':'甲己','地支':'子寅辰酉未巳'}" +
                    "]," +

                    "'兑宫':[" +
                    "{'卦序':9,'卦名':'兑为泽','卦爻':'110110','天干':'丁丁','地支':'巳卯丑亥酉未'}," +
                    "{'卦序':10,'卦名':'泽水困','卦爻':'010110','天干':'戊丁','地支':'寅辰午亥酉未'}," +
                    "{'卦序':11,'卦名':'泽地萃','卦爻':'000110','天干':'乙丁','地支':'未巳卯亥酉未'}," +
                    "{'卦序':12,'卦名':'泽山咸','卦爻':'001110','天干':'丙丁','地支':'辰午申亥酉未'}," +
                    "{'卦序':13,'卦名':'水山蹇','卦爻':'001010','天干':'丙戊','地支':'辰午申申戌子'}," +
                    "{'卦序':14,'卦名':'地山谦','卦爻':'001000','天干':'丙癸','地支':'辰午申丑亥酉'}," +
                    "{'卦序':15,'卦名':'雷山小过','卦爻':'001100','天干':'丙庚','地支':'辰午申午申戌'}," +
                    "{'卦序':16,'卦名':'雷泽归妹','卦爻':'110100','天干':'丁庚','地支':'巳卯丑午申戌'}" +
                    "]," +

                    "'离宫':[" +
                    "{'卦序':17,'卦名':'离为火','卦爻':'101101','天干':'己己','地支':'卯丑亥酉未巳'}," +
                    "{'卦序':18,'卦名':'火山旅','卦爻':'001101','天干':'丙己','地支':'辰午申酉未巳'}," +
                    "{'卦序':19,'卦名':'火风鼎','卦爻':'011101','天干':'辛己','地支':'丑亥酉酉未巳'}," +
                    "{'卦序':20,'卦名':'火水未济','卦爻':'010101','天干':'戊己','地支':'寅辰午酉未巳'}," +
                    "{'卦序':21,'卦名':'山水蒙','卦爻':'010001','天干':'戊丙','地支':'寅辰午戌子寅'}," +
                    "{'卦序':22,'卦名':'风水涣','卦爻':'010011','天干':'戊辛','地支':'寅辰午未巳卯'}," +
                    "{'卦序':23,'卦名':'天水讼','卦爻':'010111','天干':'戊壬','地支':'寅辰午午申戌'}," +
                    "{'卦序':24,'卦名':'天火同人','卦爻':'101111','天干':'己壬','地支':'卯丑亥午申戌'}" +
                    "]," +

                    "'震宫':[" +
                    "{'卦序':25,'卦名':'震为雷','卦爻':'100100','天干':'庚庚','地支':'子寅辰午申戌'}," +
                    "{'卦序':26,'卦名':'雷地豫','卦爻':'000100','天干':'乙庚','地支':'未巳卯午申戌'}," +
                    "{'卦序':27,'卦名':'雷水解','卦爻':'010100','天干':'戊庚','地支':'寅辰午午申戌'}," +
                    "{'卦序':28,'卦名':'雷风恒','卦爻':'011100','天干':'辛庚','地支':'丑亥酉午申戌'}," +
                    "{'卦序':29,'卦名':'地风升','卦爻':'011000','天干':'辛癸','地支':'丑亥酉丑亥酉'}," +
                    "{'卦序':30,'卦名':'水风井','卦爻':'011010','天干':'辛戊','地支':'丑亥酉申戌子'}," +
                    "{'卦序':31,'卦名':'泽风大过','卦爻':'011110','天干':'辛丁','地支':'丑亥酉亥酉未'}," +
                    "{'卦序':32,'卦名':'泽雷随','卦爻':'100110','天干':'庚丁','地支':'子寅辰亥酉未'}" +
                    "]," +

                    "'巽宫':[" +
                    "{'卦序':33,'卦名':'巽为风','卦爻':'011011','天干':'辛辛','地支':'丑亥酉未巳卯'}," +
                    "{'卦序':34,'卦名':'风天小畜','卦爻':'111011','天干':'甲辛','地支':'子寅辰未巳卯'}," +
                    "{'卦序':35,'卦名':'风火家人','卦爻':'101011','天干':'己辛','地支':'卯丑亥未巳卯'}," +
                    "{'卦序':36,'卦名':'风雷益','卦爻':'100011','天干':'庚辛','地支':'子寅辰未巳卯'}," +
                    "{'卦序':37,'卦名':'天雷无妄','卦爻':'100111','天干':'庚壬','地支':'子寅辰午申戌'}," +
                    "{'卦序':38,'卦名':'火雷噬嗑','卦爻':'100101','天干':'庚己','地支':'子寅辰酉未巳'}," +
                    "{'卦序':39,'卦名':'山雷颐','卦爻':'100001','天干':'庚丙','地支':'子寅辰戌子寅'}," +
                    "{'卦序':40,'卦名':'山风蛊','卦爻':'011001','天干':'辛丙','地支':'丑亥酉戌子寅'}" +
                    "]," +

                    "'坎宫':[" +
                    "{'卦序':41,'卦名':'坎为水','卦爻':'010010','天干':'戊戊','地支':'寅辰午申戌子'}," +
                    "{'卦序':42,'卦名':'水泽节','卦爻':'110010','天干':'丁戊','地支':'巳卯丑申戌子'}," +
                    "{'卦序':43,'卦名':'水雷屯','卦爻':'100010','天干':'庚戊','地支':'子寅辰申戌子'}," +
                    "{'卦序':44,'卦名':'水火既济','卦爻':'101010','天干':'己戊','地支':'卯丑亥申戌子'}," +
                    "{'卦序':45,'卦名':'泽火革','卦爻':'101110','天干':'己丁','地支':'卯丑亥亥酉未'}," +
                    "{'卦序':46,'卦名':'雷火丰','卦爻':'101100','天干':'己庚','地支':'卯丑亥午申戌'}," +
                    "{'卦序':47,'卦名':'地火明夷','卦爻':'101000','天干':'己癸','地支':'卯丑亥丑亥酉'}," +
                    "{'卦序':48,'卦名':'地水师','卦爻':'010000','天干':'戊癸','地支':'寅辰午丑亥酉'}" +
                    "]," +

                    "'艮宫':[" +
                    "{'卦序':49,'卦名':'艮为山','卦爻':'001001','天干':'丙丙','地支':'辰午申戌子寅'}," +
                    "{'卦序':50,'卦名':'山火贲','卦爻':'101001','天干':'己丙','地支':'卯丑亥戌子寅'}," +
                    "{'卦序':51,'卦名':'山天大畜','卦爻':'111001','天干':'甲丙','地支':'子寅辰戌子寅'}," +
                    "{'卦序':52,'卦名':'山泽损','卦爻':'110001','天干':'丁丙','地支':'巳卯丑戌子寅'}," +
                    "{'卦序':53,'卦名':'火泽睽','卦爻':'110101','天干':'丁己','地支':'巳卯丑酉未巳'}," +
                    "{'卦序':54,'卦名':'天泽履','卦爻':'110111','天干':'丁壬','地支':'巳卯丑午申戌'}," +
                    "{'卦序':55,'卦名':'风泽中孚','卦爻':'110011','天干':'丁辛','地支':'巳卯丑未巳卯'}," +
                    "{'卦序':56,'卦名':'风山渐','卦爻':'001011','天干':'丙辛','地支':'辰午申未巳卯'}" +
                    "]," +

                    "'坤宫':[" +
                    "{'卦序':57,'卦名':'坤为地','卦爻':'000000','天干':'乙癸','地支':'未巳卯丑亥酉'}," +
                    "{'卦序':58,'卦名':'地雷复','卦爻':'100000','天干':'庚癸','地支':'子寅辰丑亥酉'}," +
                    "{'卦序':59,'卦名':'地泽临','卦爻':'110000','天干':'丁癸','地支':'巳卯丑丑亥酉'}," +
                    "{'卦序':60,'卦名':'地天泰','卦爻':'111000','天干':'甲癸','地支':'子寅辰丑亥酉'}," +
                    "{'卦序':61,'卦名':'雷天大壮','卦爻':'111100','天干':'甲庚','地支':'子寅辰午申戌'}," +
                    "{'卦序':62,'卦名':'泽天夬','卦爻':'111110','天干':'甲丁','地支':'子寅辰亥酉未'}," +
                    "{'卦序':63,'卦名':'水天需','卦爻':'111010','天干':'甲戊','地支':'子寅辰申戌子'}," +
                    "{'卦序':64,'卦名':'水地比','卦爻':'000010','天干':'乙戊','地支':'未巳卯申戌子'}" +
                    "]" +

                    "}";


            JO_GUA64 = new JSONObject(gua);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println("基础数据初始化完成");
    }
}