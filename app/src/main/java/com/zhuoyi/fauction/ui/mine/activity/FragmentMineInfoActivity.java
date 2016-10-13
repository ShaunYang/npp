package com.zhuoyi.fauction.ui.mine.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.squareup.picasso.Picasso;
import com.yintai.common.util.Logger;
import com.yintai.common.util.SystemUtil;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.ProductDetail2;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.mine.fragment.AlertPasswordActivity;
import com.zhuoyi.fauction.ui.mine.fragment.AlertUserNameActivity;
import com.zhuoyi.fauction.ui.mine.fragment.RegisterFragment;
import com.zhuoyi.fauction.ui.other.AddressActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.view.PagerSlidingTabStrip;
import com.zhuoyi.fauction.view.SelectPicPopWindow;
import com.zhuoyi.fauction.view.SelectPricePopWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class FragmentMineInfoActivity extends Activity  {

    private static final String TAG = "FragmentMineInfoActivity";
    @Bind(R.id.mine_info_avator) ImageView mineInfoAvator;
    @Bind(R.id.mine_info_arror) ImageView mineInfoArror;
    @Bind(R.id.avator) RoundedImageView avator;
    @Bind(R.id.alert_username) RelativeLayout alertUsername;
    @Bind(R.id.alert_avator) RelativeLayout alertAvator;
    @Bind(R.id.mine_info_username) ImageView mineInfoUsername;
    @Bind(R.id.mine_info_arror_1) ImageView mineInfoArror1;
    @Bind(R.id.username) TextView username;
    @Bind(R.id.address) RelativeLayout address;
    @Bind(R.id.mine_info_address) ImageView mineInfoAddress;
    @Bind(R.id.mine_info_phone) ImageView mineInfoPhone;
    @Bind(R.id.mine_info_arror_phone) ImageView mineInfoArrorPhone;
    @Bind(R.id.phone) TextView phone;
    @Bind(R.id.alert_psd) RelativeLayout alertPsd;
    @Bind(R.id.mine_info_alertpsd) ImageView mineInfoAlertpsd;

    SelectPicPopWindow selectPicPopWindow;

    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    private static int CROP_REQUEST_CODE = 3;

    String domain;

    private View.OnClickListener itemsOnClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                //拍照
                case R.id.paizhao:
                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent1, CAMERA_REQUEST_CODE);
                    break;
                //相册
                case R.id.xiangce:
                    Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                    intent2.setType("image/*");
                    startActivityForResult(intent2, GALLERY_REQUEST_CODE);
                    break;
                case R.id.price_comfirm:
                    selectPicPopWindow.dismiss();
                    break;

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mine_info);
        ButterKnife.bind(this);
        memberUserInfoPost();
        if(Common.userAvator!=null){
            Picasso.with(FragmentMineInfoActivity.this).load(Common.userAvator).into(avator);
        }

    }

    @Override
    protected void onRestart() {
        memberUserInfoPost();
        super.onRestart();
    }

    @OnClick(R.id.back) void onBackClick() {
        //TODO implement
        onBackPressed();
    }

    @OnClick(R.id.alert_username)
    public void alertName(){
        Intent intent=new Intent(FragmentMineInfoActivity.this, AlertUserNameActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.alert_avator)
    public void alertAvator(){
        selectPicPopWindow=new SelectPicPopWindow(FragmentMineInfoActivity.this,itemsOnClick);
        selectPicPopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //显示的位置
        selectPicPopWindow.showAtLocation(FragmentMineInfoActivity.this.findViewById(R.id.main_do), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        backgroundAlpha(0.5f);
        selectPicPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    @OnClick(R.id.address)
    public void address(){
        Intent intent=new Intent(FragmentMineInfoActivity.this, AddressActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.alert_psd)
    public void alertPassword(){
        Intent intent=new Intent(FragmentMineInfoActivity.this, AlertPasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.exit_login) void onExitLoginClick() {
        memberLoginOutPost();
    }

    private void memberLoginOutPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(FragmentMineInfoActivity.this, timestamp, "Member", "loginOut");

        OkHttpUtils.post()
                .url(Constant.MEMBER_LOGINOUT)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(FragmentMineInfoActivity.this))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(FragmentMineInfoActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(FragmentMineInfoActivity.this))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                        Logger.i(TAG + "333333333333=", response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        int code = jsonObject.getIntValue("code");
                        if (code == 0) {
                            Logger.e("mgc", "退出登录！！！");


                            ConfigUserManager.setAlreadyLogin(FragmentMineInfoActivity.this, false);
                            //取消token
                            ConfigUserManager.setToken(FragmentMineInfoActivity.this, "");
                            //ConfigUserManager.setAlreadyLogin(FragmentMineInfoActivity.this,false);
                            Common.categories = null;
                            Intent intent = new Intent(FragmentMineInfoActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }



                    }
                });
    }

    private void memberUserInfoPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(FragmentMineInfoActivity.this, timestamp, "Member", "userInfo");

        OkHttpUtils.post()
                .url(Constant.MEMBER_USERINFO)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(FragmentMineInfoActivity.this))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(FragmentMineInfoActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(FragmentMineInfoActivity.this))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                        Logger.i(TAG + "333333333333=", response);
                        Logger.i(TAG + "333333333333=", response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        int code = jsonObject.getIntValue("code");
                        if (code == 0) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            String name = data.getString("user_name");
                            if (name != null && !name.equals("")) {
                                username.setText(name);
                            } else {
                                username.setText("");
                            }
                            phone.setText(data.getString("mobile"));
                            if(avator!=null){
                                if(data.getString("img")!=null){
                                    Picasso.with(FragmentMineInfoActivity.this).load(data.getString("img")).into(avator);
                                }

                            }
                        }else if(code==-1){
                            //登录超时 重新登录
                            ConfigUserManager.setAlreadyLogin(FragmentMineInfoActivity.this, false);
                            //((MainActivity)getActivity()).mTabHost.setCurrentTab(2);
                            Intent intent=new Intent(FragmentMineInfoActivity.this,MainActivity.class);
                            intent.putExtra("tab",2);
                            startActivity(intent);
                            finish();

                        }

                    }
                });
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private Uri saveBitmap(Bitmap bm)
    {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.jikexueyuan.avater");
        if(!tmpDir.exists())
        {
            tmpDir.mkdir();
        }
        File img = new File(tmpDir.getAbsolutePath() + "avater.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Uri convertUri(Uri uri)
    {
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return saveBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void startImageZoom(Uri uri)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUEST_CODE)
        {
            if(data == null)
            {
                return;
            }
            else
            {
                Bundle extras = data.getExtras();
                if(extras != null)
                {
                    Bitmap bm = extras.getParcelable("data");
                    Uri uri = saveBitmap(bm);
                    startImageZoom(uri);
                }
            }
        }
        else if(requestCode == GALLERY_REQUEST_CODE)
        {
            if(data == null)
            {
                return;
            }
            Uri uri;
            uri = data.getData();
            Uri fileUri = convertUri(uri);
            startImageZoom(fileUri);
        }
        else if(requestCode == CROP_REQUEST_CODE)
        {
            if(data == null)
            {
                return;
            }
            Bundle extras = data.getExtras();
            if(extras == null){
                return;
            }
            Bitmap bm = extras.getParcelable("data");
            ImageView imageView = (ImageView)findViewById(R.id.avator);
            imageView.setImageBitmap(bm);
            sendImage(bm);
        }
    }

    private void sendImage(Bitmap bm)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 60, stream);
        byte[] bytes = stream.toByteArray();
        String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        memberGetUpTokenPost(bytes);
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params = new RequestParams();
//        params.add("img", img);
//        client.post("http://192.168.56.1/ImgUpload.php", params, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                Toast.makeText(MainActivity.this, "Upload Success!", Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                Toast.makeText(MainActivity.this, "Upload Fail!", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    private void memberGetUpTokenPost(final byte[] data) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(FragmentMineInfoActivity.this, timestamp, "Member", "getUpToken");

        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.MEMBER_GETUPTOKEN)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(FragmentMineInfoActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(FragmentMineInfoActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(FragmentMineInfoActivity.this))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        int code = jsonObject.getIntValue("code");
                        if(code==0){
                            String token = jsonObject.getString("token");
                            domain = jsonObject.getString("domain");
                            // 重用 uploadManager。一般地，只需要创建一个 uploadManager 对象
                            UploadManager uploadManager = new UploadManager();
                            String key = null;
                            uploadManager.put(data, key, token, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, org.json.JSONObject response) {
                                    Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + response);
                                    //头像保持接口
                                    try {
                                        key = response.getString("key");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i("domain+key=", domain +key);
                                    memberSavePicturePost(domain + key);

                                }
                            },null);


                        }

                    }
                });
    }

    private void memberSavePicturePost(final String img) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(FragmentMineInfoActivity.this, timestamp, "Member", "savePicture");

        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.MEMBER_SAVEPICTURE)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(FragmentMineInfoActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(FragmentMineInfoActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(FragmentMineInfoActivity.this))
                .addParams("img",img)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        int code = jsonObject.getIntValue("code");
                        if(code==0){
                            ToastUtil.showLongToast(FragmentMineInfoActivity.this,"保存成功");
                            Common.userAvator=img;
                        }

                    }
                });
    }


}
