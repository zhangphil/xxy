package zhangfei.xingxiangyi.activitys;


import java.util.Calendar;


import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.content.Context;
import android.graphics.Color;
import android.widget.NumberPicker;
import android.view.LayoutInflater;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

import java.util.Random;

import zhangfei.xingxiangyi.MainActivity;
import zhangfei.xingxiangyi.R;
import zhangfei.xingxiangyi.core.GuaFormat;


public class PaiGua extends XingXiangYiActivity {


    private boolean SHOUGONGZHIDING = true;

    private String NAME = "";
    private EditText nameEditText = null;

    private String THINGS = "";
    private String GENDER = "男";
    private String BIRTHDAY_YEAR = "0000";

    private CheckBox genderMale = null, genderFemale = null;
    private TextView txtViewBirthday = null;

    private EditText editTextThings = null;

    private TextView txtViewYearMonthDay = null, txtViewHourMinute = null;
    private int Year = 0, Month = 0, Day = 0, Hour = 0, Minute = 0;

    //顺序必须这样排，不得改动次序
    private static final String[] gua6Yao = {GuaFormat.ToDBC("老阴 ▅▅　  ▅▅ ╳"), GuaFormat.ToDBC("少阳 ▅▅▅▅▅"), GuaFormat.ToDBC("少阴 ▅▅ 　 ▅▅"), GuaFormat.ToDBC("老阳 ▅▅▅▅▅ ○")};

