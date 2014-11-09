package com.point.meet;

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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.point.meet.R;
import com.point.meet.activitylistadapter.MyViewHolder;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Activityinfo extends ActionBarActivity {

	TextView title,activity,date, timestart,timeend,location,zipcode;
	ImageButton participate;
	ProgressDialog pDialog;
	AlertDialog.Builder alert;
	InputStream inputStream;
	String result,email,reply;
	ListView listv;
	ArrayList<ArrayList<String>> arraylist;
	ArrayList<String> participantarray;
	participantlistadapter adapter;
	Usersession session;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activityinfo);
		title =(TextView)findViewById(R.id.textView1);
		activity=(TextView)findViewById(R.id.textView2);
		date=(TextView)findViewById(R.id.textView8);
		timestart=(TextView)findViewById(R.id.textView3);
		timeend=(TextView)findViewById(R.id.textView6);
		location=(TextView)findViewById(R.id.textView7);
		zipcode=(TextView)findViewById(R.id.textView4);
		session = new Usersession(Activityinfo.this);
		participate=(ImageButton)findViewById(R.id.imageButton1);
		
		listv = (ListView)findViewById(R.id.listView1);
		
		arraylist = new ArrayList<ArrayList<String>>();
		participantarray = new ArrayList<String>();
		
		participate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v==participate)
				{
					email =session.getinfo("email");
					new participant().execute();
				}
				
			}
		});
		
		new getinfo().execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activityinfo, menu);
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
	class getinfo extends AsyncTask<String,String,String>
	{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Activityinfo.this);
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
				list.add(new BasicNameValuePair("id",Ongoingfragment.activityid.toString().trim()));
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://www.point.web44.net/activityinfo.php");
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
				alert = new AlertDialog.Builder(Activityinfo.this);
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
				alert = new AlertDialog.Builder(Activityinfo.this);
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
			int i,j,k;
			for(i=0;i<jarray.length();i++)
			{
				ArrayList<String> array=new ArrayList<String>();
				JSONArray jjarray=new JSONArray();
				try {
					jjarray = jarray.getJSONArray(i);
				} catch (JSONException e) {
					alert = new AlertDialog.Builder(Activityinfo.this);
					alert.setTitle("ERROR");
					alert.setMessage(e.toString());
					alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//kill activity
						}
					}).show();
				}
					for(j=0;j<jjarray.length();j++)
					{
						try {
							array.add(jjarray.getString(j).toString());
						} catch (JSONException e) {
							alert = new AlertDialog.Builder(Activityinfo.this);
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
					}
					arraylist.add(array);
			}
			title.setText("Title: "+arraylist.get(0).get(4).toString().trim());
			activity.setText("Activity: "+arraylist.get(0).get(5).toString().trim());
			date.setText("Date: "+arraylist.get(0).get(6).toString().trim());
			timestart.setText("Begin: "+arraylist.get(0).get(7).toString().trim());
			timeend.setText("End: "+arraylist.get(0).get(8).toString().trim());
			location.setText("Location: "+arraylist.get(0).get(9).toString().trim());
			zipcode.setText("Zipcode: "+arraylist.get(0).get(10).toString().trim());
			for(k=1;k<arraylist.size();k++)
			{
				if(!arraylist.get(k).get(0).equals("null"))
				participantarray.add(arraylist.get(k).get(0));
			}
			adapter = new participantlistadapter(Activityinfo.this,participantarray);
			listv.setAdapter(adapter);
		
		}
		else
		{
			alert = new AlertDialog.Builder(Activityinfo.this);
			alert.setTitle("ERROR");
			alert.setMessage("Activity Loading Error");
			alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//kill activity
				}
			}).show();
		}
	}
	class participant extends AsyncTask<String,String,String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(Activityinfo.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> list=new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("email",email));
			list.add(new BasicNameValuePair("id",Ongoingfragment.activityid));
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://www.point.web44.net/newparticipant.php");
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
			if(reply.equals("alreadypart"))
			{
				alreadypart();
			}
			else
			{
				successful();
			}
			pDialog.dismiss();
		}
		
	}
	public void alreadypart()
	{
		alert = new AlertDialog.Builder(Activityinfo.this);
		alert.setTitle("ERROR");
		alert.setMessage("You are already a participant of this activity");
		alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//kill activity
			}
		}).show();
	}
	public void successful()
	{
		alert = new AlertDialog.Builder(Activityinfo.this);
		alert.setTitle("YEAH");
		alert.setMessage("You are now a participant of this activity");
		alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//kill activity
			}
		}).show();
	}
}
class participantlistadapter extends ArrayAdapter<String>
{
	Context context;
	ArrayList<String> parray;
	participantlistadapter(Context c,ArrayList<String> _parray)
	{
		super(c,R.layout.activityinfosinglerow,R.id.textView1,_parray);
		this.context=c;
		this.parray=_parray;
	}
	class MyViewHolder
	{
		TextView name;
		MyViewHolder(View v)
		{
			name = (TextView)v.findViewById(R.id.textView1);
		}
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		MyViewHolder holder = null;
		if(row==null)
		{
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row=inflater.inflate(R.layout.activityinfosinglerow,parent,false);
				holder = new MyViewHolder(row);
				row.setTag(holder);
		}
		else
		{
			holder = (MyViewHolder)row.getTag();
		}
		holder.name.setText(parray.get(position));
		return row;
		
	}
	
}
