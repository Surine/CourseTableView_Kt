package cn.surine.coursetableview_kt

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.surine.coursetableview_kt.data.BCourse
import cn.surine.coursetableview_kt.data.BTimeTable
import cn.surine.coursetableview_kt.utils.Drawables
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bTimeTable = BTimeTable(
            listOf(
                BTimeTable.BTimeInfo(1, "8:00", "10:00"),
                BTimeTable.BTimeInfo(2, "8:00", "10:00"),
                BTimeTable.BTimeInfo(3, "8:00", "10:00"),
                BTimeTable.BTimeInfo(4, "8:00", "10:00"),
                BTimeTable.BTimeInfo(5, "8:00", "10:00")
            )
        )

        courseTableView.init {
            totalSession = 12
            timeBarWidth = 80
            sessionSlotHeight = 200
            isShowTimeBarNo = true
            isShowTimeBarStartTime = false
//            hideSat = true
//            hideSun = true
            weekFirstDay = 7
            isShowTimeBarEndTime = true
//            isShowNotCurWeekCourse = true
            itemClicker { view, course, tag, normal ->
                Toast.makeText(this@MainActivity, "click $tag, is $normal", Toast.LENGTH_SHORT)
                    .show()
            }
//            hookNormalItemView { view, tag, isSelect ->
//                if (isSelect) {
//                    view.background = Drawables.getDrawable(
//                        Color.parseColor("#852196F3"),
//                        20,
//                        3,
//                        Color.WHITE
//                    )
//                } else {
//                    view.background = null
//                }
//            }
//            hookCourseItemView { view, tag, isCurWeek ->
//                if (!isCurWeek) {
//                    view.background = Drawables.getDrawable(
//                        Color.parseColor("#852196F3"),
//                        180,
//                        3,
//                        Color.WHITE
//                    )
//                }
//            }
        }.load(
            listOf(
                BCourse(
                    "1", 1, name = "这是一段非常长的文本非常长的文本", position = "teda", teacher = "Mr.z",
                    day = 1, startSection = 1, continueSession = 2, color = "#333333",
                    week = listOf(1, 3, 5, 7, 9), score = "1.0"
                ),
                BCourse(
                    "2", 1, name = "Android", position = "teda", teacher = "Mr.z",
                    day = 3, startSection = 1, continueSession = 3, color = "#333333",
                    week = listOf(2, 4, 6, 8, 10), score = "1.0"
                ),
                BCourse(
                    "3", 1, name = "English", position = "teda", teacher = "Mr.z",
                    day = 2, startSection = 3, continueSession = 1, color = "#333333",
                    week = listOf(1, 3, 5, 7, 9), score = "1.0"
                )
            ), bTimeTable, "2020-9-15"
        )

        button.setOnClickListener {
            courseTableView.init(true) {
                isShowNotCurWeekCourse = !isShowNotCurWeekCourse
            }
//            courseTableView.toWeek(2)
        }

    }
}