package com.martinforget;





import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyOnClickListener extends Activity implements OnClickListener {
	Button btn;
	public MyOnClickListener(Button btn){
		this.btn = btn;
	}
    @Override
    public void onClick(View v)
    {
        //read your lovely variable
    }

}
