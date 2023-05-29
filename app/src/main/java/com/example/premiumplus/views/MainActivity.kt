package com.example.premiumplus.views

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.premiumplus.R
import com.example.premiumplus.adapter.ClientAdapter
import com.example.premiumplus.adapter.ItemClickListner
import com.example.premiumplus.databinding.ActivityMainBinding
import com.example.premiumplus.network.BroadCast
import com.example.premiumplus.network.NetWorkUtill
import com.example.premiumplus.network.NetworkServiceInterface
import com.example.premiumplus.responseModel.DataItem
import com.example.premiumplus.viewModels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), ItemClickListner {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var edit: SharedPreferences.Editor
    private var MyReceiver: BroadcastReceiver? = null
    private val TAG: String? = "MainActivity"
    private val ADD_CLIENT: Int = 88
    private lateinit var clientAdapter: ClientAdapter
    private var clientList: ArrayList<DataItem>? = null
    private var requestInterface: NetworkServiceInterface? = null
    private lateinit var mainViewModel: MainViewModel
    private var mBinding: ActivityMainBinding? = null
    private val BASE_URL = "https://accounts.travelize.in/premiumplus/api/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        requestInterface = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(NetworkServiceInterface::class.java)
        sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE)
        edit = sharedPreferences.edit()
        MyReceiver = BroadCast()
        checkInternet()
        initView()
        subScribeClientData()
        subscribeProgressData()
        subscribeFilterData()
    }

    private fun checkInternet() {
        registerReceiver(MyReceiver,  IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        if (NetWorkUtill.getConnectivityStatusString(applicationContext)?.equals("Internet Available") == true) {
            mainViewModel.getClientList(requestInterface)
        }
    }

    private fun subscribeFilterData() {
        mainViewModel.filtered.observe(this) {
            if (clientAdapter != null)
                clientAdapter.filterList(it)
        }
    }

    private fun subscribeProgressData() {
        mainViewModel.showProgress.observe(this) {
            mBinding?.progress?.visibility = if (it == true) View.VISIBLE else View.GONE
        }
    }

    private fun initView() {
        mainViewModel.getClientList(requestInterface)
        mBinding?.searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (clientList!=null && clientList?.size!! > 0) {
                    mainViewModel.getFilteredData(newText, clientList)
                }
                return false
            }
        })

        mBinding?.ivAdd?.setOnClickListener {
            startActivityForResult(Intent(applicationContext,AddClientActivity::class.java),ADD_CLIENT)
        }
        mBinding?.ivLogout?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            sharedPreferences.edit().clear()
            edit?.putBoolean("isLogin",false)
            edit.commit()
            startActivity(Intent(applicationContext,LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
    }

    private fun subScribeClientData() {
        mainViewModel.clientList.observe(this) {
            setUpClientRV(it)
            clientList = it
            mBinding?.ivDowload?.visibility = View.VISIBLE
            mBinding?.ivDowload?.setOnClickListener {m->
                mainViewModel?.callMakeExcel(it,applicationContext,this)
            }
        }
    }

    private fun setUpClientRV(it: ArrayList<DataItem>) {
        val layoutManager = LinearLayoutManager(applicationContext)
        mBinding?.rvClient?.layoutManager = layoutManager
        clientAdapter = ClientAdapter(it, applicationContext,this)
        mBinding?.rvClient?.adapter = clientAdapter
        clientAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === ADD_CLIENT) {
            if (resultCode === RESULT_OK) {
                mainViewModel?.getClientList(requestInterface)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(MyReceiver);
    }

    override fun onResume() {
        super.onResume()
        checkInternet()
    }

    override fun getItem(dataItem: DataItem) {}
}