package com.lie.puremusic.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lie.puremusic.adapter.MyRecyclerGridAdapter
import com.lie.puremusic.utils.SpacesItemDecoration
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.FragmentSquare3Binding
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class SquareFragment3 : Fragment() {
    private var _binding: FragmentSquare3Binding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSquare3Binding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val adapter = context?.let { StaticData.Square.get(2)?.getSongsLists()?.let { it1 -> MyRecyclerGridAdapter(it, it1, 2) } }
//        val alphaAdapter = adapter?.let { AlphaInAnimationAdapter(it) }
//        binding.CardGroup.setAdapter(alphaAdapter?.let { AlphaInAnimationAdapter(it) })
//        val layoutManager = GridLayoutManager(context, 3)
//        layoutManager.orientation = RecyclerView.VERTICAL
//        binding.CardGroup.setLayoutManager(layoutManager)
//        binding.CardGroup.addItemDecoration(SpacesItemDecoration())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}