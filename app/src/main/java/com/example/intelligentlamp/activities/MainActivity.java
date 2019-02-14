package com.example.intelligentlamp.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.intelligentlamp.R;
import com.example.intelligentlamp.adapters.MainAdapter;
import com.example.intelligentlamp.bluetooth.BluetoothChatUtil;
import com.example.intelligentlamp.bluetooth.GetHttpConnectionData;
import com.example.intelligentlamp.classes.Model;
import com.example.intelligentlamp.classes.UserPassword;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;


    private Model[] model = {new Model("学习模式", R.drawable.learn),
            new Model("生活模式", R.drawable.live), new Model("娱乐模式", R.drawable.amuse),
            new Model("自定义模式", R.drawable.user_defined),new Model("歌曲列表", R.drawable.item_player),
            new Model("蓝牙", R.drawable.bluetooth)};

    private List<Model> modelList = new ArrayList<>();

    private ArrayList<BluetoothDevice> scanBlue = new ArrayList<>();

    private List<UserPassword> userPasswords = DataSupport.findAll(UserPassword.class);

    private BluetoothDevice currentBlue;

    private MainAdapter adapter;

    private SwipeRefreshLayout swipeRefresh;

    private String name = null;

    private String userName;

    private byte[] headImage = null;

    private static final String USERNAME = "userName";

    private final static String TAG = "MainActivity";
    //设置绑定的蓝牙名称
    public static final String BLUETOOTH_NAME1 = "HC05_SLAVE";
    public static final String BLUETOOTH_NAME2 = "HC-05";
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Context mContext = this;
    private ProgressDialog mProgressDialog;
    private BluetoothChatUtil mBlthChatUtil;

    private SharedPreferences pref;
    private boolean isNight;
    final String SP_KEY_NIGHT = "night";

    private String provider;//位置提供器
    private LocationManager locationManager;//位置服务
    private Location location;

    private boolean blueConnection = true;

    private String messagesend = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView headShot=(CircleImageView)headerView.findViewById(R.id.icon_image);
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        String night = sp.getString(SP_KEY_NIGHT, "");

        Intent intent = getIntent();
        userName = intent.getStringExtra(USERNAME);
        TextView userShow = (TextView)headerView.findViewById(R.id.username);
        Glide.with(mContext).load(R.drawable.icon_image).error(R.drawable.icon_image).into(headShot);
//        headShot.setImageResource(R.drawable.icon_image);
//        userShow.setText("123456");

        for (UserPassword userPassword : userPasswords) {
            if (userName.equals(userPassword.getUser())) {
                headImage = userPassword.getHeadShot();
                Glide.with(mContext).load(userPassword.getHeadShot()).error(R.drawable.icon_image).into(headShot);
                userShow.setText(userName);
//                Log.i("Header",userName);
            }
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_call:
                        name = "Call";
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "18888888888"));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case R.id.nav_friends:
                        name = "Friend";

                        break;
                    case R.id.nav_location:
                        name = "Location";
                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//获得位置服务
                        provider = judgeProvider(locationManager);
                        if (provider != null) {//有位置提供器的情况
                            //为了压制getLastKnownLocation方法的警告
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            }
                            location = locationManager.getLastKnownLocation(provider);
                            if (location != null) {
                                getLocation(location);//得到当前经纬度并开启线程去反向地理编码
                            } else {

                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage("暂时无法获得当前位置");
                                builder.show();
                                Toast.makeText(MainActivity.this,"暂时无法获得当前位置",Toast.LENGTH_LONG);
                            }
                        }else{//不存在位置提供器的情况

                        }
                        break;
                    case R.id.nav_mail:
                        name = "Mail";
                        break;
                    case R.id.nav_task:
                        name = "Task";
                        break;
                    case R.id.nav_theme:
                        name = "Theme";
                        break;
                    case R.id.nav_night:
                        name = "Night";

                        isNight = pref.getBoolean("night", false);
                        if (isNight) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            pref.edit().putBoolean("night", false).commit();
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            pref.edit().putBoolean("night", true).commit();
                        }
                        recreate();
                        break;
                    case R.id.nav_setting:
                        name = "Setting";
                        break;
                    case R.id.nav_suggestion:
                        name = "Suggestion";
                        break;
                    case R.id.nav_about:
                        name = "About";
                        break;
                    default:
                        break;
                }
                if (name != null) {
                    Toast.makeText(MainActivity.this, "You clicked " + name,
                            Toast.LENGTH_SHORT).show();
                }
                name = null;
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                TextView userName = (TextView) findViewById(R.id.username);
//                int ironImage = R.drawable.icon_image;
                intent.putExtra(UserActivity.MUSIC_NAME, userName.getText());
                intent.putExtra(UserActivity.MUSIC_IMAGE_ID, headImage);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
            }
        });
        initMusics();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainAdapter(modelList);
        recyclerView.setAdapter(adapter);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });
        initView();
        initBluetooth();
        mBlthChatUtil = BluetoothChatUtil.getInstance(mContext);
        mBlthChatUtil.registerHandler(mHandler);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "连接蓝牙", Snackbar.LENGTH_SHORT)
