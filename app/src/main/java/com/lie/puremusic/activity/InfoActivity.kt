package com.lie.puremusic.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tv1.setText("网易云ID " + StaticData.user?.getMusic_id())
        binding.tv2.setText("账号 " + StaticData.user?.getAccount())
        binding.tv3.setText("密码 " + StaticData.user?.getPassword())
        binding.tv4.setText("网易云名称 " + StaticData.user?.getName())
        binding.Quit.setOnClickListener(View.OnClickListener {
//            val file = File(Environment.getExternalStorageDirectory().path + "/PureMusic/Data/" + "PureUser.txt")
//            if (file.exists()) {
//                file.delete()
//            }
            startActivity(Intent(this@InfoActivity, LoginActivity::class.java))
        })
    }
}