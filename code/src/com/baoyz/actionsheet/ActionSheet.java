package com.baoyz.actionsheet;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * UIActionSheet
 * 
 * @author Baoyz
 * 
 * @date 2014-6-6 ÏÂÎç12:36:53
 */
public class ActionSheet extends Fragment implements OnClickListener {

	private static final String ARG_CANCEL_BUTTON_TITLE = "cancel_button_title";
	private static final String ARG_OTHER_BUTTON_TITLES = "other_button_titles";
	private static final String ARG_CANCELABLE_ONTOUCHOUTSIDE = "cancelable_ontouchoutside";
	private static final int CANCEL_BUTTON_ID = 100;
	private static final int BG_VIEW_ID = 10;
	private static final int TRANSLATE_DURATION = 200;
	private static final int ALPHA_DURATION = 300;

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
		if (mListener != null) {
			mListener.onDismiss(this);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mAttrs = readAttribute();

		mView = createView();
		mGroup = (ViewGroup) getActivity().getWindow().getDecorView();

		createItems();

		mGroup.addView(mView);
		mBg.startAnimation(createAlphaInAnimation());
		mPanel.startAnimation(createTranslationInAnimation());
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private Animation createTranslationInAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				1, type, 0);
		an.setDuration(TRANSLATE_DURATION);
		return an;
	}

	private Animation createAlphaInAnimation() {
		AlphaAnimation an = new AlphaAnimation(0, 1);
		an.setDuration(ALPHA_DURATION);
		return an;
	}

	private Animation createTranslationOutAnimation() {
		int type = TranslateAnimation.RELATIVE_TO_SELF;
		TranslateAnimation an = new TranslateAnimation(type, 0, type, 0, type,
				0, type, 1);
		an.setDuration(TRANSLATE_DURATION);
		an.setFillAfter(true);
		return an;
	}

	private Animation createAlphaOutAnimation() {
		AlphaAnimation an = new AlphaAnimation(1, 0);
		an.setDuration(ALPHA_DURATION);
		an.setFillAfter(true);
		return an;
	}

	private View createView() {
		FrameLayout parent = new FrameLayout(getActivity());
		parent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mBg = new View(getActivity());
		mBg.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mBg.setBackgroundColor(Color.argb(136, 0, 0, 0));
		mBg.setId(BG_VIEW_ID);
		mBg.setOnClickListener(this);

		mPanel = new LinearLayout(getActivity());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.gravity = Gravity.BOTTOM;
		mPanel.setLayoutParams(params);
		mPanel.setOrientation(LinearLayout.VERTICAL);

		parent.addView(mBg);
		parent.addView(mPanel);
		return parent;
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
					LinearLayout.LayoutParams params = createButtonLayoutParams();
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
		LinearLayout.LayoutParams params = createButtonLayoutParams();
		params.topMargin = mAttrs.cancelButtonMarginTop;
		mPanel.addView(bt, params);

		mPanel.setBackgroundDrawable(mAttrs.background);
		mPanel.setPadding(mAttrs.padding, mAttrs.padding, mAttrs.padding,
				mAttrs.padding);
	}

	public LinearLayout.LayoutParams createButtonLayoutParams() {
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
			return mAttrs.getOtherButtonMiddleBackground();
		}
		return null;
	}

	@Override
	public void onDestroyView() {
		mPanel.startAnimation(createTranslationOutAnimation());
		mBg.startAnimation(createAlphaOutAnimation());
		mView.postDelayed(new Runnable() {
			@Override
			public void run() {
				mGroup.removeView(mView);
			}
		}, ALPHA_DURATION);
		super.onDestroyView();
	}

	private Attributes readAttribute() {
		Attributes attrs = new Attributes(getActivity());
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

	private boolean getCancelableOnTouchOutside() {
		return getArguments().getBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE);
	}

	public void setActionSheetListener(ActionSheetListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == BG_VIEW_ID && !getCancelableOnTouchOutside()) {
			return;
		}
		dismiss();
		if (mListener != null) {
			if (v.getId() == CANCEL_BUTTON_ID || v.getId() == BG_VIEW_ID) {
				mListener.onCancel(this);
			} else {
				mListener.onOtherButtonClick(this, v.getId() - CANCEL_BUTTON_ID
						- 1);
			}
			;
		}
	}

	public static Builder createBuilder(Context context,
			FragmentManager fragmentManager) {
		return new Builder(context, fragmentManager);
	}

	private static class Attributes {
		private Context mContext;

		public Attributes(Context context) {
			mContext = context;
			this.background = new ColorDrawable(Color.TRANSPARENT);
			this.cancelButtonBackground = new ColorDrawable(Color.BLACK);
			ColorDrawable gray = new ColorDrawable(Color.GRAY);
			this.otherButtonTopBackground = gray;
			this.otherButtonMiddleBackground = gray;
			this.otherButtonBottomBackground = gray;
			this.otherButtonSingleBackground = gray;
			this.cancelButtonTextColor = Color.WHITE;
			this.otherButtonTextColor = Color.BLACK;
			this.padding = 20;
			this.otherButtonSpacing = 2;
			this.cancelButtonMarginTop = 10;
			this.actionSheetTextSize = 16;
		}

		public Drawable getOtherButtonMiddleBackground() {
			if (otherButtonMiddleBackground instanceof StateListDrawable) {
				TypedArray a = mContext.getTheme().obtainStyledAttributes(null,
						R.styleable.ActionSheet, R.attr.actionSheetStyle, 0);
				otherButtonMiddleBackground = a
						.getDrawable(R.styleable.ActionSheet_otherButtonMiddleBackground);
				a.recycle();
			}
			return otherButtonMiddleBackground;
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
		private boolean mCancelableOnTouchOutside;
		private ActionSheetListener mListener;

		public Builder(Context context, FragmentManager fragmentManager) {
			mContext = context;
			mFragmentManager = fragmentManager;
		}

		public Builder setCancelButtonTitle(String title) {
			mCancelButtonTitle = title;
			return this;
		}

		public Builder setCancelButtonTitle(int strId) {
			return setCancelButtonTitle(mContext.getString(strId));
		}

		public Builder setOtherButtonTitles(String... titles) {
			mOtherButtonTitles = titles;
			return this;
		}

		public Builder setTag(String tag) {
			mTag = tag;
			return this;
		}

		public Builder setListener(ActionSheetListener listener) {
			this.mListener = listener;
			return this;
		}

		public Builder setCancelableOnTouchOutside(boolean cancelable) {
			mCancelableOnTouchOutside = cancelable;
			return this;
		}

		public Bundle prepareArguments() {
			Bundle bundle = new Bundle();
			bundle.putString(ARG_CANCEL_BUTTON_TITLE, mCancelButtonTitle);
			bundle.putStringArray(ARG_OTHER_BUTTON_TITLES, mOtherButtonTitles);
			bundle.putBoolean(ARG_CANCELABLE_ONTOUCHOUTSIDE,
					mCancelableOnTouchOutside);
			return bundle;
		}

		public ActionSheet show() {
			ActionSheet actionSheet = (ActionSheet) Fragment.instantiate(
					mContext, ActionSheet.class.getName(), prepareArguments());
			actionSheet.setActionSheetListener(mListener);
			actionSheet.show(mFragmentManager, mTag);
			return actionSheet;
		}

	}

	public static interface ActionSheetListener {

		void onDismiss(ActionSheet actionSheet);

		void onCancel(ActionSheet actionSheet);

		void onOtherButtonClick(ActionSheet actionSheet, int index);
	}

}
