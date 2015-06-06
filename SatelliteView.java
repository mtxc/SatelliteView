package com.mtxc.myapplication;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;

public class SatelliteView extends View {

    /**
     * Log打印标签
     */
    private static final String TAG = "SatelliteView";
    /**
     * 默认动画起始角度
     */
    private static final int FROM_DEGREE_DEFAULT = 270;
    /**
     * 默认动画截止角度
     */
    private static final int TO_DEGREE_DEFAULT = 360;
    /**
     * 默认按钮个数
     */
    private static final int BUTTON_NUM_DEFAULT = 6;
    /**
     * 默认动画半径
     */
    private static final int RADIUS_DEFAULT = 300;
    /**
     * 默认动画持续时间
     */
    private static final int DURATION_DEFAULT = 500;

    /**
     * 动画起始角度
     */
    private int fromDegree = FROM_DEGREE_DEFAULT;
    /**
     * 动画截止角度
     */
    private int toDegree = TO_DEGREE_DEFAULT;
    /**
     * 按钮个数
     */
    private int buttonNum = BUTTON_NUM_DEFAULT;
    /**
     * 动画半径
     */
    private int radius = RADIUS_DEFAULT;
    /**
     * 动画持续时间
     */
    private int duration = DURATION_DEFAULT;
    /**
     * 视图是否展开的标志位
     */
    private boolean isOpen = false;
    /**
     * 菜单展开或折叠按钮
     */
    private ImageButton top;
    /**
     * 菜单按钮数组
     */
    private ArrayList<ImageButton> btnList;
    /**
     * 存储所有按钮展开后的位置
     */
    private ArrayList<MyPoint> points = new ArrayList<MyPoint>();
    /**
     * view所在上下文
     */
    private Context mContext;

    public SatelliteView(Context context) {
        this(context, null, 0);
    }

    public SatelliteView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SatelliteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        //从xml获取attr属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SatelliteView);
        fromDegree = array.getInt(R.styleable.SatelliteView_fromDegree, FROM_DEGREE_DEFAULT);
        toDegree = array.getInt(R.styleable.SatelliteView_toDegree, TO_DEGREE_DEFAULT);
        buttonNum = array.getInt(R.styleable.SatelliteView_buttonNum, BUTTON_NUM_DEFAULT);
        radius = array.getInt(R.styleable.SatelliteView_radius, RADIUS_DEFAULT);
        duration = array.getInt(R.styleable.SatelliteView_duration, DURATION_DEFAULT);
        array.recycle();
        //做初始化操作
        init();
    }

    /**
     * view初始化操作
     */
    private void init() {
        top = new ImageButton(mContext);
        //设置菜单折叠按钮监听事件
        top.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimator();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 展开或折叠动画播放
     */
    private void startAnimator() {
        if (isOpen) {
            //收起菜单
            Log.e(TAG, "收起菜单");
            AnimatorSet set = new AnimatorSet();
            //顶部按钮的缩放动画
            ObjectAnimator topX = ObjectAnimator.ofFloat(top, "scaleX", 1.0f, 0.7f, 1.0f);
            topX.setDuration(duration);
            ObjectAnimator topY = ObjectAnimator.ofFloat(top, "scaleY", 1.0f, 0.7f, 1.0f);
            topY.setDuration(duration);
            set.play(topX).with(topY);
            //其余按钮的平移动画
            for (int i = 0; i < buttonNum; i++) {
                ObjectAnimator actX = ObjectAnimator.ofFloat(btnList.get(i), "translationX", points.get(i).x, 0);
                actX.setDuration(duration);
                ObjectAnimator actY = ObjectAnimator.ofFloat(btnList.get(i), "translationY", points.get(i).y, 0);
                actY.setDuration(duration);
                set.play(actX).with(actY).with(topX);
            }
            set.start();
            isOpen = false;
        } else {
            //展开菜单
            Log.e(TAG, "展开菜单");
            AnimatorSet set = new AnimatorSet();
            //顶部按钮的缩放动画
            ObjectAnimator topX = ObjectAnimator.ofFloat(top, "scaleX", 1.0f, 0.7f, 1.0f);
            topX.setDuration(duration);
            ObjectAnimator topY = ObjectAnimator.ofFloat(top, "scaleY", 1.0f, 0.7f, 1.0f);
            topY.setDuration(duration);
            set.play(topX).with(topY);
            //其余按钮的平移动画
            for (int i = 0; i < buttonNum; i++) {
                ObjectAnimator actX = ObjectAnimator.ofFloat(btnList.get(i), "translationX", 0, points.get(i).x);
                actX.setDuration(duration);
                ObjectAnimator actY = ObjectAnimator.ofFloat(btnList.get(i), "translationY", 0, points.get(i).y);
                actY.setDuration(duration);
                set.play(actX).with(actY).with(topX);
            }
            set.start();
            isOpen = true;
        }
    }

    /**
     * 计算各按钮展开后的位置
     */
    private void calculate() {
        points.clear();
        for(int i=0; i<buttonNum; i++){
            double angle = (toDegree-fromDegree) / (buttonNum-1) * i;
            MyPoint point = new MyPoint((float) (radius * (Math.cos(Math.toRadians(angle)))), (float) (radius * (Math.sin(Math.toRadians(angle)))));
            points.add(point);
        }
    }

    /**
     * 自定义存放按钮动画位置的类
     */
    class MyPoint {

        public float x;
        public float y;

        public MyPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "MyPoint{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

    }
}
