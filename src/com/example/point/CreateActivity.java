package com.example.point;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class CreateActivity extends ActionBarActivity {

	ImageButton cancel,create_activity;
	EditText title,activity,starttime,endtime,location,zipcode, date;
	String _title,_activity,_date,_starttime,_endtime,_location,_zipcode,reply, first_name,last_name,email;
	ProgressDialog pDialog;
	AlertDialog.Builder alert;
	HttpClient httpclient;
	HttpGet request;
	HttpResponse response;
	Usersession session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create);
		
		cancel=(ImageButton) findViewById(R.id.imageButton2);
		create_activity=(ImageButton)findViewById(R.id.imageButton1);
		title=(EditText)findViewById(R.id.editText1);
		activity=(EditText)findViewById(R.id.editText2);
		date=(EditText)findViewById(R.id.editText7);
		starttime=(EditText)findViewById(R.id.editText3);
		endtime=(EditText)findViewById(R.id.editText4);
		location=(EditText)findViewById(R.id.editText6);
		zipcode=(EditText)findViewById(R.id.editText5);
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v==cancel)
				{
					finish();
				}
				
			}
		});
		create_activity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v==create_activity)
				{
					//get input to string
					_title=title.getText().toString().trim();
					_activity=activity.getText().toString().trim();
					_date = date.getText().toString().trim();
					_starttime=starttime.getText().toString().trim();
					_endtime=endtime.getText().toString().trim();
					_location=location.getText().toString().trim();
					_zipcode=zipcode.getText().toString().trim();
					if((_title.length()==0)||(_activity.length()==0)||
							(_date.length()==0)||(_starttime.length()==0)||(_endtime.length()==0)
							||(_location.length()==0)||(_zipcode.length()==0))
					{
						alert = new AlertDialog.Builder(CreateActivity.this);
						alert.setTitle("ERROR");
						alert.setMessage("Please fill out all fields");
						alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							
							public void onClick(DialogInterface dialog, int which) {
								//do nothing
							}
						}).show();
					}
					else
					{
						session = new Usersession(getApplicationContext());
						first_name=session.getinfo("first_name");
						last_name =session.getinfo("last_name");
						email=session.getinfo("email");
						new createactivity().execute();
					}
				}
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create, menu);
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
	public void successful()
	{
		alert = new AlertDialog.Builder(CreateActivity.this);
		alert.setTitle("YEAH");
		alert.setMessage("Activity creation successful");
		alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//kill activity
				finish();
			}
		}).show();
	}
	public void failed()
	{
		alert = new AlertDialog.Builder(CreateActivity.this);
		alert.setTitle("ERROR");
		alert.setMessage("Activity creation Error");
		alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//kill activity
			}
		}).show();
	}
	class createactivity extends AsyncTask<String,String,String>
	{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(CreateActivity.this);
			pDialog.setMessage("Creating...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			
			List<NameValuePair> list=new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("title",_title));
			list.add(new BasicNameValuePair("activity",_activity));
			list.add(new BasicNameValuePair("date",_date));
			list.add(new BasicNameValuePair("starttime",_starttime));
			list.add(new BasicNameValuePair("endtime",_endtime));
			list.add(new BasicNameValuePair("location",_location));
			list.add(new BasicNameValuePair("zipcode",_zipcode));
			list.add(new BasicNameValuePair("first_name",first_name));
			list.add(new BasicNameValuePair("last_name",last_name));
			list.add(new BasicNameValuePair("email",email));
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://www.point.web44.net/create_activity.php");
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
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			if(reply.equals("successful"))
			{
				successful();
			}
			else
				failed();
			pDialog.dismiss();
		}
		
			
	}
}
