package com.point.meet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.point.meet.R;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class Login extends ActionBarActivity {

	ImageButton sign_up_button,sign_in_button;
	EditText _username,_password;
	String username, passphrase,reply;
	ProgressDialog pDialog;
	AlertDialog.Builder alert;
	Usersession session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		sign_up_button = (ImageButton) findViewById(R.id.imageButton1);
		sign_in_button=(ImageButton) findViewById(R.id.imageButton2);
		_username=(EditText)findViewById(R.id.editText2);
		_password=(EditText)findViewById(R.id.editText1);
		sign_up_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v==sign_up_button)
				{
					Intent signup = new Intent(Login.this,SignUpActivity.class);
					startActivity(signup);
				}
				
			}
		});
		sign_in_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v==sign_in_button)
				{
					username = _username.getText().toString().trim();
					passphrase = _password.getText().toString().trim();
					if(username.length()==0)
					{
						alert = new AlertDialog.Builder(Login.this);
						alert.setTitle("ERROR");
						alert.setMessage("Please enter email");
						alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								//do nothing
							}
						}).show();
					}
					else if(passphrase.length()==0)
					{
						alert = new AlertDialog.Builder(Login.this);
						alert.setTitle("ERROR");
						alert.setMessage("Please enter passphrase");
						alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								//do nothing
							}
						}).show();
					}
					else
					{
						// hash here
						 MessageDigest md = null;
						try {
							md = MessageDigest.getInstance("SHA");
							md.update(passphrase.getBytes("UTF-8"));
						} catch (Exception e) {
						}
						byte raw[] = md.digest();

						StringBuffer result = new StringBuffer();
						for (byte b : raw) {
							result.append(String.format("%02X", b));
						}
						passphrase = result.toString();
						new login().execute();
					}
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onBackPressed()
	{
		
		return;
	}
	public void successful()
	{
		String[] tokens = reply.split(" ");
		session=new Usersession(getApplicationContext());
		session.userlogin(tokens[0],tokens[1],Integer.parseInt(tokens[2]),username);
		finish();
	}
	public void wrongpass()
	{
		alert = new AlertDialog.Builder(Login.this);
		alert.setTitle("ERROR");
		alert.setMessage("you have entered a wrong passphrase");
		alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				//do nothing
			}
		}).show();
	}
	public void wrongusername()
	{
		alert = new AlertDialog.Builder(Login.this);
		alert.setTitle("ERROR");
		alert.setMessage("you have entered a wrong email address");
		alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				//do nothing
			}
		}).show();
	}
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    Integer.parseInt(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	class login extends AsyncTask<String,String,String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Login In...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> list=new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("email",username));
			list.add(new BasicNameValuePair("passphrase",passphrase));
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://www.point.web44.net/login.php");
			try {
				post.setEntity(new UrlEncodedFormEntity(list));
				
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
			}
			try {
				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				reply = EntityUtils.toString(entity).trim();
				
			} catch (ClientProtocolException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(String file_url)
		{
			if(reply.equals("wrong_username"))
			{
				wrongusername();
			}
			else if(reply.equals("wrong_passphrase"))
			{
				wrongpass();
			}
			else
			{
				successful();
			}
			pDialog.dismiss();
		}
		
	}
}
