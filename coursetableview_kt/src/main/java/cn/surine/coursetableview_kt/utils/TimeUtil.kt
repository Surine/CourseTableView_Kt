package cn.surine.coursetableview_kt.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

/**
 * Created by Surine on 2019/2/25.
 * 时间工具类
 */
object TimeUtil {
    const val yyyyMMdd = "yyyy-MM-dd"
    const val ONE_DAY = 24 * 60 * 60 * 1000

    /**
     * 当前周几？
     */
    val weekDay: Int
        get() {
            val w = Calendar.getInstance()[Calendar.DAY_OF_WEEK] - 1
            return if (w <= 0) 7 else w
        }

    /**
     * 当前几月？
     */
    val month: Int
        get() = Calendar.getInstance()[Calendar.MONTH] + 1

    val day: Int
        get() = Calendar.getInstance()[Calendar.DAY_OF_MONTH]


    /**
     * 获取日期
     *
     * @param format 格式
     */
    fun getDate(format: String = yyyyMMdd): String {
        val date = Date()
        @SuppressLint("SimpleDateFormat") val simpleDateFormat =
            SimpleDateFormat(format)
        return simpleDateFormat.format(date)
    }

    /**
     * 时间字符串比较大小
     * @param s1 时间1
     * @param s2 时间2
     * @param format 格式
     * @return 1:s1大于s2   -1:s1小于s2
     */
    fun compareDate(s1: String?, s2: String?, format: String?): Int {
        val sd = SimpleDateFormat(format)
        val sTime1: Long
        val sTime2: Long
        try {
            sTime1 = sd.parse(s1).time
            sTime2 = sd.parse(s2).time
            if (sTime1 < sTime2) {
                return -1
            }
            if (sTime1 == sTime2) {
                return 0
            }
            if (sTime1 > sTime2) {
                return 1
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }

    /**
     * 获取一个时间的前几天或者后几天
     * @param dateString 时间字符串
     * @param format 格式
     * @param number 几天？
     * @param isAfter 前几天还是后几天 true 为后，false为前
     * @return 处理后时间字符串
     */
    fun getDateBeforeOrAfter(
        dateString: String,
        format: String? = yyyyMMdd,
        number: Int
    ): String {
        val sdf = SimpleDateFormat(format)
        val date: Date
        val date1: Date
        try {
            date = sdf.parse(dateString)
            val calendar = Calendar.getInstance()
            calendar.time = date
            // 正数表示该日期后n天，负数表示该日期的前n天
            calendar.add(Calendar.DATE, number)
            date1 = calendar.time
            return sdf.format(date1)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dateString
    }

    /**
     * CourseTableView 日期计算工具
     * @param curTermStartDate 当前学期起始日期
     * @param currentWeek 选中的当前周
     * @param i 第 {currentWeek} 周 星期几（i）
     */
    fun getTodayInfoString(
        curTermStartDate: String,
        currentWeek: Int,
        i: Int
    ): String {
        val day = getDateBeforeOrAfter(
            curTermStartDate,
            yyyyMMdd,
            (currentWeek - 1) * 7 + (i - 1)
        )
        return getStringByTimeString(day, "dd")
    }

    /**
     * 月份个性提示串
     */
    fun getWeekInfoString(curTermStartDate: String, currentWeek: Int): String {
        val day = getDateBeforeOrAfter(
            curTermStartDate,
            yyyyMMdd,
            (currentWeek - 1) * 7
        )
        return getStringByTimeString(day, "MM")
    }

    /**
     * 获取某天是星期几
     */
    fun getWeekDayByString(curTermStartDate: String?): Int {
        val sd = SimpleDateFormat(yyyyMMdd)
        val c = Calendar.getInstance()
        var dayForWeek = 0
        try {
            c.time = sd.parse(curTermStartDate)
            dayForWeek = if (c[Calendar.DAY_OF_WEEK] == 1) {
                7
            } else {
                c[Calendar.DAY_OF_WEEK] - 1
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return dayForWeek
    }

    /**
     * 通过时间字符串解析时间值
     */
    fun getStringByTimeString(dateString: String, pattern: String?): String {
        val format = SimpleDateFormat(yyyyMMdd)
        val formatResult = SimpleDateFormat(pattern) //设置日期格式
        val date: Date
        return try {
            date = format.parse(dateString)
            formatResult.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            dateString
        }
    }

    /**
     * 看是否合法
     * @param curTermStartDate
     */
    fun isVaild(curTermStartDate: String?): Boolean {
        val sdf = SimpleDateFormat(yyyyMMdd)
        try {
            val date = sdf.parse(curTermStartDate)
            return true
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 获取当前真实周
     * @param curTermStartDate 开学日期
     * @return 返回负值代表开学日期还没到，这是开学前 i 周
     * 返回正值代表开学日期在过去，这是开学第 i 周
     */
    @Throws(ParseException::class)
    fun getRealWeek(curTermStartDate: String?): Int {
        if (isVaild(curTermStartDate)) {
            if (compareDate(curTermStartDate, "2000-1-1", yyyyMMdd) == 1) {
                return if (getWeekDayByString(curTermStartDate) == 1) {
                    val sd = SimpleDateFormat(yyyyMMdd)
                    //解析开学日期
                    val dateForTermStart = sd.parse(curTermStartDate)
                    val termStartTime = dateForTermStart.time
                    val curTime = Date().time
                    //开学日期在未来
                    if (termStartTime > curTime) {
                        val day = (termStartTime - curTime) / (24 * 60 * 60 * 1000)
                        -1 * (day / 7).toInt() + 1
                    } else {
                        //开学日期在过去
                        val day = (curTime - termStartTime) / (24 * 60 * 60 * 1000)
                        (day / 7).toInt() + 1
                    }
                } else {
                    throw IllegalArgumentException("this date is not monday")
                }
            }
        } else {
            throw IllegalArgumentException("please set a vaild time format : yyyy-MM-dd")
        }
        return 0
    }


    /**
     * get the real week
     * */
    fun getCurRealWeek(curTermStartDate: String): Int {
        if (!isVaild(curTermStartDate)) throw IllegalArgumentException("please set a vaild time format : yyyy-MM-dd")
        val termMills = getDateObj(curTermStartDate).time
        val curMills = Date().time
        return ((if(curMills - termMills < 0) -1 else 1) * abs(curMills - termMills) / ONE_DAY / 7 + 1).toInt()
    }


    fun getDateObj(dateStr: String, format: String = yyyyMMdd): Date {
        @SuppressLint("SimpleDateFormat") val simpleDateFormat =
            SimpleDateFormat(format)
        try {
            return simpleDateFormat.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Date()
    }

    @SuppressLint("SimpleDateFormat")
    fun getWeekDay(dateStr: String): Int {
        val f = SimpleDateFormat(yyyyMMdd)
        val cal = Calendar.getInstance()
        val date: Date
        try {
            date = f.parse(dateStr)
            cal.time = date
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val day = cal[Calendar.DAY_OF_WEEK]
        return if (day == 1) {
            7
        } else day - 1
    }
}