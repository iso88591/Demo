package grg.research.demo.holderview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.ArrayList

class HolderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    companion object {
        const val TYPE_RECT = 0
        const val TYPE_ROUND = 1
    }

    private var isDrawHolder = false
        set(value) {
            field = value
            invalidate()
        }
    var viewids = arrayOf<Int>()

    //key :id  value: 要画的形状 是方形还是其他什么形状
    private val shapeMapping = HashMap<Int, Int>()

    private val rectList = ArrayList<Rect>()
    private val pathList = ArrayList<Path>()

    val draw = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    fun appointShape(viewId: Int, type: Int) {
        shapeMapping[viewId] = type
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        rectList.clear()
        pathList.clear()
        findViewSizeAndPositions(this, 0, 0)
    }


    private fun findViewSizeAndPositions(viewGroup: ViewGroup, offsetX: Int, offsetY: Int) {
        val childCount = viewGroup.childCount

        for (i in 0 until childCount) {
            val view = viewGroup.getChildAt(i)
            if (view.id in viewids) {
                handleDrawData(view, offsetX, offsetY)
            } else if (view is ViewGroup) {
                findViewSizeAndPositions(view, offsetX + view.left, offsetY + view.top)
            }
        }
    }

    private fun handleDrawData(view: View, offsetX: Int, offsetY: Int) {
        val type = shapeMapping[view.id]
        when (type) {
            TYPE_ROUND -> {
                pathList.add(Path().apply {
                    addRoundRect(
                        RectF(
                            (view.left + offsetX).toFloat(),
                            (view.top + offsetY).toFloat(),
                            (view.right + offsetX).toFloat(),
                            (view.bottom + offsetY).toFloat()
                        ),
                        view.width / 2f, view.height / 2f, Path.Direction.CCW
                    )
                })
            }
            else -> {
                rectList.add(
                    Rect(
                        view.left + offsetX,
                        view.top + offsetY,
                        view.right + offsetX,
                        view.bottom + offsetY
                    )
                )
            }
        }
    }

    fun showLoading() {
        isDrawHolder = true
    }

    fun dismissLoading() {
        isDrawHolder = false
    }

    fun toggle() {
        isDrawHolder = !isDrawHolder
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        if (isDrawHolder) {

            canvas?.apply {

                rectList.forEach {
                    drawRect(it, draw)
                }

                pathList.forEach {
                    drawPath(it, draw)
                }

            }

        }
    }

}

