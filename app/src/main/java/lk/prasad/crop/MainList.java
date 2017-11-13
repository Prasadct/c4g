package lk.prasad.crop;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Prasadct on 9/19/2015.
 */
public class MainList extends ArrayAdapter<String> {
	private final Activity context;
	private final String[] mainList;
	private final Integer[] imageId;

	public MainList(Activity context, String[] mainList, Integer[] imageId) {
		super(context, R.layout.main_list, mainList);
		this.context = context;
		this.mainList = mainList;
		this.imageId = imageId;
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
	
		imageView.setImageResource(imageId[position]);
		return rowView;
	}
}
