package com.nicholasnkk.shiftup.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nicholasnkk.shiftup.R
import com.nicholasnkk.shiftup.activities.MainActivity

class NextFragment : Fragment() {
    private val TAG = NextFragment::class.qualifiedName
    private lateinit var main: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_next, container, false)
        main = activity as MainActivity

        return root
    }
}
