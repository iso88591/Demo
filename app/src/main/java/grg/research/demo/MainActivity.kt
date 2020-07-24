package grg.research.demo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import grg.research.demo.holderview.HolderView
import grg.research.demo.looperview.LooperViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val inflater by lazy { LayoutInflater.from(this) }

    val adapter = object : LooperViewAdapter() {

        val data = listOf<List<String>>(
            listOf("1", "2", "3", "4", "5"),
            listOf("21", "22", "23", "24", "25"),
            listOf("31", "32", "33", "34", "35"),
            listOf("41", "42", "43", "44", "45")
        )

        override fun onCreateView(context: Context, position: Int): View {
            return inflater.inflate(R.layout.bug_send,null,false).apply {

                val d = data.get(position)

                val views = listOf<TextView>(
                    findViewById<TextView>(R.id.textView),
                    findViewById<TextView>(R.id.textView2),
                    findViewById<TextView>(R.id.textView3),
                    findViewById<TextView>(R.id.textView4),
                    findViewById<TextView>(R.id.textView5)
                )

                for (i in 0 until views.size){
                    views[i].text = d.get(i)
                }

            }
        }

        override fun itemCount(): Int {
            return data.size
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        holderView.viewids= arrayOf(
            R.id.textView,
            R.id.textView2,
            R.id.textView3,
            R.id.tv1
        )
        holderView.appointShape(R.id.tv1,HolderView.TYPE_ROUND)

        looperView.adapter = adapter
        looperView.setOnClickListener {

//            looperView.start()
            holderView.toggle()


        }

        holderView

    }

}