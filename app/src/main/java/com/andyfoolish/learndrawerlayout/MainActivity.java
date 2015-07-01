package com.andyfoolish.learndrawerlayout;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.net.URI;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    private String[] drawTitles;
    private DrawerLayout myDrawerLayout;
    private ListView myDrawerList;
    private ArrayAdapter<String> adapter;
    private ActionBarDrawerToggle myDrawerToggle;

    private String myTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTitle = (String) getTitle();

        drawTitles = getResources().getStringArray(R.array.planets_array);
        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        myDrawerList = (ListView) findViewById(R.id.left_drawer);

        // 为list view设置adapter
        adapter = new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawTitles);
        myDrawerList.setAdapter(adapter);
        // 为list设置click listener
        myDrawerList.setOnItemClickListener(this);

        myDrawerToggle = new ActionBarDrawerToggle(
                this,   /* host Activity */
                myDrawerLayout, /* DrawerLayout object */
                R.drawable.ic_drawer,/* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,/* "open drawer" description for accessibility */
                R.string.drawer_close/* "close drawer" description for accessibility */
            ){
                //覆写draweropened和drawerclosed方法
            /** 当drawer处于完全关闭的状态时调用 */
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(myTitle);
                invalidateOptionsMenu();// 创建对onPrepareOptionsMenu()的调用,系统自动调用

            }
            /** 当drawer处于完全打开的状态时调用 */
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //更改标题
                getActionBar().setTitle("请选择");
                invalidateOptionsMenu();// 创建对onPrepareOptionsMenu()的调用,系统自动调用

            }
        };
        // 设置drawer触发器为DrawerListener
        myDrawerLayout.setDrawerListener(myDrawerToggle);
        //开启actionBar上APP ICON的功能
        getActionBar().setDisplayHomeAsUpEnabled(true);//启动左上角的返回图标
        getActionBar().setHomeButtonEnabled(true);//使左上角的HomeButton启用


    }

    /* 当invalidateOptionsMenu()调用时调用 */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isDrawerOpen= myDrawerLayout.isDrawerOpen(myDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!isDrawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // 在onRestoreInstanceState发生后，同步触发器状态.
        //同步ActionDrawerToggle和DrawerLayout的状态
        //同时将ActionDrawerToggle的drawer图标，设置为ActionBar中的HomeButton的icon
        myDrawerToggle.syncState();
    }

    //屏幕旋转时
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        myDrawerToggle.onConfigurationChanged(newConfig);
    }

    //当actionBar元素被选择时
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //将actionBar上的图标与Drawer结合起来
        // 将事件传递给ActionBarDrawerToggle, 如果返回true，表示app 图标点击事件已经被处理
        if (myDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }


        int id = item.getItemId();
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        switch (id) {
            case R.id.action_websearch:
                Intent intent = new Intent();

                intent.setAction("android.intent.action.VIEW");
                Uri uri = Uri.parse("http://www.baidu.com");
                intent.setData(uri);

                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 创建一个新的fragment
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString("text", drawTitles[position]);
        fragment.setArguments(args);

        // 通过替换已存在的fragment来插入新的fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, fragment)
                       .commit();

        // 高亮被选择的item, 更新标题
        myDrawerList.setItemChecked(position, true);
        setTitle(drawTitles[position]);
        //关闭侧滑菜单drawer
        myDrawerLayout.closeDrawer(myDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        myTitle = (String) title;
        getActionBar().setTitle(myTitle);
    }
}
