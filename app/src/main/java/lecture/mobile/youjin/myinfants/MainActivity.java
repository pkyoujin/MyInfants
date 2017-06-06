package lecture.mobile.youjin.myinfants;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int Btn_s_clicked = 0;

    SharedPreferences setting; //프레퍼런스 정의
    SharedPreferences.Editor editor; //저장,기록을 위해 editor 정의

    //Main
    Button btn_s, btn_p;

    //Supervisor
    ImageView h1,h2,h3,h4,h5,h6,h7,h8,h9,h10,h11,h12,h13,h14,h15,h16,h17,h18,h19,h20;
    ImageButton add;

    //Parent
    ListView list;

    String[] dates = {
            "The Wizard",
            "The Tree",
            "The Sky"
    };

    Integer[] images = {
            R.drawable.empty,
            R.drawable.full,
            R.drawable.empty
    };

    String[] contests = {
            "연락옴",
            "연락올예정",
            "연락오지않음"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setting = getSharedPreferences("setting", 0); // 기록할 파일을 불러옴
        editor= setting.edit(); // 프레퍼런스의 에디터 역할

        switch (setting.getInt("Btn_s_clicked",0)) {

            case 0 :

                Toast.makeText(getApplicationContext(),"초기설정값없음", Toast.LENGTH_SHORT).show();

                setContentView(R.layout.activity_main);

                btn_s = (Button) findViewById(R.id.btn_s);
                btn_s.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"교사용으로 설정합니다.", Toast.LENGTH_SHORT).show();
                        editor.putInt("Btn_s_clicked",1); //Btn_s_clicked = 1;
                        editor.commit();
                        setContentView(R.layout.for_supervisor);
                    }
                });

                btn_p = (Button) findViewById(R.id.btn_p);
                btn_p.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"부모용으로 설정합니다.", Toast.LENGTH_SHORT).show();
                        editor.putInt("Btn_s_clicked",2); //Btn_s_clicked = 2;
                        editor.commit();
                        setContentView(R.layout.for_parent);
                    }
                });

                break;

            case 1 :
                setContentView(R.layout.for_supervisor);

                h1 = (ImageView) findViewById(R.id.heart1);
                h2 = (ImageView) findViewById(R.id.heart2);
                h3 = (ImageView) findViewById(R.id.heart3);
                h4 = (ImageView) findViewById(R.id.heart4);
                h5 = (ImageView) findViewById(R.id.heart5);
                h6 = (ImageView) findViewById(R.id.heart6);
                h7 = (ImageView) findViewById(R.id.heart7);
                h8 = (ImageView) findViewById(R.id.heart8);
                h9 = (ImageView) findViewById(R.id.heart9);
                h10 = (ImageView) findViewById(R.id.heart10);
                h11 = (ImageView) findViewById(R.id.heart11);
                h12 = (ImageView) findViewById(R.id.heart12);
                h13 = (ImageView) findViewById(R.id.heart13);
                h14 = (ImageView) findViewById(R.id.heart14);
                h15 = (ImageView) findViewById(R.id.heart15);
                h16 = (ImageView) findViewById(R.id.heart16);
                h17 = (ImageView) findViewById(R.id.heart17);
                h18 = (ImageView) findViewById(R.id.heart18);
                h19 = (ImageView) findViewById(R.id.heart19);
                h20 = (ImageView) findViewById(R.id.heart20);

                add = (ImageButton) findViewById(R.id.btn_add);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getApplicationContext(), " 버튼이 클릭되었습니다 ", Toast.LENGTH_SHORT).show();

                        if (h2.getVisibility() == View.INVISIBLE) {
                            h2.setVisibility(View.VISIBLE);
                        } else if (h3.getVisibility() == View.INVISIBLE) {
                            h3.setVisibility(View.VISIBLE);
                        } else if (h4.getVisibility() == View.INVISIBLE) {
                            h4.setVisibility(View.VISIBLE);
                        } else if (h5.getVisibility() == View.INVISIBLE) {
                            h5.setVisibility(View.VISIBLE);
                        } else if (h6.getVisibility() == View.INVISIBLE) {
                            h6.setVisibility(View.VISIBLE);
                        } else if (h7.getVisibility() == View.INVISIBLE) {
                            h7.setVisibility(View.VISIBLE);
                        } else if (h8.getVisibility() == View.INVISIBLE) {
                            h8.setVisibility(View.VISIBLE);
                        } else if (h9.getVisibility() == View.INVISIBLE) {
                            h9.setVisibility(View.VISIBLE);
                        } else if (h10.getVisibility() == View.INVISIBLE) {
                            h10.setVisibility(View.VISIBLE);
                        } else if (h11.getVisibility() == View.INVISIBLE) {
                            h11.setVisibility(View.VISIBLE);
                        } else if (h12.getVisibility() == View.INVISIBLE) {
                            h12.setVisibility(View.VISIBLE);
                        } else if (h13.getVisibility() == View.INVISIBLE) {
                            h13.setVisibility(View.VISIBLE);
                        } else if (h14.getVisibility() == View.INVISIBLE) {
                            h14.setVisibility(View.VISIBLE);
                        } else if (h15.getVisibility() == View.INVISIBLE) {
                            h15.setVisibility(View.VISIBLE);
                        } else if (h16.getVisibility() == View.INVISIBLE) {
                            h16.setVisibility(View.VISIBLE);
                        } else if (h17.getVisibility() == View.INVISIBLE) {
                            h17.setVisibility(View.VISIBLE);
                        } else if (h18.getVisibility() == View.INVISIBLE) {
                            h18.setVisibility(View.VISIBLE);
                        } else if (h19.getVisibility() == View.INVISIBLE) {
                            h19.setVisibility(View.VISIBLE);
                        } else if (h20.getVisibility() == View.INVISIBLE) {
                            h20.setVisibility(View.VISIBLE);
                        }
                    }
                });


                break;
            case 2 :
                setContentView(R.layout.for_parent);

                SMSList adapter = new SMSList(MainActivity.this);
                list = (ListView) findViewById(R.id.list);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getBaseContext(), dates[+position], Toast.LENGTH_SHORT).show();
                    }
                });

                break;
        }
    }

    public class SMSList extends ArrayAdapter<String> {
        private final Activity context;

        public SMSList(Activity context) {
            super(context, R.layout.listitem, dates);
            this.context = context;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listitem, null, true);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.image );
            TextView date = (TextView) rowView.findViewById(R.id.date);
            TextView contents = (TextView) rowView.findViewById(R.id.contents);
            date.setText(dates[position]);
            imageView.setImageResource(images[position]);
            contents.setText(contests[position]);
            return rowView;
        }
    }
}

