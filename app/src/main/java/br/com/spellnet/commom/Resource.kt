package br.com.spellnet.commom

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val message: String? = null) : Resource<T>()
    class Loading<T> : Resource<T>()
}

typealias LiveDataResource<T> = LiveData<Resource<T>>
typealias MutableLiveDataResource<T> = MutableLiveData<Resource<T>>
typealias MediatorLiveDataResource<T> = MediatorLiveData<Resource<T>>

fun <T> MediatorLiveData<Resource<T>>.addResourceSource(
    source: LiveData<Resource<T>>,
    onChanged: (Resource<T>?) -> Unit
) {
    this.removeSource(source)
    this.addSource(source) {
        if (it is Resource.Success || it is Resource.Error) {
            removeSource(source)
            onChanged(it)
        }
    }
}

fun <T> MediatorLiveData<Resource<T>>.addResourceSourceWithRetries(
    sourceBuilder: (() -> LiveData<Resource<T>>),
    retries: Int,
    onChanged: (Resource<T>?) -> Unit
) {
    val source = sourceBuilder.invoke()
    this.removeSource(source)
    this.addSource(source) {
        if (it is Resource.Success || it is Resource.Error) {
            removeSource(source)
        }
        when {
            it is Resource.Error && retries > 0 -> {
                Log.d(
                    "MediatorLiveData",
                    "Retrying addResourceSourceWithRetries | retries left = ${retries - 1}"
                )
                addResourceSourceWithRetries(sourceBuilder, retries - 1, onChanged)
            }
            it is Resource.Error || it is Resource.Success -> onChanged(it)
        }
    }
}