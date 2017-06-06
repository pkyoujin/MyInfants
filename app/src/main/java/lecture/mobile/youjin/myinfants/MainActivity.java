package lecture.mobile.youjin.myinfants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    int Btn_s_clicked = 0;

    SharedPreferences setting; //프레퍼런스 정의
    SharedPreferences.Editor editor; //저장,기록을 위해 editor 정의

    //Main
    Button btn_s, btn_p;

    //Supervisor
    ImageView h1,h2,h3,h4,h5,h6,h7,h8,h9,h10,h11,h12,h13,h14,h15,h16,h17,h18,h19,h20;
    ImageButton add;
    private LinkedList<BluetoothDevice> mBluetoothDevices = new LinkedList<BluetoothDevice>();
    private ArrayAdapter<String> mDeviceArrayAdapter;

    private EditText mEditTextInput;
    private TextView mTextView;
    private Button mButtonSend;
    private ProgressDialog mLoadingDialog;
    private AlertDialog mDeviceListDialog;
    private Menu mMenu;
    private BluetoothSerialClient mClient;

    private EditText receiver, message;

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
                mClient = BluetoothSerialClient.getInstance();

                if(mClient == null) {
                    Toast.makeText(getApplicationContext(), "Cannot use the Bluetooth device.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                overflowMenuInActionBar();
                initProgressDialog();
                initDeviceListDialog();
                initWidget();

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

    public void onClickSMS(View v) {
        Uri n = Uri.parse("sms to: " + receiver.getText());
        Intent intent = new Intent(Intent.ACTION_SENDTO,n);
        String t = message.getText().toString();
        intent.putExtra("sms_body",t);
        startActivity(intent);
    }

    private void overflowMenuInActionBar(){
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // 무시한다. 3.x 이 예외가 발생한다.
            // 또, 타블릿 전용으로 만들어진 3.x 버전의 디바이스는 보통 하드웨어 버튼이 존재하지 않는다.
        }
    }


    @Override
    protected void onPause() {
        mClient.cancelScan(getApplicationContext());
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableBluetooth();


    }

    private void initProgressDialog() {
        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setCancelable(false);
    }

    private void initWidget() {
        mTextView = (TextView) findViewById(R.id.textViewTerminal);
        mTextView.setMovementMethod(new ScrollingMovementMethod());
        mEditTextInput = (EditText) findViewById(R.id.editTextInput);
        mButtonSend = (Button) findViewById(R.id.buttonSend);
        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendStringData(mEditTextInput.getText().toString());
                mEditTextInput.setText("");
            }
        });
    }

    private void initDeviceListDialog() {
        mDeviceArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.item_device);
        ListView listView = new ListView(getApplicationContext());
        listView.setAdapter(mDeviceArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item =  (String) parent.getItemAtPosition(position);
                for(BluetoothDevice device : mBluetoothDevices) {
                    if(item.contains(device.getAddress())) {
                        connect(device);
                        mDeviceListDialog.cancel();
                    }
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select bluetooth device");
        builder.setView(listView);
        builder.setPositiveButton("Scan",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        scanDevices();
                    }
                });
        mDeviceListDialog = builder.create();
        mDeviceListDialog.setCanceledOnTouchOutside(false);
    }

    private void addDeviceToArrayAdapter(BluetoothDevice device) {
        if(mBluetoothDevices.contains(device)) {
            mBluetoothDevices.remove(device);
            mDeviceArrayAdapter.remove(device.getName() + "\n" + device.getAddress());
        }
        mBluetoothDevices.add(device);
        mDeviceArrayAdapter.add(device.getName() + "\n" + device.getAddress() );
        mDeviceArrayAdapter.notifyDataSetChanged();

    }


    private void enableBluetooth() {
        BluetoothSerialClient btSet =  mClient;
        btSet.enableBluetooth(this, new BluetoothSerialClient.OnBluetoothEnabledListener() {
            @Override
            public void onBluetoothEnabled(boolean success) {
                if(success) {
                    getPairedDevices();
                } else {
                    finish();
                }
            }
        });
    }

    private void addText(String text) {
        mTextView.append(text);
        final int scrollAmount = mTextView.getLayout().getLineTop(mTextView.getLineCount()) - mTextView.getHeight();
        if (scrollAmount > 0)
            mTextView.scrollTo(0, scrollAmount);
        else
            mTextView.scrollTo(0, 0);
    }


    private void getPairedDevices() {
        Set<BluetoothDevice> devices =  mClient.getPairedDevices();
        for(BluetoothDevice device: devices) {
            addDeviceToArrayAdapter(device);
        }
    }

    private void scanDevices() {
        BluetoothSerialClient btSet = mClient;
        btSet.scanDevices(getApplicationContext(), new BluetoothSerialClient.OnScanListener() {
            String message ="";
            @Override
            public void onStart() {
                Log.d("Test", "Scan Start.");
                mLoadingDialog.show();
                message = "Scanning....";
                mLoadingDialog.setMessage("Scanning....");
                mLoadingDialog.setCancelable(true);
                mLoadingDialog.setCanceledOnTouchOutside(false);
                mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        BluetoothSerialClient btSet = mClient;
                        btSet.cancelScan(getApplicationContext());
                    }
                });
            }

            @Override
            public void onFoundDevice(BluetoothDevice bluetoothDevice) {
                addDeviceToArrayAdapter(bluetoothDevice);
                message += "\n" + bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress();
                mLoadingDialog.setMessage(message);
            }

            @Override
            public void onFinish() {
                Log.d("Test", "Scan finish.");
                message = "";
                mLoadingDialog.cancel();
                mLoadingDialog.setCancelable(false);
                mLoadingDialog.setOnCancelListener(null);
                mDeviceListDialog.show();
            }
        });
    }


    private void connect(BluetoothDevice device) {
        mLoadingDialog.setMessage("Connecting....");
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.show();
        BluetoothSerialClient btSet =  mClient;
        btSet.connect(getApplicationContext(), device, mBTHandler);
    }

    private BluetoothSerialClient.BluetoothStreamingHandler mBTHandler = new BluetoothSerialClient.BluetoothStreamingHandler() {
        ByteBuffer mmByteBuffer = ByteBuffer.allocate(1024);

        @Override
        public void onError(Exception e) {
            mLoadingDialog.cancel();
            addText("Messgae : Connection error - " +  e.toString() + "\n");
            mMenu.getItem(0).setTitle(R.string.action_connect);
        }

        @Override
        public void onDisconnected() {
            mMenu.getItem(0).setTitle(R.string.action_connect);
            mLoadingDialog.cancel();
            addText("Messgae : Disconnected.\n");
        }
        @Override
        public void onData(byte[] buffer, int length) {
            if(length == 0) return;
            if(mmByteBuffer.position() + length >= mmByteBuffer.capacity()) {
                ByteBuffer newBuffer = ByteBuffer.allocate(mmByteBuffer.capacity() * 2);
                newBuffer.put(mmByteBuffer.array(), 0,  mmByteBuffer.position());
                mmByteBuffer = newBuffer;
            }
            mmByteBuffer.put(buffer, 0, length);
            if(buffer[length - 1] == '\0') {
                addText(mClient.getConnectedDevice().getName() + " : " +
                        new String(mmByteBuffer.array(), 0, mmByteBuffer.position()) + '\n');
                mmByteBuffer.clear();
            }
        }

        @Override
        public void onConnected() {
            addText("Messgae : Connected. " + mClient.getConnectedDevice().getName() + "\n");
            mLoadingDialog.cancel();
            mMenu.getItem(0).setTitle(R.string.action_disconnect);
        }
    };

    public void sendStringData(String data) {
        data += '\0';
        byte[] buffer = data.getBytes();
        if(mBTHandler.write(buffer)) {
            addText("Me : " + data + '\n');
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        mClient.clear();
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean connect = mClient.isConnection();
        if(item.getItemId() == R.id.action_connect) {
            if (!connect) {
                mDeviceListDialog.show();
            } else {
                mBTHandler.close();
            }
            return true;
        } else {
            showCodeDlg();
            return true;
        }
    }

    private void showCodeDlg() {
        TextView codeView = new TextView(this);
        codeView.setText(Html.fromHtml(readCode()));
        codeView.setMovementMethod(new ScrollingMovementMethod());
        codeView.setBackgroundColor(Color.parseColor("#202020"));
        new AlertDialog.Builder(this, android.R.style.Theme_Holo_Light_DialogWhenLarge)
                .setView(codeView)
                .setPositiveButton("OK", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    private String readCode() {
        try {
            InputStream is = getAssets().open("HC_06_Echo.txt");
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            is.close();
            String code = new String(buffer);
            buffer = null;
            return code;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}

