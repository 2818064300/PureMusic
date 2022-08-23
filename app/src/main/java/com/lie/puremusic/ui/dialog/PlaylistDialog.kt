package com.lie.puremusic.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.adapter.PlaylistDialogAdapter
import com.lie.puremusic.databinding.DialogPlayListBinding

class PlaylistDialog: BottomSheetDialogFragment() {

    private var _binding: DialogPlayListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.rvPlaylist.adapter = PlaylistDialogAdapter(StaticData.PlayList_now)
        binding.tvPlaylist.text = this.context?.getString(R.string.playlist_number, StaticData.PlayList_now?.size)
        binding.rvPlaylist.scrollToPosition(StaticData.Songs?.let {
            StaticData.PlayList_now?.indexOf(
                it
            )
        }
            ?: 0)
        binding.rvPlaylist.layoutManager = LinearLayoutManager(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}