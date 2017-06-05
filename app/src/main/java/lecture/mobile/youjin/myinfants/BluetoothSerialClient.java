package lecture.mobile.youjin.myinfants;

/**
 * Created by You Jin on 2017-06-05.
 */

import android.app.Activity;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothSocket;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Looper;
        import android.util.Log;

        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.util.Set;
        import java.util.UUID;
        import java.util.concurrent.ExecutorService;
        import java.util.concurrent.Executors;
        import java.util.concurrent.atomic.AtomicBoolean;
        import android.os.Handler;

/**
 * BluetoothSerialClient
 */

public class BluetoothSerialClient {


    static final private String SERIAL_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    static private BluetoothSerialClient sThis = null;

    private BluetoothAdapter mBluetoothAdapter;
    private OnBluetoothEnabledListener mOnBluetoothUpListener;
    private OnScanListener mOnScanListener;
    private BluetoothSocket mBluetoothSocket;
    private UUID mUUID = UUID.fromString(SERIAL_UUID);
    private AtomicBoolean mIsConnection = new AtomicBoolean(false);
    private ExecutorService mReadExecutor;
    private ExecutorService mWriteExecutor;
    private BluetoothStreamingHandler mBluetoothStreamingHandler;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private BluetoothDevice mConnectedDevice = null;
    private InputStream mInputStream;
    private OutputStream mOutputStream;


    public static BluetoothSerialClient getInstance() { // BluetoothSerialClient 의 싱글 인스턴스를 가져온다
        if(sThis == null) {
            sThis = new BluetoothSerialClient();
        }
        if(sThis.mBluetoothAdapter == null) { // 블루투스 사용 불가 기기면 null 값 리턴
            sThis = null;
            return null;
        }

        return sThis;
    }


