package com.example.cccc.dao

import androidx.room.*
import com.example.cccc.entity.UserMetricsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserMetricsDao {
    @Query("SELECT * FROM user_metrics WHERE userId = :userId")
    fun getUserMetrics(userId: String): Flow<UserMetricsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserMetrics(metrics: UserMetricsEntity)

    @Update
    suspend fun updateUserMetrics(metrics: UserMetricsEntity)

    @Query("SELECT * FROM user_metrics WHERE lastSynced < :timestamp")
    suspend fun getUnsyncedMetrics(timestamp: Long): List<UserMetricsEntity>

    @Query("UPDATE user_metrics SET coursesCompleted = coursesCompleted + 1 WHERE userId = :userId")
    suspend fun incrementCoursesCompleted(userId: String)

    @Query("UPDATE user_metrics SET testsPassed = testsPassed + 1 WHERE userId = :userId")
    suspend fun incrementTestsPassed(userId: String)

    @Query("UPDATE user_metrics SET totalHoursWatched = totalHoursWatched + :hours WHERE userId = :userId")
    suspend fun addHoursWatched(userId: String, hours: Int)

    @Query("UPDATE user_metrics SET certificatesEarned = certificatesEarned + 1 WHERE userId = :userId")
    suspend fun incrementCertificatesEarned(userId: String)
} 