package com.nicholasnkk.shiftup

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.nicholasnkk.shiftup.activities.MainActivity
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class ShiftDialog : DialogFragment() {
    lateinit var shift: Shift

    var delete: Boolean = false

    fun populate(shift: Shift) {
        this.shift = shift
    }

    fun toogleDelete() {
        delete = !delete
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shift_dialog, container, false)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val main: MainActivity = activity as MainActivity

        val title: TextView = view.findViewById(R.id.shift_title)
        val daySpinner: Spinner = view.findViewById(R.id.day_spinner)
        val roleText: TextView = view.findViewById(R.id.role_text)
        val roleSpinner: Spinner = view.findViewById(R.id.role_spinner)
        val doneBtn: Button = view.findViewById(R.id.dialogAddButton)
        val days = resources.getStringArray(R.array.days)
        val roles: Array<String?> = arrayOfNulls(main.groupRoles.size)
        var cal: Calendar = Calendar.getInstance()

        if (delete) {
            roleSpinner.visibility = View.GONE
            roleText.visibility = View.GONE
            title.text = getString(R.string.delete_shift)
            doneBtn.setText(R.string.delete)
        } else {
            roleSpinner.visibility = View.VISIBLE
            roleText.visibility = View.VISIBLE
            title.setText(R.string.add_shift)
            doneBtn.setText(R.string.add)
        }

        //get next week
        cal.add(Calendar.DAY_OF_YEAR, 7)
        cal.firstDayOfWeek = Calendar.MONDAY
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        main.groupRoles.forEachIndexed { index, role ->
            roles[index] = role.name
        }

        val dayAdapter = ArrayAdapter(
            activity as Context,
            R.layout.padded_spinner_item, days
        )
        daySpinner.adapter = dayAdapter

        val roleAdapter = ArrayAdapter(
            activity as Context,
            R.layout.padded_spinner_item, roles
        )
        roleSpinner.adapter = roleAdapter

        doneBtn.setOnClickListener {
            //calculate date to string
            cal.add(Calendar.DAY_OF_YEAR, daySpinner.selectedItemPosition)
            val date = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
            shift = Shift("", main.user.uid, roleSpinner.selectedItemPosition.toString(), date)
            val dialogListener = activity as ShiftDialog.DialogListener
            dialogListener.onFinishEditDialog(shift, delete)
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val width =
            resources.displayMetrics.widthPixels - resources.getDimensionPixelSize(R.dimen.dialog_total_width_margin)
        var height = resources.getDimensionPixelSize(R.dimen.shift_dialog_height)
        if (delete)
            height = resources.getDimensionPixelSize(R.dimen.shift_dialog_height_delete)
        dialog?.window?.setLayout(width, height)
    }

    interface DialogListener {
        fun onFinishEditDialog(shift: Shift, delete:Boolean)
    }
}
