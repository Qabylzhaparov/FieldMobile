package com.example.cccc.entity

import java.sql.Date

data class UserCourse(
    val user: User,
    val course: Course,
    val date: Date
)
