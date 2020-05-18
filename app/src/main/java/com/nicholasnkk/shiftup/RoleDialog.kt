package com.nicholasnkk.shiftup

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.pes.androidmaterialcolorpickerdialog.ColorPicker


class RoleDialog : DialogFragment() {
    var selectedColor: Int = Color.BLACK

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.role_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEt = view.findViewById<EditText>(R.id.dialogRoleName)
        val startEt = view.findViewById<EditText>(R.id.dialogRoleStartTime)
        val endEt = view.findViewById<EditText>(R.id.dialogRoleEndTime)
        val colorBtn = view.findViewById<Button>(R.id.dialogSelectColorButton)
        val doneBtn = view.findViewById<Button>(R.id.dialogAddButton)

        colorBtn.setBackgroundColor(selectedColor)
        colorBtn.setTextColor(Color.WHITE)
        colorBtn.setOnClickListener {
            val cp = ColorPicker(
                activity,
                Color.red(selectedColor),
                Color.blue(selectedColor),
                Color.green(selectedColor)
            )
            cp.show()
            cp.enableAutoClose()
            cp.setCallback { color ->
                selectedColor = color
                colorBtn.setBackgroundColor(color)
                if (selectedColor == Color.BLACK)
                    colorBtn.setTextColor(Color.WHITE)
                else
                    colorBtn.setTextColor(Color.BLACK)
            }
        }

        doneBtn.setOnClickListener {
            val dialogListener = activity as DialogListener
            dialogListener.onFinishEditDialog(
                nameEt.text.toString(),
                startEt.text.toString(),
                endEt.text.toString(),
                selectedColor
            )
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val width = resources.displayMetrics.widthPixels - resources.getDimensionPixelSize(R.dimen.role_dialog_total_width_margin)
        val height = resources.getDimensionPixelSize(R.dimen.role_dialog_height)
        dialog?.window?.setLayout(width, height)
    }

    interface DialogListener {
        fun onFinishEditDialog(name: String, startTime: String, endTime: String, color: Int)
    }
}