    private TextView txtView6Yao, txtView5Yao, txtView4Yao, txtView3Yao, txtView2Yao, txtView1Yao;
    private int[] YaoValue = {-1, -1, -1, -1, -1, -1};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paigua);

        String qgfs = this.getIntent().getStringExtra(getString(R.string.qiguafangshi));

        if (qgfs.equals(getString(R.string.shougongzhiding)))
            SHOUGONGZHIDING = true;
        else
            SHOUGONGZHIDING = false;


        genderMale = (CheckBox) findViewById(R.id.checkBoxGenderMale);
        genderMale.setChecked(true);
        genderFemale = (CheckBox) findViewById(R.id.checkBoxGenderFemale);

        GenderCheckBoxListener genderCheckBoxListener = new GenderCheckBoxListener();
        genderMale.setOnCheckedChangeListener(genderCheckBoxListener);
        genderFemale.setOnCheckedChangeListener(genderCheckBoxListener);


        txtViewBirthday = (TextView) findViewById(R.id.txtViewBirthday);
        txtViewBirthday.setClickable(true);
        txtViewBirthday.setFocusable(true);
        //txtViewBirthday.setFocusableInTouchMode(true);
        txtViewBirthday.setOnClickListener(new TextViewBirthdayListener(this));


        nameEditText = (EditText) findViewById(R.id.editTextName);
        nameEditText.setHint("姓名");


        editTextThings = (EditText) findViewById(R.id.editTextThings);
        editTextThings.setHint("占卜事项");


        Calendar cal = Calendar.getInstance();
        Year = cal.get(Calendar.YEAR);
        Month = cal.get(Calendar.MONTH);
        Day = cal.get(Calendar.DAY_OF_MONTH);
        Hour = cal.get(Calendar.HOUR_OF_DAY);
        Minute = cal.get(Calendar.MINUTE);

        txtViewYearMonthDay = (TextView) findViewById(R.id.txtViewYearMonthDay);
        txtViewYearMonthDay.setClickable(true);
        txtViewYearMonthDay.setFocusable(true);
        txtViewYearMonthDay.setOnClickListener(new TextViewYearMonthDayListener(this));

        txtViewHourMinute = (TextView) findViewById(R.id.txtViewHourMinute);
        txtViewHourMinute.setClickable(true);
        txtViewHourMinute.setFocusable(true);
        txtViewHourMinute.setOnClickListener(new TextViewHourMinuteListener(this));

        txtViewYearMonthDay.setText(Year + "年" + (Month + 1) + "月" + Day + "日");
        txtViewHourMinute.setText(Hour + "时" + Minute + "分");


        TextViewYaoListener txtViewYaoListener = new TextViewYaoListener();
        txtView6Yao = (TextView) findViewById(R.id.txtView6Yao);
        txtView6Yao.setOnClickListener(txtViewYaoListener);
        txtView5Yao = (TextView) findViewById(R.id.txtView5Yao);
        txtView5Yao.setOnClickListener(txtViewYaoListener);
        txtView4Yao = (TextView) findViewById(R.id.txtView4Yao);
        txtView4Yao.setOnClickListener(txtViewYaoListener);
        txtView3Yao = (TextView) findViewById(R.id.txtView3Yao);
        txtView3Yao.setOnClickListener(txtViewYaoListener);
        txtView2Yao = (TextView) findViewById(R.id.txtView2Yao);
        txtView2Yao.setOnClickListener(txtViewYaoListener);
        txtView1Yao = (TextView) findViewById(R.id.txtView1Yao);
        txtView1Yao.setOnClickListener(txtViewYaoListener);


        TextView txtViewPaiPan = (TextView) findViewById(R.id.txtViewPaiPan);
        txtViewPaiPan.setTextColor(Color.WHITE);
        txtViewPaiPan.setGravity(Gravity.CENTER_HORIZONTAL + Gravity.CENTER_VERTICAL);
        txtViewPaiPan.setOnClickListener(new TextViewPaiPanListener());

        //Bitmap bitmap=Utils.makeBitmap(175,40);
        //Bitmap rcbitmap=Utils.getRoundedCornerBitmap(bitmap,2.0f);
        //BitmapDrawable bd= new BitmapDrawable(null,rcbitmap);
        txtViewPaiPan.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        //txtViewPaiPan.setBackgroundDrawable( getResources().getDrawable( android.R.drawable.dialog_holo_light_frame));


        if (!SHOUGONGZHIDING) {
//			txtView6Yao.setEnabled(false);
//			txtView5Yao.setEnabled(false);
//			txtView4Yao.setEnabled(false);
//			txtView3Yao.setEnabled(false);
//			txtView2Yao.setEnabled(false);
//			txtView1Yao.setEnabled(false);

//			txtView6Yao.setVisibility(View.INVISIBLE);
//			txtView5Yao.setVisibility(View.INVISIBLE);
//			txtView4Yao.setVisibility(View.INVISIBLE);
//			txtView3Yao.setVisibility(View.INVISIBLE);
//			txtView2Yao.setVisibility(View.INVISIBLE);
//			txtView1Yao.setVisibility(View.INVISIBLE);

            txtView6Yao.setVisibility(View.GONE);
            txtView5Yao.setVisibility(View.GONE);
            txtView4Yao.setVisibility(View.GONE);
            txtView3Yao.setVisibility(View.GONE);
            txtView2Yao.setVisibility(View.GONE);
            txtView1Yao.setVisibility(View.GONE);


//			txtView6Yao.setHeight(1);
//			txtView5Yao.setHeight(1);
//			txtView4Yao.setHeight(1);
//			txtView3Yao.setHeight(1);
//			txtView2Yao.setHeight(1);
//			txtView1Yao.setHeight(1);
        }
    }

	/*
    Bitmap	makeBitmap(int w,int h)
	{
		Bitmap bmp= Bitmap.createBitmap(w,h, Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		canvas.drawColor(0xff33B5E5);

		return	bmp;
	}

	//获得圆角图片的方法
	Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx)
	{
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

		Canvas canvas = new Canvas(output);
		int color = 0xff424242;

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
	*/


    /*在这里开始相应“开始排卦”按钮跳转到另外一个Activity*/
    class TextViewPaiPanListener implements TextView.OnClickListener {
        private Bundle bundle = null;

        public void onClick(View v) {
            bundle = collectUserInputData();

            if (bundle != null) {
                Intent intent = new Intent(PaiGua.this, DisplayActivity.class);
                //if(SHOUGONGZHIDING)
                //	intent.putExtra("起卦方式","手工指定");
                //else
                //	intent.putExtra("起卦方式","电脑自动");
                intent.putExtra("UserData", bundle);
                startActivity(intent);
            }

        }
    }


    private Bundle collectUserInputData() {
        Bundle bundle = new Bundle();

        String gy = "";
        if (SHOUGONGZHIDING) {
            int n = -1;
            for (int i = 0; i < 6; i++) {
                n = YaoValue[i];
                if (n < 0) {
                    new AlertDialog.Builder(PaiGua.this).setTitle("卦爻不能为空").setIcon(android.R.drawable.ic_dialog_alert).setMessage("第 " + (i + 1) + " 爻未选").setPositiveButton("确定", null).show();

                    return null;
                } else
                    gy = gy + n;
            }
        } else {
            gy = "";
			/*随机生成卦爻的值*/

            Random rand = new Random();

            for (int i = 0; i < 6; i++) {
                int sum = 0;

                for (int j = 0; j < 3; j++) {
                    sum = sum + rand.nextInt(2);
                }

                gy = gy + sum;
            }
        }

        if (SHOUGONGZHIDING)
            bundle.putString(getString(R.string.qiguafangshi), getString(R.string.shougongzhiding));
        else
            bundle.putString(getString(R.string.qiguafangshi), getString(R.string.diannaozidong));

        NAME = (nameEditText.getText() + "").trim();
        if (NAME.equals(""))
            NAME = getResources().getString(R.string.unknown);
        bundle.putString("姓名", NAME);

        bundle.putString("性别", GENDER);

        if (BIRTHDAY_YEAR.equals("0000"))
            BIRTHDAY_YEAR = getResources().getString(R.string.unknown);
        bundle.putString("生年", BIRTHDAY_YEAR);

        THINGS = (editTextThings.getText() + "").trim();
        if (THINGS.equals(""))
            THINGS = getResources().getString(R.string.unknown);
        bundle.putString("占卜事项", THINGS);

        int[] datetime = {Year, Month, Day, Hour, Minute};
        bundle.putIntArray("起卦时间", datetime);

        bundle.putString("卦爻", gy);


        return bundle;
    }


    private class TextViewYaoListener implements TextView.OnClickListener {
        private AlertDialog yaoDialog = null;
        private TextView txtView = null;

        private int index = -1;

        public TextViewYaoListener() {
            AlertDialog.Builder builder = new AlertDialog.Builder(PaiGua.this);
            builder.setTitle("选择卦爻");
            builder.setSingleChoiceItems(gua6Yao, -1, new DialogInterfaceOnClickListenerImpl());

			/*
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
											{
												public void onClick(DialogInterface dialog, int id)
												{
													if(index<0)
														return;

													txtView.setText( gua6Yao[index] );

													if(txtView==txtView1Yao)
													{
														YaoValue[0]=index;
														return;
													}
													if(txtView==txtView2Yao)
													{
														YaoValue[1]=index;
														return;
													}
													if(txtView==txtView3Yao)
													{
														YaoValue[2]=index;
														return;
													}
													if(txtView==txtView4Yao)
													{
														YaoValue[3]=index;
														return;
													}
													if(txtView==txtView5Yao)
													{
														YaoValue[4]=index;
														return;
													}
													if(txtView==txtView6Yao)
													{
														YaoValue[5]=index;
														return;
													}
												}
											}
									);

			builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
											{
												public void onClick(DialogInterface dialog, int id)
												{
													index=-1;
												}
											}
									);

			*/

            yaoDialog = builder.create();
        }

        @Override
        public void onClick(View v) {
            txtView = (TextView) v;

            yaoDialog.show();
        }


        private class DialogInterfaceOnClickListenerImpl implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int index) {
                yaoDialog.dismiss();

                if (index < 0)
                    return;

                txtView.setText(gua6Yao[index]);

                if (txtView == txtView1Yao) {
                    YaoValue[0] = index;
                    return;
                }
                if (txtView == txtView2Yao) {
                    YaoValue[1] = index;
                    return;
                }
                if (txtView == txtView3Yao) {
                    YaoValue[2] = index;
                    return;
                }
                if (txtView == txtView4Yao) {
                    YaoValue[3] = index;
                    return;
                }
                if (txtView == txtView5Yao) {
                    YaoValue[4] = index;
                    return;
                }
                if (txtView == txtView6Yao) {
                    YaoValue[5] = index;
                    return;
                }
            }
        }
    }


    private class TextViewYearMonthDayListener implements TextView.OnClickListener {
        private DatePickerDialog dateDialog;
        //private	int y,m,d;

        public TextViewYearMonthDayListener(Context context) {
            dateDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    if ((year <= 1900) || (year >= 2100)) {
                        /**
                         Toast toast = Toast.makeText(getApplicationContext(),
                         "年的设置范围是 1900 ~ 2100", Toast.LENGTH_LONG);
                         toast.setGravity(Gravity.CENTER, 0, 0);	toast.show();
                         */

                        new AlertDialog.Builder(PaiGua.this).setTitle("年设置超出范围").setIcon(android.R.drawable.ic_dialog_alert).setMessage("年的设置范围是 1900 ~ 2100").setPositiveButton("确定", null).show();

                        return;
                    }

                    Year = year;
                    Month = monthOfYear;
                    Day = dayOfMonth;

                    txtViewYearMonthDay.setText(Year + "年" + (Month + 1) + "月" + Day + "日");
                }
            }


                    , Year, Month, Day);

            dateDialog.setTitle("设置年、月、日");

			/*
			dateDialog.setButton(AlertDialog.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {

		           }
		       });

			dateDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"取消", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dateDialog.cancel();
		           }
		       });
		     */
        }

        public void onClick(View tv) {
            dateDialog.show();
        }
    }


    private class TextViewHourMinuteListener implements TextView.OnClickListener {
        private TimePickerDialog timeDialog;


        public TextViewHourMinuteListener(Context context) {
            timeDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Hour = hourOfDay;
                    Minute = minute;

                    txtViewHourMinute.setText(Hour + "时" + Minute + "分");
                }
            },
                    Hour, Minute, true);

            timeDialog.setTitle("设置时、分");

			/*
			timeDialog.setButton(AlertDialog.BUTTON_POSITIVE,"确定", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {

		           }
		       });

			timeDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"取消", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   	timeDialog.cancel();
		           }
		       });
		     */
        }

        public void onClick(View tv) {
            timeDialog.show();
        }
    }


    private class TextViewBirthdayListener implements TextView.OnClickListener {
        private NumberPicker picker = null;
        private Dialog dialog = null;

        public TextViewBirthdayListener(Context ctx) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

            Context context = ctx;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.birthdayyearnumberpicker, null);

            picker = (NumberPicker) view.findViewById(R.id.birthdayYearNumberPicker);
            picker.setMinValue(1900);
            picker.setMaxValue(2100);
            picker.setValue(1990);

            //picker.setOnLongPressUpdateInterval(300);

            builder.setView(view);

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    BIRTHDAY_YEAR = picker.getValue() + "";
                    txtViewBirthday.setText("生年:" + BIRTHDAY_YEAR);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    /**什么也不做*/
                }
            }).setTitle("选择出生年份");

            dialog = builder.create();
        }

        public void onClick(View tv) {
            dialog.show();
        }
    }


    /**
     * 为性别CheckBox的设置监听
     */
    private class GenderCheckBoxListener implements CheckBox.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            CheckBox cb = (CheckBox) buttonView;

            if (genderMale == cb && isChecked) {
                GENDER = "男";
                genderFemale.setChecked(false);

                Log.d(this.getClass().getName(), GENDER);
            }

            if (genderFemale == cb && isChecked) {
                GENDER = "女";
                genderMale.setChecked(false);

                Log.d(this.getClass().getName(), GENDER);
            }

            if ((!genderFemale.isChecked()) && (!genderMale.isChecked())) {
                GENDER = getResources().getString(R.string.unknown);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

