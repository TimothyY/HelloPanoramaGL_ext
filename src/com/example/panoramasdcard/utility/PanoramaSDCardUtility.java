package com.example.panoramasdcard.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.panoramasdcard.R;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

public class PanoramaSDCardUtility {
	
	Context mContext;
	
	public PanoramaSDCardUtility(Context mContext) {
		super();
		this.mContext = mContext;
	}

	/**Prepare for needed json data and panorama jpg.
	 * Only copying those files from res/raw to SD Card.
	 * In real apps, you might want to use your own json data and your camera's pic.*/
	public void prepareRequiredDataToSDCard(){
		createExternalStoragePrivateFile("hotspot.png",R.raw.hotspot);
		createExternalStoragePrivateFile("json_cubic.data",R.raw.json_cubic);
		createExternalStoragePrivateFile("json_cubic_quito2.data",R.raw.json_cubic_quito2);
		createExternalStoragePrivateFile("json_cubic_quito3.data",R.raw.json_cubic_quito3);
		createExternalStoragePrivateFile("json_cylindrical.data",R.raw.json_cylindrical);
		createExternalStoragePrivateFile("json_cylindrical_quito2.data",R.raw.json_cylindrical_quito2);
		createExternalStoragePrivateFile("json_cylindrical_quito3.data",R.raw.json_cylindrical_quito3);
		createExternalStoragePrivateFile("json_spherical.data",R.raw.json_spherical);
		createExternalStoragePrivateFile("json_spherical_quito2.data",R.raw.json_spherical_quito2);
		createExternalStoragePrivateFile("json_spherical_quito3.data",R.raw.json_spherical_quito3);
		createExternalStoragePrivateFile("json_spherical2.data",R.raw.json_spherical2);
		createExternalStoragePrivateFile("json_spherical2_quito2.data",R.raw.json_spherical2_quito2);
		createExternalStoragePrivateFile("json_spherical2_quito3.data",R.raw.json_spherical2_quito3);
		createExternalStoragePrivateFile("quito1_b.jpg",R.raw.quito1_b);
		createExternalStoragePrivateFile("quito1_d.jpg",R.raw.quito1_d);
		createExternalStoragePrivateFile("quito1_f.jpg",R.raw.quito1_f);
		createExternalStoragePrivateFile("quito1_l.jpg",R.raw.quito1_l);
		createExternalStoragePrivateFile("quito1_p.jpg",R.raw.quito1_p);
		createExternalStoragePrivateFile("quito1_r.jpg",R.raw.quito1_r);
		createExternalStoragePrivateFile("quito1_s.jpg",R.raw.quito1_s);
		createExternalStoragePrivateFile("quito1_s2.jpg",R.raw.quito1_s2);
		createExternalStoragePrivateFile("quito1_sp.jpg",R.raw.quito1_sp);
		createExternalStoragePrivateFile("quito1_u.jpg",R.raw.quito1_u);
		createExternalStoragePrivateFile("quito2_b.jpg",R.raw.quito2_b);
		createExternalStoragePrivateFile("quito2_d.jpg",R.raw.quito2_d);
		createExternalStoragePrivateFile("quito2_f.jpg",R.raw.quito2_f);
		createExternalStoragePrivateFile("quito2_l.jpg",R.raw.quito2_l);
		createExternalStoragePrivateFile("quito2_p.jpg",R.raw.quito2_p);
		createExternalStoragePrivateFile("quito2_r.jpg",R.raw.quito2_r);
		createExternalStoragePrivateFile("quito2_s.jpg",R.raw.quito2_s);
		createExternalStoragePrivateFile("quito2_s2.jpg",R.raw.quito2_s2);
		createExternalStoragePrivateFile("quito2_sp.jpg",R.raw.quito2_sp);
		createExternalStoragePrivateFile("quito2_u.jpg",R.raw.quito2_u);
		createExternalStoragePrivateFile("quito3_b.jpg",R.raw.quito3_b);
		createExternalStoragePrivateFile("quito3_d.jpg",R.raw.quito3_d);
		createExternalStoragePrivateFile("quito3_f.jpg",R.raw.quito3_f);
		createExternalStoragePrivateFile("quito3_l.jpg",R.raw.quito3_l);
		createExternalStoragePrivateFile("quito3_p.jpg",R.raw.quito3_p);
		createExternalStoragePrivateFile("quito3_r.jpg",R.raw.quito3_r);
		createExternalStoragePrivateFile("quito3_s.jpg",R.raw.quito3_s);
		createExternalStoragePrivateFile("quito3_s2.jpg",R.raw.quito3_s2);
		createExternalStoragePrivateFile("quito3_sp.jpg",R.raw.quito3_sp);
		createExternalStoragePrivateFile("quito3_u.jpg",R.raw.quito3_u);
	}
	
	/**Copying file from res/raw to SD Card*/
	private void createExternalStoragePrivateFile(String targetFileName, int resID) {
	    // Create a path where we will place our private file on external storage.
	    File file = new File(mContext.getExternalFilesDir(null), targetFileName);

	    try {
	        // Very simple code to copy a picture from the application's
	        // resource into the external file.  Note that this code does
	        // no error checking, and assumes the picture is small (does not
	        // try to copy it in chunks).  Note that if external storage is
	        // not currently mounted this will silently fail.
	        InputStream is = mContext.getResources().openRawResource(resID);
	        OutputStream os = new FileOutputStream(file);
	        byte[] data = new byte[is.available()];
	        is.read(data);
	        os.write(data);
	        is.close();
	        os.close();
//	        Toast.makeText(mContext, mContext.getExternalFilesDir(null).getAbsolutePath(), Toast.LENGTH_SHORT).show();
	        // Tell the media scanner about the new file so that it is
	        // immediately available to the user.
	        MediaScannerConnection.scanFile(mContext,
	                new String[] { file.toString() }, null,
	                new MediaScannerConnection.OnScanCompletedListener() {
	            @Override
	        	public void onScanCompleted(String path, Uri uri) {
	                
	            }
	        });
	    } catch (IOException e) {
	        // Unable to create file, likely because external storage is
	        // not currently mounted.
	    	Toast.makeText(mContext, "Error writing " + file+": "+e, Toast.LENGTH_LONG).show();
	    }

	}
}
