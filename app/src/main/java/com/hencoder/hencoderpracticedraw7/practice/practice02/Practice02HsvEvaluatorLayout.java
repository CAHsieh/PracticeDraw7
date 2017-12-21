package com.hencoder.hencoderpracticedraw7.practice.practice02;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hencoder.hencoderpracticedraw7.R;

public class Practice02HsvEvaluatorLayout extends RelativeLayout {
    Practice02HsvEvaluatorView view;
    Button animateBt;

    public Practice02HsvEvaluatorLayout(Context context) {
        super(context);
    }

    public Practice02HsvEvaluatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice02HsvEvaluatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        view = (Practice02HsvEvaluatorView) findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofInt(view, "color", 0xffff0000, 0xff00ff00);
                animator.setEvaluator(new HsvEvaluator()); // 使用自定义的 HsvEvaluator
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(2000);
                animator.start();
            }
        });
    }

    private class HsvEvaluator implements TypeEvaluator<Integer> {

        float[] startHSV = new float[3];
        float[] endHSV = new float[3];
        float[] nowHSV = new float[3];

        // 重写 evaluate() 方法，让颜色按照 HSV 来变化
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {

            Color.colorToHSV(startValue, startHSV);
            Color.colorToHSV(endValue, endHSV);

            if (endHSV[0] - startHSV[0] > 180) {
                startHSV[0] -= 360;
            } else if (endHSV[0] - startHSV[0] < -180) {
                startHSV[0] += 360;
            }
            nowHSV[0] = startHSV[0] + (endHSV[0] - startHSV[0]) * fraction;
            if (nowHSV[0] > 360) {
                nowHSV[0] -= 360;
            } else if (nowHSV[0] < 0) {
                nowHSV[0] += 360;
            }

            nowHSV[1] = startHSV[1] + (endHSV[1] - startHSV[1]) * fraction;
            nowHSV[2] = startHSV[2] + (endHSV[2] - startHSV[2]) * fraction;

            int alpha = startValue >> 24 + (int) ((endValue >> 24 - startValue >> 24) * fraction);
            return Color.HSVToColor(alpha, nowHSV);
        }
    }
}
