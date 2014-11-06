package com.example.point;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SignUpActivity extends ActionBarActivity {

	ImageButton signup,cancel,uploadimage;
	ProgressDialog pDialog;
	AlertDialog.Builder alert;
	EditText firstname,lastname,email_add,password,confirm_pass,zip_code,pet_name,school_name;
	String first_name, last_name,email,passphrase,confirmpass,zipcode,petname,schoolname,reply;
	HttpClient httpclient;
	HttpGet request;
	HttpResponse response;
	String register; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);
		signup=(ImageButton)findViewById(R.id.imageButton1);
		uploadimage=(ImageButton)findViewById(R.id.imageButton2);
		cancel=(ImageButton)findViewById(R.id.imageButton3);
		firstname=(EditText)findViewById(R.id.editText1);
		lastname=(EditText)findViewById(R.id.editText2);
		email_add=(EditText)findViewById(R.id.editText3);
		password=(EditText)findViewById(R.id.editText4);
		confirm_pass=(EditText)findViewById(R.id.editText5);
		zip_code=(EditText)findViewById(R.id.editText6);
		pet_name=(EditText)findViewById(R.id.editText7);
		school_name=(EditText)findViewById(R.id.editText8);
		uploadimage.setOnClickListener(new View.OnClickListener(){
			
			@Override
			public void onClick(View v) {
				if (v == uploadimage){
				}
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			    photoPickerIntent.setType("image/*");
			    startActivityForResult(photoPickerIntent, 1);
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v==cancel)
				{
					finish();
				}
				
			}
		});
		signup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(v==signup)
				{
					//get text from input
					first_name = firstname.getText().toString().trim();
					last_name = lastname.getText().toString().trim();
					email = email_add.getText().toString().trim();
					passphrase=password.getText().toString().trim();
					confirmpass=confirm_pass.getText().toString().trim();
					zipcode=zip_code.getText().toString().trim();
					petname=pet_name.getText().toString().trim();
					schoolname=school_name.getText().toString().trim();
					
					if((first_name.length()==0)||(last_name.length()==0)
							||(email.length()==0)||(passphrase.length()==0)
							||(confirmpass.length()==0)||(zipcode.length()==0)
							||(petname.length()==0)||(schoolname.length()==0))
					{
						alert = new AlertDialog.Builder(SignUpActivity.this);
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
						//check if email is an email
						if(!isEmailValid(email))
						{
							alert = new AlertDialog.Builder(SignUpActivity.this);
							alert.setTitle("ERROR");
							alert.setMessage("Email is not valid, please re-enter");
							alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									email_add.setText("");
								}
							}).show();
						}
						//check if passphrase match
						else if(!passphrase.equals(confirmpass))
						{
							alert = new AlertDialog.Builder(SignUpActivity.this);
							alert.setTitle("ERROR");
							alert.setMessage("Passphrase does not match");
							alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									password.setText("");
									confirm_pass.setText("");
								}
							}).show();
							
						}
						else if(passphrase.length()<6)
						{
							alert = new AlertDialog.Builder(SignUpActivity.this);
							alert.setTitle("ERROR");
							alert.setMessage("Passphrase must be at lease 6 characters long");
							alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									password.setText("");
									confirm_pass.setText("");
								}
							}).show();
						}
						else if(zipcode.length()<5)
						{
							alert = new AlertDialog.Builder(SignUpActivity.this);
							alert.setTitle("ERROR");
							alert.setMessage("Zipcode is not valid");
							alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									zip_code.setText("");
								}
							}).show();
						}
						else
						{
							// hash here
							// Testing GiHub App for Mac
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
							//connect and register
							new register().execute();
						}
					}
					
					
				}
				
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
	    switch (requestCode) {
	    case 1:
	     {
	      if (resultCode == RESULT_OK){
	      
	        try {
	        	  final Uri imageUri = data.getData();
	        	  final InputStream imageStream = getContentResolver().openInputStream(imageUri);
				  final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
					
	      		  ImageView imageview = (ImageView)findViewById(R.id.imageView1);
	      		  imageview.setImageBitmap(selectedImage);
	     }catch(Exception e){
	     		}
	      	}
	      
	      }
	    }  
	  }
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
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
	public void emailinuse()
	{
		alert = new AlertDialog.Builder(SignUpActivity.this);
		alert.setTitle("ERROR");
		alert.setMessage("email is in use, please use another email");
		alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//reset email input
				email_add.setText("");
			}
		}).show();
	}
	public void successful()
	{
		alert = new AlertDialog.Builder(SignUpActivity.this);
		alert.setTitle("YEAH");
		alert.setMessage("YOU HAVE SUCCESSFULLY SIGNED UP");
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
		alert = new AlertDialog.Builder(SignUpActivity.this);
		alert.setTitle("ERROR");
		alert.setMessage("AN ERROR HAS OCCUR, PLEASE TRY AGAIN");
		alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//do nothing
			}
		}).show();
	}
	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	class register extends AsyncTask<String,String,String>
	{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SignUpActivity.this);
			pDialog.setMessage("Signing Up...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		@Override
		protected String doInBackground(String... params) {
			
			List<NameValuePair> list=new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("first_name",first_name));
			list.add(new BasicNameValuePair("last_name",last_name));
			list.add(new BasicNameValuePair("email",email));
			list.add(new BasicNameValuePair("passphrase",passphrase));
			list.add(new BasicNameValuePair("zipcode",zipcode));
			list.add(new BasicNameValuePair("petname",petname));
			list.add(new BasicNameValuePair("schoolname",schoolname));
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://www.point.web44.net/register.php");
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
			if(reply.equals("email_in_use"))
			{
				emailinuse();
			}
			else if(reply.equals("successful"))
			{
				successful();
			}
			else if(reply.equals("failed"))
			{
				failed();
			}
			pDialog.dismiss();
		}
		
			
	}
}

