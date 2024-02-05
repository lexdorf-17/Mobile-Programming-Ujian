package com.binus.timezone.ui.zonawaktu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.binus.timezone.network.ApiKey
import com.binus.timezone.network.TimezoneService
import com.binus.timezone.ui.zonawaktu.model.TimezoneResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TimezoneViewModel: ViewModel() {

    private val timezoneService: TimezoneService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(ApiKey.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        timezoneService = retrofit.create(TimezoneService::class.java)
    }

    private val _dataList = MutableLiveData<TimezoneResponse>()
    val dataList: LiveData<TimezoneResponse> get() = _dataList

    private val _onProgress by lazy { MutableLiveData<Boolean>() }
    val onProgress: LiveData<Boolean> get() = _onProgress

    private val _onError by lazy { MutableLiveData<String>() }
    val onError: LiveData<String> get() = _onError

    fun getDataList(): Disposable {
        _onProgress.postValue(true)

        return timezoneService.getList()
            .subscribeOn(Schedulers.io())
            .subscribeWith(subscribeToServer())
    }

    private fun subscribeToServer(): DisposableSubscriber<TimezoneResponse> {
        return object: DisposableSubscriber<TimezoneResponse>() {
            override fun onNext(t: TimezoneResponse?) {
                if (t != null) {
                    _dataList.postValue(t)
                    _onProgress.postValue(false)
                } else {
                    _onProgress.postValue(false)
                }
            }

            override fun onError(t: Throwable?) {
                _onProgress.postValue(false)
            }

            override fun onComplete() {
                _onProgress.postValue(false)
            }
        }
    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun getDataListViewModel() {
        compositeDisposable.add(getDataList())
    }
}