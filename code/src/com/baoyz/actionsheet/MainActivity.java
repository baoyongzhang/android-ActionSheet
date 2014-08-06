package com.baoyz.actionsheet;

import com.baoyz.actionsheet.ActionSheet.ActionSheetListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * @author baoyz
 * @date 2014-6-6
 *
 */
public class MainActivity extends FragmentActivity implements
		ActionSheetListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ios6:
			setTheme(R.style.ActionSheetStyleIOS6);
			break;
		case R.id.ios7:
			setTheme(R.style.ActionSheetStyleIOS7);
			break;
		}
		showActionSheet();
	}

	public void showActionSheet() {
		ActionSheet.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle("Cancel")
				.setOtherButtonTitles("Item1", "Item2", "Item3", "Item4")
				.setCancelableOnTouchOutside(true).setListener(this).show();
	}

	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		Toast.makeText(getApplicationContext(), "click item index = " + index,
				0).show();
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
		Toast.makeText(getApplicationContext(), "dismissed isCancle = " + isCancle, 0).show();
	}

}
