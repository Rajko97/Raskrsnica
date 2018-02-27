package com.raskrsnica.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sandr on 2/27/2018.
 */

public class GradientTextView extends android.support.v7.widget.AppCompatTextView {
    public GradientTextView( Context context )
    {
        super( context, null, -1 );
    }
    public GradientTextView( Context context,
                             AttributeSet attrs )
    {
        super( context, attrs, -1 );
    }
    public GradientTextView( Context context,
                             AttributeSet attrs, int defStyle )
    {
        super( context, attrs, defStyle );
    }

    @Override
    protected void onLayout( boolean changed,
                             int left, int top, int right, int bottom )
    {
        super.onLayout( changed, left, top, right, bottom );
        if(changed)
        {
            getPaint().setShader( new LinearGradient(
                    0, 0,getWidth(),getHeight(),
                    Color.rgb(217, 6, 71), Color.rgb(235, 64, 44),
                    Shader.TileMode.CLAMP ) );
        }
    }
}
