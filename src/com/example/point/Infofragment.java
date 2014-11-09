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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class Infofragment extends Fragment {

	static TextView firstname,lastname;
	QuickContactBadge icon;
	Usersession session;
	ProgressDialog pDialog;
	AlertDialog.Builder alert;
	HttpClient httpclient;
	HttpGet request;
	HttpResponse response;
	InputStream inputStream;
	String reply;
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
		icon=(QuickContactBadge)revlay.findViewById(R.id.quickContactBadge1);
		firstname.setText(session.getinfo("first_name"));
		lastname.setText(session.getinfo("last_name"));
		new getimage().execute();

		
		return revlay;
	}
	class getimage extends AsyncTask<String,String,String>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Signing Up...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			try
			{
				List<NameValuePair> list=new ArrayList<NameValuePair>();
				list.add(new BasicNameValuePair("email",session.getinfo("email").toString().trim()));
				HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost("http://www.point.web44.net/getimage.php");
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
				    reply = sb.toString();
				    
					
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
		@Override
		protected void onPostExecute(String result) {
			transcriptimage();
			pDialog.dismiss();
		}
	}
	public void transcriptimage()
	{
		try {
			JSONArray jarray = new JSONArray(reply);
			turnimage(jarray);
		} catch (JSONException e) {

			alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("ERROR");
			alert.setMessage(e.toString());
			alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//reset email input
				}
			}).show();
			e.printStackTrace();
		}
	}
	public void turnimage(JSONArray jarray)
	{
		byte[] bytearray = null;
		String image = null;
		try {
			image = jarray.get(0).toString().trim();
			if(!image.isEmpty())
			{
				bytearray=image.getBytes();
				Bitmap bitmap = BitmapFactory.decodeByteArray(bytearray, 0, bytearray.length);
				icon.setImageBitmap(bitmap);
			}
		} catch (JSONException e) {
			alert = new AlertDialog.Builder(getActivity());
			alert.setTitle("ERROR");
			alert.setMessage(e.toString());
			alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//reset email input
				}
			}).show();
			e.printStackTrace();
		}
		alert = new AlertDialog.Builder(getActivity());
		alert.setTitle("ERROR");
		alert.setMessage(image);
		alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//reset email input
			}
		}).show();
		
	}

}
