package com.lie.puremusic.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.ActivityLoginBinding
import com.lie.puremusic.pojo.PureUser
import es.dmoral.toasty.Toasty
import com.lie.puremusic.utils.Dao

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isLogin = true
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //欢迎
        Toasty.custom(
            this@LoginActivity,
            "欢迎使用Pure Music!",
            R.mipmap.ic_launcher_round,
            R.color.material_blue_500,
            1500,
            false,
            true
        ).show()
        binding.loginButton.setOnClickListener(View.OnClickListener {
            val user = PureUser("暂无", binding.account.text.toString(), binding.password.text.toString())
            if (isLogin) {
                Thread {
                    StaticData.user = Dao.select(StaticData.connection, user)
                    if (!StaticData.user?.getId().equals("null")) {
                        startActivity(Intent(this@LoginActivity, IntroActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
                        finish()
                    }
                }.start()
            } else {
                if (binding.account.text != null && binding.password.text != null) {
                    Thread { //缺少检测判断
                        Dao.insert(StaticData.connection, user)
                        startActivity(Intent(this@LoginActivity, LoginActivity::class.java))
                        finish()
                    }.start()
                }
            }
        })
        binding.tv5.setOnClickListener {
            if (isLogin) {
                isLogin = false
                binding.tv3.text = "注册"
                binding.tv4.text = "已有Pure Music账户?"
                binding.tv5.text = "登录"
            } else {
                isLogin = true
                binding.tv3.text = "登录"
                binding.tv4.text = "还没有拥有一个Pure Music账户?"
                binding.tv5.text = "注册"
            }
        }
    }
}