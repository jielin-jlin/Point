package com.point.meet;

import android.content.Context;
import android.content.SharedPreferences;

public class Usersession {
	SharedPreferences sharedpref;
	SharedPreferences.Editor editor;
	public Usersession(Context ctx)
	{
		sharedpref=ctx.getSharedPreferences("login",Context.MODE_PRIVATE);
		editor=sharedpref.edit();
	}
	public void userlogin(String first_name,String last_name,int zipcode, String email)
	{
		editor.putString("first_name", first_name);
		editor.putString("last_name", last_name);
		editor.putInt("zipcode", zipcode);
		editor.putString("email", email);
		editor.commit();
		editor.clear();
	}
	public String getinfo(String str)
	{
		String string;
		string = sharedpref.getString(str, "");
		return string;
		
	}
	public boolean checklogin()
	{
		if(sharedpref.contains("first_name"))
			return true;
		else
			return false;
		
	}
	public void remove(String str)
	{
		editor.remove(str);
		editor.commit();
		editor.clear();
	}
	
}
