package com.usacamp.utils;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usacamp.BuildConfig;
import com.usacamp.R;
import com.usacamp.activities.MyApplication;
import com.usacamp.libs.DoConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import androidx.core.content.FileProvider;

public class Util {

	private static final String TAG = "SDK_Sample.Util";
	static Dialog versionDialog = null;
	static View VersionDialogView = null;
	public static class DownloadNewVersion extends AsyncTask<String,Integer,Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			LayoutInflater factory = LayoutInflater.from(MyApplication.getInstance().getCurrentActivity());
			VersionDialogView = factory.inflate(R.layout.versionupgradeprogressdlg, null);
			versionDialog = new Dialog(MyApplication.getInstance().getCurrentActivity());
			versionDialog.setCanceledOnTouchOutside(false);
			versionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			versionDialog.setContentView(VersionDialogView);
			versionDialog.getWindow().setGravity(Gravity.BOTTOM);
			versionDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			versionDialog.show();

		}
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			((sekbartextmerge)VersionDialogView.findViewById(R.id.progressBar)).seekBar.setMax(100);
			((sekbartextmerge)VersionDialogView.findViewById(R.id.progressBar)).seekBar.setProgress(progress[0]);
			((sekbartextmerge)VersionDialogView.findViewById(R.id.progressBar)).setThumbText(((sekbartextmerge)VersionDialogView.findViewById(R.id.progressBar)).seekBar.getProgress());
			((sekbartextmerge)VersionDialogView.findViewById(R.id.progressBar)).setTvShowing("Loading");

		}
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

