package com.baoyz.android_actionsheet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;

public class MainActivity extends AppCompatActivity implements
        ActionSheet.ActionSheetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ios6:
                setTheme(R.style.ActionSheetStyleiOS6);
                break;
            case R.id.ios7:
                setTheme(R.style.ActionSheetStyleiOS7);
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
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
        Toast.makeText(getApplicationContext(), "dismissed isCancle = " + isCancle, Toast.LENGTH_SHORT).show();
    }
}