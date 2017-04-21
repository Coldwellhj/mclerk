package com.eaosoft.util;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eaosoft.mclerk.R;

public class SwitchButton extends LinearLayout {

    /**
     * ����ͼƬ
     */
    private LinearLayout switchParent;
    /**
     * ����ͼƬ
     */
    private ImageView switchButton;
    /**
     * ��ť״̬��Ĭ�Ϲر�
     */
    private boolean isOn = false;
    /**
     * ������Ҫ�����ľ���
     */
    private int scrollDistance;
    /**
     * ���ذ�ť������
     */
    private SwitchChangedListner listner;

    public SwitchButton(Context context) {
        super(context);
        initWedgits(context);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWedgits(context);
    }

    /**
     * ��ʼ�����
     *
     * @param context
     *            �����Ļ���
     */
    private void initWedgits(Context context) {
        try {
            View view = LayoutInflater.from(context).inflate(
                    R.layout.switch_button, this);
            switchParent = (LinearLayout) view.findViewById(R.id.switch_parent);
            switchButton = (ImageView) view.findViewById(R.id.switch_button);
            addListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ����¼�������
     */
    private void addListeners() {
        try {
            switchParent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isOn = !isOn;
                    scrollSwitch();
                    if (null != listner) {
                        // ���ؿ������߹رյĻص�����
                        listner.switchChanged(getId(), isOn);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ��������
     */
    private void scrollSwitch() {
        // ��ȡ������Ҫ�����ľ��룬����������ڸ��齨�Ŀ�ȼ�ȥ����Ŀ��
        scrollDistance = switchParent.getWidth() - switchButton.getWidth();
        // ��ʼ�������¼�
        Animation animation = null;
        if (isOn) {
            animation = new TranslateAnimation(0, scrollDistance, 0, 0);
        } else {
            animation = new TranslateAnimation(scrollDistance, 0, 0, 0);
        }
        // ���û���ʱ��
        animation.setDuration(200);
        // ����֮�󱣳�״̬
        animation.setFillAfter(true);
        // ��ʼ����
        switchButton.startAnimation(animation);
    }

    /**
     * ��ȡ����״̬
     *
     * @return ��true:�򿪡���false:�رա�
     */
    public boolean isOn() {
        return isOn;
    }

    /**
     * ���ÿ���״̬
     *
     * @param isOn
     *            ����״̬��true:�򿪡���false:�رա�
     */
    public void setOn(boolean isOn) {
        if (this.isOn == isOn) {
            return;
        }
        this.isOn = isOn;
        post(new Runnable() {
            @Override
            public void run() {
                scrollSwitch();
            }
        });
    }

    /**
     * ���ÿ���״̬������
     *
     * @param listner
     *            ����״̬������
     */
    public void setOnSwitchListner(SwitchChangedListner listner) {
        this.listner = listner;
    }

    /**
     * ����״̬������
     *
     * @author llew
     *
     */
    public interface SwitchChangedListner {
        /**
         * ����״̬�ı�
         *
         * @param viewId
         *            ��ǰ����ID
         * @param isOn
         *            �����Ƿ�򿪡�true:�򿪡���false:�رա�
         */
        public void switchChanged(Integer viewId, boolean isOn);
    }
}
