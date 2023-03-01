package com.wahyush04.androidphincon.ui.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wahyush04.core.data.source.local.entity.NotificationEntity
import com.wahyush04.core.data.source.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    fun getNotification(): LiveData<List<NotificationEntity>> =
        repository.getNotification()

    fun insertNotif(data: NotificationEntity) =
        repository.insertNotif(data)


    fun deleteCheckedNotif() =
        repository.deleteCheckedNotif()

    fun unCheckAll() =
        repository.unCheckAllNotif()

    fun updateStatusChecked() =
        repository.updateStatusCheckedNotif()

    fun updateStatus(id: Int, status: Int) =
        repository.updateStatusNotif(id, status)

    fun updateCheck(id: Int, status: Int) =
        repository.updateCheckNotif(id, status)

    fun countCheckedNotif() : Int =
        repository.countCheckedNotif()
}
