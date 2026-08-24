package com.example.data.local

import androidx.room.*
import com.example.data.models.AppointmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments ORDER BY timestamp DESC")
    fun getAllAppointments(): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointments WHERE id = :id LIMIT 1")
    fun getAppointmentById(id: String): Flow<AppointmentEntity?>

    @Query("SELECT * FROM appointments WHERE id = :id LIMIT 1")
    suspend fun getAppointmentByIdSync(id: String): AppointmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppointment(appointment: AppointmentEntity)

    @Query("UPDATE appointments SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("UPDATE appointments SET status = 'CANCELLED', cancellationReason = :reason WHERE id = :id")
    suspend fun cancelAppointment(id: String, reason: String)

    @Query("UPDATE appointments SET date = :newDate, timeSlot = :newTimeSlot WHERE id = :id")
    suspend fun rescheduleAppointment(id: String, newDate: String, newTimeSlot: String)

    @Query("UPDATE appointments SET ratingGiven = :rating, reviewGiven = :review WHERE id = :id")
    suspend fun submitRating(id: String, rating: Float, review: String)

    @Query("UPDATE appointments SET assignedNurseId = :nurseId, assignedNurseName = :nurseName, assignedNursePhone = :nursePhone, assignedNurseQualification = :qualification, assignedNurseRating = :rating, status = 'NURSE_ASSIGNED' WHERE id = :id")
    suspend fun assignNurse(
        id: String,
        nurseId: String,
        nurseName: String,
        nursePhone: String,
        qualification: String,
        rating: Double
    )
}
