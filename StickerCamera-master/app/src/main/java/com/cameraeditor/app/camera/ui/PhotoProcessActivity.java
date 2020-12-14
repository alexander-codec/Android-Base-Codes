package com.cameraeditor.app.camera.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.util.FileUtils;
import com.common.util.ImageUtils;
import com.common.util.StringUtils;
import com.customview.MyImageViewDrawableOverlay;
import com.github.skykai.stickercamera.R;
import com.cameraeditor.App;
import com.cameraeditor.app.camera.CameraBaseActivity;
import com.cameraeditor.app.camera.CameraManager;
import com.cameraeditor.app.camera.EffectService;
import com.cameraeditor.app.camera.adapter.FilterAdapter;
import com.cameraeditor.app.camera.adapter.StickerToolAdapter;
import com.cameraeditor.app.camera.effect.FilterEffect;
import com.cameraeditor.app.camera.util.EffectUtil;
import com.cameraeditor.app.camera.util.GPUImageFilterTools;
import com.cameraeditor.app.model.Addon;
import com.cameraeditor.app.model.FeedItem;
import com.cameraeditor.app.model.TagItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import it.sephiroth.android.library.widget.HListView;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

/**
 * 图片处理界面
 * Created by sky on 2015/7/8.
 * Weibo: http://weibo.com/2030683111
 * Email: 1132234509@qq.com
 */
public class PhotoProcessActivity extends CameraBaseActivity {

    //滤镜图片
    @InjectView(R.id.gpuimage)
    GPUImageView mGPUImageView;
    //绘图区域
    @InjectView(R.id.drawing_view_container)
    ViewGroup drawArea;
    //底部按钮
    @InjectView(R.id.sticker_btn)
    TextView stickerBtn;
    @InjectView(R.id.filter_btn)
    TextView filterBtn;
    //工具区
    @InjectView(R.id.list_tools)
    HListView bottomToolBar;
    @InjectView(R.id.toolbar_area)
    ViewGroup toolArea;
    private MyImageViewDrawableOverlay mImageView;
    ImageView stickers;

