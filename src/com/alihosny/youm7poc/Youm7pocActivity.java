package com.alihosny.youm7poc;

import android.app.Activity;

import android.content.Context;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Html;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alihosny.youm7poc.singlenews;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStreamReader;

import java.net.*;
import java.nio.charset.Charset;

import java.util.ArrayList;

public class Youm7pocActivity extends Activity
	{
		/** Called when the activity is first created. */
		downld							grapper	= new downld();
		public ArrayList<singlenews>	news;
		ArrayList<TextView>				newsgroup;
		public LinearLayout				newsholder;
		public TableLayout				newstable;
		ArrayList<TableRow>				newsblock;
		ArrayList<ImageView>			newspic;

		LayoutInflater					inflater;
		ArrayList<Integer>				cached;

		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.main);
				news = new ArrayList<singlenews>();
				newstable = (TableLayout) findViewById(R.id.newstable);
				newsholder = (LinearLayout) findViewById(R.id.newsholder);
				newsblock = new ArrayList<TableRow>();
				newsgroup = new ArrayList<TextView>();
				newspic = new ArrayList<ImageView>();
				inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				cached = new ArrayList<Integer>();
				if (news.size() == 0)
					grapper.execute();

			}

		@Override
		protected void onPause()

			{
				super.onPause();
				Log.i("pause", "pause");

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
				Asynccaheclear cleaner = new Asynccaheclear();
				cleaner.execute(getExternalCacheDir());

			}

		private class downld extends AsyncTask<Void, Void, String> implements
				android.view.View.OnClickListener
			{
				URL					url;
				String				s	= "";
				HttpURLConnection	conn;

				InputStreamReader	reader;
				BufferedReader		buffread;

				@Override
				protected String doInBackground(Void... params)
					{
						try
							{
								url = new URL("http://www2.youm7.com/NewsSection.asp?SecID=65");
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

								reader = new InputStreamReader(url.openStream(), Charset.forName("windows-1256"));

								buffread = new BufferedReader(reader);

								File cache = new File(getExternalCacheDir(), "youm7cache");
								FileOutputStream fos = new FileOutputStream(cache);
								BufferedOutputStream fos1 = new BufferedOutputStream(fos);
								int i = 0;

								String line;

								while ((line = buffread.readLine()) != null)
									{

										fos1.write(line.getBytes());
										fos1.write("\r\n".getBytes());

										if (line.contains(getString(R.string.NewsBriefblock)))
											{
												news.add(new singlenews());
												continue;
											}

										else if (line.contains(getString(R.string.NewsBriefImg)))
											{

												news.get(i).newslink = line.substring(41, (line.indexOf("IssueID=0") + 9));

												news.get(i).setimglink(line.substring(line.indexOf("src", 109) + 5, line.indexOf(".jpg") + 4));

											}

										else if (line.contains(getString(R.string.NewslistContentData)) && (line = buffread.readLine()) != null)
											{
												fos1.write(line.getBytes());
												fos1.write("\r\n".getBytes());

												news.get(i).newshead = Html.fromHtml(line).toString();
												news.get(i).newshead = news.get(i).newshead.trim();

											}
										else if (line.contains("<span class=\"red\">") || line.contains("<span>"))
											{
												String temp = line;
												line = buffread.readLine();
												temp += line;
												temp = temp.replace("<span class=\"red\">", "");
												temp = temp.replace("</span>", "");
												temp = temp.trim();
												news.get(i).newsdate = temp;

												i++;

												continue;
											}
										else if (line.contains(getString(R.string.SectiontopImg)))
											{
												news.get(i).newslink = line.substring(41, (line.indexOf("IssueID=0") + 9));

												news.get(i).setimglink(line.substring(line.indexOf("src", 109) + 5, line.indexOf(".jpg") + 4));
											}
										else if (line.contains(getString(R.string.SectionTopData)) && (line = buffread.readLine()) != null)
											{
												news.get(i).newshead = line.substring(line.indexOf("IssueID=0") + 11, line.indexOf("</a></h3>"));
												// Log.i("head",news.get(i).newshead);

											}

										else if (line.contains(getString(R.string.SectionTop)))
											{
												news.add(new singlenews());
												continue;
											}

									}

								fos1.close();
								fos.close();
							}
						catch (Exception f)
							{

								Log.d("startconimpl", f.toString());

							}
						finally
							{
								conn.disconnect();
							}

						return s;
					}

				protected void onPostExecute(String result)
					{

						try
							{
								for (int j = 0; j < news.size(); j++)
									{
										news.get(j).setid();
										newsblock.add((TableRow) inflater.inflate(R.layout.rowtemplt, newstable, false));

										newstable.addView(newsblock.get(j));
										newspic.add((ImageView) inflater.inflate(R.layout.imagetemplate, newsblock.get(j), false));

										newsgroup.add((TextView) inflater.inflate(R.layout.textviewtmplt, newsblock.get(j), false));

										newsblock.get(j).addView(newsgroup.get(j));

										newsblock.get(j).addView(newspic.get(j));
										newsgroup.get(j).setText(news.get(j).newshead);
										newsblock.get(j).setId(j);
										newsblock.get(j).setOnClickListener(this);

										news.get(j).setimage(newspic.get(j));

									}

							}
						catch (Exception z)
							{
								Log.i("errorview", z.toString());
							}

						// }

					}

				public void onClick(View v)
					{
						// TODOAuto-generated method stub
						Intent b = new Intent("com.alihosny.youm7poc.newsview");

						b.putExtras(creatmsg(v.getId()));
						startActivity(b);

					}

			}

		public void refresh(View v)
			{
				try
					{
						refresh ref = new refresh();

						ref.execute();
					}
				catch (Exception e)
					{
						Log.i("refresh", e.toString());
					}

			}

		protected Bundle creatmsg(int id)
			{
				Bundle msg = new Bundle();
				if (cached.contains(news.get(id).newsid))
					msg.putBoolean("cached", true);
				else
					{
						msg.putBoolean("cached", false);
						cached.add(news.get(id).newsid);
					}
				msg.putString("newshead", news.get(id).newshead);
				msg.putString("newslink", news.get(id).newslink);
				msg.putString("newsimagelinklarge", news.get(id).newsimagelinklarge);
				msg.putString("newsid", Integer.toString(news.get(id).newsid));
				msg.putString("newsdate", news.get(id).newsdate);
				return msg;

			}

		private class refresh extends AsyncTask<Void, Void, String> implements
				android.view.View.OnClickListener
			{
				URL					url;
				String				s	= "";
				HttpURLConnection	conn;

				InputStreamReader	reader;
				BufferedReader		buffread;
				int					rnc	= 0;

				@Override
				protected String doInBackground(Void... params)
					{
						try
							{
								url = new URL("http://www2.youm7.com/NewsSection.asp?SecID=65");
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

								reader = new InputStreamReader(url.openStream(), Charset.forName("windows-1256"));

								buffread = new BufferedReader(reader);

								File cache = new File(getExternalCacheDir(), "youm7cacherefresh");
								FileOutputStream fos = new FileOutputStream(cache);
								BufferedOutputStream fos1 = new BufferedOutputStream(fos);
								int i = rnc;
								String temp1 = "";
								String line;

								while ((line = buffread.readLine()) != null)
									{

										fos1.write(line.getBytes());
										fos1.write("\r\n".getBytes());

										if (line.contains(getString(R.string.NewsBriefImg)))
											{
												temp1 = line.substring(41, (line.indexOf("IssueID=0") + 9));
												if (temp1 != news.get(i).newslink)
													{
														news.add(i, new singlenews());
														news.get(i).newslink = line.substring(41, (line.indexOf("IssueID=0") + 9));

														news.get(i).setimglink(line.substring(line.indexOf("src", 109) + 5, line.indexOf(".jpg") + 4));
													}
												else
													break;

											}

										else if (line.contains(getString(R.string.NewslistContentData)) && (line = buffread.readLine()) != null && temp1 != news.get(i).newslink)
											{
												fos1.write(line.getBytes());
												fos1.write("\r\n".getBytes());

												news.get(i).newshead = Html.fromHtml(line).toString();
												news.get(i).newshead = news.get(i).newshead.trim();

											}
										else if ((line.contains("<span class=\"red\">") || line.contains("<span>")) && temp1 != news.get(i).newslink)
											{
												String temp = line;
												line = buffread.readLine();
												temp += line;
												temp = temp.replace("<span class=\"red\">", "");
												temp = temp.replace("</span>", "");
												temp = temp.replace("<span>", "");
												temp = temp.trim();
												news.get(i).newsdate = temp;

												i++;

												continue;
											}
										else if (line.contains(getString(R.string.SectiontopImg)))
											{
												temp1 = line.substring(41, (line.indexOf("IssueID=0") + 9));
												if (!temp1.equals(news.get(i).newslink))
													{
														news.add(i, new singlenews());

														news.get(i).newslink = line.substring(41, (line.indexOf("IssueID=0") + 9));

														news.get(i).setimglink(line.substring(line.indexOf("src", 109) + 5, line.indexOf(".jpg") + 4));

													}
												else
													break;

											}
										else if (line.contains(getString(R.string.SectionTopData)) && (line = buffread.readLine()) != null && temp1 != news.get(i).newslink)
											{
												news.get(i).newshead = line.substring(line.indexOf("IssueID=0") + 11, line.indexOf("</a></h3>"));
												// Log.i("head",news.get(i).newshead);

											}

									}
								rnc = i;
								fos1.close();
								fos.close();
							}
						catch (Exception f)
							{

								Log.d("startconimpl", f.toString());

							}
						finally
							{
								conn.disconnect();
							}

						return s;
					}

				protected void onPostExecute(String result)
					{
						int j = 0;

						try
							{
								for (j = 0; j < rnc; j++)
									{
										news.get(j).setid();
										newsblock.add(j, (TableRow) inflater.inflate(R.layout.rowtemplt, newstable, false));
										newstable.addView(newsblock.get(j), j);
										newspic.add(j, (ImageView) inflater.inflate(R.layout.imagetemplate, newsblock.get(j), false));

										newsgroup.add(j, (TextView) inflater.inflate(R.layout.textviewtmplt, newsblock.get(j), false));

										newsblock.get(j).addView(newsgroup.get(j));

										newsblock.get(j).addView(newspic.get(j));
										newsgroup.get(j).setText(news.get(j).newshead);
										newsblock.get(j).setId(news.get(j).newsid);
										newsblock.get(j).setOnClickListener(this);

										news.get(j).setimage(newspic.get(j));

									}
								for (int i = 0; i < newsblock.size(); i++)
									{
										newsblock.get(i).setId(i);
									}

							}
						catch (Exception z)
							{
								Log.i("errorview", z.toString());
							}

						// }

					}

				public void onClick(View v)
					{
						// TODOAuto-generated method stub
						Intent b = new Intent("com.alihosny.youm7poc.newsview");

						b.putExtras(creatmsg(v.getId()));
						startActivity(b);

					}

			}
	}
