package com.alihosny.youm7poc;

import java.io.File;

import android.os.AsyncTask;
import android.util.Log;

public class Asynccaheclear extends AsyncTask<File, Void,Void>
	{
		@Override
        protected Void doInBackground(File...cachedir) 
    	{
    		File cache=cachedir[0];
    		
    	for(int i=0;i<cache.listFiles().length;i++)
			try
				{
					cache.listFiles()[i].delete();
				}
			catch (Exception e)
				{
					// TODO Auto-generated catch block
					Log.i("cachedeleteerror", e.toString());
				}
			return null;
    		
    		
    	}
    
    	 
    	protected void onPostExecute()
    
    	{
    		
    	     
    
    	
	
    		
    		
    	}

		
		
	}
