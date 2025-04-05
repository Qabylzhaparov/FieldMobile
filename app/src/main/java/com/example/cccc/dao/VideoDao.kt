package com.example.cccc.dao

import androidx.room.*
import com.example.cccc.entity.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos WHERE lessonId = :lessonId ORDER BY `order` ASC")
    fun getVideosByLessonId(lessonId: String): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE id = :videoId")
    suspend fun getVideoById(videoId: String): VideoEntity?

    @Query("SELECT * FROM videos WHERE lessonId = :lessonId AND `order` = :order")
    suspend fun getVideoByOrder(lessonId: String, order: Int): VideoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoEntity>)

    @Update
    suspend fun updateVideo(video: VideoEntity)

    @Delete
    suspend fun deleteVideo(video: VideoEntity)

    @Query("SELECT * FROM videos WHERE lastSynced < :timestamp")
    suspend fun getUnsyncedVideos(timestamp: Long): List<VideoEntity>

    @Query("SELECT COUNT(*) FROM videos WHERE lessonId = :lessonId")
    suspend fun getVideoCount(lessonId: String): Int
} 