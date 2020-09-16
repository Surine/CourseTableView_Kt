package cn.surine.coursetableview_kt.data

/**
 * Intro：
 *
 * @author sunliwei
 * @date 9/14/20 14:28
 */
data class BTimeTable(
    val timeInfoList:List<BTimeInfo>
){
    data class BTimeInfo(
        val sessionNo:Int,
        val startTime:String,
        val endTime:String
    )
}
