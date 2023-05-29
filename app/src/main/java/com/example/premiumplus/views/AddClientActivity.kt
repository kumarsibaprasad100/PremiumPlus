package com.example.premiumplus.views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.premiumplus.R
import com.example.premiumplus.databinding.ActivityAddClientBinding
import com.example.premiumplus.network.NetworkServiceInterface
import com.example.premiumplus.viewModels.AddClientViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class AddClientActivity : AppCompatActivity() {

    private var mUserId: String? = ""
    private val REQUEST_LOCATION: Int = 55
    private var mBinding: ActivityAddClientBinding? = null
    private lateinit var viewModel: AddClientViewModel
    private var requestInterface: NetworkServiceInterface? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val BASE_URL = "https://accounts.travelize.in/premiumplus/api/"
    private var latitude = ""
    private var longitude = ""
    private var address = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_client)
        viewModel = ViewModelProvider(this).get(AddClientViewModel::class.java)

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(NetworkServiceInterface::class.java)
        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE)
        mUserId = sharedPreferences.getString("userId", "")
        initView()
        subScribeSuccess()
        subScribeError()
    }

    private fun subScribeError() {
        viewModel?.errormsg?.observe(this){
            Toast.makeText(applicationContext,it,Toast.LENGTH_SHORT).show()
        }
    }

    private fun subScribeSuccess() {
        viewModel?.successmsg?.observe(this){
            Toast.makeText(applicationContext,it,Toast.LENGTH_SHORT).show()
            val intent = Intent();
            setResult(RESULT_OK, intent);
            finish()
        }
    }

    private fun initView() {
        mBinding?.ivLocation?.setOnClickListener {
            val i = Intent(applicationContext, MapActivity::class.java)
            startActivityForResult(i, REQUEST_LOCATION);
        }
        mBinding?.btnaddClient?.setOnClickListener {
            viewModel.addClient(
                requestInterface,
                latitude.toString(),
                longitude.toString(),
                address,
                mBinding?.etName?.text.toString(),
                mUserId.toString(),
                mBinding?.etMobile?.text.toString(),
                mBinding?.etLanline?.text.toString(),
                mBinding?.etPerson?.text.toString()
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_LOCATION) {
            if (resultCode === RESULT_OK) {
                if (data != null) {
                    mBinding?.etLocation?.text = data.getStringExtra("address")
                    latitude = data.getStringExtra("lat").toString()
                    longitude = data.getStringExtra("lng").toString()
                    address = data.getStringExtra("address").toString()
                }
            }
        }
    }
}