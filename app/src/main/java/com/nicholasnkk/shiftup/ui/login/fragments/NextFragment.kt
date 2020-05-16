package com.nicholasnkk.shiftup.ui.login.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.ui.login.ui.next.NextViewModel

class NextFragment : Fragment() {

    private lateinit var homeViewModel: NextViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(NextViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_next, container, false)

        return root
    }
}