    //当前选择底部按钮
    private TextView currentBtn;
    //当前图片
    private Bitmap currentBitmap;
    //用于预览的小图片
    private Bitmap smallImageBackgroud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);
        ButterKnife.inject(this);
        EffectUtil.clear();
        initView();
        initEvent();
        initStickerToolBar();

        ImageUtils.asyncLoadImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                currentBitmap = result;
                mGPUImageView.setImage(currentBitmap);
            }
        });

        ImageUtils.asyncLoadSmallImage(this, getIntent().getData(), new ImageUtils.LoadImageCallback() {
            @Override
            public void callback(Bitmap result) {
                smallImageBackgroud = result;
            }
        });

    }
    private void initView() {
        //添加贴纸水印的画布
        View overlay = LayoutInflater.from(PhotoProcessActivity.this).inflate(
                R.layout.view_drawable_overlay, null);
        mImageView = (MyImageViewDrawableOverlay) overlay.findViewById(R.id.drawable_overlay);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(App.getApp().getScreenWidth(),
                App.getApp().getScreenWidth());
        mImageView.setLayoutParams(params);
        overlay.setLayoutParams(params);
        drawArea.addView(overlay);
        //添加标签选择器
        RelativeLayout.LayoutParams rparams = new RelativeLayout.LayoutParams(App.getApp().getScreenWidth(), App.getApp().getScreenWidth());

        //初始化滤镜图片
        mGPUImageView.setLayoutParams(rparams);
    }

    private void initEvent() {
        /*stickers = (ImageView)findViewById(R.id.imageView3);
        stickers.setVisibility(View.VISIBLE);*/
        stickerBtn.setOnClickListener(v ->{
            if (!setCurrentBtn(stickerBtn)) {
                return;
            }
            bottomToolBar.setVisibility(View.VISIBLE);
            initStickerToolBar();
        });

        filterBtn.setOnClickListener(v -> {
            if (!setCurrentBtn(filterBtn)) {
                return;
            }
            bottomToolBar.setVisibility(View.VISIBLE);
            initFilterToolBar();
        });


        titleBar.setRightBtnOnclickListener(v -> {
            savePicture();
        });
    }

    //保存图片
    private void savePicture(){
        //加滤镜
        final Bitmap newBitmap = Bitmap.createBitmap(mImageView.getWidth(), mImageView.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        RectF dst = new RectF(0, 0, mImageView.getWidth(), mImageView.getHeight());
        try {
            cv.drawBitmap(mGPUImageView.capture(), null, dst, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
            cv.drawBitmap(currentBitmap, null, dst, null);
        }
        //加贴纸水印
        EffectUtil.applyOnSave(cv, mImageView);

        new SavePicToFileTask().execute(newBitmap);
        Toast.makeText(PhotoProcessActivity.this,
                "Selfie salva na galeria", Toast.LENGTH_LONG).show();
    }

    private class SavePicToFileTask extends AsyncTask<Bitmap,Void,String>{
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("Procesando imagem...");
        }

        @Override
        protected String doInBackground(Bitmap... params) {
            String fileName = null;
            try {
                bitmap = params[0];
                 fileName = ImageUtils.saveToFile(FileUtils.getInst().getPhotoSavedPath(), false, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                toast("Erro de processamento de imagem, por favor sair da câmara e tente novamente", Toast.LENGTH_LONG);
            }
            return fileName;
        }

        @Override
        protected void onPostExecute(String fileName) {
            super.onPostExecute(fileName);
            dismissProgressDialog();
            if (StringUtils.isEmpty(fileName)) {
                return;
            }
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{fileName}, null, null);

            //将图片信息通过EventBus发送到MainActivity
            CameraManager.getInst().close();
        }
    }


    private boolean setCurrentBtn(TextView btn) {
        if (currentBtn == null) {
            currentBtn = btn;
        } else if (currentBtn.equals(btn)) {
            return false;
        } else {
            currentBtn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        Drawable myImage = getResources().getDrawable(R.drawable.select_icon);
        btn.setCompoundDrawablesWithIntrinsicBounds(null, null, null, myImage);
        currentBtn = btn;
        return true;
    }


    //初始化贴图
    private void initStickerToolBar(){

        bottomToolBar.setAdapter(new StickerToolAdapter(PhotoProcessActivity.this, EffectUtil.addonList));
        bottomToolBar.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> arg0,
                                    View arg1, int arg2, long arg3) {
                Addon sticker = EffectUtil.addonList.get(arg2);
                EffectUtil.addStickerImage(mImageView, PhotoProcessActivity.this, sticker,
                        new EffectUtil.StickerCallback() {
                            @Override
                            public void onRemoveSticker(Addon sticker) {

                            }
                        });
            }
        });
        setCurrentBtn(stickerBtn);
    }


    //初始化滤镜
    private void initFilterToolBar(){
        final List<FilterEffect> filters = EffectService.getInst().getLocalFilters();
        final FilterAdapter adapter = new FilterAdapter(PhotoProcessActivity.this, filters,smallImageBackgroud);
        bottomToolBar.setAdapter(adapter);
        bottomToolBar.setOnItemClickListener(new it.sephiroth.android.library.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(it.sephiroth.android.library.widget.AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (adapter.getSelectFilter() != arg2) {
                    adapter.setSelectFilter(arg2);
                    GPUImageFilter filter = GPUImageFilterTools.createFilterForType(
                            PhotoProcessActivity.this, filters.get(arg2).getType());
                    mGPUImageView.setFilter(filter);
                    GPUImageFilterTools.FilterAdjuster mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(filter);
                    //可调节颜色的滤镜
                    if (mFilterAdjuster.canAdjust()) {
                        //mFilterAdjuster.adjust(100); 给可调节的滤镜选一个合适的值
                    }
                }
            }
        });
    }
}
