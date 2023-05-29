package com.example.premiumplus.views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.premiumplus.R
import com.example.premiumplus.databinding.ActivityOtpVerificationBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit


class OtpVerificationActivity : AppCompatActivity() {

    private lateinit var edit: SharedPreferences.Editor
    private lateinit var sharedPreferences: SharedPreferences
    private var phone: String? = null
    private var mBinding: ActivityOtpVerificationBinding? = null
    private var mAuth: FirebaseAuth? = null
    private var verificationId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_otp_verification)
        mAuth = FirebaseAuth.getInstance()
        getIntentData()
        initView()
    }

    private fun getIntentData() {
        phone = intent.getStringExtra("number")
         sendVerificationCode(phone.toString())
    }

    private fun initView() {
        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE)
        edit = sharedPreferences.edit()
        mBinding?.btnOtp?.setOnClickListener {
            if (TextUtils.isEmpty(mBinding?.etOtp?.text.toString())) {
                Toast.makeText(applicationContext, "Please enter OTP", Toast.LENGTH_SHORT).show()
            } else {
                verifyCode(mBinding?.etOtp?.text.toString())
            }
        }
    }

    private fun sendVerificationCode(phone: String) {
        Toast.makeText(applicationContext,"A Verification code has been send to your mobile number",Toast.LENGTH_SHORT).show()
        val options = mAuth?.let {
            PhoneAuthOptions.newBuilder(it)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallBack)
                .build()
        }
        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private val mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                verificationId = s
            }
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    mBinding?.etOtp?.setText(code)
                    verifyCode(code)
                }
            }
            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)?.addOnCompleteListener { task ->
            if (task.isSuccessful()) {
                edit?.putString("userId",mAuth?.uid)
                edit?.putBoolean("isLogin",true)
                edit.commit()
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    applicationContext, task.exception?.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}