    private BluetoothSerialClient() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mReadExecutor = Executors.newSingleThreadExecutor();
        mWriteExecutor = Executors.newSingleThreadExecutor();

    }

    public void clear() { // 연결닫고 자원을 해지
        close();
        mReadExecutor.shutdownNow(); // 앱 종료 시 반드시 호출해주어야함
        mWriteExecutor.shutdownNow();
        sThis = null;
    }

    /**
     * @param context activity
     * @param onBluetoothEnabledListener 블루투스 on/off 에 대한 이벤트.
     */

    public void enableBluetooth(Context context, OnBluetoothEnabledListener onBluetoothEnabledListener) {
        if(!mBluetoothAdapter.isEnabled()) { // 블루투스 사용가능 상태가 아니라면
            mOnBluetoothUpListener = onBluetoothEnabledListener;
            Intent intent = new Intent(context, BluetoothUpActivity.class);
            context.startActivity(intent); // 사용가능한 상태로 만들어줌
        } else {
            onBluetoothEnabledListener.onBluetoothEnabled(true); // 기기의 블루투스 사용이 꺼져있다면, 사용 선택 창 출력
        }
    }

    public boolean isEnabled() {
        return mBluetoothAdapter.isEnabled();
    } // 블루투스 사용 가능 상태인지 확인 , false 라면 블루투스 off 상태


    /**
     * @param context
     * @param device 블루투스 디바이스. {@link getPairedDevices} 또는 {@link scanDevices} 를 통하여 가져온 블루투스 디바이스 인스턴스.
     * @param bluetoothStreamingHandler 블루투스 스트리밍 핸들러.
     * @return 만약 블루투스를 사용할 수 없는 상태라면 false. {@link  enableBluetooth} 를 통하여 블루투스를 사용 가능한 상태로 만들어줘야 한다.
     */
    public boolean connect(final Context context,final BluetoothDevice device, final BluetoothStreamingHandler bluetoothStreamingHandler) { // 블루투스 디바이스와 시리얼로 연결
        if(!isEnabled()) return false;
        mConnectedDevice = device;
        mBluetoothStreamingHandler = bluetoothStreamingHandler;
        if(isConnection()) {
            mWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        mIsConnection.set(false);
                        mBluetoothSocket.close();
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }  catch (IOException e) {
                        e.printStackTrace();
                    }
                    connect(context, device, bluetoothStreamingHandler);
                }
            });
        } else {
            mIsConnection.set(true);
            connectClient();
        }
        return true;
    }


    public Set<BluetoothDevice> getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices(); // 과거 페어링했던 블루투스 디바이스 목록 불러오기
        return pairedDevices;
    }

    /**
     * @param context
     * @param OnScanListener 블루투스를 스캔 이벤트.
     */

    public boolean scanDevices(Context context, OnScanListener OnScanListener) { // 주변 블루투스 가능 디바이스 스캔
        if(!mBluetoothAdapter.isEnabled()) return false;
        if(mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            try {
                context.unregisterReceiver(mDiscoveryReceiver);
            } catch(IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        mOnScanListener = OnScanListener;
        IntentFilter filterFound = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filterFound.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filterFound.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mDiscoveryReceiver, filterFound);
        mBluetoothAdapter.startDiscovery();
        return true;
    }


    public void cancelScan(Context context) { //스캔 취소
        if(!mBluetoothAdapter.isEnabled() || !mBluetoothAdapter.isDiscovering()) return;
        mBluetoothAdapter.cancelDiscovery();
        try {
            context.unregisterReceiver(mDiscoveryReceiver);
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        if(mOnScanListener != null) mOnScanListener.onFinish();
    }

    public boolean isConnection() {
        return mIsConnection.get();
    } // 블루투스 디바이스 연결 되었는지 가져옴

    public BluetoothDevice getConnectedDevice() {
        return mConnectedDevice;
    } // 연결된 디바이스 가져오기, 연결된 디바이스 없다면 null



    private void connectClient() {
        try {
            mBluetoothSocket = mConnectedDevice.createRfcommSocketToServiceRecord(mUUID);
        } catch (IOException e) {
            close();
            e.printStackTrace();
            mBluetoothStreamingHandler.onError(e);
            return;
        }
        mWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mBluetoothAdapter.cancelDiscovery();
                    mBluetoothSocket.connect();
                    manageConnectedSocket(mBluetoothSocket);
                    callConnectedHandlerEvent();
                    mReadExecutor.execute(mReadRunnable);
                } catch (final IOException e) {
                    close();
                    e.printStackTrace();
                    mMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mBluetoothStreamingHandler.onError(e);
                        }
                    });
                    mIsConnection.set(false);
                    try {
                        mBluetoothSocket.close();
                    } catch (Exception ec) {
                        ec.printStackTrace();
                    }
                }
            }
        });
    }


    private void manageConnectedSocket(BluetoothSocket socket) throws IOException {
        mInputStream =  socket.getInputStream();
        mOutputStream = socket.getOutputStream();
    }

    private void callConnectedHandlerEvent() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mBluetoothStreamingHandler.onConnected();
            }
        });
    }


    private boolean write(final byte[] buffer) {
        if(!mIsConnection.get())
            return false;
        mWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    mOutputStream.write(buffer);
                } catch (Exception e) {
                    close();
                    e.printStackTrace();
                    mBluetoothStreamingHandler.onError(e);
                }
            }
        });
        return true;
    }


    private boolean close() {
        mConnectedDevice = null;
        if(mIsConnection.get()) {
            mIsConnection.set(false);
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMainHandler.post(mCloseRunable);
            return true;
        }
        return false;
    }

    private Runnable mCloseRunable = new Runnable() {
        @Override
        public void run() {
            if(mBluetoothStreamingHandler != null) {
                mBluetoothStreamingHandler.onDisconnected();
            }
        }
    };

    private Runnable mReadRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                final byte[] buffer = new byte[256];
                final int readBytes = mInputStream.read(buffer);
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mBluetoothStreamingHandler != null) {
                            mBluetoothStreamingHandler.onData(buffer ,readBytes);

//                            // 입력받은 숫자가 아스키 코드로 출력됨!
//
//                            for (int n; (n = is.read(readBytes)) != -1;) {
//                                buffer.append(new Integer(readBytes, 0, n));
//                            }
//                            Integer str = buffer.toInteger();


                        }
                    }
                });
                mReadExecutor.execute(mReadRunnable);
            } catch (Exception e) {
                close();
                e.printStackTrace();
            }
        }
    };


    private BroadcastReceiver  mDiscoveryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(mOnScanListener != null) mOnScanListener.onFoundDevice(device);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if(mOnScanListener != null) mOnScanListener.onFinish();
                try {
                    context.unregisterReceiver(mDiscoveryReceiver);
                } catch(IllegalArgumentException e) {
                    e.printStackTrace();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                if(mOnScanListener != null)
                    mOnScanListener.onStart();
            }
        }
    };



    public static class BluetoothUpActivity extends Activity {
        private static int REQUEST_ENABLE_BT = 2;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().getDecorView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    upbluetoothDevice();
                }
            }, 100);
        }
        private void upbluetoothDevice() {
            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!btAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT) ;
            }
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == REQUEST_ENABLE_BT) {
                OnBluetoothEnabledListener onBluetoothEnabledListener = getInstance().mOnBluetoothUpListener;
                if (resultCode == Activity.RESULT_OK) {
                    if(onBluetoothEnabledListener != null)
                        onBluetoothEnabledListener.onBluetoothEnabled(true);
                    finish();
                } else {
                    if(onBluetoothEnabledListener != null)
                        onBluetoothEnabledListener.onBluetoothEnabled(false);
                    finish();
                }
            }
        }
    }
    // End BluetoothUpActivity


    public static interface OnBluetoothEnabledListener {
        public void onBluetoothEnabled(boolean success);
    }

    public static interface OnScanListener {
        public void onStart();
        public void onFoundDevice(BluetoothDevice bluetoothDevice);
        public void onFinish();
    }

    public abstract static class BluetoothStreamingHandler {
        public abstract void onError(Exception e);
        public abstract void onConnected();
        public abstract void onDisconnected();
//        public abstract void onData(byte[] buffer, int length);

        public void onData(byte[] buffer, int length) {
            // 입력받은 숫자가 아스키 코드로 출력됨!
            String s = new String(buffer, 0, 0, length);
            Log.v("BluetoothSerialCliet", "received = " + s);

//            for (int n; (n = is.read(readBytes)) != -1;) {
//                buffer.append(new Integer(readBytes, 0, n));
//            }
//            Integer str = buffer.toInteger();

        }

        public final boolean close() {
            BluetoothSerialClient btSet = getInstance();
            if(btSet != null)
                return btSet.close();
            return false;
        }
        public final boolean write(byte[] buffer) {
            BluetoothSerialClient btSet = getInstance();
            if(btSet != null)
                return btSet.write(buffer);
            return false;
        }
    }

}
