package cn.surine.coursetableview_kt.data

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/14/20 14:23
 */
data class BCourse(
    val id: String,
    val scheduleId: Int,
    val name: String = "",
    val position: String = "",
    val teacher: String = "",
    val day: Int = 1,
    val startSection: Int,
    val continueSession:Int,
    val color: String,
    val week:List<Int>,
    val score:String
){
    fun getSimpleName(): String? {
        return if (name.length > 6) {
            name.substring(0, 6) + "..."
        } else name
    }
}
