package com.slife.sharelife.life;

import android.content.res.Configuration;
import android.support.annotation.IdRes;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.gyf.barlibrary.ImmersionBar;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class Main2Activity extends AppCompatActivity {

    private StandardGSYVideoPlayer detailPlayer;
    private OrientationUtils orientationUtils;
    private boolean isPlay;
    private boolean isPause;
    private ImmersionBar mImmersionBar;
    private RadioGroup radioGroup;
    private NestedScrollView nestedScrollView;
    private View linerDetail;
    private View linerComment;
    private View linerTreasure;
    private View linerRecommend;
    private View video;
    private int measureHeight;
    private int linerCommentHeight;
    private int linerDetailHeight;
    private int linerRecommendHeight;
    private int videoHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initImmersionBar();
        init();
        initView();
        setListenter();
    }

    private void setListenter() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                Log.d("嘎嘎","--"+scrollY+"--"+linerComment.getTop()+"--"+linerDetail.getTop()+"--"+linerRecommend.getTop());

                if (0<scrollY&&scrollY<linerComment.getTop()){
                    setChecked(0);
                }else if (linerComment.getTop()<=scrollY&& scrollY<linerDetail.getTop()){
                    setChecked(1);
                }else if (linerDetail.getTop()<=scrollY&&scrollY<linerRecommend.getTop()){
                    setChecked(2);
                }else if (linerRecommend.getTop()<=scrollY){
                    setChecked(3);
                }

            }
        });
    }

    private void setChecked(int select){
        for(int i=0;i<radioGroup.getChildCount();i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setTextColor(i==select ? getResources().getColor(R.color.black) : getResources().getColor(R.color.gray));

        }
    }

    private void initView() {
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                measureHeight = getMeasureHeight(linerTreasure);
                linerCommentHeight = getMeasureHeight(linerComment);
                linerDetailHeight = getMeasureHeight(linerDetail);
                linerRecommendHeight = getMeasureHeight(linerRecommend);
                videoHeight = getMeasureHeight(video);
                Log.d("www嘎嘎",measureHeight+"--"+linerCommentHeight+"--"+linerDetailHeight+"--"+linerRecommendHeight+"--"+videoHeight);
            }
        });
        radioGroup.setOnCheckedChangeListener(radioGroupListener);
    }

    private RadioGroup.OnCheckedChangeListener radioGroupListener =new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            for(int i=0;i<radioGroup.getChildCount();i++){
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setTextColor(radioButton.isChecked()?getResources().getColor(R.color.black):getResources().getColor(R.color.gray));
                if(radioButton.isChecked()){
                    if (i==0){
                        nestedScrollView.scrollTo(0,0);
                    }else  if (i==1){
                        nestedScrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                nestedScrollView.scrollTo(0,  linerComment.getTop());
                            }
                        });
                    } else if (i==2){
                        nestedScrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                nestedScrollView.scrollTo(0,  linerDetail.getTop());
                            }
                        });
                    } else if (i==3){
                        nestedScrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                nestedScrollView.scrollTo(0,  linerRecommend.getTop());
                            }
                        });
                    }
                }
            }
        }
    };
    private void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar
                .navigationBarEnable(false)   //是否可以修改导航栏颜色，默认为true
                .navigationBarWithKitkatEnable(false)  //是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题
                .statusBarDarkFont(true, 0.2f)
                .fullScreen(true)
                .init();
    }

    public int getMeasureHeight(View view){
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(width, height);
        return view.getMeasuredHeight();
    }
    private void init() {
        detailPlayer = findViewById(R.id.detail_player);
        radioGroup = findViewById(R.id.radioGroup);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        linerTreasure = findViewById(R.id.linerTreasure);
        linerDetail = findViewById(R.id.linerDetail);
        video = findViewById(R.id.video);
        linerComment = findViewById(R.id.linerComment);
        linerRecommend = findViewById(R.id.linerRecommend);
        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
//初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        //增加封面

        ImageView imageView = new ImageView(this);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setImageResource(R.mipmap.xxx1);

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setThumbImageView(imageView)
                .setIsTouchWiget(true)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setAutoFullWithSize(true)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setUrl("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
                .setCacheWithPlay(false)
                .setVideoTitle("测试视频")
                .setVideoAllCallBack(new GSYSampleCallBack() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        //开始播放了才能旋转和全屏
                        orientationUtils.setEnable(true);
                        isPlay = true;
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
                        Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
                        if (orientationUtils != null) {
                            orientationUtils.backToProtVideo();
                        }
                    }
                }).setLockClickListener(new LockClickListener() {
            @Override
            public void onClick(View view, boolean lock) {
                if (orientationUtils != null) {
                    //配合下方的onConfigurationChanged
                    orientationUtils.setEnable(!lock);
                }
            }
        }).build(detailPlayer);
        detailPlayer.getTitleTextView().setPadding(0,70,0,0);
        //设置返回键
        detailPlayer.getBackButton().setPadding(0,70,0,0);
        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(Main2Activity.this, true, true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        detailPlayer.getCurrentPlayer().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        detailPlayer.getCurrentPlayer().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        if (isPlay) {
            detailPlayer.getCurrentPlayer().release();
        }
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }
}
