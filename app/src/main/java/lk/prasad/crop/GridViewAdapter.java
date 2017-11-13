package lk.prasad.crop;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
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

public class GridViewAdapter extends ArrayAdapter {
	private Context context;
	private int layoutResourceId;

	
	/*--list which loads list of video types --*/
	List<String> VIDEO_TYPE_LIST = new ArrayList<String>();
	
	/*--list which loads list of video types --*/
	List<String> VIDEO_TYPE_IMG_URL_LIST = new ArrayList<String>();

	public GridViewAdapter(Context context, int layoutResourceId, List<String> typeList,List<String> imgList) {
		super(context, layoutResourceId, typeList);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.VIDEO_TYPE_LIST = typeList;
		this.VIDEO_TYPE_IMG_URL_LIST = imgList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.imageTitle = (TextView) row.findViewById(R.id.text);
			holder.image = (ImageView) row.findViewById(R.id.image);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		holder.imageTitle.setText(VIDEO_TYPE_LIST.get(position));
		if (DashBoard.languageId == 2) {
			Typeface font = Typeface.createFromAsset(DashBoard.assetManager,
					"FM-BINDU.TTF");
			holder.imageTitle.setTypeface(font);
		} 
	
		
		String s = VIDEO_TYPE_IMG_URL_LIST.get(position);
		
		if (!DashBoard.arrayFiles.contains(s)) {
			DashBoard.arrayFiles.add(s);
			holder.image.setImageResource(R.drawable.loading);

			FakeNetLoader fl = new FakeNetLoader();
			fl.execute(holder.image, s, position);
		} else {

			try {
				File f = new File(DashBoard.directory.toString(), s);
				Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
				holder.image.setImageBitmap(b);
			} catch (FileNotFoundException e) {
			}
		}
		
		
//		ImageItem item = data.get(position);
//		holder.imageTitle.setText(item.getTitle());
//		holder.image.setImageBitmap(item.getImage());
		return row;
	}

	static class ViewHolder {
		TextView imageTitle;
		ImageView image;
	}
	
	
	
	
	
	
	
	private class FakeNetLoader extends AsyncTask<Object, Void, List<Object>> {

		@Override
		protected List<Object> doInBackground(Object... urls) {

			List<Object> val = new ArrayList<Object>();

			val.add((ImageView) urls[0]);
			val.add((Integer) urls[2]);

			try {

				try {
					URL imageUrl = new URL(context.getResources().getString(R.string.image_url)
							+ (String) urls[1]);
					HttpURLConnection connection = (HttpURLConnection) imageUrl
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					val.add(BitmapFactory.decodeStream(input));

				} catch (Exception e) {
					val.add(null);
				}
				val.add((String) urls[1]);

			} catch (Exception e) {

			}
			return val;
		}

		@Override
		protected void onPostExecute(List<Object> result) {
			super.onPostExecute(result);
			ImageView img = (ImageView) result.get(0);
			Integer position = (Integer) result.get(1);
			Bitmap bitmap = (Bitmap) result.get(2);

			if (bitmap != null) {

				img.setImageBitmap(bitmap);
				saveImage(bitmap, (String) result.get(3));
			} else {
				img.setImageResource(R.drawable.fail);
			}

		}

	}

	public void saveImage(Bitmap bitmap, String name) {

		File mypath = new File(DashBoard.directory, name);

		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(mypath);
			// Bitmap image=resizeBitMapImage1(mypath.toString(),600,600);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



}