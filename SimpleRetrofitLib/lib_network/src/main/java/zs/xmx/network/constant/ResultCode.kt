package zs.xmx.network.constant

/*
 * @创建者     默小铭
 * @博客       http://blog.csdn.net/u012792686
 * @本类描述	  请求结果码常量类
 * @内容说明   这里的结果和服务器后端沟通
 *
 */
class ResultCode {
    companion object {
        const val Success = 100
        const val DisConnect = 403//与服务器断开连接
        const val OtherException = 405//其他异常
    }
}