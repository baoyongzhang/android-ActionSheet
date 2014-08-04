# ActionSheetForAndroid
模仿IOS里面的UIActionSheet控件，有IOS6和IOS7两种风格，可以自定义风格，背景图片、按钮图片、文字颜色、间距等。

<p>
   <img src="https://raw.githubusercontent.com/baoyongzhang/ActionSheetForAndroid/master/screenshot-1.png" width="320" alt="Screenshot"/>
   &nbsp;&nbsp;
   <img src="https://raw.githubusercontent.com/baoyongzhang/ActionSheetForAndroid/master/screenshot-2.png" width="320" alt="Screenshot"/>
</p>

# 使用方法

### 创建一个ActionSheet并显示

```java
ActionSheet.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle("Cancel")
				.setOtherButtonTitles("Item1", "Item2", "Item3", "Item4")
				.setCancelableOnTouchOutside(true)
				.setListener(this).show();
```

### 方法说明

* `setCancelButtonTitle()` 设置取消按钮的标题
* `setOtherButtonTitles()` 设置条目，String[]
* `setCancelableOnTouchOutside()` 设置点击空白处关闭
* `setListener()` 设置事件监听器
* `show()` 返回`ActionSheet`对象，可以调用`ActionSheet`对象的`dismiss()`方法手动关闭

### 事件监听

实现`ActionSheetListener`接口
* `onOtherButtonClick()` 点击某个条目，`index`是条目的下标
* `onDismiss()` 关闭事件，`isCancel` 参数表示是否是点击取消按钮、返回键、或者点击空白处(`setCancelableOnTouchOutside(true)`)

```java
   	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		Toast.makeText(getApplicationContext(), "click item index = " + index,
				0).show();
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
		Toast.makeText(getApplicationContext(), "dismissed isCancle = " + isCancle, 0).show();
	}
```

### 样式

默认的样式非常丑陋，项目中提供了两种Style，可以配置Theme

```xml
<!-- Application theme. -->
    <style name="AppTheme" parent="AppBaseTheme">
        <item name="actionSheetStyle">@style/ActionSheetStyleIOS6</item>
        or
        <item name="actionSheetStyle">@style/ActionSheetStyleIOS7</item>
    </style>
```

还可以自定义样式，自定义一个style即可，可以参考ActionSheetStyleIOS6/ActionSheetStyleIOS7的写法

```xml
 <!-- IOS7样式 -->
 <style name="ActionSheetStyleIOS7">
        <item name="actionSheetBackground">@android:color/transparent</item>
        <item name="cancelButtonBackground">@drawable/slt_as_ios7_cancel_bt</item>
        <item name="otherButtonTopBackground">@drawable/slt_as_ios7_other_bt_top</item>
        <item name="otherButtonMiddleBackground">@drawable/slt_as_ios7_other_bt_middle</item>
        <item name="otherButtonBottomBackground">@drawable/slt_as_ios7_other_bt_bottom</item>
        <item name="otherButtonSingleBackground">@drawable/slt_as_ios7_other_bt_single</item>
        <item name="cancelButtonTextColor">#1E82FF</item>
        <item name="otherButtonTextColor">#1E82FF</item>
        <item name="actionSheetPadding">10dp</item>
        <item name="otherButtonSpacing">0dp</item>
        <item name="cancelButtonMarginTop">10dp</item>
        <item name="actionSheetTextSize">12sp</item>
    </style>
```

### Style属性介绍
* `actionSheetBackground` 背景
* `cancelButtonBackground` 取消按钮背景
* `otherButtonTopBackground` 选项顶部按钮背景
* `otherButtonMiddleBackground` 选项中部按钮背景
* `otherButtonBottomBackground` 选项底部按钮背景
* `otherButtonSingleBackground` 选项只有一个的按钮背景
* `cancelButtonTextColor` 取消按钮的文字颜色
* `otherButtonTextColor` 选项按钮的文字颜色
* `actionSheetPadding` 内边距
* `otherButtonSpacing` 选项按钮的间距
* `cancelButtonMarginTop` 取消按钮顶部间距
* `actionSheetTextSize` 选项按钮文字颜色


