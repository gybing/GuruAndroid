package cn.dcs.leef.slidingdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ColorMenuFragment extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] colors = getResources().getStringArray(R.array.color_names);
		
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, colors);
		
		setListAdapter(colorAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			newContent = new ColorFragment(R.color.red);
			break;
		case 1:
			newContent = new ColorFragment(R.color.green);
			break;
		case 2:
			newContent = new ColorFragment(R.color.blue);
			break;
		case 3:
			newContent = new ColorFragment(android.R.color.white);
			break;
		case 4:
			newContent = new ColorFragment(android.R.color.black);
			break;
		}
		if (newContent != null)
			switchFragment(newContent);
	}

	// �л�Fragment��ͼ��ring
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof SlidingMenuActivity_4) {
			SlidingMenuActivity_4 fca = (SlidingMenuActivity_4) getActivity();
			fca.switchContent(fragment);
		} 
	}


}
