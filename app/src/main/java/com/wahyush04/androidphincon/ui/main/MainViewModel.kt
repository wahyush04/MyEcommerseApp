package com.wahyush04.androidphincon.ui.main

import androidx.lifecycle.ViewModel
import com.wahyush04.androidphincon.core.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {
    fun countTrolley() = repository.countItemsTrolley()

    fun countNotif() = repository.countNotif()

}