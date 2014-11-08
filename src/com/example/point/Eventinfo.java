	package com.example.point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import org.json.JSONArray;
import org.json.JSONException;

import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Eventinfo extends ActionBarActivity {

	TextView title,activity,date, timestart,timeend,location,zipcode;
	ImageButton chat;
	ProgressDialog pDialog;
	AlertDialog.Builder alert;
	InputStream inputStream;
	String result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventinfo);
		title =(TextView)findViewById(R.id.textView1);
		activity=(TextView)findViewById(R.id.textView2);
		date=(TextView)findViewById(R.id.textView7);
		timestart=(TextView)findViewById(R.id.textView3);
		timeend=(TextView)findViewById(R.id.textView4);
		location=(TextView)findViewById(R.id.textView5);
		zipcode=(TextView)findViewById(R.id.textView6);
		chat=(ImageButton)findViewById(R.id.imageButton1);
		chat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent newchat = new Intent(Eventinfo.this,Chatpage.class);
				startActivity(newchat);
				
			}
		});
		new geteventinfo().execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.eventinfo, menu);
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
	class geteventinfo extends AsyncTask<String,String,String>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Eventinfo.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			try
			{
				List<NameValuePair> list=new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("id",Eventfragment.eventid.toString().trim()));
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://www.point.web44.net/eventinfo.php");
				try {
					post.setEntity(new UrlEncodedFormEntity(list));
					
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
				}
				try {
					HttpResponse response = client.execute(post);
					HttpEntity entity = response.getEntity();
	
				    inputStream = entity.getContent();
				    // json is UTF-8 by default
				    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				    StringBuilder sb = new StringBuilder();
	
				    String line = null;
				    while ((line = reader.readLine()) != null)
				    {
				        sb.append(line + "\n");
				    }
				    result = sb.toString();
				    
					
				} catch (ClientProtocolException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			catch(Exception e)
			{
				alert = new AlertDialog.Builder(Eventinfo.this);
				alert.setTitle("SORRY");
				alert.setMessage("Either you have no internet connect at the moment or your ISP provider has blocked us, please contact your ISP provider");
				alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//kill activity
					}
				}).show();
			}
			return null;
		}
		@TargetApi(Build.VERSION_CODES.KITKAT)
		@Override
		protected void onPostExecute(String file_url) {
			try {
				JSONArray jarray = new JSONArray(result);
				convertresult(jarray);
				
			} catch (JSONException e) {
				alert = new AlertDialog.Builder(Eventinfo.this);
				alert.setTitle("ERROR");
				alert.setMessage(e.toString());
				alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//kill activity
					}
				}).show();
				e.printStackTrace();
			}
			pDialog.dismiss();
		}
	}
	public void convertresult(JSONArray jarray)
	{
		if(jarray.length()!=0)
		{
			int i;
			ArrayList<String> array = new ArrayList<String>();
			for(i=0;i<jarray.length();i++)
			{
				try {
					array.add(jarray.get(i).toString().trim());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			title.setText("Title: "+array.get(1));
			activity.setText("Activity: "+array.get(2));
			date.setText("Date: "+array.get(3));
			timestart.setText("Begin: "+array.get(4));
			timeend.setText("End: "+array.get(5));
			location.setText("Location: "+array.get(6));
			zipcode.setText("Zipcode: "+array.get(7));
			
		}
		else
		{
			alert = new AlertDialog.Builder(Eventinfo.this);
			alert.setTitle("ERROR");
			alert.setMessage("Event Loading Error");
			alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//kill activity
				}
			}).show();
		}
	}
}
