package cn.surine.coursetableview_kt.utils

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable

/**
 * Intro：创建drawable
 *
 * @author sunliwei
 * @date 2020-01-29 17:24
 */
object Drawables {
    /**
     * 创建一个常规的shape形状
     *
     * @param color       填充颜色
     * @param corner      圆角
     * @param strokeWidth 描边宽度
     * @param strokeColor 描边颜色
     */
    fun getDrawable(
        color: Int,
        corner: Int,
        strokeWidth: Int,
        strokeColor: Int
    ): Drawable {
        val gradientDrawable = GradientDrawable()
        gradientDrawable.setColor(color)
        gradientDrawable.cornerRadius = corner.toFloat()
        gradientDrawable.setStroke(strokeWidth, strokeColor)
        return gradientDrawable
    }

    /**
     * 创建一个渐变shape形状
     * @param orientation 方向
     * @param colors 颜色值
     * @param strokeColor 描边颜色
     * @param corner 圆角
     * @param strokeWidth 描边宽度
     */
    fun getDrawable(
        orientation: GradientDrawable.Orientation?,
        colors: IntArray?,
        corner: Int,
        strokeWidth: Int,
        strokeColor: Int
    ): Drawable {
        val gradientDrawable = GradientDrawable(orientation, colors)
        gradientDrawable.cornerRadius = corner.toFloat()
        gradientDrawable.setStroke(strokeWidth, strokeColor)
        return gradientDrawable
    }
}