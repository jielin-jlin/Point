package com.example.point;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Infofragment extends Fragment {

	static TextView firstname,lastname;
	Usersession session;
	public Infofragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		RelativeLayout revlay = (RelativeLayout)inflater.inflate(R.layout.fragment_infofragment, container,
				false);
		session = new Usersession(getActivity().getApplicationContext());
		firstname=(TextView)revlay.findViewById(R.id.textView1);
		lastname=(TextView)revlay.findViewById(R.id.textView2);
		firstname.setText(session.getinfo("first_name"));
		lastname.setText(session.getinfo("last_name"));

		
		return revlay;
	}

}
