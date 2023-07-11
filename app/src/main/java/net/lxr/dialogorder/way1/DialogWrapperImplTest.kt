package net.lxr.dialogorder.way1

import android.app.Activity
import android.app.AlertDialog

class DialogWrapperImplTest(override val priority: Int, private val activity: Activity) : DialogWrapper {
    override var next: DialogWrapper? = null

    override fun showDialog(onDismiss: () -> Unit) {
        AlertDialog.Builder(activity).run {
            setTitle("这是标题 $priority")
            setOnDismissListener { onDismiss() }

            create().show()
        }
    }
}