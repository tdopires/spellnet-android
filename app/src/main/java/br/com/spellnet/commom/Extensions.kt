package br.com.spellnet.commom

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Error<T>(val message: String? = null) : Resource<T>()
    class Loading<T> : Resource<T>()
}

typealias LiveDataResource<T> = LiveData<Resource<T>>
typealias MutableLiveDataResource<T> = MutableLiveData<Resource<T>>
typealias MediatorLiveDataResource<T> = MediatorLiveData<Resource<T>>

fun <T> MediatorLiveData<Resource<T>>.addResourceSource(source: LiveData<Resource<T>>, onChanged: (Resource<T>?) -> Unit) {
    this.removeSource(source)
    this.addSource(source) {
        if (it is Resource.Success || it is Resource.Error) {
            onChanged(it)
        }
    }
}

fun <T1: Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2)->R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}
fun <T1: Any, T2: Any, T3: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}
fun <T1: Any, T2: Any, T3: Any, T4: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, block: (T1, T2, T3, T4)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}
fun <T1: Any, T2: Any, T3: Any, T4: Any, T5: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, p4: T4?, p5: T5?, block: (T1, T2, T3, T4, T5)->R?): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null) block(p1, p2, p3, p4, p5) else null
}