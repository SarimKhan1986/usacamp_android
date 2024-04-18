package com.usacamp.net;

import android.util.Log;

import com.usacamp.activities.MyApplication;
import com.usacamp.constants.Constants;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class CampNetConnection {

//    public static final String SERVER_PORT = "80/";

    //connection timeout - 30 sec
    private static final int CONNECT_TIMEOUT_MS = 5 * 1000;

    URL mURL = null;
    HttpURLConnection mConnection = null;

    public String doGet(String strFuncURL) throws IOException {

        mURL = new URL(MyApplication.getInstance().SERVER_API+strFuncURL);
        mConnection = (HttpURLConnection)mURL.openConnection();
        mConnection.setRequestMethod("GET");
        mConnection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        mConnection.setReadTimeout(CONNECT_TIMEOUT_MS);
        mConnection.setDoInput(true);
        //starts the query
        mConnection.connect();

        InputStream stream = mConnection.getInputStream();
        BufferedReader reader;
        String strResult;
        reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

        strResult = reader.readLine();


        stream.close();

        return strResult;
    }

    public String doPost(String strFuncURL, String strParam) throws IOException {
        BufferedReader reader;
        String strResult = null;
        DataOutputStream out;
        InputStream stream;
        if(mConnection != null)
            mConnection.disconnect();
        mURL = new URL(MyApplication.getInstance().SERVER_API + strFuncURL);
        mConnection = (HttpURLConnection) mURL.openConnection();
        mConnection.setRequestMethod("POST");
        mConnection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        mConnection.setReadTimeout(CONNECT_TIMEOUT_MS);
        mConnection.setUseCaches(false);
        mConnection.setDoInput(true);
        mConnection.setDoOutput(true);

        //phone=15246231081&hardTypeId=2&hardware=111111111
        //mConnection.addRequestProperty("Content-Type", "charset=utf-8");
        out = new DataOutputStream(mConnection.getOutputStream());

        out.write(strParam.getBytes());
        out.close();

        //starts the query
        //mConnection.connect();
        stream = mConnection.getInputStream();

        reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

        strResult = reader.readLine();

        stream.close();

        return strResult;
    }



    public String doPostWithAttachedFile(String strFuncURL, String strParam, ArrayList<String> listAttachedFilePath ) throws IOException {
        BufferedReader reader;
        String strResult = null;
        DataOutputStream out;
        InputStream stream;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "----camp20190801";
        int serverResponseCode = 0;

        int i = 0;
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        String strPicFilePath;

        mURL = new URL(MyApplication.getInstance().SERVER_API + strFuncURL);
        mConnection = (HttpURLConnection) mURL.openConnection();
        mConnection.setRequestMethod("POST");
        mConnection.setUseCaches(false);
        mConnection.setDoInput(true);
        mConnection.setDoOutput(true);
        mConnection.setRequestProperty("Connection", "Keep-Alive");
        mConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
        mConnection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + boundary);

        if (strFuncURL.equals("picUpload")) {
            if (listAttachedFilePath.size() == 0)
                return strResult;

            strPicFilePath =listAttachedFilePath.get(0);
            if (strPicFilePath == null || strPicFilePath.equals("") == true)
                return strResult;

            File selectedFile = new File(strPicFilePath);

            if (!selectedFile.isFile()) {
                return strResult;
            } else {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                mConnection.setRequestProperty("profile_pic", strPicFilePath);

                //creating new dataoutputstream
                out = new DataOutputStream(mConnection.getOutputStream());

                //writing bytes to data outputstream
                String[] parts =  strParam.split("&");
                i = 0;
                while ( i < parts.length ) {
                    String[] value = parts[i].split("=");

                    out.writeBytes(twoHyphens + boundary + lineEnd);
                    out.writeBytes("Content-Disposition: form-data; name=\"" + value[0] + "\"" + lineEnd + lineEnd);
                    out.writeBytes(value[1] + lineEnd);
                    i++;
                }

                out.writeBytes(twoHyphens + boundary + lineEnd);
                out.writeBytes("Content-Disposition: form-data; name=\"profile_pic\";filename=\""
                        + strPicFilePath + "\"" + lineEnd);

                out.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();

                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);

                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    try {
                        //write the bytes read from inputstream
                        out.write(buffer, 0, bufferSize);
                    } catch (OutOfMemoryError e) {
                        return strResult;
                    }
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                out.writeBytes(lineEnd);
                out.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                try {
                    serverResponseCode = mConnection.getResponseCode();
                } catch (OutOfMemoryError e) {
                    return strResult;
                }
                String serverResponseMessage = mConnection.getResponseMessage();


                out.flush();
                out.close();
                fileInputStream.close();

                stream = mConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                strResult = reader.readLine();
                stream.close();
            }

        }
        else if (strFuncURL.equals("sendReport")) {
            //creating new dataoutputstream
            mConnection.addRequestProperty("Content-Type", "charset=utf-8");
            out = new DataOutputStream(mConnection.getOutputStream());
            String[] parts =  strParam.split("&");
            i = 0;
            while ( i < parts.length ) {
                String[] value = parts[i].split("=");
                out.writeBytes(twoHyphens + boundary + lineEnd);
                out.writeBytes("Content-Disposition: form-data; name=\"" + value[0] + "\"" + lineEnd + lineEnd);
                out.write((value[1] + lineEnd).getBytes(StandardCharsets.UTF_8));
                i++;
            }
            i = 0;
            while (i < listAttachedFilePath.size()) {
                strPicFilePath = listAttachedFilePath.get(i);
                if (strPicFilePath == null || strPicFilePath.equals("") == true)
                    continue;

                File selectedFile = new File(strPicFilePath);

                if (!selectedFile.isFile()) {
                    continue;
                } else {
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);

                    //writing bytes to data outputstream
                    out.writeBytes(twoHyphens + boundary + lineEnd);
                    out.writeBytes("Content-Disposition: form-data; name=\"report_file[" + i +"]\";filename=\""
                            + strPicFilePath + "\"" + lineEnd);

                    out.writeBytes(lineEnd);

                    //returns no. of bytes present in fileInputStream
                    bytesAvailable = fileInputStream.available();

                    //selecting the buffer size as minimum of available bytes or 1 MB
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);

                    //setting the buffer as byte array of size of bufferSize
                    buffer = new byte[bufferSize];

                    //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                    while (bytesRead > 0) {
                        try {
                            //write the bytes read from inputstream
                            out.write(buffer, 0, bufferSize);
                        } catch (OutOfMemoryError e) {
                            return strResult;
                        }
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    out.writeBytes(lineEnd);
                    out.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    fileInputStream.close();
                }

                i++;
            }

            out.flush();
            out.close();

            try {
                serverResponseCode = mConnection.getResponseCode();
            } catch (OutOfMemoryError e) {
                return strResult;
            }
            String serverResponseMessage = mConnection.getResponseMessage();
            stream = mConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            strResult = reader.readLine();
            stream.close();
        }

        return strResult;
    }
}
