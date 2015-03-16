package com.kinglin.addressbook;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class IconSelectDlg extends Dialog {

	private Context context;
	private GridView gvIconSelect = null;
	private int pstion = 0;

	public IconSelectDlg(Context context) {
		super(context);
		this.context = context;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// 设置对话框使用的布局文件
		this.setContentView(R.layout.icon_select);
	
		gvIconSelect = (GridView) findViewById(R.id.gv_iconSelect);
		getAdapter();
		
		// 为GridView设置监听器
		gvIconSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 显示信息
				//Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
				pstion = position;
				
				//点击item后Dialog消失
				IconSelectDlg.this.dismiss();
				
			}
			
		});
		
	}
	
	//适配器将item装入gridview中
	private void getAdapter()
	{
		int[] images = new int[]
				{
				R.drawable.s_icon1,R.drawable.s_icon2,
				R.drawable.s_icon3,R.drawable.s_icon4,
				R.drawable.s_icon5,R.drawable.s_icon6,
				R.drawable.s_icon7,R.drawable.s_icon8,
				R.drawable.s_icon9,
				};
		//String[] texts = new String[] { "","","","","","","","","",};
		
		//生成动态数组，并转载数据
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 9; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", images[i]);
            //map.put("itemText", texts[i]);
            lstImageItem.add(map);
        }
        
        //生成适配器
        SimpleAdapter saImageItems = new SimpleAdapter(context, 
        		lstImageItem, 
        		R.layout.item_gridview, 
        		new String[] { "itemImage", " " }, 
        		new int[] { R.id.iv_icon, R.id.tv_imageText });
        //添加并显示
        gvIconSelect.setAdapter(saImageItems);
	}
		
	//返回点击的位置
	public int getPosition() {
		return pstion;
	}

}
