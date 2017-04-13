package com.choco.limsclient.Activities.QRCode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.choco.limsclient.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class GenQRCodeActivity extends AppCompatActivity {
    String name;
    String type;
    String principal;
    ImageView qrCodeImageView;
    TextView tvDeviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设备信息");
        setContentView(R.layout.activity_gen_qrcode);
        Intent intent = getIntent();
        name = "设备名称：" + intent.getStringExtra("DEVICE_NAME") + "\n";
        type = "设备类型：" + intent.getStringExtra("DEVICE_TYPE") + "\n";
        principal = "设备负责人：" + intent.getStringExtra("DEVICE_PRINCIPAL") + "\n";

        qrCodeImageView = (ImageView) findViewById(R.id.imageView_qrCode);
        qrCodeImageView.setImageBitmap(encodeAsBitmap(name + type + principal));

        tvDeviceInfo = (TextView) findViewById(R.id.textView_deviceInfo);
        tvDeviceInfo.setText(name+type+principal);
    }

    public Bitmap encodeAsBitmap(String text) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        HashMap<EncodeHintType,String> hints = new HashMap<>() ;
        hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
        try {
            result = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200,hints);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) {
            return null;
        }
        return bitmap;
    }
}
