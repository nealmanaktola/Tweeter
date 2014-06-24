package com.codepath.apps.basictwitter;

import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;




public class ComposeDialog extends DialogFragment  {
	private EditText etCompose;
	private Button btnTweet;
	
	public interface ComposeDialogListener {
		void onFinishComposeDialog(String tweetBody);
	}
	public ComposeDialog()
	{
		//Empty Constructor req'd
	}

	public static ComposeDialog newInstance(String title) {
		ComposeDialog frag = new ComposeDialog();
		Bundle args = new Bundle();
		args.putString("title", title);
		frag.setArguments(args);
		return frag;
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_compose,container);
		etCompose = (EditText) view.findViewById(R.id.tvComposeBody);
		String title = getArguments().getString("title", "Enter body of Tweet");
		getDialog().setTitle(title);
		
		etCompose.requestFocus();
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		btnTweet = (Button)view.findViewById(R.id.btnTweet);
		btnTweet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        ComposeDialogListener listener = (ComposeDialogListener) getActivity();
		        listener.onFinishComposeDialog(etCompose.getText().toString());
		        dismiss();		
			}
		});
		
		return view;
		
	}
	
}
	
