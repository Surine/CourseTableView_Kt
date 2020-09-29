package cn.surine.coursetableview_kt.data

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/14/20 14:23
 */
data class BCourse(
    val id: String,
    var scheduleId: Long,
    var name: String = "",
    var position: String = "",
    var teacher: String = "",
    var day: Int = 1,
    var startSection: Int,
    var continueSession:Int,
    var color: String,
    var week:List<Int>,
    var score:String
){
    fun getSimpleName(): String? {
        return if (name.length > 6) {
            name.substring(0, 6) + "..."
        } else name
    }
}
