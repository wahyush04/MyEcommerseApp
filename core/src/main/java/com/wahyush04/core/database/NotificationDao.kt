package com.wahyush04.core.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data : NotificationEntity)

    @Delete
    fun delete(data : NotificationEntity)

    @Query("SELECT * FROM tbl_notification ORDER BY status ASC, date DESC")
    fun getNotification(): LiveData<List<NotificationEntity>>

    @Query("UPDATE tbl_notification SET status = :state WHERE id = :id")
    fun updateStatus(id : Int, state : Int) : Int

    @Query("SELECT COUNT(*) FROM tbl_notification WHERE status = 0")
    fun countItems(): Int

    @Query("UPDATE tbl_notification SET isCheck = :state WHERE id = :id")
    fun updateCheck(id: Int, state : Int): Int

    @Query("UPDATE tbl_notification SET status= :state")
    fun readAll(state : Int) : Int

    @Query("DELETE FROM tbl_notification WHERE isCheck = 1")
    fun deleteCheckedNotif() : Int

    @Query("UPDATE tbl_notification SET isCheck = 0")
    fun unCheckAll() : Int

    @Query("UPDATE tbl_notification SET status = 1 WHERE isCheck = 1")
    fun updateStatusChecked() : Int


}