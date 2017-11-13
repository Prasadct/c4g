package lk.prasad.crop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Prasadct on 9/19/2015.
 */
public class DiseasesList extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] mainList;
	private final String[] image;

	public static View view;
	public DiseasesList(Activity context, String[] mainList, String[] image) {
		super(context, R.layout.main_list, mainList);
		this.context = context;
		this.mainList = mainList;
		this.image = image;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.main_list_single_item, null,
				true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_main_list);

		ImageView imageView = (ImageView) rowView
				.findViewById(R.id.img_main_list);
		txtTitle.setText(mainList[position]);
		if (DashBoard.languageId == 2) {
			Typeface font = Typeface.createFromAsset(DashBoard.assetManager,
					"FM-BINDU.TTF");
			txtTitle.setTypeface(font);
		} 
		view=rowView;
	//	imageView.setImageResource(imageId[position]);
		imageView.setImageResource(R.drawable.loading);
		
		FakeNetLoader fl = new FakeNetLoader();
		fl.execute(imageView,image[position],position);
		return rowView;
	}
	
	
	
	
	
	

	private class FakeNetLoader extends AsyncTask<Object, Void, List<Object>> {

	


		@Override
		protected List<Object> doInBackground(Object... urls) {	
			
			List<Object> val=new ArrayList<Object>();
			
			val.add((ImageView) urls[0]);
			val.add((Integer) urls[2]);
			
			try {
				view = (ImageView) urls[0];
		 
					
							try {
								
							
						
							URL imageUrl = new URL("http://weddingsrilanka.com/jokeapp/photos/joke/"+(String)urls[1]);
	    					HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
	    					connection.setDoInput(true);
	    					connection.connect();
	    					InputStream input = connection.getInputStream();
	    					val.add(BitmapFactory.decodeStream(input));
	    					
							} catch (Exception e) {
								val.add(null);
							}
							val.add((String)urls[1]);
					 
					
			
				} catch (Exception e) {	
					
					
				} 
			return val;
		}
		

		@Override
		protected void onPostExecute(List<Object> result) {			
			super.onPostExecute(result);
			ImageView img=(ImageView) result.get(0);
			Integer position=(Integer) result.get(1);
			Bitmap bitmap=(Bitmap)result.get(2);
			

			
			if(bitmap!=null){
				img.setImageBitmap(bitmap);
				saveImage(bitmap,(String)result.get(3));
			}
			else{
				img.setImageResource(R.drawable.fail);
			}

		}
		
			
	}
	
 
	
	//save image
	
	public void saveImage(Bitmap bitmap,String name){
		
		 File mypath=new File(DashBoard.directory,name);

	        FileOutputStream fos = null;
	        try {           

	            fos = new FileOutputStream(mypath);
	          //  Bitmap image=resizeBitMapImage1(mypath.toString(),600,600);
	            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, fos);
	            fos.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
	}
	
	
	
	
	
	
}
