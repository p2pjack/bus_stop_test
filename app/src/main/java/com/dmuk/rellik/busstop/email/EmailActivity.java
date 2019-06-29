package com.dmuk.rellik.busstop.email;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.dmuk.rellik.busstop.R;
import com.dmuk.rellik.busstop.Utills.Utilities;
import com.dmuk.rellik.busstop.activities.MainActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class EmailActivity extends AppCompatActivity {


  ArrayList<String> f = new ArrayList<>();
  File[] listFile;
  private String mSubjectForEmail, mBodyForEmail;
  private String[] emails = {"e.ballinger@denso-mfg.co.uk", "b.page@denso-mfg.co.uk", "eaun"
      + ".ballinger@gmail.com", "d.gaston@denso-mfg.co.uk"};
  private FloatingActionButton fab;
  private String[] arrPath;
  private ImageAdapter imageAdapter;
  private int count;
  private Bitmap[] thumbnails;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_email);
    Toolbar toolbar = findViewById(R.id.toolbar);
    TextView mReport = findViewById(R.id.textView_report);
    setSupportActionBar(toolbar);
    getFromSdcard();
    fab = findViewById(R.id.fab);
    GridView imagegrid = findViewById(R.id.gridView);
    imageAdapter = new ImageAdapter();
    imagegrid.setAdapter(imageAdapter);

    Intent intent = getIntent();
    mSubjectForEmail = Objects.requireNonNull(intent.getExtras()).getString("sub");
    mBodyForEmail = Objects.requireNonNull(intent.getExtras()).getString("body");
    mReport.setText(mBodyForEmail);

    getClick();
  }

  private void sendMyEmail() {
    final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
    emailIntent.setType("message/rfc822");
    emailIntent
        .putExtra(Intent.EXTRA_EMAIL, emails);
    emailIntent
        .putExtra(Intent.EXTRA_SUBJECT, mSubjectForEmail);
    emailIntent.putExtra(Intent.EXTRA_TEXT, mBodyForEmail);
    String[] mDirectory = {"/My Bus Stop/export report/CVS EXPORT/report.csv",
        "/My Bus Stop/saved img/PIC1.JPG", "/My Bus Stop/saved img/PIC2.JPG",
        "/My Bus Stop/saved img/PIC3.JPG", "/My Bus Stop/saved img/PIC4.JPG",
        "/My Bus Stop/saved img/PIC5.JPG"};
    ArrayList<Uri> uris = new ArrayList<>();
    for (String file : mDirectory) {
      File mFileIn = new File(Environment.getExternalStorageDirectory(), file);
      Uri u = Uri.fromFile(mFileIn);
      uris.add(u);
    }
    emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
    try {
      startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void getClick() {
    fab.setOnClickListener(view ->
        sendMyEmail()
    );
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    Utilities
        .delete(new File(Environment.getExternalStorageDirectory(), "/My Bus Stop/saved img/"));
    Utilities.delete(
        new File(Environment.getExternalStorageDirectory(), "/My Bus Stop/export report/CVS "
            + "EXPORT/"));
    Intent mIntent = new Intent(this, MainActivity.class);
    startActivity(mIntent);
    finish();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    Utilities
        .delete(new File(Environment.getExternalStorageDirectory(), "/My Bus Stop/saved img/"));
    Utilities.delete(
        new File(Environment.getExternalStorageDirectory(), "/My Bus Stop/export report/CVS "
            + "EXPORT/"));
    Intent mIntent = new Intent(this, MainActivity.class);
    startActivity(mIntent);
    finish();
  }

  public void getFromSdcard() {
    File file = new File(Environment.getExternalStorageDirectory(), "/My Bus Stop/saved img/");

    if (file.isDirectory()) {
      listFile = file.listFiles();

      for (int i = 0; i < listFile.length; i++) {

        f.add(listFile[i].getAbsolutePath());

      }
    }
  }

  public class ImageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    public ImageAdapter() {
      mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
      return f.size();
    }

    public Object getItem(int position) {
      return position;
    }

    public long getItemId(int position) {
      return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder holder;
      if (convertView == null) {
        holder = new ViewHolder();
        convertView = mInflater.inflate(
            R.layout.gelleryitem, null);
        holder.imageview = convertView.findViewById(R.id.thumbImage);

        convertView.setTag(holder);
      } else {
        holder = (ViewHolder) convertView.getTag();
      }

      Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
      holder.imageview.setImageBitmap(myBitmap);
      return convertView;
    }
  }

  class ViewHolder {

    ImageView imageview;


  }
}



