package com.alihosny.youm7poc;

import java.net.*;
import java.util.Date;

import java.io.InputStream;
import java.lang.String;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;

import android.util.Log;

import android.widget.ImageView;

@SuppressWarnings("unused")
public class singlenews {
	int newsid;
	String newslink;
	String newsimagelinklarge;
	String newsimagelinksmall;
	String newsimagelinkmedium;
   String newsdate;
    String newshead;
    ImageView imgview;
    String newscontent;
    protected Bitmap newsimage;
   boolean cached=false;
   singlenews()
	{
		newslink=" ";
		
		
	}
    protected class imgdownload extends AsyncTask<Void, Void,  Bitmap>
    {
    	
    	
    	//Bitmap newsimage;
    	@Override
        protected Bitmap doInBackground(Void...params) 
    	{
    		try{
    			
    		InputStream in = new URL(newsimagelinkmedium).openConnection().getInputStream();
    	   newsimage=BitmapFactory.decodeStream(in);
    	  
    		}
    		catch(Exception e)
    		{
    			Log.d("imagedownload error",e.toString());
    			
    		}
    //	Log.i("bitmapres",Integer.toString(newsimage.getHeight()));
    		return newsimage;
    	}
    
    	 
    	protected void onPostExecute(Bitmap result)
    
    	{
    		//FileOutputStream fos;
    	//	File cache=new File(Environment.getDownloadCacheDirectory(),(Integer.toString(newsid)));
    	//	Log.i("path",cache.getPath());
    		
		//	try {
		//		fos = new FileOutputStream(cache);
			//	newsimage.compress(Bitmap.CompressFormat.PNG, 100, fos);
		//	} catch (FileNotFoundException e) {
		//		// TODO Auto-generated catch block
		//		e.printStackTrace();
			//}
    		imgview.setImageBitmap(result);
    	//	Log.i("height",Integer.toString(newsimage.getHeight()));
    	//Log.i("width",Integer.toString(newsimage.getWidth()));
    	     
    
    	
	
    		
    		
    	}
    }
    
	public void setimglink(String imgurl)
	{
		newsimagelinksmall=imgurl;
		newsimagelinklarge=imgurl.replace("Small", "large");
		newsimagelinkmedium=imgurl.replace("Small", "medium");
		
	}
	
	
	public void setimage(ImageView v)
	{
		imgview=v;
		new imgdownload().execute();
       
		
		
	}
	public void setid()
	{
		newsid =Integer.parseInt(newslink.substring((newslink.lastIndexOf("NewsID=")+7),(newslink.lastIndexOf("&SecID"))));
	}
	

}
