package com.martinforget.cardiaccoherencelite;

public interface CallbackListener {
	public void onStart();
	public void onStop();
	public void onPause();
	public void onResume();
	public void onAbort();
	public void onDemoEnd();
}
