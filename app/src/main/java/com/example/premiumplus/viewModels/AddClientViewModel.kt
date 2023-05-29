package com.example.premiumplus.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.premiumplus.network.NetworkServiceInterface
import com.example.premiumplus.requestModel.AddClientRequestModel
import com.example.premiumplus.responseModel.AddClientResponse
import com.example.premiumplus.responseModel.ClientResponse
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Response

class AddClientViewModel : ViewModel() {
    var errormsg = MutableLiveData<String>()
    var successmsg = MutableLiveData<String>()
    var showProgress = MutableLiveData<Boolean>()

    fun addClient(
        requestInterface: NetworkServiceInterface?,
        latitude: String,
        longitude: String,
        address: String,
        name: String,
        userId: String,
        moblile: String,
        landline: String,
        person: String
    ) {
        if (latitude.isEmpty() || longitude.isEmpty() || address.isEmpty() || name.isEmpty() || moblile.isEmpty() || landline.isEmpty() || person.isEmpty()) {
            errormsg.postValue("Please Fill All The Fields")
        } else {
            val addClientRequestModel = AddClientRequestModel(
                "1", landline, userId, moblile, name,
                latitude, longitude, person, address
            )
            val responseObservable: Observable<Response<ResponseBody>>? =
                requestInterface?.addClient(addClientRequestModel)
            responseObservable?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe(object : Observer<Response<ResponseBody>> {
                    override fun onSubscribe(d: Disposable) {}

                    override fun onError(e: Throwable) {
                        errormsg.postValue(e.message)
                    }

                    override fun onComplete() {}

                    override fun onNext(t: Response<ResponseBody>) {
                        if (t.code() == 200) {
                            val responseString = t.body()!!.string()
                            val clientResponse: AddClientResponse =
                                Gson().fromJson(responseString, AddClientResponse::class.java)
                            if (clientResponse.success == 1) {
                                successmsg.postValue("Client Successfully added.")
                            } else {
                                errormsg.postValue(clientResponse.msg)
                            }
                        } else {
                            errormsg.postValue(t.errorBody().toString())
                        }
                    }
                })
        }
    }
}