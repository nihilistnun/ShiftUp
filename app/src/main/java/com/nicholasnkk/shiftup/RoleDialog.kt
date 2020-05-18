package com.nicholasnkk.shiftup

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import java.text.SimpleDateFormat
import java.util.*


class RoleDialog : DialogFragment() {
    var rid: String = ""
    var name: String = ""
    var startTime: String = "00:00"
    var endTime: String = "00:00"
    var selectedColor: Int = Color.BLACK

    fun populate(role: Role) {
        rid = role.rid
        name = role.name
        startTime = role.startTime
        endTime = role.endTime
        selectedColor = role.color
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.role_dialog, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEt = view.findViewById<EditText>(R.id.dialogRoleName)
        val startView = view.findViewById<TextView>(R.id.startTimeView)
        val endView = view.findViewById<TextView>(R.id.endTimeView)
        val startBtn = view.findViewById<Button>(R.id.editStartTime)
        val endBtn = view.findViewById<Button>(R.id.editEndTime)
        val colorBtn = view.findViewById<Button>(R.id.dialogSelectColorButton)
        val doneBtn = view.findViewById<Button>(R.id.dialogAddButton)

        if (name.isNotEmpty())
            nameEt.setText(name)

        colorBtn.setBackgroundColor(selectedColor)
        colorBtn.setTextColor(Color.WHITE)
        colorBtn.setOnClickListener {
            val cp = ColorPicker(
                activity,
                Color.red(selectedColor),
                Color.green(selectedColor),
                Color.blue(selectedColor)
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
        //update texts
        val sInitialText = "Start time: $startTime"
        startView.text = sInitialText
        val eInitialText = "End time: $endTime"
        endView.text = eInitialText
        //button listeners
        startBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            val startListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                startTime = SimpleDateFormat("HH:mm").format(cal.time)
                val text = "Start time: $startTime"
                startView.text = text
                //update end cal
            }
            TimePickerDialog(
                activity,
                startListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }
        endBtn.setOnClickListener {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            val startListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                endTime = SimpleDateFormat("HH:mm").format(cal.time)
                val text = "End time: $endTime"
                endView.text = text
            }
            TimePickerDialog(
                activity,
                startListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
        }

        doneBtn.setOnClickListener {
            val dialogListener = activity as DialogListener
            dialogListener.onFinishEditDialog(
                rid,
                nameEt.text.toString(),
                startTime,
                endTime,
                selectedColor
            )
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val width =
            resources.displayMetrics.widthPixels - resources.getDimensionPixelSize(R.dimen.role_dialog_total_width_margin)
        val height = resources.getDimensionPixelSize(R.dimen.role_dialog_height)
        dialog?.window?.setLayout(width, height)
    }

    interface DialogListener {
        fun onFinishEditDialog(rid:String, name: String, startTime: String, endTime: String, color: Int)
    }
}