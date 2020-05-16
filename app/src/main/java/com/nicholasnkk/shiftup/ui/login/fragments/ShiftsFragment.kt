package com.nicholasnkk.shiftup.ui.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.ui.login.ui.shifts.ShiftsViewModel

class ShiftsFragment : Fragment() {

    private lateinit var homeViewModel: ShiftsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(ShiftsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shifts, container, false)

        return root
    }
}