//                        .setAction("Undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(MainActivity.this, "Data restored",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .show();

                String[] blue = {"HC05_SLAVE","HC-05"};
//                for (int i = 0; i < scanBlue.size(); i++)
//                {
//                    blue[i+2] = scanBlue.get(i).getName();
//                }
                final String[] list = blue;
                AlertDialog.Builder blueDialog = new AlertDialog.Builder(mContext);
                blueDialog.setTitle("可支持蓝牙");
                blueDialog.setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        if(scanBlue.get(which) != null){
//                            name = list[which];
//                            currentBlue = scanBlue.get(which);
//                            if(name != null && (name.equals(BLUETOOTH_NAME1) || name.equals(BLUETOOTH_NAME2))){
//                                mBluetoothAdapter.cancelDiscovery(); //取消扫描
//                                mProgressDialog.setTitle(getResources().getString(R.string.progress_connecting));
//                                mBlthChatUtil.connect(currentBlue);
//                                if (mBlthChatUtil.getState() == BluetoothChatUtil.STATE_CONNECTED) {
//                                    Toast.makeText(mContext, "蓝牙已连接", Toast.LENGTH_SHORT).show();
//                                }else {
//                                    discoveryDevices();
//                                }
//                            } else{
//                                Toast.makeText(mContext, "蓝牙不匹配", Toast.LENGTH_SHORT).show();
//                            }
//                        }
                                if (mBlthChatUtil.getState() == BluetoothChatUtil.STATE_CONNECTED) {
                                    Toast.makeText(mContext, "蓝牙已连接", Toast.LENGTH_SHORT).show();
                                    if(mProgressDialog.isShowing()){
                                        mProgressDialog.dismiss();
                                    }
                                }else {
                                    discoveryDevices();
//                                    Toast.makeText(mContext, "蓝牙不匹配", Toast.LENGTH_SHORT).show();
                                    if(mProgressDialog.isShowing()){
                                        mProgressDialog.dismiss();
                                    }
                                }
                    }
                });
                blueDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        if(name != null && name.equals(BLUETOOTH_NAME)){
//                            mBluetoothAdapter.cancelDiscovery(); //取消扫描
//                            mProgressDialog.setTitle(getResources().getString(R.string.progress_connecting));
//                            mBlthChatUtil.connect(currentBlue);
//                            if (mBlthChatUtil.getState() == BluetoothChatUtil.STATE_CONNECTED) {
//                                Toast.makeText(mContext, "蓝牙已连接", Toast.LENGTH_SHORT).show();
//                            }else {
//                                discoveryDevices();
//                            }
//                        } else{
//                            Toast.makeText(mContext, "蓝牙不匹配", Toast.LENGTH_SHORT).show();
//                        }
//                        Toast.makeText(mContext, scanBlue.get(0).getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                blueDialog.show();

//                                if (mBlthChatUtil.getState() == BluetoothChatUtil.STATE_CONNECTED) {
//                                    Toast.makeText(mContext, "蓝牙已连接", Toast.LENGTH_SHORT).show();
//                                }else {
//                                    discoveryDevices();
//                                    Toast.makeText(mContext, "蓝牙不匹配", Toast.LENGTH_SHORT).show();
//                                }
            }
        });


    }

    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initMusics();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initMusics() {
        modelList.clear();
        for(int i = 0; i<model.length; i++)
            modelList.add(model[i]);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
//                Toast.makeText(this, "You clicked Backup", Toast.LENGTH_SHORT).show();
                messagesend = "z";
                mBlthChatUtil.write(messagesend.getBytes());
                break;
            case R.id.delete:
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case BluetoothChatUtil.STATE_CONNECTED:
                    String deviceName = msg.getData().getString(BluetoothChatUtil.DEVICE_NAME);
                    if (blueConnection) {
                        Toast.makeText(MainActivity.this, "已成功连接到设备", Toast.LENGTH_SHORT).show();
                        blueConnection = false;
                    }
                    if(mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    break;
                case BluetoothChatUtil.STATAE_CONNECT_FAILURE:
                    if(mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.MESSAGE_DISCONNECTED:
                    if(mProgressDialog.isShowing()){
                        mProgressDialog.dismiss();
                    }
                    Toast.makeText(MainActivity.this, "与设备断开连接", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatUtil.MESSAGE_READ:{
                    byte[] buf = msg.getData().getByteArray(BluetoothChatUtil.READ_MSG);
                    String str = new String(buf,0,buf.length);
//                    Toast.makeText(MainActivity.this, "读成功" + str, Toast.LENGTH_SHORT).show();
                    break;
                }
                case BluetoothChatUtil.MESSAGE_WRITE:{
                    byte[] buf = (byte[]) msg.obj;
                    String str = new String(buf,0,buf.length);
//                    Toast.makeText(MainActivity.this, "发送成功" + str, Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        };
    };


    private void initView() {
        mProgressDialog = new ProgressDialog(this);
    }

    //开启蓝牙
    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {//设备不支持蓝牙
            Toast.makeText(getApplicationContext(), "设备不支持蓝牙",
                    Toast.LENGTH_LONG).show();
//            finish();
            return;
        }
        //判断蓝牙是否开启
        if (!mBluetoothAdapter.isEnabled()) {//蓝牙未开启
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            //mBluetoothAdapter.enable();此方法直接开启蓝牙，不建议这样用。
        }
        //注册广播接收者，监听扫描到的蓝牙设备
        IntentFilter filter = new IntentFilter();
        //发现设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mBluetoothReceiver, filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult request="+requestCode+" result="+resultCode);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){

            }else if(resultCode == RESULT_CANCELED){
                finish();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mBlthChatUtil != null) {
            if (mBlthChatUtil.getState() == BluetoothChatUtil.STATE_CONNECTED){
                BluetoothDevice device = mBlthChatUtil.getConnectedDevice();
                if(null != device && null != device.getName()){
                    Toast.makeText(MainActivity.this, "已成功连接到设备" + device.getName(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "已成功连接到设备", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mBlthChatUtil = null;
        unregisterReceiver(mBluetoothReceiver);
    }

//    @Override
//    public void onClick(View arg0) {
//        switch(arg0.getId()){
//            case R.id.btn_blth_connect:
//                if (mBlthChatUtil.getState() == BluetoothChatUtil.STATE_CONNECTED) {
//                    Toast.makeText(mContext, "蓝牙已连接", Toast.LENGTH_SHORT).show();
//                }else {
//                    discoveryDevices();
//                }
//                break;
//            case R.id.btn_blth_disconnect:
//                if (mBlthChatUtil.getState() != BluetoothChatUtil.STATE_CONNECTED) {
//                    Toast.makeText(mContext, "蓝牙未连接", Toast.LENGTH_SHORT).show();
//                }else {
//                    mBlthChatUtil.disconnect();
//                }
//                break;
//            case R.id.btn_sendmessage:
//                String messagesend = mEdttMessage.getText().toString();
//                if(null == messagesend || messagesend.length() == 0){
//                    return;
//                }
//                mBlthChatUtil.write(messagesend.getBytes());
//                break;
//            default:
//                break;
//        }
//    }

    //搜索设备
    private void discoveryDevices() {
        if(mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
        if (mBluetoothAdapter.isDiscovering()){
            //如果正在扫描则返回
            return;
        }
        mProgressDialog.setTitle(getResources().getString(R.string.progress_scaning));
        mProgressDialog.show();
        // 扫描蓝牙设备
        mBluetoothAdapter.startDiscovery();

    }

    //监听扫描结果
    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG,"mBluetoothReceiver action ="+action);
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                //获取蓝牙设备
                BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (scanDevice.getBondState() != BluetoothDevice.BOND_BONDED){
                    scanBlue.add(scanDevice);
                }
                if(scanDevice == null || scanDevice.getName() == null) return;
                Log.d(TAG, "name="+scanDevice.getName()+"address="+scanDevice.getAddress());
                //蓝牙设备名称
                String name = scanDevice.getName();
                if(name != null && (name.equals(BLUETOOTH_NAME1) || name.equals(BLUETOOTH_NAME2))){
                    mBluetoothAdapter.cancelDiscovery(); //取消扫描
                    mProgressDialog.setTitle(getResources().getString(R.string.progress_connecting));
                    mBlthChatUtil.connect(scanDevice);
               }
            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
            }
        }
    };

    //获取本地蓝牙信息
    @SuppressWarnings("unused")
    private void getBtDeviceInfo(){
        //获取本机蓝牙名称
        String name = mBluetoothAdapter.getName();
        //获取本机蓝牙地址
        String address = mBluetoothAdapter.getAddress();
        Log.d(TAG,"bluetooth name ="+name+" address ="+address);
        //获取已配对蓝牙设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        Log.d(TAG, "bonded device size ="+devices.size());
        for(BluetoothDevice bonddevice:devices){
            Log.d(TAG, "bonded device name ="+bonddevice.getName()+
                    " address"+bonddevice.getAddress());
        }
    }

    /**
     * 得到当前经纬度并开启线程去反向地理编码
     */
    public void getLocation(Location location) {
        String latitude = location.getLatitude()+"";
        String longitude = location.getLongitude()+"";
        String url = "http://api.map.baidu.com/geocoder/v2/?ak=XkH03bdws1VUD5eZKUWT033jLQtLOxfX&callback=renderReverse&location="+latitude+","+longitude+"&output=json&pois=0";
        new MyAsyncTask(url).execute();
    }

    /**
     * 判断是否有可用的内容提供器
     * @return 不存在返回null
     */
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if(prodiverlist.contains(LocationManager.NETWORK_PROVIDER)){
            return LocationManager.NETWORK_PROVIDER;
        }else if(prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("没有可用的位置提供器");
            builder.show();            Toast.makeText(MainActivity.this,"没有可用的位置提供器",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    class MyAsyncTask extends AsyncTask<Void,Void,Void> {
        String url = null;//要请求的网址
        String str = null;//服务器返回的数据
        String address = null;
        public MyAsyncTask(String url){
            this.url = url;
        }
        @Override
        protected Void doInBackground(Void... params) {
            str = GetHttpConnectionData.getData(url);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                str = str.replace("renderReverse&&renderReverse","");
                str = str.replace("(","");
                str = str.replace(")","");
                JSONObject jsonObject = new JSONObject(str);
                JSONObject address = jsonObject.getJSONObject("result");
                String city = address.getString("formatted_address");
                String district = address.getString("sematic_description");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("当前位置："+city+district);
                builder.show();
                Toast.makeText(MainActivity.this,"当前位置："+city+district,Toast.LENGTH_LONG);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(aVoid);
        }
    }

    public static void actionStart(Context context, String user){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USERNAME, user);
        context.startActivity(intent);
    }

}
