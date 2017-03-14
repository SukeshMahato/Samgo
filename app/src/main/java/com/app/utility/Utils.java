package com.app.utility;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {

	// public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =
	// 123;

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	/*
	 * @TargetApi(Build.VERSION_CODES.JELLY_BEAN) public static boolean
	 * checkPermission(final Context context) { int currentAPIVersion =
	 * Build.VERSION.SDK_INT; if (currentAPIVersion >=
	 * android.os.Build.VERSION_CODES.M) { if
	 * (ContextCompat.checkSelfPermission(context,
	 * Manifest.permission.READ_EXTERNAL_STORAGE) !=
	 * PackageManager.PERMISSION_GRANTED) { if
	 * (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
	 * Manifest.permission.READ_EXTERNAL_STORAGE)) { AlertDialog.Builder
	 * alertBuilder = new AlertDialog.Builder(context);
	 * alertBuilder.setCancelable(true); alertBuilder.setTitle(
	 * "Permission necessary"); alertBuilder.setMessage(
	 * "External storage permission is necessary");
	 * alertBuilder.setPositiveButton(android.R.string.yes, new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @TargetApi(Build.VERSION_CODES.JELLY_BEAN) public void
	 * onClick(DialogInterface dialog, int which) {
	 * ActivityCompat.requestPermissions((Activity) context, new String[] {
	 * Manifest.permission.READ_EXTERNAL_STORAGE },
	 * MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE); } }); AlertDialog alert =
	 * alertBuilder.create(); alert.show(); } else {
	 * ActivityCompat.requestPermissions((Activity) context, new String[] {
	 * Manifest.permission.READ_EXTERNAL_STORAGE },
	 * MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE); } return false; } else {
	 * return true; } } else { return true; } }
	 */
}