package com.laodev.tictic.SegmentProgress;

/**
 * Created by AQEEL on 3/25/2019.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;
import java.util.List;

public class SegmentedProgressBar extends View {

    private static final String TAG = "SegmentedProgressBar";
    private static final int FPS_IN_MILLI = 16; // 16.66 ~ 60fps

    private Paint progressPaint = new Paint();
    private Paint dividerPaint = new Paint();
    private int[] gradientColors = new int[3];

    private float lastDividerPosition;
    private float percentCompleted;


    private int progressBarWidth;
    private long maxTimeInMillis;

    private int dividerCount = 0;
    private float dividerWidth = 1;

    private boolean isDividerEnabled;

    private List<Float> dividerPositions;
    private CountDownTimerWithPause countDownTimerWithPause;

    private float cornerRadius;

    ProgressBarListener listener;

    public SegmentedProgressBar(Context context) {
        super(context);
        init();
    }

    public SegmentedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SegmentedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(new RectF(0, 0, percentCompleted, getHeight()), cornerRadius, cornerRadius, progressPaint);

        if (dividerCount > 0 && isDividerEnabled) {
            for (int i = 0; i < dividerCount; i++) {
                float leftPosition = dividerPositions.get(i);
                canvas.drawRect(leftPosition, 0, leftPosition + dividerWidth, getHeight(), dividerPaint);
            }
        }
    }

    private void init() {
        dividerPositions = new ArrayList<>();
        cornerRadius = 0;

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    progressBarWidth = getWidth();
                    Log.d(TAG, "setShader: progressBarWidth : " + progressBarWidth);

                    if (gradientColors.length > 0) {
                        Shader shader = new LinearGradient(0, 0, progressBarWidth, getHeight(), gradientColors, null, Shader.TileMode.MIRROR);
                        progressPaint.setShader(shader);
                    }
                }
            });
        }
    }



    public void SetListener(ProgressBarListener listeter){
        this.listener=listeter;
    }

    /**
     * Updates the progress bar based on time passed
     *
     * @param millisPassed
     */
    private void updateProgress(long millisPassed) {
        listener.TimeinMill(millisPassed);
        percentCompleted = progressBarWidth * (float) millisPassed / maxTimeInMillis;
        invalidate();
    }

    /**
     * Updates the progress bar based on manually passed percent value.
     *
     * @param percentValue
     */
    private void updateProgress(float percentValue) {
        percentCompleted = progressBarWidth * percentValue;
        invalidate();
    }


    public float GetPercentComplete(){
        return percentCompleted;
    }


    public void pause() {
        if (countDownTimerWithPause == null) {
            Log.e(TAG, "pause: Auto progress is not initialized. Use \"enableAutoProgressView\" to initialize the progress bar.");
            return;
        }

        countDownTimerWithPause.pause();
    }

    /**
     * Resume the progress bar
     */
    public void resume() {
        if (countDownTimerWithPause == null) {
            Log.e(TAG, "resume: Auto progress is not initialized. Use \"enableAutoProgressView\" to initialize the progress bar.");
            return;
        }

        countDownTimerWithPause.resume();
    }

    public void reset() {
        countDownTimerWithPause.cancel();
        enableAutoProgressView(maxTimeInMillis);
        dividerPositions.removeAll(dividerPositions);
        percentCompleted = 0;
        lastDividerPosition = 0;
        dividerCount = 0;
        invalidate();
    }

    public void cancel() {
        if (countDownTimerWithPause == null) {
            Log.e(TAG, "cancel: Auto progress is not initialized. Use \"enableAutoProgressView\" to initialize the progress bar.");
            return;
        }

        countDownTimerWithPause.cancel();
    }

    /**
     * Apply the shader for the for the progress bar.
     *
     * @param colors
     */
    public void setShader(final int[] colors) {
        this.gradientColors = colors;

        if (progressBarWidth > 0) {
            Shader shader = new LinearGradient(0, 0, progressBarWidth, getHeight(), colors, null, Shader.TileMode.MIRROR);
            progressPaint.setShader(shader);
        }
    }

    /**
     * Set the color for the progress bar
     *
     * @param color
     */
    public void setProgressColor(int color) {
        progressPaint.setColor(color);
    }

    /**
     * Set the color for the divider bar
     *
     * @param color
     */
    public void setDividerColor(int color) {
        dividerPaint.setColor(color);
    }

    /**
     * set the width of the divider
     *
     * @param width
     */
    public void setDividerWidth(float width) {
        if (width < 0) {
            Log.w(TAG, "setDividerWidth: Divider width can not be negative");
            return;
        }

        dividerWidth = width;
    }

    /**
     * The progress bar will be auto progressing within the given time limit towards completion
     * where the it can be paused and resumed.
     *
     * @param timeInMillis
     */
    public void enableAutoProgressView(long timeInMillis) {
        if (timeInMillis < 0) {
            Log.w(TAG, "enableAutoProgressView: Time can not be in negative");
            return;
        }

        this.maxTimeInMillis = timeInMillis;

        countDownTimerWithPause = new CountDownTimerWithPause(maxTimeInMillis, FPS_IN_MILLI, false) {
            @Override
            public void onTick(long millisUntilFinished) {
                long timePassed = SegmentedProgressBar.this.maxTimeInMillis - millisUntilFinished;
                updateProgress(timePassed);
            }

            @Override
            public void onFinish() {
                updateProgress(SegmentedProgressBar.this.maxTimeInMillis);
            }
        }.create();
    }

    public void setDividerEnabled(boolean value) {
        isDividerEnabled = value;
    }

    /**
     * Manually update the progress bar completion status
     *
     * @param value can only be in between 0 and 1 inclusive.
     */
    public void publishProgress(final float value) {
        if (value < 0 || value > 1) {
            Log.w(TAG, "publishProgress: Progress value can only be in between 0 and 1");
            return;
        }

        updateProgress(value);
    }

    /**
     * Add Divider to current position
     */
    public void addDivider() {
        if (lastDividerPosition != percentCompleted) {
            lastDividerPosition = percentCompleted;
            dividerCount += 1;
            dividerPositions.add(percentCompleted);
            invalidate();
        } else {
            Log.w(TAG, "addDivider: Divider already added to current position");
        }
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }
}