//			if(result){
//
//				Toast.makeText(getApplicationContext(),"Done!!", Toast.LENGTH_SHORT).show();
//			}else{
//				Toast.makeText(getApplicationContext(),"Error: Try Again", Toast.LENGTH_SHORT).show();
//			}
		}
		@Override
		protected Boolean doInBackground(String... arg0) {
			Boolean flag = false;
			try {
				URL url = new URL(MyApplication.mNetProc.mLoginUserInf.versionInfo.apk_link);
				Log.d(TAG, MyApplication.mNetProc.mLoginUserInf.versionInfo.apk_link);
				HttpURLConnection c = (HttpURLConnection) url.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(false);
				c.connect();
				String PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
				File file = new File(PATH);
//                file.mkdirs();
				File outputFile = new File(file,"usacamp.apk");
				if(outputFile.exists()){
					outputFile.delete();
				}
				FileOutputStream fos = new FileOutputStream(outputFile);
				InputStream is = c.getInputStream();
				int total_size = 4581692;//size of apk
				byte[] buffer = new byte[1024];
				int len1 = 0;
				int per = 0;
				int downloaded=0;
				while ((len1 = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len1);
					downloaded +=len1;
					per = (int) (downloaded * 100 / total_size);
					publishProgress(per);
				}
				fos.close();
				is.close();
				OpenNewVersion(PATH);
				flag = true;
			} catch (Exception e) {
				Log.e("versionUpgrade", "Update Error: " + e.getMessage());
				e.printStackTrace();
				flag = false;
			}
			return flag;
		}
	}
	static void OpenNewVersion(String location) {
		//versionLayout.setVisibility(View.GONE);
		versionDialog.dismiss();
		Uri uri = FileProvider.getUriForFile(MyApplication.getInstance().getCurrentActivity(), BuildConfig.APPLICATION_ID + ".provider",new File(location+"/usacamp.apk"));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		MyApplication.getInstance().startActivity(intent);
	}

	private static class HttpAsync extends AsyncTask <String, String, HttpResponse>
	{
		@Override
		protected HttpResponse doInBackground(String... params)
		{
			String url = params[0];
			String entity = params[1];

			HttpClient httpclient = new DoConnection().getHttpClient();
			HttpPost request = new HttpPost(url);
			try {
				request.setEntity(new StringEntity(entity));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			HttpResponse response = null;
			try {
				response = httpclient.execute(request);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(HttpResponse result)
		{
		}
	}

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static byte[] httpGet(final String url) {
		if (url == null || url.length() == 0) {
			Log.e(TAG, "httpGet, url is null");
			return null;
		}

		HttpAsync http = new HttpAsync();

		try {
			HttpResponse resp = http.execute(url).get();
			if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Log.e(TAG, "httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
				return null;
			}

			return EntityUtils.toByteArray(resp.getEntity());

		} catch (Exception e) {
			Log.e(TAG, "httpGet exception, e = " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] httpPost(String url, String entity) {
		if (url == null || url.length() == 0) {
			Log.e(TAG, "httpPost, url is null");
			return null;
		}

		HttpAsync http = new HttpAsync();

		try {
			HttpResponse resp = http.execute(url, entity).get();

			if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				Log.e(TAG, "httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
				return null;
			}

			return EntityUtils.toByteArray(resp.getEntity());
		} catch (Exception e) {
			Log.e(TAG, "httpPost exception, e = " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private static class SSLSocketFactoryEx extends SSLSocketFactory {

		SSLContext sslContext = SSLContext.getInstance("TLS");

		public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain,	String authType) throws java.security.cert.CertificateException {
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
			return sslContext.getSocketFactory().createSocket(socket, host,	port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	private static HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);

			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	public static byte[] readFromFile(String fileName, int offset, int len) {
		if (fileName == null) {
			return null;
		}

		File file = new File(fileName);
		if (!file.exists()) {
			Log.i(TAG, "readFromFile: file not found");
			return null;
		}

		if (len == -1) {
			len = (int) file.length();
		}

		if(offset <0){
			Log.e(TAG, "readFromFile invalid offset:" + offset);
			return null;
		}
		if(len <=0 ){
			Log.e(TAG, "readFromFile invalid len:" + len);
			return null;
		}
		if(offset + len > (int) file.length()){
			Log.e(TAG, "readFromFile invalid file len:" + file.length());
			return null;
		}

		byte[] b = null;
		try {
			RandomAccessFile in = new RandomAccessFile(fileName, "r");
			b = new byte[len]; // ���������ļ���С������
			in.seek(offset);
			in.readFully(b);
			in.close();

		} catch (Exception e) {
			Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
			e.printStackTrace();
		}
		return b;
	}

	private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
	public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
//		Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

		BitmapFactory.Options options = new BitmapFactory.Options();

		try {
			options.inJustDecodeBounds = true;
			Bitmap tmp = BitmapFactory.decodeFile(path, options);
			if (tmp != null) {
				tmp.recycle();
				tmp = null;
			}

			final double beY = options.outHeight * 1.0 / height;
			final double beX = options.outWidth * 1.0 / width;
			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
			if (options.inSampleSize <= 1) {
				options.inSampleSize = 1;
			}

			// NOTE: out of memory error
			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
				options.inSampleSize++;
			}

			int newHeight = height;
			int newWidth = width;
			if (crop) {
				if (beY > beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			} else {
				if (beY < beX) {
					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
				} else {
					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
				}
			}

			options.inJustDecodeBounds = false;

			Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
			Bitmap bm = BitmapFactory.decodeFile(path, options);
			if (bm == null) {
				Log.e(TAG, "bitmap decode failed");
				return null;
			}

			Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
			if (scale != null) {
				bm.recycle();
				bm = scale;
			}

			if (crop) {
				final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
				if (cropped == null) {
					return bm;
				}

				bm.recycle();
				bm = cropped;
				Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
			}
			return bm;

		} catch (final OutOfMemoryError e) {
			Log.e(TAG, "decode bitmap failed: " + e.getMessage());
			options = null;
		}

		return null;
	}

	public static String sha1(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}

		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes());

			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] buf = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}

	public static List<String> stringsToList(final String[] src) {
		if (src == null || src.length == 0) {
			return null;
		}
		final List<String> result = new ArrayList<String>();
		for (int i = 0; i < src.length; i++) {
			result.add(src[i]);
		}
		return result;
	}
}

//package com.usacamp.util;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.security.KeyManagementException;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.UnrecoverableKeyException;
//import java.security.cert.X509Certificate;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.HttpVersion;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ClientConnectionManager;
//import org.apache.http.conn.scheme.PlainSocketFactory;
//import org.apache.http.conn.scheme.Scheme;
//import org.apache.http.conn.scheme.SchemeRegistry;
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.params.HttpProtocolParams;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.util.EntityUtils;
//
//import junit.framework.Assert;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Bitmap.CompressFormat;
//import android.util.Log;
//
//public class Util {
//
//	private static final String TAG = "SDK_Sample.Util";
//
//	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
//		ByteArrayOutputStream output = new ByteArrayOutputStream();
//		bmp.compress(CompressFormat.PNG, 100, output);
//		if (needRecycle) {
//			bmp.recycle();
//		}
//
//		byte[] result = output.toByteArray();
//		try {
//			output.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//
//	public static byte[] httpGet(final String url) {
//		if (url == null || url.length() == 0) {
//			Log.e(TAG, "httpGet, url is null");
//			return null;
//		}
//
//		HttpClient httpClient = getNewHttpClient();
//		HttpGet httpGet = new HttpGet(url);
//
//		try {
//			HttpResponse resp = httpClient.execute(httpGet);
//			if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//				Log.e(TAG, "httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
//				return null;
//			}
//
//			return EntityUtils.toByteArray(resp.getEntity());
//
//		} catch (Exception e) {
//			Log.e(TAG, "httpGet exception, e = " + e.getMessage());
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	public static byte[] httpPost(String url, String entity) {
//		if (url == null || url.length() == 0) {
//			Log.e(TAG, "httpPost, url is null");
//			return null;
//		}
//
//		HttpClient httpClient = getNewHttpClient();
//
//		HttpPost httpPost = new HttpPost(url);
//
//		try {
//			httpPost.setEntity(new StringEntity(entity));
//			httpPost.setHeader("Accept", "application/json");
//			httpPost.setHeader("Content-type", "application/json");
//
//			HttpResponse resp = httpClient.execute(httpPost);
//			if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
//				Log.e(TAG, "httpGet fail, status code = " + resp.getStatusLine().getStatusCode());
//				return null;
//			}
//
//			return EntityUtils.toByteArray(resp.getEntity());
//		} catch (Exception e) {
//			Log.e(TAG, "httpPost exception, e = " + e.getMessage());
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	private static class SSLSocketFactoryEx extends SSLSocketFactory {
//
//		SSLContext sslContext = SSLContext.getInstance("TLS");
//
//		public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
//			super(truststore);
//
//			TrustManager tm = new X509TrustManager() {
//
//				public X509Certificate[] getAcceptedIssuers() {
//					return null;
//				}
//
//				@Override
//				public void checkClientTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
//				}
//
//				@Override
//				public void checkServerTrusted(X509Certificate[] chain,	String authType) throws java.security.cert.CertificateException {
//				}
//			};
//
//			sslContext.init(null, new TrustManager[] { tm }, null);
//		}
//
//		@Override
//		public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
//			return sslContext.getSocketFactory().createSocket(socket, host,	port, autoClose);
//		}
//
//		@Override
//		public Socket createSocket() throws IOException {
//			return sslContext.getSocketFactory().createSocket();
//		}
//	}
//
//	private static HttpClient getNewHttpClient() {
//		try {
//			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//			trustStore.load(null, null);
//
//			SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
//			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//			HttpParams params = new BasicHttpParams();
//			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//
//			SchemeRegistry registry = new SchemeRegistry();
//			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//			registry.register(new Scheme("https", sf, 443));
//
//			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
//
//			return new DefaultHttpClient(ccm, params);
//		} catch (Exception e) {
//			return new DefaultHttpClient();
//		}
//	}
//
//	public static byte[] readFromFile(String fileName, int offset, int len) {
//		if (fileName == null) {
//			return null;
//		}
//
//		File file = new File(fileName);
//		if (!file.exists()) {
//			Log.i(TAG, "readFromFile: file not found");
//			return null;
//		}
//
//		if (len == -1) {
//			len = (int) file.length();
//		}
//
//
//		if(offset <0){
//			Log.e(TAG, "readFromFile invalid offset:" + offset);
//			return null;
//		}
//		if(len <=0 ){
//			Log.e(TAG, "readFromFile invalid len:" + len);
//			return null;
//		}
//		if(offset + len > (int) file.length()){
//			Log.e(TAG, "readFromFile invalid file len:" + file.length());
//			return null;
//		}
//
//		byte[] b = null;
//		try {
//			RandomAccessFile in = new RandomAccessFile(fileName, "r");
//			b = new byte[len]; // ´´½¨ºÏÊÊÎÄ¼þ´óÐ¡µÄÊý×é
//			in.seek(offset);
//			in.readFully(b);
//			in.close();
//
//		} catch (Exception e) {
//			Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
//			e.printStackTrace();
//		}
//		return b;
//	}
//
//	private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
//	public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
//		Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);
//
//		BitmapFactory.Options options = new BitmapFactory.Options();
//
//		try {
//			options.inJustDecodeBounds = true;
//			Bitmap tmp = BitmapFactory.decodeFile(path, options);
//			if (tmp != null) {
//				tmp.recycle();
//				tmp = null;
//			}
//
//			final double beY = options.outHeight * 1.0 / height;
//			final double beX = options.outWidth * 1.0 / width;
//			options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
//			if (options.inSampleSize <= 1) {
//				options.inSampleSize = 1;
//			}
//
//			// NOTE: out of memory error
//			while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
//				options.inSampleSize++;
//			}
//
//			int newHeight = height;
//			int newWidth = width;
//			if (crop) {
//				if (beY > beX) {
//					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
//				} else {
//					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
//				}
//			} else {
//				if (beY < beX) {
//					newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
//				} else {
//					newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
//				}
//			}
//
//			options.inJustDecodeBounds = false;
//
//			Log.i(TAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
//			Bitmap bm = BitmapFactory.decodeFile(path, options);
//			if (bm == null) {
//				Log.e(TAG, "bitmap decode failed");
//				return null;
//			}
//
//			Log.i(TAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
//			final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
//			if (scale != null) {
//				bm.recycle();
//				bm = scale;
//			}
//
//			if (crop) {
//				final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
//				if (cropped == null) {
//					return bm;
//				}
//
//				bm.recycle();
//				bm = cropped;
//				Log.i(TAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
//			}
//			return bm;
//
//		} catch (final OutOfMemoryError e) {
//			Log.e(TAG, "decode bitmap failed: " + e.getMessage());
//			options = null;
//		}
//
//		return null;
//	}
//
//	public static String sha1(String str) {
//		if (str == null || str.length() == 0) {
//			return null;
//		}
//
//		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
//
//		try {
//			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
//			mdTemp.update(str.getBytes());
//
//			byte[] md = mdTemp.digest();
//			int j = md.length;
//			char buf[] = new char[j * 2];
//			int k = 0;
//			for (int i = 0; i < j; i++) {
//				byte byte0 = md[i];
//				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
//				buf[k++] = hexDigits[byte0 & 0xf];
//			}
//			return new String(buf);
//		} catch (Exception e) {
//			return null;
//		}
//	}
//
//	public static List<String> stringsToList(final String[] src) {
//		if (src == null || src.length == 0) {
//			return null;
//		}
//		final List<String> result = new ArrayList<String>();
//		for (int i = 0; i < src.length; i++) {
//			result.add(src[i]);
//		}
//		return result;
//	}
//}
