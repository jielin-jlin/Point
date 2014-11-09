package com.point.meet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.point.meet.R;

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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Eventfragment extends Fragment {

	public static String eventid;
	String reply;
	InputStream inputStream;
	String result;
	JSONObject jObject;
	ProgressDialog pDialog;
	AlertDialog.Builder alert;
	ArrayList<ArrayList<String>> arraylist;
	ListView listv;
	eventlistadapter adapter;
	ArrayList<String> idarray;
	ArrayList<String> titlearray;
	ArrayList<String> activityarray;
	public Eventfragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view =  inflater.inflate(R.layout.fragment_eventsfragment, container,
				false);
		arraylist = new ArrayList<ArrayList<String>>();
		idarray = new ArrayList<String>();
		titlearray = new ArrayList<String>();
		activityarray = new ArrayList<String>();
		listv=(ListView) view.findViewById(R.id.listView1);
		listv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				eventid = ((TextView)view.findViewById(R.id.textView1)).getText().toString().trim();
				Intent singleevent = new Intent(getActivity(),Eventinfo.class);
				startActivity(singleevent);
				
			}
		});
		
		new populateevent().execute();
		return view;
		
	}
	class populateevent extends AsyncTask<String,String,String>
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
			try
			{
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://www.point.web44.net/getevent.php");
				
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
				alert = new AlertDialog.Builder(getActivity());
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
		protected void onPostExecute(String file_url)
		{
			findtable();
			pDialog.dismiss();
				
		}
		
	}
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
				titlearray.add(arraylist.get(k).get(1));
				activityarray.add(arraylist.get(k).get(2));
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
	public void pushtolist()
	{
		adapter = new eventlistadapter(getActivity(),idarray,titlearray,activityarray);
		listv.setAdapter(adapter);
	}

}
class eventlistadapter extends ArrayAdapter<String>
{
	Context context;
	ArrayList<String> id;
	ArrayList<String> title;
	ArrayList<String> activity;
	eventlistadapter(FragmentActivity fragmentActivity,ArrayList<String> ids, ArrayList<String> titles, ArrayList<String> activities)
	{
		super(fragmentActivity,R.layout.eventsinglerow,R.id.textView1,titles);
		this.context=fragmentActivity;
		this.id=ids;
		this.title=titles;
		this.activity=activities;
	}
	class MyeventViewHolder
	{
		ImageView myimage;
		TextView id;
		TextView title;
		TextView activity;
		MyeventViewHolder(View v)
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
		MyeventViewHolder holder = null;
		if(row==null)
		{
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row=inflater.inflate(R.layout.eventsinglerow,parent,false);
				holder = new MyeventViewHolder(row);
				row.setTag(holder);
		}
		else
		{
			holder = (MyeventViewHolder)row.getTag();
		}
		holder.id.setText(id.get(position));
		holder.title.setText(title.get(position));
		holder.activity.setText(activity.get(position));
		return row;
		
	}
	
}
