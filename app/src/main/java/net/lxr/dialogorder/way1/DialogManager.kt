package net.lxr.dialogorder.way1

import android.os.Handler
import android.os.Looper

/**
 * 写Dialog的只需要关心自己的Dialog是否显示，数据以及优先级，然后创建一个DialogWrapper,然后入队列即可
 */
object DialogManager {
    private const val STATUS_NOT_SHOW = 0
    private const val STATUS_SHOWING = 1

    private var head: DialogWrapper? = null
    private val LOCK = Any()
    private var status = STATUS_NOT_SHOW

    private val mainHandler = Handler(Looper.getMainLooper())

    /**
     * 新弹窗入队，按优先级排序，相同优先级按插入先后排序
     */
    fun enqueueDialogWrapper(dialogWrapper: DialogWrapper) {
        if (dialogWrapper.priority <= 0) {
            throw IllegalArgumentException("DialogWrapper's priority mast greater than 0")
        }

        synchronized(LOCK) {
            when {
                head == null -> {
                    head = dialogWrapper
                }
                dialogWrapper.priority > (head?.priority ?: 0) -> {
                    dialogWrapper.next = head
                    head = dialogWrapper
                }
                else -> {
                    // 将dialogWrapper按优先级插入到具体的位置，优先级一致则按插入先后排序
                    var curDialogWrapper = head
                    var next = curDialogWrapper?.next
                    while (curDialogWrapper != null) {
                        if (dialogWrapper.priority <= curDialogWrapper.priority && dialogWrapper.priority > (next?.priority
                                ?: 0)
                        ) {
                            curDialogWrapper.next = dialogWrapper
                            dialogWrapper.next = next
                            break
                        } else {
                            curDialogWrapper = curDialogWrapper.next
                            next = curDialogWrapper?.next
                        }
                    }
                }
            }

            notifyToShow()
        }
    }

    /**
     * 通知该显示弹窗了
     */
    fun notifyToShow() {
        mainHandler.post {
            showNext()
        }
    }

    /**
     * 显示弹窗
     */
    private fun showNext() {
        if (status == STATUS_SHOWING || head == null) {
            return
        }

        status = STATUS_SHOWING
        head?.run {
            showDialog {
                status = STATUS_NOT_SHOW
                notifyToShow()
            }

            head = next
        }
    }
}