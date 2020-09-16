package cn.surine.coursetableview_kt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.surine.coursetableview_kt.data.BCourse
import cn.surine.coursetableview_kt.data.BTimeTable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bTimeTable = BTimeTable(
            listOf(
                BTimeTable.BTimeInfo(1, "8:00", "9:00"),
                BTimeTable.BTimeInfo(2, "10:00", "11:00"),
                BTimeTable.BTimeInfo(3, "11:00", "12:00"),
                BTimeTable.BTimeInfo(4, "1:00", "2:00"),
                BTimeTable.BTimeInfo(5, "2:00", "3:00"),
                BTimeTable.BTimeInfo(6, "4:00", "5:00"),
                BTimeTable.BTimeInfo(7, "5:00", "6:00"),
                BTimeTable.BTimeInfo(8, "7:00", "8:00"),
                BTimeTable.BTimeInfo(9, "8:00", "10:00"),
                BTimeTable.BTimeInfo(10, "10:00", "11:00")
            )
        )

        courseTableView.init {
            totalSession = 12
            timeBarWidth = 80
            sessionSlotHeight = 200
//            isShowTimeBarNo = true
//            isShowTimeBarStartTime = false
//            hideSat = true
//            hideSun = true
            weekFirstDay = 7
//            isShowTimeBarEndTime = true
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
                    "1", 1, name = "Advanced Mathematics", position = "8-210", teacher = "Mr.z",
                    day = 1, startSection = 1, continueSession = 2, color = "#2196F3",
                    week = listOf(1, 3, 5, 7, 9), score = "1.0"
                ),
                BCourse(
                    "2", 1, name = "College English", position = "8-221", teacher = "Mr.l",
                    day = 3, startSection = 1, continueSession = 3, color = "#009688",
                    week = listOf(2, 4, 6, 8, 10), score = "1.0"
                ),
                BCourse(
                    "3", 1, name = "Liner Algebra", position = "9-121", teacher = "Mr.z",
                    day = 2, startSection = 3, continueSession = 1, color = "#FF5722",
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