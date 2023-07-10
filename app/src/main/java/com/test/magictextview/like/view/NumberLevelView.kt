package com.test.magictextview.like.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.test.magictextview.like.factory.BitmapProvider
import com.test.magictextview.utils.PublicMethod

/**
 * 数字和等级文案view
 * @author xiaoman
 */
@SuppressLint("ViewConstructor")
class NumberLevelView @JvmOverloads constructor(
    context: Context,
    private val provider: BitmapProvider.Provider?,
    private val x: Int,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var textPaint: Paint = Paint()

    /**
     * 点击次数
     */
    private var mNumber = 0

    /**
     * 等级
     */
    private var level = 0


    /**
     * 数字图片的总宽度
     */
    private var offsetX = 0

    /**
     * x 初始位置
     */
    private var initialValue = 0

    /**
     * 默认数字和等级文案图片间距
     */
    private var spacing = 0

    init {
        textPaint.isAntiAlias = true
        initialValue = x - PublicMethod.dp2px(context, 120f)
        spacing = PublicMethod.dp2px(context, 10f)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val levelBitmap = provider?.getLevelBitmap(level) ?: return
        //等级图片的宽度
        val levelBitmapWidth = levelBitmap.width

        val dst = Rect()
        initialValue = x - levelBitmapWidth
        dst.left =  initialValue
        dst.right = initialValue + levelBitmapWidth
        dst.top = 0
        dst.bottom = levelBitmap.height
        //绘制等级文案图标
        canvas.drawBitmap(levelBitmap, null, dst, textPaint)
        offsetX = 0
        while (mNumber > 0) {
            val number = mNumber % 10
            val bitmap = provider.getNumberBitmap(number) ?: continue
            offsetX += bitmap.width
            //这里是数字
            val rect = Rect()
            rect.top = 0
            rect.bottom = bitmap.height
            rect.left = initialValue - spacing - offsetX
            rect.right = initialValue - spacing - offsetX + bitmap.width
            //绘制数字，依次绘制个位、十位、百位。
            canvas.drawBitmap(bitmap, null, rect, textPaint)
            mNumber /= 10
        }
    }

    /**
     *  点赞次数和点赞文案的关系
     *  20次以内是 次赞；20-80次是 太棒了；超过80次是 超赞同。
     */
    fun setNumber(number: Int) {
        this.mNumber = number
        if (mNumber > 999){
            mNumber = 999
        }
        level = when (mNumber) {
            in 1..20 -> {
                0
            }
            in 21..80 -> {
                1
            }
            else -> {
                2
            }
        }
        invalidate()
    }
}