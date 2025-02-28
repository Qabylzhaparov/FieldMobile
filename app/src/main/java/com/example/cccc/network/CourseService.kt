package com.example.cccc.network

import com.example.cccc.entity.AddStudentRequest
import com.example.cccc.entity.AddStudentResponse
import com.example.cccc.entity.CourseItem
import com.example.cccc.entity.CourseStats
import com.example.cccc.entity.CreateCourseForm
import com.example.cccc.entity.CreateCourseRequest
import com.example.cccc.entity.ExamItem
import com.example.cccc.entity.MaterialUploadRequest
import com.example.cccc.entity.MaterialUploadResponse
import com.example.cccc.entity.PaymentItem
import com.example.cccc.entity.RefreshTokenRequest
import com.example.cccc.entity.ReviewRequest
import com.example.cccc.entity.ReviewResponse
import com.example.cccc.entity.StudentItem
import com.example.cccc.entity.TeacherDashboard
import com.example.cccc.entity.TeacherProfile
import com.example.cccc.entity.TeacherProfileUpdate
import com.example.cccc.entity.TokenRequest
import com.example.cccc.entity.TokenResponse
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT

interface CourseService {
//
//    // Courses
//    @GET("api/courses/")
//    suspend fun getCourses(): List<CourseItem>
//
//    // Exam
//    @GET("exam/take_test/{slug}/")
//    suspend fun getExam(@Path("slug") slug: String): ExamItem
//
//    @POST("exam/take_test/{slug}/")
//    suspend fun submitExam(@Path("slug") slug: String, @Body answers: ExamAnswers): ExamResult
//
//    // Payment
//    @GET("payment_successful")
//    suspend fun getPaymentSuccessful(): List<PaymentItem>
//
//    @GET("payments/payment_successful")
//    suspend fun getPaymentsSuccessful(): List<PaymentItem>
//
//    @POST("payments/review/create/{course_slug}/")
//    suspend fun createPaymentReview(
//        @Path("course_slug") courseSlug: String,
//        @Body review: ReviewRequest
//    ): ReviewResponse
//
//    // Review
//    @POST("review/create/{course_slug}/")
//    suspend fun createReview(
//        @Path("course_slug") courseSlug: String,
//        @Body review: ReviewRequest
//    ): ReviewResponse
//
//    // Teacher
//    @GET("teacher/add_student/")
//    suspend fun getStudents(): List<StudentItem>
//
//    @POST("teacher/add_student/")
//    suspend fun addStudent(@Body studentRequest: AddStudentRequest): AddStudentResponse
//
//    @GET("teacher/course/{course_id}/stats/")
//    suspend fun getCourseStats(@Path("course_id") courseId: Int): CourseStats
//
//    @GET("teacher/courses/")
//    suspend fun getTeacherCourses(): List<CourseItem>
//
//    @GET("teacher/create_course/")
//    suspend fun getCreateCourseForm(): CreateCourseForm
//
//    @POST("teacher/create_course/")
//    suspend fun createCourse(@Body courseRequest: CreateCourseRequest): CourseItem
//
//    @GET("teacher/dashboard/")
//    suspend fun getTeacherDashboard(): TeacherDashboard
//
//    @GET("teacher/edit-profile/")
//    suspend fun getTeacherProfile(): TeacherProfile
//
//    @PUT("teacher/edit-profile/")
//    suspend fun updateTeacherProfile(@Body profile: TeacherProfileUpdate): TeacherProfile
//
//    @GET("teacher/manage-courses/")
//    suspend fun getManageCourses(): List<CourseItem>
//
//    @DELETE("teacher/remove_student/{student_id}/{course_id}/")
//    suspend fun removeStudent(
//        @Path("student_id") studentId: Int,
//        @Path("course_id") courseId: Int
//    )
//
//    @POST("teacher/upload_material/{course_id}/")
//    suspend fun uploadMaterial(
//        @Path("course_id") courseId: Int,
//        @Body material: MaterialUploadRequest
//    ): MaterialUploadResponse
//
//    // User
//    @POST("user/api/token/")
//    suspend fun getToken(@Body tokenRequest: TokenRequest): TokenResponse
//
//    @POST("user/api/token/refresh/")
//    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): TokenResponse
}
