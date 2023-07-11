package net.lxr.dialogorder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import net.lxr.dialogorder.way1.DialogManager
import net.lxr.dialogorder.way1.DialogWrapperImplTest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDialog()
    }

    private fun initDialog() {
        Thread {
            DialogManager.enqueueDialogWrapper(DialogWrapperImplTest(1, this))
        }.start()

        Thread {
            Thread.sleep(6000)
            DialogManager.enqueueDialogWrapper(DialogWrapperImplTest(2, this))
        }.start()

        Thread {
            DialogManager.enqueueDialogWrapper(DialogWrapperImplTest(4, this))
        }.start()

        Thread {
            DialogManager.enqueueDialogWrapper(DialogWrapperImplTest(3, this))
        }.start()
    }
}