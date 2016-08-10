package com.example.camera;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,ImageUtil.CropHandler{
	Dialog dialog;
	ImageView img;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		img=(ImageView)findViewById(R.id.imageView1);
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
	           showDiaLog();			
			}

		});
	}
	private void showDiaLog() {
		
		View view = getLayoutInflater().inflate(R.layout.icolayout, null);
		Button pic = (Button) view.findViewById(R.id.pic);
		Button camera = (Button) view.findViewById(R.id.camera);
		Button cancle = (Button) view.findViewById(R.id.cancle);
		pic.setOnClickListener(this);
		camera.setOnClickListener(this);
		cancle.setOnClickListener(this);
		dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
		DialogStyle.setDialog(this, dialog, view);
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		ImageUtil.getCropHelperInstance().sethandleResultListerner(this, requestCode, resultCode, data);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pic:
			Intent intent =ImageUtil.getCropHelperInstance().buildGalleryIntent();
			startActivityForResult(intent, ImageUtil.REQUEST_GALLERY);
			dialog.dismiss();
			break;
        case R.id.camera:
        	Intent mintent =ImageUtil.getCropHelperInstance().buildCameraIntent();
			startActivityForResult(mintent, ImageUtil.REQUEST_CAMERA);
			dialog.dismiss();
			break;
      case R.id.cancle:
    	  dialog.cancel();
			break;
		}
	}
	@Override
	public Activity getContext() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	public void onPhotoCropped(Bitmap photo, int requesCode) {
		img.setImageBitmap(photo);
	}
	@Override
	public void onCropCancel() {
		
	}
	@Override
	public void onCropFailed(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
	
	

}
