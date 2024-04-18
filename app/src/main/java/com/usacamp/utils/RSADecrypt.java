package com.usacamp.utils;

import android.os.Build;
import android.util.Log;

import com.bumptech.glide.load.model.ModelLoader;
import com.usacamp.R;
import com.usacamp.activities.MyApplication;

import javax.crypto.Cipher;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import androidx.annotation.RequiresApi;

public class RSADecrypt {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) throws Exception {
        String encrypted = args[0];
        String prikey = args[1];

        String result = decrypt(encrypted);
        System.out.println(result);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String cryptograph) throws Exception {
        String prikey = LoadPrivateKey();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(prikey));
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        byte [] b = Base64.getDecoder().decode(cryptograph);
        return new String(cipher.doFinal(b));
    }
    public static String LoadPrivateKey() {
        String tContents = "";
        InputStream stream = MyApplication.getInstance().getResources().openRawResource(R.raw.key);
        try {
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            Log.d("FileReader", e.toString());
            // Handle exceptions here
        }

        return tContents;

    }
}