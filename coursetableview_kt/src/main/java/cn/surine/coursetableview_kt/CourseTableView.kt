package cn.surine.coursetableview_kt

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import cn.surine.coursetableview_kt.data.BCourse
import cn.surine.coursetableview_kt.data.BTimeTable
import cn.surine.coursetableview_kt.utils.Drawables
import cn.surine.coursetableview_kt.utils.TimeUtil
import kotlinx.android.synthetic.main.main_course_view.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/14/20 14:20
 */
class CourseTableView @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    private val defStyleAttr: Int = -1
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        const val SAT = 6
        const val SUN = 7
        const val MON = 1

        const val NORMAL = "NORMAL"
        const val CUR_WEEK = "CUR_WEEK"
        const val NOT_CUR_WEEK = "NOT_CUR_WEEK"

        const val NORMAL_SELECT_COLOR = "#4D64E6D7"
        const val NOR_CUR_WEEK_COLOR = "#8BBCBCBC"
    }

    private var curRealWeek: Int = 1
    private lateinit var curTermStartDate: String
    private lateinit var courseList: List<BCourse>
    private lateinit var bTimeTable: BTimeTable
    private var curConfigWeek: Int = 1

    var totalSession = 12
        set(value) {
            //300: for more interesting functions
            if (value > 300) {
                throw IllegalArgumentException("max session is less than 24")
            }
            field = value
        }
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
    var sessionSlotWidth: Int = 70   //Mainly used for auxiliary calculation
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
        set(value) {
            if (value == MON || value == SUN) {
                field = value
            } else {
                throw IllegalArgumentException("only accept 1 or 7")
            }
        }

    private var weekInfos = arrayOf("一", "二", "三", "四", "五", "六", "日")
    private var weekInfos2 = arrayOf("日", "一", "二", "三", "四", "五", "六")
    private val dayPanelList = mutableListOf<LinearLayout>()
    private val normalItemReferenceMap = HashMap<String, View>()
    val normalSelectItemReferenceMap = HashMap<String, View>()

    init {
        LayoutInflater.from(context).inflate(R.layout.main_course_view, this)
        dayPanelList.addAll(
            listOf(
                weekPanel_1,
                weekPanel_2,
                weekPanel_3,
                weekPanel_4,
                weekPanel_5,
                weekPanel_6,
                weekPanel_7
            )
        )
    }

    //jump to %d week
    fun toWeek(curWeek: Int) {
        this.curConfigWeek = curWeek
        render()
    }


    /**
     * initialize the common ui
     * */
    fun init(forceUpdate: Boolean = false, block: CourseTableView.() -> Unit): CourseTableView {
        this.block()
        if (forceUpdate) {
            render()
        }
        return this
    }


    /**
     * load the course data
     * */
    fun load(
        courseList: List<BCourse>,
        bTimeTable: BTimeTable,
        curTermStartDate: String,
        block: CourseTableView.() -> Unit = {}
    ) {
        this.courseList = courseList
        this.bTimeTable = bTimeTable
        this.curTermStartDate = curTermStartDate
        render()
        //after render
        this.block()
    }


    private fun render() {
        //init the section view
        initSectionView()
        //init the week panel
        initWeekPanelView()
        //init the normal item panel
        initNormalItemPanel()
        //init the course info
        initCourseInfo()
    }

    @SuppressLint("SimpleDateFormat")
    private fun initSectionView() {
        sectionView.layoutParams =
            LayoutParams(timeBarWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        sectionView.removeAllViews()
        //TODO:cur session info high light
        val curTime = SimpleDateFormat("HH:mm").format(Date())
        for (i in 1..totalSession) {
            sectionView.addView(TextView(context).apply {
                setTextColor(mainUiColor)
                textSize = sessionTextSize
                layoutParams = LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    sessionSlotHeight + sessionSlotTopMargin
                ).apply {
                    gravity = Gravity.CENTER
                }
                gravity = Gravity.CENTER
                //resizeable the time info text size
                val spannableString =
                    if (i < bTimeTable.timeInfoList.size) {
                        val startTime =
                            "\n${bTimeTable.timeInfoList[i - 1].startTime}".takeIf { isShowTimeTable && isShowTimeBarStartTime && bTimeTable.timeInfoList.isNotEmpty() }
                                ?: ""
                        val endTime =
                            "\n${bTimeTable.timeInfoList[i - 1].endTime}".takeIf { isShowTimeTable && isShowTimeBarEndTime && bTimeTable.timeInfoList.isNotEmpty() }
                                ?: ""
                        val no = if (isShowTimeBarNo) i.toString() else ""
                        SpannableString("$no${startTime}${endTime}")
                    } else {
                        SpannableString(if (isShowTimeBarNo) i.toString() else "")
                    }.apply {
                        try {
                            setSpan(
                                RelativeSizeSpan(0.8f), i.toString().length, length,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                        }catch (e:Exception){}
                    }
                text = spannableString
                gravity = Gravity.CENTER
                hookSessionView(i, this)
            })
        }
    }


    private var hookSessionView: (position: Int, textView: TextView) -> Unit = { _, _ -> }

    //for more interesting function
    fun hookSessionView(block: (position: Int, textView: TextView) -> Unit) {
        //hook
        this.hookSessionView = block
    }

    @SuppressLint("SetTextI18n")
    private fun initWeekPanelView() {
        weekView.removeAllViews()
        //the first view is INVISIBLE at present
        weekView.addView(TextView(context).apply {
            text = "${TimeUtil.getWeekInfoString(curTermStartDate, curConfigWeek)}\n月"
            gravity = Gravity.CENTER
            textSize = weekBarTextSize
            setTextColor(mainUiColor)
            layoutParams =
                LayoutParams(sessionSlotWidth, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    gravity = Gravity.CENTER
                }
            visibility = View.INVISIBLE
        })
        //due to the first day config, we should consider the offset of select index
        val curSelect = if (weekFirstDay == MON) TimeUtil.weekDay else (TimeUtil.weekDay + 1) % 7
        //init the week data
        for (i in 1..7) {
            weekView.addView(TextView(context).apply {
                layoutParams = LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.CENTER
                    weight = 1F //divide by max day
                }
                gravity = Gravity.CENTER
                val isCurSelect = i == curSelect
                setTextColor(if (isCurSelect) weekBarSelectWeekColor else mainUiColor)
                background = if (isCurSelect) {
                    this.setPadding(0, 5, 0, 5)
                    weekBarSelectBackground
                } else {
                    null
                }
                textSize = weekBarTextSize
                paint.isFakeBoldText = isCurSelect

                //calculate the date
                curRealWeek = TimeUtil.getCurRealWeek(curTermStartDate)
                val todayStr = TimeUtil.getDateBeforeOrAfter(
                    TimeUtil.getDate(),
                    number = i - curSelect - (curRealWeek - curConfigWeek) * 7
                )
                val today = TimeUtil.getStringByTimeString(
                    todayStr, "dd"
                )
                text = if (weekFirstDay == SUN) {
                    weekInfos2[i - 1]
                } else {
                    weekInfos[i - 1]
                } + "\n" + today

                dayPanelList[SAT - 1].visibility = View.VISIBLE
                dayPanelList[SUN - 1].visibility = View.VISIBLE
                if (hideSat) {
                    dayPanelList[SAT - 1].visibility = View.GONE
                    if (TimeUtil.getWeekDay(todayStr) == SAT) {
                        visibility = View.GONE
                    }
                }
                if (hideSun) {
                    dayPanelList[SUN - 1].visibility = View.GONE
                    if (TimeUtil.getWeekDay(todayStr) == SUN) {
                        visibility = View.GONE
                    }
                }
                hookWeekView(i, this, i == curSelect)
            })
        }
    }

    private var hookWeekView: (position: Int, textView: TextView, isSelect: Boolean) -> Unit =
        { _, _, _ -> }

    //for more interesting function
    fun hookWeekView(block: (position: Int, textView: TextView, isSelect: Boolean) -> Unit) {
        //hook
        this.hookWeekView = block
    }


    private fun initNormalItemPanel() {
        normalItemReferenceMap.clear()
        normalSelectItemReferenceMap.clear()
        dayPanelList.forEachIndexed { index, it ->
            it.removeAllViews()
            it.setPadding(sessionSlotExternalPadding, 0, sessionSlotExternalPadding / 2, 0)
            for (i in 0 until totalSession) {
                val vFrame = FrameLayout(context).apply {
                    layoutParams = LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        sessionSlotHeight
                    ).apply {
                        setMargins(0, sessionSlotTopMargin, 0, 0)
                    }
                }
                //use NORMAL tag mark the normal item view
                vFrame.tag = "$NORMAL-$index-$i"
                hookNormalItemView(vFrame, vFrame.tag as String, false)

                val listener = fun() {
                    val status: Boolean
                    if (normalSelectItemReferenceMap.containsKey(vFrame.tag as String)) {
                        status = false
                        normalSelectItemReferenceMap.remove(vFrame.tag as String)
                    } else {
                        normalSelectItemReferenceMap[vFrame.tag as String] = vFrame
                        status = true
                    }
                    vFrame.background = Drawables.getDrawable(
                        if (status) Color.parseColor(NORMAL_SELECT_COLOR)
                        else Color.TRANSPARENT,
                        20,
                        0,
                        mainUiColor
                    )
                    hookNormalItemView(vFrame, vFrame.tag as String, status)
                }

                vFrame.setOnClickListener {
                    listener()
                    //callback and cover the normal config
                    onItemClick(it, null, vFrame.tag as String, true)
                }
                vFrame.setOnLongClickListener {
                    listener()
                    onItemLongClick(it, null, vFrame.tag as String, true)
                    true
                }
                //backup
                normalItemReferenceMap[vFrame.tag as String] = vFrame
                it.addView(vFrame)
            }
        }
    }

    private var hookNormalItemView: (view: View, tag: String, isSelect: Boolean) -> Unit =
        { _, _, _ -> }

    //for more interesting function
    fun hookNormalItemView(block: (view: View, tag: String, isSelect: Boolean) -> Unit) {
        //hook
        this.hookNormalItemView = block
    }


    @SuppressLint("SetTextI18n")
    private fun initCourseInfo() {
        for (index in courseList.indices) {
            val bCourse = courseList[index]
            val vFrame = FrameLayout(context).apply {
                val realHeight =
                    sessionSlotHeight * bCourse.continueSession + (bCourse.continueSession - 1) * sessionSlotTopMargin
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, realHeight).apply {
                    setMargins(0, sessionSlotTopMargin, 0, 0)
                }
                val tagPrefix = if (bCourse.week.contains(curRealWeek)) CUR_WEEK else NOT_CUR_WEEK
                tag = "$tagPrefix-${bCourse.id}"
                background = Drawables.getDrawable(
                    Color.parseColor(if (isCurWeek(tagPrefix)) bCourse.color else NOR_CUR_WEEK_COLOR),
                    sessionSlotRadius,
                    sessionSlotStrokeWidth,
                    sessionSlotStrokeColor
                )

                //add the course info
                addView(TextView(context).apply {
                    layoutParams =
                        LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                    setTextColor(if (isCurWeek(tagPrefix)) sessionSlotCurWeekTextColor else sessionSlotNotCurWeekTextColor)
                    setPadding(
                        sessionSlotInternalPadding,
                        sessionSlotInternalPadding,
                        sessionSlotInternalPadding,
                        sessionSlotInternalPadding
                    )
                    gravity = sessionSlotTextGravity
                    paint.isFakeBoldText = true
                    textSize = sessionTextSize
                    val str =
                        "${bCourse.name}@${bCourse.position}\n${if (isShowTeacher) bCourse.teacher else ""}${if (isCurWeek(
                                tagPrefix
                            )
                        ) "" else notCurWeekText}"
                    text = str
                })
            }
            vFrame.setOnClickListener {
                onItemClick(it, bCourse, vFrame.tag as String, false)
            }
            vFrame.setOnLongClickListener {
                onItemLongClick(it, bCourse, vFrame.tag as String, false)
                true
            }

            dayPanelList[bCourse.day - 1].apply {
                //if current position has course,skip cover
                var deleteAccess = false
                for (i in bCourse.startSection - 1 until bCourse.continueSession + bCourse.startSection) {
                    if ((getChildAt(i).tag as String).startsWith(CUR_WEEK)) {
                        deleteAccess = true
                    }
                }
                if (!deleteAccess) {
                    hookCourseItemView(
                        vFrame,
                        vFrame.tag as String,
                        (vFrame.tag as String).startsWith(CUR_WEEK)
                    )
                    if (isNotCurWeek(vFrame.tag as String)) {
                        if (isShowNotCurWeekCourse) {
                            removeViews(bCourse.startSection - 1, bCourse.continueSession)
                            addView(vFrame, bCourse.startSection - 1)
                        }
                    } else {
                        removeViews(bCourse.startSection - 1, bCourse.continueSession)
                        addView(vFrame, bCourse.startSection - 1)
                    }
                }
            }
        }
        //handle the first day
        if (weekFirstDay == SUN) {
            val view = mainContent[mainContent.childCount - 1]
            mainContent.apply {
                removeViewAt(mainContent.childCount - 1)
                mainContent.addView(view, 1)
            }
            val value = dayPanelList.removeAt(dayPanelList.size - 1)
            dayPanelList.add(0, value)
        }
    }


    private var hookCourseItemView: (view: View, tag: String, isCurWeek: Boolean) -> Unit =
        { _, _, _ -> }

    //for more interesting function
    fun hookCourseItemView(block: (view: View, tag: String, isCurWeek: Boolean) -> Unit) {
        //hook
        this.hookCourseItemView = block
    }

    private var onItemClick: (view: View, courseInfo: BCourse?, tag: String, normalItem: Boolean) -> Unit =
        { _, _, _, _ -> }

    fun itemClicker(block: (view: View, courseInfo: BCourse?, tag: String, normalItem: Boolean) -> Unit) {
        this.onItemClick = block
    }

    private var onItemLongClick: (view: View, courseInfo: BCourse?, tag: String, normalItem: Boolean) -> Unit =
        { _, _, _, _ -> }

    fun itemLongClicker(block: (view: View, courseInfo: BCourse?, tag: String, normalItem: Boolean) -> Unit) {
        this.onItemClick = block
    }


    fun isCurWeek(tag: String) = tag.startsWith(CUR_WEEK)
    fun isNotCurWeek(tag: String) = tag.startsWith(NOT_CUR_WEEK)
    fun isNormal(tag: String) = tag.startsWith(NORMAL)
}