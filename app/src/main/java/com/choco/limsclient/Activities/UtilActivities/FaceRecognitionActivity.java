package com.choco.limsclient.Activities.UtilActivities;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.hardware.camera2.params.Face;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.choco.limsclient.R;
import com.choco.limsclient.Util.*;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;

public class FaceRecognitionActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQUEST_CAMERA_IMAGE = 1;
    private int rt;

    private Toast mToast;
    private ProgressDialog mProDialog;
    private String mAuthid;
    private FaceRequest mFaceRequest;
    private Bitmap mImage = null;
    private byte[] mImageData = null;
    private File mPictureFile;
    private Uri mPictureUri;

    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            try {
                String result = new String(buffer, "utf-8");
                Log.d("FaceDemo", result);

                JSONObject object = new JSONObject(result);
                String type = object.optString("sst");
                if ("reg".equals(type)) {
                    register(object);
                } else if ("verify".equals(type)) {
                    verify(object);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO: handle exception
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            if (error != null) {
                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                        showTip("该账号已经注册");
                        Intent intent = getIntent();
                        intent.putExtra("REGISTER_RESULT","FAILED");
                        FaceRecognitionActivity.this.setResult(RESULT_OK,intent);
                        FaceRecognitionActivity.this.finish();
                        break;

                    default:
                        showTip(error.getPlainDescription(true));
                        break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.xunfei_appid));

        findViewById(R.id.iv_userFace).setOnClickListener(this);
        findViewById(R.id.btn_reg_verify).setOnClickListener(this);

        rt = getIntent().getIntExtra("REQUEST_TYPE",Global.FACE_VERIFY);

        updateButtonText();

        mAuthid = Global.userInfo.getUserId();
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");

        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (null != mFaceRequest) {
                    mFaceRequest.cancel();
                }
            }
        });
        mFaceRequest = new FaceRequest(this);
    }

    private void updateButtonText(){
        Button btn = (Button)findViewById(R.id.btn_reg_verify);
        switch(rt){
            case Global.FACE_REGISTER:
                btn.setText("注册");
                break;
            case Global.FACE_VERIFY:
                btn.setText("签到");
                break;
            default:
                break;
        }
    }

    private void register(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        Intent intent = getIntent();
        if (ret != 0) {
            showTip("注册失败");
            intent.putExtra("REGISTER_RESULT","FAILED");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            showTip("注册成功");
            intent.putExtra("REGISTER_RESULT","SUCCESS");
        } else {
            showTip("注册失败");
            intent.putExtra("REGISTER_RESULT","FAILED");
        }
        FaceRecognitionActivity.this.setResult(RESULT_OK,intent);
        FaceRecognitionActivity.this.finish();
    }

    private void verify(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        Intent intent = getIntent();
        if (ret != 0) {
            intent.putExtra("VERIFY_RESULT","FAILED");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            if (obj.getBoolean("verf")) {
                intent.putExtra("VERIFY_RESULT","SUCCESS");
            } else {
                intent.putExtra("VERIFY_RESULT","FAILED");
            }
        } else {
            intent.putExtra("VERIFY_RESULT","FAILED");
        }
        FaceRecognitionActivity.this.setResult(RESULT_OK,intent);
        FaceRecognitionActivity.this.finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reg_verify:
                processRegOrVerify();
                break;
            case R.id.iv_userFace:
                // 设置相机拍照后照片保存路径

                mPictureFile = new File(Environment.getExternalStorageDirectory(),
                        "user-face_" + mAuthid + "_" + System.currentTimeMillis() / 1000 + ".jpg");

                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, mPictureFile.getAbsolutePath());
                mPictureUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

                // 启动拍照,并保存到临时文件
                Intent mIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPictureUri);
                startActivityForResult(mIntent, REQUEST_CAMERA_IMAGE);
                break;

            default:
                break;
        }
    }

    private void processRegOrVerify(){
        switch (rt){
            case Global.FACE_REGISTER:
                if (null != mImageData) {
                    mProDialog.setMessage("注册中...");
                    mProDialog.show();
                    // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                    // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                    mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                    mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
                    mFaceRequest.sendRequest(mImageData, mRequestListener);
                } else {
                    showTip("请选择图片后再注册");
                }
                break;
            case Global.FACE_VERIFY:

                if (null != mImageData) {
                    mProDialog.setMessage("验证中...");
                    mProDialog.show();
                    // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                    // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                    mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                    mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
                    mFaceRequest.sendRequest(mImageData, mRequestListener);
                } else {
                    showTip("请选择图片后再验证");
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        String fileSrc = null;
        if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (null == mPictureFile) {
                showTip("拍照失败，请重试");
                return;
            }

            FaceUtil.cropPicture(this,mPictureUri);
        } else if (requestCode == FaceUtil.REQUEST_CROP_IMAGE) {
            // 获取返回数据
            Bitmap bmp = data.getParcelableExtra("data");
            // 若返回数据不为null，保存至本地，防止裁剪时未能正常保存
            if(null != bmp){
                FaceUtil.saveBitmapToFile(this, bmp);
            }
            // 获取图片保存路径
            fileSrc = FaceUtil.getImagePath(this);
            // 获取图片的宽和高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            mImage = BitmapFactory.decodeFile(fileSrc, options);

            // 压缩图片
            options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                    (double) options.outWidth / 1024f,
                    (double) options.outHeight / 1024f)));
            options.inJustDecodeBounds = false;
            mImage = BitmapFactory.decodeFile(fileSrc, options);


            // 若mImageBitmap为空则图片信息不能正常获取
            if(null == mImage) {
                showTip("图片信息无法正常获取！");
                return;
            }

            // 部分手机会对图片做旋转，这里检测旋转角度
            int degree = FaceUtil.readPictureDegree(fileSrc);
            if (degree != 0) {
                // 把图片旋转为正的方向
                mImage = FaceUtil.rotateImage(degree, mImage);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //可根据流量及网络状况对图片进行压缩
            mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            mImageData = baos.toByteArray();

            ((ImageView) findViewById(R.id.iv_userFace)).setImageBitmap(mImage);
        }
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
}
