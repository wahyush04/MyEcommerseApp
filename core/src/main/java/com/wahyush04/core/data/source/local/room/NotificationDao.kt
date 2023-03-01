package com.wahyush04.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wahyush04.core.data.source.local.entity.NotificationEntity

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data : NotificationEntity)

    @Query("SELECT * FROM tbl_notification ORDER BY status ASC, date DESC")
    fun getNotification(): LiveData<List<NotificationEntity>>

    @Query("UPDATE tbl_notification SET status = :state WHERE id = :id")
    fun updateStatus(id : Int, state : Int) : Int

    @Query("SELECT COUNT(*) FROM tbl_notification WHERE status = 0")
    fun countItems(): Int

    @Query("SELECT COUNT(*) FROM tbl_notification WHERE isCheck = 1")
    fun countItemsChecked(): Int

    @Query("UPDATE tbl_notification SET isCheck = :state WHERE id = :id")
    fun updateCheck(id: Int, state : Int): Int

    @Query("UPDATE tbl_notification SET status = 1")
    fun readAll() : Int

    @Query("DELETE FROM tbl_notification WHERE isCheck = 1")
    fun deleteCheckedNotif()

    @Query("UPDATE tbl_notification SET isCheck = 0")
    fun unCheckAll()

    @Query("UPDATE tbl_notification SET status = 1 WHERE isCheck = 1")
    fun updateStatusChecked()


}