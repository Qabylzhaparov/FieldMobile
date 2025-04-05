package com.example.cccc.dao

import androidx.room.*
import com.example.cccc.entity.TestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TestDao {
    @Query("SELECT * FROM tests WHERE lessonId = :lessonId ORDER BY `order` ASC")
    fun getTestsByLessonId(lessonId: String): Flow<List<TestEntity>>

    @Query("SELECT * FROM tests WHERE id = :testId")
    suspend fun getTestById(testId: String): TestEntity?

    @Query("SELECT * FROM tests WHERE lessonId = :lessonId AND `order` = :order")
    suspend fun getTestByOrder(lessonId: String, order: Int): TestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTest(test: TestEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTests(tests: List<TestEntity>)

    @Update
    suspend fun updateTest(test: TestEntity)

    @Delete
    suspend fun deleteTest(test: TestEntity)

    @Query("SELECT * FROM tests WHERE lastSynced < :timestamp")
    suspend fun getUnsyncedTests(timestamp: Long): List<TestEntity>

    @Query("SELECT COUNT(*) FROM tests WHERE lessonId = :lessonId")
    suspend fun getTestCount(lessonId: String): Int
} 