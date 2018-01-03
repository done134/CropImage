package com.example.yinzhendong.cropimage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.yinzhendong.cropimage.utils.Crop;
import com.example.yinzhendong.cropimage.utils.CropUtil;
import com.example.yinzhendong.cropimage.utils.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yinzhendong.cropimage.view.PinchImageView;

import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {


    private Uri sourceUri;
    private Uri saveUri;

    private boolean isSaving;
    String tempImageUrl = "https://wx1.sinaimg.cn/mw690/4c56dd36gy1fn2ig33nkuj211x1kwb29.jpg";



    private PinchImageView imageView;
    private ImageView resultImage;
    private static final int REQUEST_CODE_READ_PERMISSION = 22;
    private static final int REQUEST_GALLERY = 21;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (PinchImageView) findViewById(R.id.image_view);
        resultImage = (ImageView) findViewById(R.id.iv_result);
        imageView.setHorizontalPadding(600);
        imageView.setVerticalPadding(264);
        Glide.with(this).load(tempImageUrl).into(imageView);
        /*startGalleryIntent();
        if (!hasGalleryPermission()) {
            askForGalleryPermission();
            return;
        }*/


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_clip_image) {
            Bitmap bitmap = imageView.clip();
            saveOutput(bitmap);
            if (bitmap != null) {
                resultImage.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                resultImage.setImageBitmap(bitmap);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent resultIntent) {
        super.onActivityResult(requestCode, responseCode, resultIntent);

        if (responseCode == RESULT_OK) {
            String absPath = BitmapUtils.getFilePathFromUri(this, resultIntent.getData());
            Bitmap bitmap = BitmapFactory.decodeFile(absPath);
            imageView.setImageBitmap(bitmap);

        }
    }

    private boolean hasGalleryPermission() {
        return ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void askForGalleryPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_READ_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_READ_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGalleryIntent();
                return;
            }
        }

        Toast.makeText(this, "Gallery permission not granted", Toast.LENGTH_SHORT).show();
    }


    private void startGalleryIntent() {

        if (!hasGalleryPermission()) {
            askForGalleryPermission();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private void saveOutput(Bitmap croppedImage) {
        if (saveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(saveUri);
                if (outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.PNG,
                            90,     // note: quality is ignored when using PNG
                            outputStream);
                }
            } catch (IOException e) {
                setResultException(e);
                Log.e("Cannot open file: " + saveUri, e);
            } finally {
                CropUtil.closeSilently(outputStream);
            }

            CropUtil.copyExifRotation(
                    CropUtil.getFromMediaUri(this, getContentResolver(), sourceUri),
                    CropUtil.getFromMediaUri(this, getContentResolver(), saveUri)
            );

            setResultUri(saveUri);
        }
        Intent intent = new Intent(this,ResultActivity.class);
        intent.putExtra("imagePath", saveUri);
        startActivity(intent);

        final Bitmap b = croppedImage;
        /*handler.post(new Runnable() {
            public void run() {
                imageView.clear();
                b.recycle();
            }
        });*/

//        finish();
    }

    private void setResultUri(Uri uri) {
        setResult(RESULT_OK, new Intent().putExtra(MediaStore.EXTRA_OUTPUT, uri));
    }

    private void setResultException(Throwable throwable) {
        setResult(Crop.RESULT_ERROR, new Intent().putExtra(Crop.Extra.ERROR, throwable));
    }
}
