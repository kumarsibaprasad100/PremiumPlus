package com.example.premiumplus.views

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.premiumplus.R
import com.example.premiumplus.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private var verificationId: String? = null
    private var mBinding: ActivityLoginBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        mBinding?.btnOtp?.setOnClickListener {
            if (TextUtils.isEmpty( mBinding?.etNumber?.text.toString())) {
                Toast.makeText(
                    this,
                    "Please enter a valid phone number.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val phone = "+91" + mBinding?.etNumber?.text.toString()
                goToOtpActivity(phone)
            }
        }
    }

    private fun goToOtpActivity(phone: String) {
        val intent = Intent(applicationContext, OtpVerificationActivity::class.java)
        intent.putExtra("number",phone)
        startActivity(intent)
        finish()
    }
}