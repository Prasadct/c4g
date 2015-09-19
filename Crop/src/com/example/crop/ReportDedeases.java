package com.example.crop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ReportDedeases extends Activity{

	EditText et_des;
	Button btn_photo,btn_submit;
	
	 // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
  
    private Uri fileUri; // file url to store image/video
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_deseases);
		
		et_des=(EditText)findViewById(R.id.et_des);
		
		btn_photo=(Button)findViewById(R.id.btn_photo);
		btn_submit=(Button)findViewById(R.id.btn_submit);
		
		btn_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ContextWrapper cw = new ContextWrapper(getApplicationContext());

//				File directory = cw.getDir("Captured_image ",
//						Context.MODE_APPEND);
//
//				File f = new File(directory.toString());
//				f.mkdirs();
//				File[] file = f.listFiles();
//
//				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//				File photoFile;
//				Intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file[0]));
//				startActivityForResult(intent, 0);
	
//				   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				   
//			      Uri  fileUri = getOutputMediaFileUri(1);
//			  
//			        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//			  
//			        // start the image capture Intent
//			        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
				
			}});
	}
	
	
	   private void captureImage() {
	        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	  
	       fileUri = getOutputMediaFileUri(1);
	  
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	  
	        // start the image capture Intent
	        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	    }
	   public Uri getOutputMediaFileUri(int type) {
	        return Uri.fromFile(getOutputMediaFile(type));
	    }
	
	   private static File getOutputMediaFile(int type) {
		   
	        // External sdcard location
	        File mediaStorageDir = new File(
	                Environment
	                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
	                IMAGE_DIRECTORY_NAME);
	  
	        // Create the storage directory if it does not exist
	        if (!mediaStorageDir.exists()) {
	            if (!mediaStorageDir.mkdirs()) {
	                return null;
	            }
	        }
	  
	        // Create a media file name
	        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
	                Locale.getDefault()).format(new Date());
	        File mediaFile;
	        if (type == MEDIA_TYPE_IMAGE) {
	            mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                    + "IMG_" + timeStamp + ".jpg");
	        } else if (type == MEDIA_TYPE_VIDEO) {
	            mediaFile = new File(mediaStorageDir.getPath() + File.separator
	                    + "VID_" + timeStamp + ".mp4");
	        } else {
	            return null;
	        }
	  
	        return mediaFile;
	    }
	   
	   
}
