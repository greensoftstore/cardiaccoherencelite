package com.martinforget;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Button;

public class MyDialogOnClickListener implements OnClickListener {
	Button btn;
	public MyDialogOnClickListener(Button btn){
		this.btn = btn;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}

}
