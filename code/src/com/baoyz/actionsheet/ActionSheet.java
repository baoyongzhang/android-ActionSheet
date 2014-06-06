package com.baoyz.actionsheet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * 上拉菜单
 * 
 * @author Baoyz
 * 
 * @date 2014-6-6 下午12:36:53
 */
public class ActionSheet extends Fragment implements OnClickListener {

	private static final String ARG_CANCEL_BUTTON_TITLE = "cancel_button_title";
	private static final String ARG_OTHER_BUTTON_TITLES = "other_button_titles";
	private static final int CANCEL_BUTTON_ID = 100;

	private boolean mDismissed = true;
	private ActionSheetListener mListener;
	private View mView;
	private LinearLayout mPanel;
	private ViewGroup mGroup;
	private View mBg;
	private Attributes mAttrs;

	public void show(FragmentManager manager, String tag) {
		if (!mDismissed) {
			return;
		}
		mDismissed = false;
		FragmentTransaction ft = manager.beginTransaction();
		ft.add(this, tag);
		ft.commit();
	}

	public void dismiss() {
		if (mDismissed) {
			return;
		}
		mDismissed = true;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.remove(this);
		ft.commit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mAttrs = readAttribute();

		mView = View.inflate(getActivity(), R.layout.as_default, null);
		mGroup = (ViewGroup) getActivity().getWindow().getDecorView();
		mPanel = (LinearLayout) mView.findViewById(R.id.panel);
		mBg = mView.findViewById(R.id.view_bg);

		// 创建元素
		createItems();

		mGroup.addView(mView);
		mBg.startAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.as_alpha_in));
		mPanel.startAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.as_bottom_in));
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void createItems() {
		String[] titles = getOtherButtonTitles();
		if (titles != null) {
			for (int i = 0; i < titles.length; i++) {
				Button bt = new Button(getActivity());
				bt.setId(CANCEL_BUTTON_ID + i + 1);
				bt.setOnClickListener(this);
				bt.setBackgroundDrawable(getOtherButtonBg(titles, i));
				bt.setText(titles[i]);
				bt.setTextColor(mAttrs.otherButtonTextColor);
				bt.setTextSize(mAttrs.actionSheetTextSize);
				if (i > 0) {
					LinearLayout.LayoutParams params = createButtonLayoutParaams();
					params.topMargin = mAttrs.otherButtonSpacing;
					mPanel.addView(bt, params);
				} else {
					mPanel.addView(bt);
				}
			}
		}
		Button bt = new Button(getActivity());
		bt.getPaint().setFakeBoldText(true);
		bt.setTextSize(mAttrs.actionSheetTextSize);
		bt.setId(CANCEL_BUTTON_ID);
		bt.setBackgroundDrawable(mAttrs.cancelButtonBackground);
		bt.setText(getCancelButtonTitle());
		bt.setTextColor(mAttrs.cancelButtonTextColor);
		bt.setOnClickListener(this);
		LinearLayout.LayoutParams params = createButtonLayoutParaams();
		params.topMargin = mAttrs.cancelButtonMarginTop;
		mPanel.addView(bt, params);

		mPanel.setBackgroundDrawable(mAttrs.background);
		mPanel.setPadding(mAttrs.padding, mAttrs.padding, mAttrs.padding,
				mAttrs.padding);
	}

	public LinearLayout.LayoutParams createButtonLayoutParaams() {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		return params;
	}

	private Drawable getOtherButtonBg(String[] titles, int i) {
		if (titles.length == 1) {
			return mAttrs.otherButtonSingleBackground;
		}
		if (titles.length == 2) {
			switch (i) {
			case 0:
				return mAttrs.otherButtonTopBackground;
			case 1:
				return mAttrs.otherButtonBottomBackground;
			}
		}
		if (titles.length > 2) {
			if (i == 0) {
				return mAttrs.otherButtonTopBackground;
			}
			if (i == (titles.length - 1)) {
				return mAttrs.otherButtonBottomBackground;
			}
			return mAttrs.otherButtonMiddleBackground;
		}
		return null;
	}

	@Override
	public void onDestroyView() {
		Animation an = AnimationUtils.loadAnimation(getActivity(),
				R.anim.as_bottom_out);
		an.setFillAfter(true);
		mPanel.startAnimation(an);
		Animation an2 = AnimationUtils.loadAnimation(getActivity(),
				R.anim.as_alpha_out);
		an2.setFillAfter(true);
		mBg.startAnimation(an2);
		mView.postDelayed(new Runnable() {
			@Override
			public void run() {
				mGroup.removeView(mView);
			}
		}, 500);
		super.onDestroyView();
	}

	private Attributes readAttribute() {
		Attributes attrs = new Attributes(getActivity());
		// 读取Style
		TypedArray a = getActivity().getTheme().obtainStyledAttributes(null,
				R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
		Drawable background = a
				.getDrawable(R.styleable.ActionSheet_actionSheetBackground);
		if (background != null) {
			attrs.background = background;
		}
		Drawable cancelButtonBackground = a
				.getDrawable(R.styleable.ActionSheet_cancelButtonBackground);
		if (cancelButtonBackground != null) {
			attrs.cancelButtonBackground = cancelButtonBackground;
		}
		Drawable otherButtonTopBackground = a
				.getDrawable(R.styleable.ActionSheet_otherButtonTopBackground);
		if (otherButtonTopBackground != null) {
			attrs.otherButtonTopBackground = otherButtonTopBackground;
		}
		Drawable otherButtonMiddleBackground = a
				.getDrawable(R.styleable.ActionSheet_otherButtonMiddleBackground);
		if (otherButtonMiddleBackground != null) {
			attrs.otherButtonMiddleBackground = otherButtonMiddleBackground;
		}
		Drawable otherButtonBottomBackground = a
				.getDrawable(R.styleable.ActionSheet_otherButtonBottomBackground);
		if (otherButtonBottomBackground != null) {
			attrs.otherButtonBottomBackground = otherButtonBottomBackground;
		}
		Drawable otherButtonSingleBackground = a
				.getDrawable(R.styleable.ActionSheet_otherButtonSingleBackground);
		if (otherButtonSingleBackground != null) {
			attrs.otherButtonSingleBackground = otherButtonSingleBackground;
		}
		attrs.cancelButtonTextColor = a.getColor(
				R.styleable.ActionSheet_cancelButtonTextColor,
				attrs.cancelButtonTextColor);
		attrs.otherButtonTextColor = a.getColor(
				R.styleable.ActionSheet_otherButtonTextColor,
				attrs.otherButtonTextColor);
		attrs.padding = (int) a.getDimension(
				R.styleable.ActionSheet_actionSheetPadding, attrs.padding);
		attrs.otherButtonSpacing = (int) a.getDimension(
				R.styleable.ActionSheet_otherButtonSpacing,
				attrs.otherButtonSpacing);
		attrs.cancelButtonMarginTop = (int) a.getDimension(
				R.styleable.ActionSheet_cancelButtonMarginTop,
				attrs.cancelButtonMarginTop);
		attrs.actionSheetTextSize = a.getDimension(
				R.styleable.ActionSheet_actionSheetTextSize,
				attrs.actionSheetTextSize);

		a.recycle();
		return attrs;
	}

	private String getCancelButtonTitle() {
		return getArguments().getString(ARG_CANCEL_BUTTON_TITLE);
	}

	private String[] getOtherButtonTitles() {
		return getArguments().getStringArray(ARG_OTHER_BUTTON_TITLES);
	}

	public void setActionSheetListener(ActionSheetListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (mListener != null) {
			if (v.getId() == CANCEL_BUTTON_ID) {
				mListener.onCancelButtonClick(this);
			} else {
				mListener.onOtherButtonClick(this, v.getId() - CANCEL_BUTTON_ID
						- 1);
			}
		}
	}

	private static class Attributes {
		public Attributes(Context context) {
			// 默认值
			this.background = new ColorDrawable(Color.TRANSPARENT);
			this.cancelButtonBackground = context.getResources().getDrawable(
					R.drawable.slt_as_ios7_cancel_bt);
			this.otherButtonTopBackground = context.getResources().getDrawable(
					R.drawable.slt_as_ios7_other_bt_top);
			this.otherButtonMiddleBackground = context.getResources()
					.getDrawable(R.drawable.slt_as_ios7_other_bt_middle);
			this.otherButtonBottomBackground = context.getResources()
					.getDrawable(R.drawable.slt_as_ios7_other_bt_bottom);
			this.otherButtonSingleBackground = context.getResources()
					.getDrawable(R.drawable.slt_as_ios7_other_bt_single);
			this.cancelButtonTextColor = Color.BLUE;
			this.otherButtonTextColor = Color.BLUE;
			this.padding = 20;
			this.otherButtonSpacing = 0;
			this.cancelButtonMarginTop = 10;
			this.actionSheetTextSize = 16;
		}

		Drawable background;
		Drawable cancelButtonBackground;
		Drawable otherButtonTopBackground;
		Drawable otherButtonMiddleBackground;
		Drawable otherButtonBottomBackground;
		Drawable otherButtonSingleBackground;
		int cancelButtonTextColor;
		int otherButtonTextColor;
		int padding;
		int otherButtonSpacing;
		int cancelButtonMarginTop;
		float actionSheetTextSize;
	}

	public static class Builder {

		private Context mContext;
		private FragmentManager mFragmentManager;
		private String mCancelButtonTitle;
		private String[] mOtherButtonTitles;
		private String mTag = "actionSheet";

		public Builder(Context context, FragmentManager fragmentManager) {
			mContext = context;
			mFragmentManager = fragmentManager;
		}

		/**
		 * 设置取消按钮标题
		 * 
		 * @param title
		 */
		public Builder setCancelButtonTitle(String title) {
			mCancelButtonTitle = title;
			return this;
		}

		/**
		 * 设置取消按钮标题
		 * 
		 * @param strId
		 */
		public Builder setCancelButtonTitle(int strId) {
			return setCancelButtonTitle(mContext.getString(strId));
		}

		/**
		 * 设置其他按钮标题
		 * 
		 * @param titles
		 * @return
		 */
		public Builder setOtherButtonTitles(String... titles) {
			mOtherButtonTitles = titles;
			return this;
		}

		public Builder setTag(String tag) {
			mTag = tag;
			return this;
		}

		public Bundle prepareArguments() {
			Bundle bundle = new Bundle();
			bundle.putString(ARG_CANCEL_BUTTON_TITLE, mCancelButtonTitle);
			bundle.putStringArray(ARG_OTHER_BUTTON_TITLES, mOtherButtonTitles);
			return bundle;
		}

		public ActionSheet show() {
			ActionSheet actionSheet = (ActionSheet) Fragment.instantiate(
					mContext, ActionSheet.class.getName(), prepareArguments());
			actionSheet.show(mFragmentManager, mTag);
			return actionSheet;
		}

	}

	public static interface ActionSheetListener {
		void onCancelButtonClick(ActionSheet actionSheet);

		void onOtherButtonClick(ActionSheet actionSheet, int index);
	}

}
