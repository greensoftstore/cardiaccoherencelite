package com.martinforget;

import android.app.Activity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;



public class MyOnLongClickListener extends Activity implements OnLongClickListener {
	Button btn;
	public MyOnLongClickListener(Button btn){
		this.btn = btn;
	}
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}

}
