package com.choco.limsclient.Activities.UtilActivities.QRCode;

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

public class GenQRCodeActivity extends AppCompatActivity {
    String qrMsg;
    ImageView qrCodeImageView;
    TextView tvDeviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设备信息");
        setContentView(R.layout.activity_gen_qrcode);
        Intent intent = getIntent();
        qrMsg = intent.getStringExtra("DEVICE_INFO");

        qrCodeImageView = (ImageView) findViewById(R.id.iv_qrCode);
        qrCodeImageView.setImageBitmap(encodeAsBitmap(qrMsg));

        tvDeviceInfo = (TextView) findViewById(R.id.textView_deviceInfo);
        tvDeviceInfo.setText(qrMsg);
    }

    public Bitmap encodeAsBitmap(String text) {
        Bitmap bitmap = null;
        BitMatrix result;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        HashMap<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            result = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200, hints);
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
