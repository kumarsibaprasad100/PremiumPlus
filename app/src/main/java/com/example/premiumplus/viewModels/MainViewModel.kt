package com.example.premiumplus.viewModels

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.premiumplus.network.NetworkServiceInterface
import com.example.premiumplus.requestModel.ClientRequestModel
import com.example.premiumplus.responseModel.ClientResponse
import com.example.premiumplus.responseModel.DataItem
import com.example.premiumplus.views.MainActivity
import com.google.gson.Gson
import com.opencsv.CSVWriter
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.FileWriter
import java.io.IOException

class MainViewModel : ViewModel() {

    private val TAG: String? = "MainViewModel"
    val dataItem = ArrayList<DataItem>()
    var clientList = MutableLiveData<ArrayList<DataItem>>()
    var errormsg = MutableLiveData<String>()
    var showProgress = MutableLiveData<Boolean>()
    val filteredlist = ArrayList<DataItem>()
    val filtered = MutableLiveData<ArrayList<DataItem>>()


    fun getClientList(requestInterface: NetworkServiceInterface?) {
        showProgress.postValue(true)
        val clientRequestModel = ClientRequestModel("SUB0000000001", "USR002", "2")
        val responseObservable: Observable<Response<ResponseBody>>? =
            requestInterface?.getClient(clientRequestModel)
        responseObservable?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribeOn(Schedulers.io())
            ?.subscribe(object : Observer<Response<ResponseBody>> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    errormsg.postValue(e.message)
                    showProgress.postValue(false)
                }

                override fun onComplete() {}

                override fun onNext(value: Response<ResponseBody>) {
                    showProgress.postValue(false)
                    if (value.code() == 200) {
                        val responseString = value.body()!!.string()
                        val clientResponse: ClientResponse = Gson().fromJson(responseString, ClientResponse::class.java)
                        dataItem.clear()
                        for (item in clientResponse.data!!) { dataItem.addAll(listOf(item)) }
                        clientList.postValue(dataItem)
                    } else {
                       errormsg.postValue(value.errorBody().toString())
                    }
                }
            })
    }

    fun getFilteredData(newText: String?, clientList: ArrayList<DataItem>?) {
        filteredlist.clear()
        if (clientList != null) {
            for (item in clientList) {
                if (item.customerName?.toLowerCase().toString().contains(newText?.toLowerCase().toString())) {
                    filteredlist.add(item)
                }
            }
        }
        if (filteredlist.isEmpty()) {
            errormsg.postValue("No Data Found..")
            filteredlist.clear()
            filtered.postValue(filteredlist)
        } else {
            filtered.postValue(filteredlist)
        }
    }

    fun callMakeExcel(
        it: ArrayList<DataItem>?,
        applicationContext: Context,
        mainActivity: MainActivity
    ) {
        fun callMakeExcel(dataItems: ArrayList<DataItem>) {
            var writer: CSVWriter? = null
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mainActivity.requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                    mainActivity.requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
                }
                val csv: String = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/download/PremiumClient_" + System.currentTimeMillis() + ".csv"
                writer = CSVWriter(FileWriter(csv))
                val data: MutableList<Array<String>> = ArrayList()
                data.add(
                    arrayOf(
                        "Id",
                        "Name",
                        "Mobile",
                        "Email",
                        "Address",
                        "contactPerson"
                    )
                )
                for (i in 0 until dataItems.size) {
                    data.add(
                        arrayOf(
                            dataItems.get(i).customerID.toString(),
                            dataItems.get(i).customerName.toString(),
                            dataItems.get(i).mobile.toString(),
                            dataItems.get(i).email.toString(),
                            dataItems.get(i).location.toString(),
                            dataItems.get(i).contactPersonName.toString()
                        )
                    )
                }
                writer.writeAll(data) // data is adding to csv
                Log.d(TAG, "$csv")
                Log.d(TAG, "Writer  $writer")
                writer.close()
                Toast.makeText(applicationContext,
                    "Excel Downloaded successfully. find in $csv !",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: IOException) {
                Log.d(TAG, "Error  " + e.message)
                e.printStackTrace()
            }
        }
    }
}