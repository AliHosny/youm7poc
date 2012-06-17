package com.alihosny.youm7poc;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class newsview extends Activity
	{
		ImageView			newsimagesingle;
		TextView			newsheadline;
		TextView			Newscontent;
		Bitmap				newsimage;
		singlenews			thisnews;
		Intent				getnews;
		Bundle				goodnews;
		URL					url;
		// ArrayList<Integer> cached;
		InputStreamReader	reader;
		BufferedReader		buffread;
		HttpURLConnection	conn;
		singlenewsgrapper	grap;
		String				templinereader;
		TextView			newsdatetime;
		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.newsviewlay);
				newsimagesingle = (ImageView) findViewById(R.id.newsimagesingle);
				newsheadline = (TextView) findViewById(R.id.newshead);
				newsdatetime = (TextView) findViewById(R.id.newsdate);

				FileReader contentreader;
				thisnews = new singlenews();
				thisnews.newscontent = "";
				Newscontent = (TextView) findViewById(R.id.newscontent);
				getnews();
				newsheadline.setText(thisnews.newshead);
				newsdatetime.setText(thisnews.newsdate);
				if (getIntent().getBooleanExtra("cached", true))
					{
						File newscontent = new File(getExternalCacheDir(),
								Integer.toString(thisnews.newsid) + "content");
						File newsimg = new File(getExternalCacheDir(),
								Integer.toString(thisnews.newsid) + "img");
						try
							{
								@SuppressWarnings("unused")
								Integer n;
								StringBuilder tempcontent = new StringBuilder();
								char[] buffer = new char[512];
								contentreader = new FileReader(newscontent);

								while ((n = contentreader.read(buffer)) != -1)
									{
										tempcontent.append(buffer);
										buffer = new char[512];
									}
								thisnews.newscontent = tempcontent.toString();
								thisnews.newsimage = BitmapFactory
										.decodeFile(newsimg.getPath());
								newsimagesingle
										.setImageBitmap(thisnews.newsimage);

								Newscontent.setText(thisnews.newscontent);
							}
						catch (FileNotFoundException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
								grap = new singlenewsgrapper();

								grap.execute();
							}
						catch (IOException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
								grap = new singlenewsgrapper();

								grap.execute();
							}

					}
				else
					{
						grap = new singlenewsgrapper();

						grap.execute();
					}
			}

		@Override
		protected void onPause()
			{
				super.onPause();

			}

		@Override
		protected void onStop()

			{
				super.onStop();
				Log.i("stop", "stop");

			}

		@Override
		protected void onResume()

			{
				super.onResume();
				Log.i("onResume", "onResume");

			}

		@Override
		protected void onStart()

			{
				super.onStart();
				Log.i("start", "start");

			}

		@Override
		protected void onRestart()

			{
				super.onRestart();
				Log.i("restart", "restart");

			}

		@Override
		protected void onDestroy()

			{
				super.onDestroy();
				Log.i("destroy", "destroy");

			}

		protected void getnews()
			{
				try
					{
						thisnews.newshead = getIntent().getExtras().getString(
								"newshead");
						thisnews.newslink = getIntent().getExtras().getString(
								"newslink");
						thisnews.newsimagelinklarge = getIntent().getExtras()
								.getString("newsimagelinklarge");
						thisnews.newsid = Integer.parseInt(getIntent()
								.getExtras().getString("newsid"));
						thisnews.newsdate = getIntent().getExtras().getString(
								"newsdate");
					}
				catch (Exception e)
					{
						Log.d("errorbundle", e.toString());
					}
			}

		protected class singlenewsgrapper extends AsyncTask<Void, Void, Bitmap>
			{
				FileWriter			savecontent;
				FileOutputStream	saveimage;
				File				imgfile;
				File				contentfile;
				
				@Override
				protected Bitmap doInBackground(Void... params)
					{
						try
							{
								
								imgfile = new File(getExternalCacheDir(),
										Integer.toString(thisnews.newsid)
												+ "img");
								contentfile = new File(getExternalCacheDir(),
										Integer.toString(thisnews.newsid)
												+ "content");
							}
						catch (Exception e)
							{
								Log.i("cache file error", e.toString());
							}
						try
							{
								url = new URL(thisnews.newslink);
								conn = (HttpURLConnection) url.openConnection();
								// Log.i("encod",conn.getContentEncoding());
							}
						catch (MalformedURLException e)
							{
								Log.d("urlerror", e.toString());
							}
						catch (IOException f)
							{
								Log.d("connerror", f.toString());
							}
						try
							{

								reader = new InputStreamReader(
										url.openStream(),
										Charset.forName("windows-1256"));
								savecontent = new FileWriter(contentfile);
								buffread = new BufferedReader(reader);
								String line;
								thisnews.newscontent = "";
								while ((line = buffread.readLine()) != null)
									{

										String temp = "";
										if (line.contains("<p>"))
											{
												temp += line;
												// Log.i("singlenews",line);
												while (!(line = buffread
														.readLine())
														.contains("</div>"))
													{
														if (line.contains("موضوعات متعلقة"))
															{
																break;
															}
														temp += line;
														// Log.i("singlenews",line);

													}
												thisnews.newscontent = thisnews.newscontent
														.concat(Html.fromHtml(
																temp)
																.toString());
												try
													{
														savecontent
																.write(thisnews.newscontent);
													}
												catch (Exception e)
													{
														Log.i("writeerror",
																e.toString());
													}
												// Log.i("newscontent",thisnews.newscontent);
												reader.close();
												buffread.close();
												savecontent.close();
												break;
											}

									}
							}
						catch (Exception e)
							{
								Log.d("imagedownload error", e.toString());

							}
						try
							{

								InputStream in = new URL(
										thisnews.newsimagelinklarge)
										.openConnection().getInputStream();
								newsimage = BitmapFactory.decodeStream(in);

								saveimage = new FileOutputStream(imgfile);
								newsimage.compress(Bitmap.CompressFormat.PNG,
										100, saveimage);

								in.close();
								saveimage.close();
							}
						catch (Exception e)
							{
								Log.d("imagedownload error", e.toString());

							}

						return newsimage;
					}

				protected void onPostExecute(Bitmap result)
					{
						newsimagesingle.setImageBitmap(result);

						Newscontent.setText(thisnews.newscontent);

					}

			}
	}
