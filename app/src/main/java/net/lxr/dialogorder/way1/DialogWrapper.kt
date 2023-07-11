package net.lxr.dialogorder.way1

interface DialogWrapper {
    val priority: Int // 当前弹窗的优先级（必须大于0）
    var next: DialogWrapper?  // 下一个弹窗
    fun showDialog(onDismiss: () -> Unit) // 显示弹窗的具体方法，onDismiss在弹窗消失时调用
}