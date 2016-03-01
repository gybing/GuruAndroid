/*
 * Copyright (c) 2014, CJFrameForAndroid 张涛 (kymjs123@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kymjs.aframe.plugin.activity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.kymjs.aframe.plugin.CJClassLoader;
import org.kymjs.aframe.plugin.CJConfig;
import org.kymjs.aframe.plugin.CJTool;
import org.kymjs.aframe.plugin.I_Proxy;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager.LayoutParams;

/**
 * 插件Activity的托管所，将负责管理插件Activity中的全部事务（包括生命周期与交互事件）<br>
 * 
 * <b>描述</b>对于APP来说，插件应用的所有Activity都是CJProxy，
 * 只不过每个Activity在启动时传递的CJConfig.KEY_EXTRA_CLASS不同。<br>
 * <b>创建时间</b> 2014-10-11 <br>
 * 
 * @author kymjs(kymjs123@gmail.com)
 * @version 1.0
 */
public class CJProxyActivity extends Activity implements I_Proxy {

    private int mAtyIndex; // 插件Activity在插件Manifest.xml中的序列（可选）
    private String mClass; // 插件Activity的完整类名（可选）
    private String mDexPath; // 插件所在绝对路径（必传）

    private Theme mTheme; // 托管插件的样式
    private Resources mResources; // 托管插件的资源
    private AssetManager mAssetManager; // 托管插件的assets

    protected I_CJActivity mPluginAty; // 插件Activity对象
    protected CJBackStack backStack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backStack = CJBackStack.create();
        Intent fromAppIntent = getIntent();
        mClass = fromAppIntent.getStringExtra(CJConfig.KEY_EXTRA_CLASS);
        mAtyIndex = fromAppIntent.getIntExtra(CJConfig.KEY_ATY_INDEX, 0);
        mDexPath = fromAppIntent.getStringExtra(CJConfig.KEY_DEX_PATH);
        initResources();
        if (mClass == null) {
            launchPluginActivity();
        } else {
            // 若已经指定要启动的插件Activity完整类名，则直接调用
            launchPluginActivity(mClass);
        }
    }

    /**
     * 通过反射，获取到插件的资源访问器
     */
    protected void initResources() {
        // 如果是独立运行插件程序，mDexPath会有一个默认值
        if (CJConfig.DEF_STR.equals(mDexPath)) {
            defResources();
        } else {
            try {
                AssetManager assetManager = AssetManager.class.newInstance();
                Method addAssetPath = assetManager.getClass().getMethod(
                        "addAssetPath", String.class);
                addAssetPath.invoke(assetManager, mDexPath);
                mAssetManager = assetManager;
                Resources superRes = super.getResources();
                mResources = new Resources(mAssetManager,
                        superRes.getDisplayMetrics(),
                        superRes.getConfiguration());
                mTheme = mResources.newTheme();
                mTheme.setTo(super.getTheme());
            } catch (Exception e) {
                // 必须保证资源系统的正常
                defResources();
            }
        }
    }

    /**
     * 默认的Res
     */
    private void defResources() {
        mResources = this.getResources();
        mAssetManager = this.getAssets();
        mTheme = this.getTheme();
    }

    /**
     * 启动插件的Activity
     */
    protected void launchPluginActivity() {
        PackageInfo packageInfo = CJTool.getAppInfo(this, mDexPath);
        if ((packageInfo.activities != null)
                && (packageInfo.activities.length > 0)) {
            mClass = packageInfo.activities[mAtyIndex].name;
            launchPluginActivity(mClass);
        }
    }

    /**
     * 启动指定的Activity
     * 
     * @param className
     *            要启动的Activity完整类名
     */
    protected void launchPluginActivity(final String className) {
        Class<?> atyClass;
        Constructor<?> atyConstructor = null;
        Object instance = null;
        try {
            if (CJConfig.DEF_STR.equals(mDexPath)) {
                atyClass = super.getClassLoader().loadClass(className);
            } else {
                atyClass = this.getClassLoader().loadClass(className);
            }
            atyConstructor = atyClass.getConstructor(new Class[] {});
            instance = atyConstructor.newInstance(new Object[] {});
        } catch (Exception e) {
            e.getStackTrace();
        }
        setRemoteActivity(instance);
        mPluginAty.setProxy(this, mDexPath);
        Bundle bundle = new Bundle();
        bundle.putInt(CJConfig.FROM, CJConfig.FROM_PROXY_APP);
        mPluginAty.onCreate(bundle);
    }

    /**
     * 设置当前要显示的插件Activity
     */
    protected void setRemoteActivity(Object activity) {
        if (activity instanceof I_CJActivity) {
            // 根据launchMode去启动一个插件Activity
            mPluginAty = backStack.launch((I_CJActivity) activity);
            // 如果返回栈中没有，则表示该插件aty没有创建
            if (mPluginAty == null) {
                mPluginAty = (I_CJActivity) activity;
            }
        } else {
            throw new ClassCastException(
                    "plugin activity must implements I_CJActivity");
        }
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }

    @Override
    public ClassLoader getClassLoader() {
        return CJClassLoader.getClassLoader(mDexPath, getApplicationContext(),
                super.getClassLoader());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPluginAty.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        mPluginAty.onStart();
        super.onStart();
    }

    @Override
    protected void onRestart() {
        mPluginAty.onRestart();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        mPluginAty.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mPluginAty.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mPluginAty.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        backStack.finish(mPluginAty);
        mPluginAty.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mPluginAty.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mPluginAty.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        mPluginAty.onNewIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        mPluginAty.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return mPluginAty.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        super.onKeyUp(keyCode, event);
        return mPluginAty.onKeyUp(keyCode, event);
    }

    @Override
    public void onWindowAttributesChanged(LayoutParams params) {
        mPluginAty.onWindowAttributesChanged(params);
        super.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mPluginAty.onWindowFocusChanged(hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }
}
