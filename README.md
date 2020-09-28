# CourseTableView_Kt  
> CourseTableView Kotlin Version， for more interesting function

### Use
Step 1. Add the JitPack repository to your build file
```
allprojects {
repositories {
...
maven { url 'https://jitpack.io' }
}
}
```
Step 2. Add the dependency


[![](https://jitpack.io/v/Surine/CourseTableView_Kt.svg)](https://jitpack.io/#Surine/CourseTableView_Kt)
```
dependencies {
implementation 'com.github.Surine:CourseTableView_Kt:Tag'
}
```

Step 3. Add the view to your layout
```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
xmlns:tools="http://schemas.android.com/tools"
android:layout_width="match_parent"
android:layout_height="match_parent"
tools:context=".MainActivity">

<cn.surine.coursetableview_kt.CourseTableView
  android:id="@+id/courseTableView"
  android:layout_width="match_parent"
  android:layout_height="0dp"
  app:layout_constraintBottom_toBottomOf="parent"
  app:layout_constraintEnd_toStartOf="@+id/button"
  app:layout_constraintStart_toStartOf="parent"
  app:layout_constraintTop_toTopOf="parent">

</cn.surine.coursetableview_kt.CourseTableView>
</androidx.constraintlayout.widget.ConstraintLayout>
```
Step 4. load data
the init() is for the ui and click listener,and the load() is for courses data
```
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
     //change attrs value
      //for click listener
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
```
This is the definition of the load method.
```
fun load(
  courseList: List<BCourse>,
  bTimeTable: BTimeTable,
  curTermStartDate: String,
  block: CourseTableView.() -> Unit = {}
) 
```

### Attrs
```
  var totalSession = 12
  var totalWeek: Int = 30
  var timeBarWidth: Int = 60
  var isShowTimeBarNo = true
  var isShowTimeBarStartTime = true
  var isShowTimeBarEndTime = true
  var mainUiColor = Color.BLACK
  var hideSat: Boolean = false
  var hideSun: Boolean = false
  var weekBarSelectBackground: Drawable? = null
  var weekBarSelectWeekColor = Color.BLUE
  var sessionTextSize = 12F
  var weekBarTextSize = 16F
  var sessionSlotTextGravity = Gravity.CENTER
  var sessionSlotWidth: Int = 70  
  var sessionSlotHeight: Int = 200
  var sessionSlotTopMargin = 3
  var sessionSlotStrokeWidth = 3
  var sessionSlotStrokeColor = Color.WHITE
  var sessionSlotRadius = 20
  var sessionSlotCurWeekTextColor = Color.WHITE
  var sessionSlotNotCurWeekTextColor = Color.BLACK
  var isShowTimeTable = true
  var sessionSlotExternalPadding = 5
  var sessionSlotInternalPadding = 5
  var isShowTeacher: Boolean = false
  var isShowNotCurWeekCourse = false
  var notCurWeekText = "[非本周]"
  var weekFirstDay = 1
```

### listener and hook

Click Listener: We provide click listener and long press listener,and you can get the view,course information,the tag and other additional information.
Tag is the value that marks the course grid type，
> NORMAL-x-y  normal item with postion (x,y) <br>
> CUR_WEEK-courseid  current week course  <br>
> NOT_CUR_WEEK-courseid  not current week course <br>


```
//click
fun itemClicker(block: (view: View, courseInfo: BCourse?, tag: String, normalItem: Boolean) -> Unit) {
    this.onItemClick = block
}
//long click
 fun itemLongClicker(block: (view: View, courseInfo: BCourse?, tag: String, normalItem: Boolean) -> Unit) {
      this.onItemClick = block
  }
```

Hook Methods : In the process of rendering the ui, some views are given the ability to be modified，you can try to customize your advanced UI in these methods,
After the method call is over, they will be added to the user interface.
```
//hook session view
fun hookSessionView(block: (position: Int, textView: TextView) -> Unit) {
    //hook
    this.hookSessionView = block
 }
//hook week view   
fun hookWeekView(block: (position: Int, textView: TextView, isSelect: Boolean) -> Unit) {
    //hook
    this.hookWeekView = block
}
//hook normal item view (normal item ui)
fun hookNormalItemView(block: (view: View, tag: String, isSelect: Boolean) -> Unit) {
    //hook
    this.hookNormalItemView = block
}
//hook course item view (week courses)
fun hookCourseItemView(block: (view: View, tag: String, isCurWeek: Boolean) -> Unit) {
    //hook
    this.hookCourseItemView = block
}
```

### LISENCE

```
MIT License

Copyright (c) 2020 Surine

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

