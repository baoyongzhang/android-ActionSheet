package com.baoyz.actionsheet;

import com.baoyz.actionsheet.ActionSheet.ActionSheetListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

/**
 * 
 * 
 * @author Baoyz
 * 
 * @date 2014-6-6 下午5:42:37
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
		ActionSheet actionSheet = new ActionSheet.Builder(this,
				getSupportFragmentManager()).setCancelButtonTitle("取消")
				.setOtherButtonTitles("选项1", "选项2", "选项3", "选项4").show();
		actionSheet.setActionSheetListener(this);
	}

	@Override
	public void onCancelButtonClick(ActionSheet actionSheet) {
		Toast.makeText(getApplicationContext(), "cancel", 0).show();
	}

	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		Toast.makeText(getApplicationContext(), "click item " + index, 0)
				.show();
	}

}
