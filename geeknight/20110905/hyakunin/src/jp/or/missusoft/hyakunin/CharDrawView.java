package jp.or.missusoft.hyakunin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CharDrawView extends View {
	private String mMessage = "";
	private Paint mPaint;
	private int width;
    private int height;
    int charSize;
    private static final int TOP_SPACE = 18;
    private static final int BOTTOM_SPACE = 18;
    private static final int FONT_SIZE = 30;

	public CharDrawView(Context context,AttributeSet attrs) {  
		  super(context,attrs);
		  // �����F  
		  mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	      mPaint.setTextSize(FONT_SIZE);
	      mPaint.setColor(Color.WHITE);
	} 
	
    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();
    }
	
	public void setText(String string) {
		this.mMessage = string;
	}
	
	protected void onDraw(Canvas canvas) { 
        float fontSpacing = mPaint.getFontSpacing();
//        float lineSpacing = fontSpacing * 2;
        float x = (width /5)  - fontSpacing;
        float y = TOP_SPACE + fontSpacing * 2;
        String[] s = mMessage.split("");
        boolean newLine = false;
        
		super.onDraw(canvas);
		
		for (int i = 1; i <= mMessage.length(); i++) {
            newLine = false;

            // �����`��
            canvas.drawText(s[i], x, y + oneCharHeight(fontSpacing,FONT_SIZE), mPaint);

            if (y + fontSpacing > height - BOTTOM_SPACE) {
                // ��������������Ȃ��ꍇ
                newLine = true;
            } else {
                // �܂�����������ꍇ
                newLine = false;
            }

            if (newLine) {
                // �I������
            	break;
            } else {
                // �����𑗂�
                y += fontSpacing;
            }
		}
	}
	/* 
	 * ���S�_����`�� 
	 */  
	private int oneCharHeight(float fontSpacing,int charSize) {  
	 return (int) ((fontSpacing / 2) + (charSize /2));  
	}            
		
}


