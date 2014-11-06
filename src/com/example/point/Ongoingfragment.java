package com.example.point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Ongoingfragment extends Fragment {

	ImageButton createactivity;
	String reply;
	InputStream inputStream;
	String result,activityid;
	JSONObject jObject;
	ProgressDialog pDialog;
	AlertDialog.Builder alert;
	ArrayList<ArrayList<String>> arraylist;
	ListView listv;
	activitylistadapter adapter;
	ArrayList<String> idarray;
	ArrayList<String> titlearray;
	ArrayList<String> activityarray;
	public Ongoingfragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RelativeLayout revlay = (RelativeLayout)inflater.inflate(R.layout.fragment_ongoingfragment, container,
				false);
		createactivity=(ImageButton) revlay.findViewById(R.id.imageButton1);
		arraylist = new ArrayList<ArrayList<String>>();
		idarray = new ArrayList<String>();
		titlearray = new ArrayList<String>();
		activityarray = new ArrayList<String>();
		listv=(ListView) revlay.findViewById(R.id.listView1);
		listv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				activityid = ((TextView)view.findViewById(R.id.textView1)).getText().toString().trim();
				Intent singleactivity = new Intent(getActivity(),Activityinfo.class);
				startActivity(singleactivity);
				
			}
		});
		createactivity.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v==createactivity)
				{
					Intent create_activity=new Intent(getActivity(),CreateActivity.class);
					startActivity(create_activity);
				}
				
			}
		});
		new populatelist().execute();
		
		
		
		
		// Inflate the layout for this fragment
		return revlay;
	}
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public void findtable()
	{	
		try {
			if(result.toString().trim().equals("nothing"))
			{
				nothing();
			}
			else
			{
				JSONArray jarray = new JSONArray(result);
				getarray(jarray);
			}
			
		} catch (JSONException e) {
			alert = new AlertDialog.Builder(getActivity());
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
	public void getarray(JSONArray jarray)
	{
		
		if(jarray.length()!=0)
		{
			int i,j,k;
			for(i=jarray.length()-1;i>=0;i--)
			{
				ArrayList<String> array=new ArrayList<String>();
				JSONArray jjarray=new JSONArray();
				try {
					jjarray = jarray.getJSONArray(i);
				} catch (JSONException e) {
					alert = new AlertDialog.Builder(getActivity());
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
							alert = new AlertDialog.Builder(getActivity());
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
			for(k=0;k<arraylist.size();k++)
			{
				idarray.add(arraylist.get(k).get(0));
				titlearray.add(arraylist.get(k).get(4));
				activityarray.add(arraylist.get(k).get(5));
			}
			pushtolist();
		
		}
		else
		{
			alert = new AlertDialog.Builder(getActivity());
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
	public void nothing()
	{
		//show that there is no ongoing activity
		alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("SORRY");
		alert.setMessage("There is no on-going activity at the moment, please check again later");
		alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//kill activity
			}
		}).show();
	}
	public void pushtolist()
	{
		adapter = new activitylistadapter(getActivity(),idarray,titlearray,activityarray);
		listv.setAdapter(adapter);
	}
	class populatelist extends AsyncTask<String,String,String>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@TargetApi(Build.VERSION_CODES.KITKAT)
		@Override
		protected String doInBackground(String... params) {
			List<NameValuePair> list=new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("query","SELECT * FROM activity WHERE 1"));
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://www.point.web44.net/getactivity.php");
			post.setHeader("Content-type", "application/json");
			
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
			return null;
		}
		@TargetApi(Build.VERSION_CODES.KITKAT)
		protected void onPostExecute(String file_url)
		{
			findtable();
			pDialog.dismiss();
				
		}
		
	}
	

}

class activitylistadapter extends ArrayAdapter<String>
{
	Context context;
	ArrayList<String> id;
	ArrayList<String> title;
	ArrayList<String> activity;
	activitylistadapter(FragmentActivity fragmentActivity,ArrayList<String> ids, ArrayList<String> titles, ArrayList<String> activities)
	{
		super(fragmentActivity,R.layout.activitysinglerow,R.id.textView1,titles);
		this.context=fragmentActivity;
		this.id=ids;
		this.title=titles;
		this.activity=activities;
	}
	class MyViewHolder
	{
		ImageView myimage;
		TextView id;
		TextView title;
		TextView activity;
		MyViewHolder(View v)
		{
			myimage=(ImageView)v.findViewById(R.id.imageView1);
			id = (TextView)v.findViewById(R.id.textView1);
			title = (TextView)v.findViewById(R.id.textView2);
			activity = (TextView)v.findViewById(R.id.textView3);
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
				row=inflater.inflate(R.layout.activitysinglerow,parent,false);
				holder = new MyViewHolder(row);
				row.setTag(holder);
		}
		else
		{
			holder = (MyViewHolder)row.getTag();
		}
		holder.id.setText(id.get(position));
		holder.title.setText(title.get(position));
		holder.activity.setText(activity.get(position));
		return row;
		
	}
	
}
