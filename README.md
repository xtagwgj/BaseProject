# BaseProject

> 个人封装的Android开发类库

> 已经依赖了 `appcompat-v7` , `recyclerview-v7` , `design` , `rxbinding2`

## 怎样使用

1. 在Project的 `build.gradle` 文件中添加

  ```groovy
  allprojects {
  	repositories {
  		...
  		maven { url 'https://jitpack.io' }
  	}
  }
  ```

2. 在module的 `build.gradle` 文件中添加依赖

  ```groovy
  dependencies {
  	compile 'com.github.xtagwgj:BaseProject:v2.1'
  }
  ```

## 如何使用DataBinding

1. 在Module级别的 `build.gradle` 做如下配置

```groovy
android {

  ...

  <!-- 开启dataBinding -->
    dataBinding {
          enabled = true  
  }
}

<!-- 开启kapt，kotlin使用 -->
kapt { generateStubs = true }

dependencies
{
     ...

      <!-- 依赖dataBinding kotlin使用 -->
      kapt 'com.android.databinding:compiler:2.3.0'

      <!-- 依赖dataBinding java使用 -->
      compile 'com.android.databinding:compiler:2.3.0'
}
```

1. 在Activity中使用重写方法 `initContentView(@LayoutRes int layoutResID)`

2. 在Fragment中使用重写方法 `initContentView(@LayoutRes int layoutRes, LayoutInflater inflater, ViewGroup container)`

3. 使用对话框继承 `_BaseRxDialog` 与3相同

## 第三方库

> 需自己添加依赖

- Permission：[RxPermissions](https://github.com/tbruyelle/RxPermissions)

- Adapter：[AdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)

- Picture：[PictureSelector](https://github.com/LuckSiege/PictureSelector)

- kotlin

  ```
  https://github.com/ReactiveX/RxKotlin
  https://github.com/JakeWharton/RxBinding
  https://github.com/trello/RxLifecycle
  ```

- 响应式

	```groovy
compile "com.jakewharton.rxbinding2:rxbinding:2.0.0"
compile "io.reactivex.rxjava2:rxandroid:2.0.1"
compile "com.trello.rxlifecycle2:rxlifecycle:2.1.0"
compile "com.trello.rxlifecycle2:rxlifecycle-android:2.1.0"
	```

## 如何使用LoadingDialog

1. 在activity中添加全局变量
	```java
	private LoadingDialog loadingDialog;
	```

2. 添加常用的方法

	```java
	protected void showLoadingDialog(@StringRes int loadingTextRes, @StringRes int successTextRes) { 	
		showLoadingDialog(getString(loadingTextRes), getString(successTextRes));
	}


protected void showLoadingDialog(@NonNull String loadingText, @NonNull String successText) {
	showLoadingDialog(loadingText, successText, false);
}

protected void showLoadingDialog(String loadingText, String successText, boolean interceptBack) {
	closeLoadingDialog();
loadingDialog = new LoadingDialog(this);
loadingDialog.setLoadingText(loadingText)
        .setSuccessText(successText)
        .setInterceptBack(interceptBack)
        .closeSuccessAnim()
        .show();
}

protected void setFailLoadingText(String failLoadingText) {
	if (loadingDialog != null) loadingDialog.setFailedText(failLoadingText);
}

protected void closeLoadingDialog() {
	 if (loadingDialog != null) {
		 loadingDialog.close();
		 loadingDialog = null;
	 }
 }

protected void successLoading() {
	runOnUiThread(new Runnable() {
		@Override public void run() {
			if (loadingDialog != null) {
				loadingDialog.loadSuccess();
				}
		}
	});

}

protected void failLoading() {
	runOnUiThread(new Runnable() {
		@Override public void run() {
			if (loadingDialog != null)
			loadingDialog.loadFailed();
    }
});
}
	```

3. 为了防止内存泄漏，在 `onDestory()` 的 `super.onDestroy();` 前添加
```java
closeLoadingDialog();
```

## 侧滑菜单的使用

> 参考[SwipeRecyclerView](https://github.com/yanzhenjie/SwipeRecyclerView)
