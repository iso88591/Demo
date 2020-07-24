package grg.research.demo.looperview

import android.animation.Animator
import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintSet
import grg.research.demo.R

class LooperView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var adapter: LooperViewAdapter? = null
        set(value) {
            field = value
            value?.let {
                removeAllViews()
                currentViewPosition = 0
                for (i in 0 until it.itemCount()) {
                    val view = it.onCreateView(context, i)
                    addView(view)
                }
                requestLayout()
            }
        }

    //循环更新体
    lateinit var run: Runnable

    private var moveTime = 500L
    private var moveDivideTime = 5000L

    //记录当前位置
    private var currentViewPosition = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (i in 0 until childCount) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (i == currentViewPosition) {
                view.layout(left, top, right, bottom)
            } else {
                view.layout(left, top, right, bottom)
                view.y = bottom + height.toFloat()
            }
        }
    }


    fun start() {
        this.handler.removeCallbacks(run)
        this.handler.postDelayed(run, moveTime + moveDivideTime)
    }

    fun moveNext() {
        if (currentViewPosition >= childCount - 1) {
            moveToTargetPosition(0)
        } else {
            moveToTargetPosition(currentViewPosition + 1)
        }
    }

    fun moveToTargetPosition(targetPosition: Int) {

        if (targetPosition >= childCount) return

        if (targetPosition == currentViewPosition) return

        val lastPosition = currentViewPosition
        currentViewPosition = targetPosition

        entryIn(targetPosition)
        entryOut(lastPosition)


    }

    private fun entryIn(position: Int) {
        getChildAt(position)?.let {
            it.animate()
                .translationY(0f)
                .setDuration(moveTime)
                .setListener(null)
                .start()
        }
    }

    private fun entryOut(position: Int) {
        val view = getChildAt(position)

        view.animate()
            .translationY(-height.toFloat())
            .setDuration(moveTime)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {

                }

                override fun onAnimationEnd(p0: Animator?) {
                    view.y = bottom + height.toFloat()
                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationStart(p0: Animator?) {

                }

            })

    }


    init {

        if (attrs != null) {
            val a =
                context.obtainStyledAttributes(attrs, R.styleable.LooperView)
            val N = a.indexCount
            for (i in 0 until N) {
                val attr = a.getIndex(i)
                when (attr) {
                    R.styleable.LooperView_viewMoveDivideTime -> {
                        moveDivideTime = a.getInt(attr, moveDivideTime.toInt()).toLong()
                    }
                    R.styleable.LooperView_viewMoveTime -> {
                        moveTime = a.getInt(attr, moveTime.toInt()).toLong()
                    }
                    else -> {

                    }
                }

            }
            a.recycle()
        }

        run = Runnable {
            moveNext()
            this.handler.postDelayed(run, moveTime + moveDivideTime)
        }

    }

}


abstract class LooperViewAdapter {
    abstract fun onCreateView(context: Context, position: Int): View
    abstract fun itemCount(): Int
}