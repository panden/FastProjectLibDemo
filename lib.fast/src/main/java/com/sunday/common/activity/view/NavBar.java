package com.sunday.common.activity.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunday.common.R;
import com.sunday.common.utils.StatusBarUtil;

/**
 * 标题栏封装
 */
public class NavBar extends RelativeLayout implements View.OnClickListener {

    private ImageButton leftBtn;
    private ImageButton leftSenBtn;
    private ImageButton rightIconIbtn;
    private Button rightTxtBtn;
    private TextView mTitle;
    private View mRootView;
    private View endLine;
    private boolean mAutoSetStatusBarColor = true;

    public void setClickListener(NavBarOnClickListener clickListener) {
        mClickListener = clickListener;
    }

    private NavBarOnClickListener mClickListener;


    public interface NavBarOnClickListener {
        void onLeftIconClick(View view);

        void onLeftSenIconClick(View view);

        void onRightIconClick(View view);

        void onRightTxtClick(View view);
    }


    public NavBar(Context context) {
        this(context, null);
    }

    public NavBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRootView = LayoutInflater.from(context).inflate(R.layout.navbar, this, false);
        initView(context, attrs, mRootView);
        addView(mRootView);
    }

    private void initView(Context context, AttributeSet attrs, View rootView) {
        leftBtn = (ImageButton) rootView.findViewById(R.id.left_btn);
        leftSenBtn = (ImageButton) rootView.findViewById(R.id.left_sen_btn);
        rightIconIbtn = (ImageButton) rootView.findViewById(R.id.right_icon_ibtn);
        rightTxtBtn = (Button) rootView.findViewById(R.id.right_txt_btn);
        mTitle = (TextView) rootView.findViewById(R.id.bar_title);
        endLine = rootView.findViewById(R.id.bar_end_line);
        leftBtn.setOnClickListener(this);
        leftSenBtn.setOnClickListener(this);
        rightIconIbtn.setOnClickListener(this);
        rightTxtBtn.setOnClickListener(this);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NavBar);

            /*左侧Icon*/
            boolean showLeftBtn = typedArray.getBoolean(R.styleable.NavBar_showLeftIcon, true);
            leftBtn.setVisibility(showLeftBtn ? View.VISIBLE : View.INVISIBLE);
            Drawable leftIcon = typedArray.getDrawable(R.styleable.NavBar_leftIcon);
            if (leftIcon != null) {
                leftBtn.setImageDrawable(leftIcon);
            }

            /*左侧第二个Icon*/
            boolean showLeftSenBtn = typedArray.getBoolean(R.styleable.NavBar_showSenLeftIcon, false);
            leftSenBtn.setVisibility(showLeftSenBtn ? View.VISIBLE : View.INVISIBLE);
            Drawable leftSenIcon = typedArray.getDrawable(R.styleable.NavBar_leftSenIcon);
            if (leftIcon != null) {
                leftSenBtn.setImageDrawable(leftSenIcon);
            }

            /*标题*/
            boolean showTitle = typedArray.getBoolean(R.styleable.NavBar_showTitle, true);
            mTitle.setVisibility(showTitle ? View.VISIBLE : View.INVISIBLE);
            int titleColor = typedArray.getColor(R.styleable.NavBar_titleColor, getResources().getColor(R.color.navbar_txtcolor));
            mTitle.setTextColor(titleColor);
            float titleSize = typedArray.getDimension(R.styleable.NavBar_titleSize, getResources().getDimension(R.dimen.text_large));
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
            String centerTitle = typedArray.getString(R.styleable.NavBar_title);
            if (centerTitle != null) {
                mTitle.setText(centerTitle);
            }

            /*右侧Icon*/
            boolean showRightIcon = typedArray.getBoolean(R.styleable.NavBar_showRightIcon, false);
            rightIconIbtn.setVisibility(showRightIcon ? View.VISIBLE : View.INVISIBLE);
            Drawable rightIcon = typedArray.getDrawable(R.styleable.NavBar_rightIcon);
            if (rightIcon != null) {
                rightIconIbtn.setImageDrawable(rightIcon);
            }

            /*右侧文字*/
            boolean showRightTxt = typedArray.getBoolean(R.styleable.NavBar_showRightText, false);
            rightTxtBtn.setVisibility(showRightTxt ? View.VISIBLE : View.INVISIBLE);
            int rightTxtColor = typedArray.getColor(R.styleable.NavBar_rightTxtColor, getResources().getColor(R.color.navbar_txtcolor));
            rightTxtBtn.setTextColor(rightTxtColor);
            float rightSize = typedArray.getDimension(R.styleable.NavBar_rightTextSize, getResources().getDimension(R.dimen.text_middle));
            rightTxtBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightSize);
            String rightTxt = typedArray.getString(R.styleable.NavBar_rightText);
            if (rightTxt != null) {
                rightTxtBtn.setText(rightTxt);
            }

            if (showRightIcon && showRightTxt) {
                rightIconIbtn.setVisibility(INVISIBLE);
            }

            int backGroundColor = typedArray.getColor(R.styleable.NavBar_backgroundColor, Color.WHITE);
            mRootView.setBackgroundColor(backGroundColor);

            typedArray.recycle();
        }
    }

    public View getBackView() {
        return leftBtn;
    }

    public TextView getTitleView() {
        return mTitle;
    }

    public View getRightView() {
        return rightIconIbtn;
    }

    public View getRightBtnView() {
        return rightTxtBtn;
    }

    public View getmBtnLeftClose() {
        return leftSenBtn;
    }

    public String getTitle() {
        return mTitle.getText().toString();
    }

    public void hidden() {
        mRootView.setVisibility(View.GONE);
    }

    public void noEndLine() {
        endLine.setVisibility(View.GONE);
    }

    public void setTitle(CharSequence title) {
        if (title == null) {
            return;
        }
        mTitle.setText(title);
    }

    public void setTitle(int titleId) {
        mTitle.setText(titleId);
    }

    public void setTitleSize(float size) {
        if (size > 0) {
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
    }

    public void setTitleColor(int color) {
        mTitle.setTextColor(color);
    }

    public void hiddenTitle() {
        mTitle.setVisibility(View.INVISIBLE);
    }

    public void showTitle() {
        mTitle.setVisibility(View.VISIBLE);
    }

    public void setLeftIcon(Drawable leftIcon) {
        leftBtn.setImageDrawable(leftIcon);
    }

    public void setLeftIcon(Bitmap leftIcon) {
        leftBtn.setImageBitmap(leftIcon);
    }

    public void setLeftIcon(int resource) {
        leftBtn.setImageResource(resource);
    }

    public void hiddenLeftIcon() {
        leftBtn.setVisibility(View.INVISIBLE);
    }

    public void showLeftIcon() {
        leftBtn.setVisibility(View.VISIBLE);
    }

    public boolean isLeftBackShow() {
        return leftBtn != null && leftBtn.getVisibility() == View.VISIBLE;
    }

    public void setLeftSenIcon(Drawable leftIcon) {
        leftSenBtn.setImageDrawable(leftIcon);
    }

    public void setLeftSenIcon(Bitmap leftIcon) {
        leftSenBtn.setImageBitmap(leftIcon);
    }

    public void setLeftSenIcon(int resource) {
        leftSenBtn.setImageResource(resource);
    }

    public void hiddenLeftSenIcon() {
        leftSenBtn.setVisibility(View.INVISIBLE);
    }

    public void showLeftSenIcon() {
        leftSenBtn.setVisibility(View.VISIBLE);
    }

    public void setRightText(String txt) {
        if (txt == null) {
            return;
        }
        rightTxtBtn.setText(txt);
    }

    public void setRightTextSize(float size) {
        if (size > 0) {
            rightTxtBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
    }

    public void setRightText(int titleId) {
        rightTxtBtn.setText(titleId);
    }

    public void setRightTextColor(int color) {
        rightTxtBtn.setTextColor(color);
    }

    public void setRightTextColor(ColorStateList color) {
        rightTxtBtn.setTextColor(color);
    }

    public void setRightTextSize(int txtSize) {
        rightTxtBtn.setTextSize(txtSize);
    }

    public void setRightTextPadding(int left, int top, int right, int bottom) {
        rightTxtBtn.setPadding(left, top, right, bottom);
    }

    public void hiddenRightText() {
        rightTxtBtn.setVisibility(View.INVISIBLE);
    }

    public void showRightText() {
        rightTxtBtn.setVisibility(View.VISIBLE);
    }


    public void setRightIcon(Drawable leftIcon) {
        rightIconIbtn.setImageDrawable(leftIcon);
    }

    public void setRightIcon(Bitmap leftIcon) {
        rightIconIbtn.setImageBitmap(leftIcon);
    }

    public void setRightImageResource(int resource) {
        rightIconIbtn.setImageResource(resource);
    }

    public void hiddenRightIcon() {
        rightIconIbtn.setVisibility(View.INVISIBLE);
    }

    public void showRightIcon() {
        rightIconIbtn.setVisibility(View.VISIBLE);
    }

    public void setAutoSetStatusBarColor(boolean autoSet){
        mAutoSetStatusBarColor = autoSet;
    }

    public void setBackGroundColor(int color) {
        mRootView.setBackgroundColor(color);
        if(mAutoSetStatusBarColor){
            StatusBarUtil.setColor((Activity) getContext(), color);
        }
    }

    public void setBackGroundColor(String color) {
        int backgroundColor = 0;
        try {
            backgroundColor = Color.parseColor(color);
        } catch (Exception e) {
            return;
        }
        mRootView.setBackgroundColor(backgroundColor);
        if(mAutoSetStatusBarColor){
            StatusBarUtil.setColor((Activity) getContext(), backgroundColor);
        }
    }

    @Override
    public void onClick(View v) {
        if (mClickListener == null || v.getVisibility() != View.VISIBLE) {
            return;
        }
        int vId = v.getId();
        if (vId == R.id.left_btn) {
            mClickListener.onLeftIconClick(v);
        } else if (vId == R.id.left_sen_btn) {
            mClickListener.onLeftSenIconClick(v);
        } else if (vId == R.id.right_icon_ibtn) {
            mClickListener.onRightIconClick(v);
        } else if (vId == R.id.right_txt_btn) {
            mClickListener.onRightTxtClick(v);
        }
    }
}
