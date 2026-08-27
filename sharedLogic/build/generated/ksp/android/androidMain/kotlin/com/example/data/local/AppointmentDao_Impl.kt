package com.example.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.`data`.models.AppointmentEntity
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Float
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppointmentDao_Impl(
  __db: RoomDatabase,
) : AppointmentDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfAppointmentEntity: EntityInsertAdapter<AppointmentEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfAppointmentEntity = object : EntityInsertAdapter<AppointmentEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `appointments` (`id`,`serviceId`,`serviceName`,`categoryName`,`price`,`date`,`timeSlot`,`address`,`patientName`,`patientAge`,`specialNotes`,`paymentGateway`,`paymentMethod`,`paymentStatus`,`transactionId`,`invoiceNumber`,`status`,`assignedNurseId`,`assignedNurseName`,`assignedNursePhone`,`assignedNurseQualification`,`assignedNurseRating`,`ratingGiven`,`reviewGiven`,`cancellationReason`,`timestamp`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: AppointmentEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.serviceId)
        statement.bindText(3, entity.serviceName)
        statement.bindText(4, entity.categoryName)
        statement.bindDouble(5, entity.price)
        statement.bindText(6, entity.date)
        statement.bindText(7, entity.timeSlot)
        statement.bindText(8, entity.address)
        statement.bindText(9, entity.patientName)
        statement.bindLong(10, entity.patientAge.toLong())
        statement.bindText(11, entity.specialNotes)
        statement.bindText(12, entity.paymentGateway)
        statement.bindText(13, entity.paymentMethod)
        statement.bindText(14, entity.paymentStatus)
        statement.bindText(15, entity.transactionId)
        statement.bindText(16, entity.invoiceNumber)
        statement.bindText(17, entity.status)
        val _tmpAssignedNurseId: String? = entity.assignedNurseId
        if (_tmpAssignedNurseId == null) {
          statement.bindNull(18)
        } else {
          statement.bindText(18, _tmpAssignedNurseId)
        }
        val _tmpAssignedNurseName: String? = entity.assignedNurseName
        if (_tmpAssignedNurseName == null) {
          statement.bindNull(19)
        } else {
          statement.bindText(19, _tmpAssignedNurseName)
        }
        val _tmpAssignedNursePhone: String? = entity.assignedNursePhone
        if (_tmpAssignedNursePhone == null) {
          statement.bindNull(20)
        } else {
          statement.bindText(20, _tmpAssignedNursePhone)
        }
        val _tmpAssignedNurseQualification: String? = entity.assignedNurseQualification
        if (_tmpAssignedNurseQualification == null) {
          statement.bindNull(21)
        } else {
          statement.bindText(21, _tmpAssignedNurseQualification)
        }
        val _tmpAssignedNurseRating: Double? = entity.assignedNurseRating
        if (_tmpAssignedNurseRating == null) {
          statement.bindNull(22)
        } else {
          statement.bindDouble(22, _tmpAssignedNurseRating)
        }
        val _tmpRatingGiven: Float? = entity.ratingGiven
        if (_tmpRatingGiven == null) {
          statement.bindNull(23)
        } else {
          statement.bindDouble(23, _tmpRatingGiven.toDouble())
        }
        val _tmpReviewGiven: String? = entity.reviewGiven
        if (_tmpReviewGiven == null) {
          statement.bindNull(24)
        } else {
          statement.bindText(24, _tmpReviewGiven)
        }
        val _tmpCancellationReason: String? = entity.cancellationReason
        if (_tmpCancellationReason == null) {
          statement.bindNull(25)
        } else {
          statement.bindText(25, _tmpCancellationReason)
        }
        statement.bindLong(26, entity.timestamp)
      }
    }
  }

  public override suspend fun insertAppointment(appointment: AppointmentEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfAppointmentEntity.insert(_connection, appointment)
  }

  public override fun getAllAppointments(): Flow<List<AppointmentEntity>> {
    val _sql: String = "SELECT * FROM appointments ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("appointments")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfServiceId: Int = getColumnIndexOrThrow(_stmt, "serviceId")
        val _columnIndexOfServiceName: Int = getColumnIndexOrThrow(_stmt, "serviceName")
        val _columnIndexOfCategoryName: Int = getColumnIndexOrThrow(_stmt, "categoryName")
        val _columnIndexOfPrice: Int = getColumnIndexOrThrow(_stmt, "price")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfTimeSlot: Int = getColumnIndexOrThrow(_stmt, "timeSlot")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfPatientName: Int = getColumnIndexOrThrow(_stmt, "patientName")
        val _columnIndexOfPatientAge: Int = getColumnIndexOrThrow(_stmt, "patientAge")
        val _columnIndexOfSpecialNotes: Int = getColumnIndexOrThrow(_stmt, "specialNotes")
        val _columnIndexOfPaymentGateway: Int = getColumnIndexOrThrow(_stmt, "paymentGateway")
        val _columnIndexOfPaymentMethod: Int = getColumnIndexOrThrow(_stmt, "paymentMethod")
        val _columnIndexOfPaymentStatus: Int = getColumnIndexOrThrow(_stmt, "paymentStatus")
        val _columnIndexOfTransactionId: Int = getColumnIndexOrThrow(_stmt, "transactionId")
        val _columnIndexOfInvoiceNumber: Int = getColumnIndexOrThrow(_stmt, "invoiceNumber")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfAssignedNurseId: Int = getColumnIndexOrThrow(_stmt, "assignedNurseId")
        val _columnIndexOfAssignedNurseName: Int = getColumnIndexOrThrow(_stmt, "assignedNurseName")
        val _columnIndexOfAssignedNursePhone: Int = getColumnIndexOrThrow(_stmt,
            "assignedNursePhone")
        val _columnIndexOfAssignedNurseQualification: Int = getColumnIndexOrThrow(_stmt,
            "assignedNurseQualification")
        val _columnIndexOfAssignedNurseRating: Int = getColumnIndexOrThrow(_stmt,
            "assignedNurseRating")
        val _columnIndexOfRatingGiven: Int = getColumnIndexOrThrow(_stmt, "ratingGiven")
        val _columnIndexOfReviewGiven: Int = getColumnIndexOrThrow(_stmt, "reviewGiven")
        val _columnIndexOfCancellationReason: Int = getColumnIndexOrThrow(_stmt,
            "cancellationReason")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<AppointmentEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: AppointmentEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpServiceId: String
          _tmpServiceId = _stmt.getText(_columnIndexOfServiceId)
          val _tmpServiceName: String
          _tmpServiceName = _stmt.getText(_columnIndexOfServiceName)
          val _tmpCategoryName: String
          _tmpCategoryName = _stmt.getText(_columnIndexOfCategoryName)
          val _tmpPrice: Double
          _tmpPrice = _stmt.getDouble(_columnIndexOfPrice)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpTimeSlot: String
          _tmpTimeSlot = _stmt.getText(_columnIndexOfTimeSlot)
          val _tmpAddress: String
          _tmpAddress = _stmt.getText(_columnIndexOfAddress)
          val _tmpPatientName: String
          _tmpPatientName = _stmt.getText(_columnIndexOfPatientName)
          val _tmpPatientAge: Int
          _tmpPatientAge = _stmt.getLong(_columnIndexOfPatientAge).toInt()
          val _tmpSpecialNotes: String
          _tmpSpecialNotes = _stmt.getText(_columnIndexOfSpecialNotes)
          val _tmpPaymentGateway: String
          _tmpPaymentGateway = _stmt.getText(_columnIndexOfPaymentGateway)
          val _tmpPaymentMethod: String
          _tmpPaymentMethod = _stmt.getText(_columnIndexOfPaymentMethod)
          val _tmpPaymentStatus: String
          _tmpPaymentStatus = _stmt.getText(_columnIndexOfPaymentStatus)
          val _tmpTransactionId: String
          _tmpTransactionId = _stmt.getText(_columnIndexOfTransactionId)
          val _tmpInvoiceNumber: String
          _tmpInvoiceNumber = _stmt.getText(_columnIndexOfInvoiceNumber)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpAssignedNurseId: String?
          if (_stmt.isNull(_columnIndexOfAssignedNurseId)) {
            _tmpAssignedNurseId = null
          } else {
            _tmpAssignedNurseId = _stmt.getText(_columnIndexOfAssignedNurseId)
          }
          val _tmpAssignedNurseName: String?
          if (_stmt.isNull(_columnIndexOfAssignedNurseName)) {
            _tmpAssignedNurseName = null
          } else {
            _tmpAssignedNurseName = _stmt.getText(_columnIndexOfAssignedNurseName)
          }
          val _tmpAssignedNursePhone: String?
          if (_stmt.isNull(_columnIndexOfAssignedNursePhone)) {
            _tmpAssignedNursePhone = null
          } else {
            _tmpAssignedNursePhone = _stmt.getText(_columnIndexOfAssignedNursePhone)
          }
          val _tmpAssignedNurseQualification: String?
          if (_stmt.isNull(_columnIndexOfAssignedNurseQualification)) {
            _tmpAssignedNurseQualification = null
          } else {
            _tmpAssignedNurseQualification = _stmt.getText(_columnIndexOfAssignedNurseQualification)
          }
          val _tmpAssignedNurseRating: Double?
          if (_stmt.isNull(_columnIndexOfAssignedNurseRating)) {
            _tmpAssignedNurseRating = null
          } else {
            _tmpAssignedNurseRating = _stmt.getDouble(_columnIndexOfAssignedNurseRating)
          }
          val _tmpRatingGiven: Float?
          if (_stmt.isNull(_columnIndexOfRatingGiven)) {
            _tmpRatingGiven = null
          } else {
            _tmpRatingGiven = _stmt.getDouble(_columnIndexOfRatingGiven).toFloat()
          }
          val _tmpReviewGiven: String?
          if (_stmt.isNull(_columnIndexOfReviewGiven)) {
            _tmpReviewGiven = null
          } else {
            _tmpReviewGiven = _stmt.getText(_columnIndexOfReviewGiven)
          }
          val _tmpCancellationReason: String?
          if (_stmt.isNull(_columnIndexOfCancellationReason)) {
            _tmpCancellationReason = null
          } else {
            _tmpCancellationReason = _stmt.getText(_columnIndexOfCancellationReason)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item =
              AppointmentEntity(_tmpId,_tmpServiceId,_tmpServiceName,_tmpCategoryName,_tmpPrice,_tmpDate,_tmpTimeSlot,_tmpAddress,_tmpPatientName,_tmpPatientAge,_tmpSpecialNotes,_tmpPaymentGateway,_tmpPaymentMethod,_tmpPaymentStatus,_tmpTransactionId,_tmpInvoiceNumber,_tmpStatus,_tmpAssignedNurseId,_tmpAssignedNurseName,_tmpAssignedNursePhone,_tmpAssignedNurseQualification,_tmpAssignedNurseRating,_tmpRatingGiven,_tmpReviewGiven,_tmpCancellationReason,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAppointmentById(id: String): Flow<AppointmentEntity?> {
    val _sql: String = "SELECT * FROM appointments WHERE id = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("appointments")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfServiceId: Int = getColumnIndexOrThrow(_stmt, "serviceId")
        val _columnIndexOfServiceName: Int = getColumnIndexOrThrow(_stmt, "serviceName")
        val _columnIndexOfCategoryName: Int = getColumnIndexOrThrow(_stmt, "categoryName")
        val _columnIndexOfPrice: Int = getColumnIndexOrThrow(_stmt, "price")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfTimeSlot: Int = getColumnIndexOrThrow(_stmt, "timeSlot")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfPatientName: Int = getColumnIndexOrThrow(_stmt, "patientName")
        val _columnIndexOfPatientAge: Int = getColumnIndexOrThrow(_stmt, "patientAge")
        val _columnIndexOfSpecialNotes: Int = getColumnIndexOrThrow(_stmt, "specialNotes")
        val _columnIndexOfPaymentGateway: Int = getColumnIndexOrThrow(_stmt, "paymentGateway")
        val _columnIndexOfPaymentMethod: Int = getColumnIndexOrThrow(_stmt, "paymentMethod")
        val _columnIndexOfPaymentStatus: Int = getColumnIndexOrThrow(_stmt, "paymentStatus")
        val _columnIndexOfTransactionId: Int = getColumnIndexOrThrow(_stmt, "transactionId")
        val _columnIndexOfInvoiceNumber: Int = getColumnIndexOrThrow(_stmt, "invoiceNumber")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfAssignedNurseId: Int = getColumnIndexOrThrow(_stmt, "assignedNurseId")
        val _columnIndexOfAssignedNurseName: Int = getColumnIndexOrThrow(_stmt, "assignedNurseName")
        val _columnIndexOfAssignedNursePhone: Int = getColumnIndexOrThrow(_stmt,
            "assignedNursePhone")
        val _columnIndexOfAssignedNurseQualification: Int = getColumnIndexOrThrow(_stmt,
            "assignedNurseQualification")
        val _columnIndexOfAssignedNurseRating: Int = getColumnIndexOrThrow(_stmt,
            "assignedNurseRating")
        val _columnIndexOfRatingGiven: Int = getColumnIndexOrThrow(_stmt, "ratingGiven")
        val _columnIndexOfReviewGiven: Int = getColumnIndexOrThrow(_stmt, "reviewGiven")
        val _columnIndexOfCancellationReason: Int = getColumnIndexOrThrow(_stmt,
            "cancellationReason")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: AppointmentEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpServiceId: String
          _tmpServiceId = _stmt.getText(_columnIndexOfServiceId)
          val _tmpServiceName: String
          _tmpServiceName = _stmt.getText(_columnIndexOfServiceName)
          val _tmpCategoryName: String
          _tmpCategoryName = _stmt.getText(_columnIndexOfCategoryName)
          val _tmpPrice: Double
          _tmpPrice = _stmt.getDouble(_columnIndexOfPrice)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpTimeSlot: String
          _tmpTimeSlot = _stmt.getText(_columnIndexOfTimeSlot)
          val _tmpAddress: String
          _tmpAddress = _stmt.getText(_columnIndexOfAddress)
          val _tmpPatientName: String
          _tmpPatientName = _stmt.getText(_columnIndexOfPatientName)
          val _tmpPatientAge: Int
          _tmpPatientAge = _stmt.getLong(_columnIndexOfPatientAge).toInt()
          val _tmpSpecialNotes: String
          _tmpSpecialNotes = _stmt.getText(_columnIndexOfSpecialNotes)
          val _tmpPaymentGateway: String
          _tmpPaymentGateway = _stmt.getText(_columnIndexOfPaymentGateway)
          val _tmpPaymentMethod: String
          _tmpPaymentMethod = _stmt.getText(_columnIndexOfPaymentMethod)
          val _tmpPaymentStatus: String
          _tmpPaymentStatus = _stmt.getText(_columnIndexOfPaymentStatus)
          val _tmpTransactionId: String
          _tmpTransactionId = _stmt.getText(_columnIndexOfTransactionId)
          val _tmpInvoiceNumber: String
          _tmpInvoiceNumber = _stmt.getText(_columnIndexOfInvoiceNumber)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpAssignedNurseId: String?
          if (_stmt.isNull(_columnIndexOfAssignedNurseId)) {
            _tmpAssignedNurseId = null
          } else {
            _tmpAssignedNurseId = _stmt.getText(_columnIndexOfAssignedNurseId)
          }
          val _tmpAssignedNurseName: String?
          if (_stmt.isNull(_columnIndexOfAssignedNurseName)) {
            _tmpAssignedNurseName = null
          } else {
            _tmpAssignedNurseName = _stmt.getText(_columnIndexOfAssignedNurseName)
          }
          val _tmpAssignedNursePhone: String?
          if (_stmt.isNull(_columnIndexOfAssignedNursePhone)) {
            _tmpAssignedNursePhone = null
          } else {
            _tmpAssignedNursePhone = _stmt.getText(_columnIndexOfAssignedNursePhone)
          }
          val _tmpAssignedNurseQualification: String?
          if (_stmt.isNull(_columnIndexOfAssignedNurseQualification)) {
            _tmpAssignedNurseQualification = null
          } else {
            _tmpAssignedNurseQualification = _stmt.getText(_columnIndexOfAssignedNurseQualification)
          }
          val _tmpAssignedNurseRating: Double?
          if (_stmt.isNull(_columnIndexOfAssignedNurseRating)) {
            _tmpAssignedNurseRating = null
          } else {
            _tmpAssignedNurseRating = _stmt.getDouble(_columnIndexOfAssignedNurseRating)
          }
          val _tmpRatingGiven: Float?
          if (_stmt.isNull(_columnIndexOfRatingGiven)) {
            _tmpRatingGiven = null
          } else {
            _tmpRatingGiven = _stmt.getDouble(_columnIndexOfRatingGiven).toFloat()
          }
          val _tmpReviewGiven: String?
          if (_stmt.isNull(_columnIndexOfReviewGiven)) {
            _tmpReviewGiven = null
          } else {
            _tmpReviewGiven = _stmt.getText(_columnIndexOfReviewGiven)
          }
          val _tmpCancellationReason: String?
          if (_stmt.isNull(_columnIndexOfCancellationReason)) {
            _tmpCancellationReason = null
          } else {
            _tmpCancellationReason = _stmt.getText(_columnIndexOfCancellationReason)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _result =
              AppointmentEntity(_tmpId,_tmpServiceId,_tmpServiceName,_tmpCategoryName,_tmpPrice,_tmpDate,_tmpTimeSlot,_tmpAddress,_tmpPatientName,_tmpPatientAge,_tmpSpecialNotes,_tmpPaymentGateway,_tmpPaymentMethod,_tmpPaymentStatus,_tmpTransactionId,_tmpInvoiceNumber,_tmpStatus,_tmpAssignedNurseId,_tmpAssignedNurseName,_tmpAssignedNursePhone,_tmpAssignedNurseQualification,_tmpAssignedNurseRating,_tmpRatingGiven,_tmpReviewGiven,_tmpCancellationReason,_tmpTimestamp)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAppointmentByIdSync(id: String): AppointmentEntity? {
    val _sql: String = "SELECT * FROM appointments WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfServiceId: Int = getColumnIndexOrThrow(_stmt, "serviceId")
        val _columnIndexOfServiceName: Int = getColumnIndexOrThrow(_stmt, "serviceName")
        val _columnIndexOfCategoryName: Int = getColumnIndexOrThrow(_stmt, "categoryName")
        val _columnIndexOfPrice: Int = getColumnIndexOrThrow(_stmt, "price")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfTimeSlot: Int = getColumnIndexOrThrow(_stmt, "timeSlot")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfPatientName: Int = getColumnIndexOrThrow(_stmt, "patientName")
        val _columnIndexOfPatientAge: Int = getColumnIndexOrThrow(_stmt, "patientAge")
        val _columnIndexOfSpecialNotes: Int = getColumnIndexOrThrow(_stmt, "specialNotes")
        val _columnIndexOfPaymentGateway: Int = getColumnIndexOrThrow(_stmt, "paymentGateway")
        val _columnIndexOfPaymentMethod: Int = getColumnIndexOrThrow(_stmt, "paymentMethod")
        val _columnIndexOfPaymentStatus: Int = getColumnIndexOrThrow(_stmt, "paymentStatus")
        val _columnIndexOfTransactionId: Int = getColumnIndexOrThrow(_stmt, "transactionId")
        val _columnIndexOfInvoiceNumber: Int = getColumnIndexOrThrow(_stmt, "invoiceNumber")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfAssignedNurseId: Int = getColumnIndexOrThrow(_stmt, "assignedNurseId")
        val _columnIndexOfAssignedNurseName: Int = getColumnIndexOrThrow(_stmt, "assignedNurseName")
        val _columnIndexOfAssignedNursePhone: Int = getColumnIndexOrThrow(_stmt,
            "assignedNursePhone")
        val _columnIndexOfAssignedNurseQualification: Int = getColumnIndexOrThrow(_stmt,
            "assignedNurseQualification")
        val _columnIndexOfAssignedNurseRating: Int = getColumnIndexOrThrow(_stmt,
            "assignedNurseRating")
        val _columnIndexOfRatingGiven: Int = getColumnIndexOrThrow(_stmt, "ratingGiven")
        val _columnIndexOfReviewGiven: Int = getColumnIndexOrThrow(_stmt, "reviewGiven")
        val _columnIndexOfCancellationReason: Int = getColumnIndexOrThrow(_stmt,
            "cancellationReason")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: AppointmentEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpServiceId: String
          _tmpServiceId = _stmt.getText(_columnIndexOfServiceId)
          val _tmpServiceName: String
          _tmpServiceName = _stmt.getText(_columnIndexOfServiceName)
          val _tmpCategoryName: String
          _tmpCategoryName = _stmt.getText(_columnIndexOfCategoryName)
          val _tmpPrice: Double
          _tmpPrice = _stmt.getDouble(_columnIndexOfPrice)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpTimeSlot: String
          _tmpTimeSlot = _stmt.getText(_columnIndexOfTimeSlot)
          val _tmpAddress: String
          _tmpAddress = _stmt.getText(_columnIndexOfAddress)
          val _tmpPatientName: String
          _tmpPatientName = _stmt.getText(_columnIndexOfPatientName)
          val _tmpPatientAge: Int
          _tmpPatientAge = _stmt.getLong(_columnIndexOfPatientAge).toInt()
          val _tmpSpecialNotes: String
          _tmpSpecialNotes = _stmt.getText(_columnIndexOfSpecialNotes)
          val _tmpPaymentGateway: String
          _tmpPaymentGateway = _stmt.getText(_columnIndexOfPaymentGateway)
          val _tmpPaymentMethod: String
          _tmpPaymentMethod = _stmt.getText(_columnIndexOfPaymentMethod)
          val _tmpPaymentStatus: String
          _tmpPaymentStatus = _stmt.getText(_columnIndexOfPaymentStatus)
          val _tmpTransactionId: String
          _tmpTransactionId = _stmt.getText(_columnIndexOfTransactionId)
          val _tmpInvoiceNumber: String
          _tmpInvoiceNumber = _stmt.getText(_columnIndexOfInvoiceNumber)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpAssignedNurseId: String?
          if (_stmt.isNull(_columnIndexOfAssignedNurseId)) {
            _tmpAssignedNurseId = null
          } else {
            _tmpAssignedNurseId = _stmt.getText(_columnIndexOfAssignedNurseId)
          }
          val _tmpAssignedNurseName: String?
          if (_stmt.isNull(_columnIndexOfAssignedNurseName)) {
            _tmpAssignedNurseName = null
          } else {
            _tmpAssignedNurseName = _stmt.getText(_columnIndexOfAssignedNurseName)
          }
          val _tmpAssignedNursePhone: String?
          if (_stmt.isNull(_columnIndexOfAssignedNursePhone)) {
            _tmpAssignedNursePhone = null
          } else {
            _tmpAssignedNursePhone = _stmt.getText(_columnIndexOfAssignedNursePhone)
          }
          val _tmpAssignedNurseQualification: String?
          if (_stmt.isNull(_columnIndexOfAssignedNurseQualification)) {
            _tmpAssignedNurseQualification = null
          } else {
            _tmpAssignedNurseQualification = _stmt.getText(_columnIndexOfAssignedNurseQualification)
          }
          val _tmpAssignedNurseRating: Double?
          if (_stmt.isNull(_columnIndexOfAssignedNurseRating)) {
            _tmpAssignedNurseRating = null
          } else {
            _tmpAssignedNurseRating = _stmt.getDouble(_columnIndexOfAssignedNurseRating)
          }
          val _tmpRatingGiven: Float?
          if (_stmt.isNull(_columnIndexOfRatingGiven)) {
            _tmpRatingGiven = null
          } else {
            _tmpRatingGiven = _stmt.getDouble(_columnIndexOfRatingGiven).toFloat()
          }
          val _tmpReviewGiven: String?
          if (_stmt.isNull(_columnIndexOfReviewGiven)) {
            _tmpReviewGiven = null
          } else {
            _tmpReviewGiven = _stmt.getText(_columnIndexOfReviewGiven)
          }
          val _tmpCancellationReason: String?
          if (_stmt.isNull(_columnIndexOfCancellationReason)) {
            _tmpCancellationReason = null
          } else {
            _tmpCancellationReason = _stmt.getText(_columnIndexOfCancellationReason)
          }
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _result =
              AppointmentEntity(_tmpId,_tmpServiceId,_tmpServiceName,_tmpCategoryName,_tmpPrice,_tmpDate,_tmpTimeSlot,_tmpAddress,_tmpPatientName,_tmpPatientAge,_tmpSpecialNotes,_tmpPaymentGateway,_tmpPaymentMethod,_tmpPaymentStatus,_tmpTransactionId,_tmpInvoiceNumber,_tmpStatus,_tmpAssignedNurseId,_tmpAssignedNurseName,_tmpAssignedNursePhone,_tmpAssignedNurseQualification,_tmpAssignedNurseRating,_tmpRatingGiven,_tmpReviewGiven,_tmpCancellationReason,_tmpTimestamp)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateStatus(id: String, status: String) {
    val _sql: String = "UPDATE appointments SET status = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, status)
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun cancelAppointment(id: String, reason: String) {
    val _sql: String =
        "UPDATE appointments SET status = 'CANCELLED', cancellationReason = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, reason)
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun rescheduleAppointment(
    id: String,
    newDate: String,
    newTimeSlot: String,
  ) {
    val _sql: String = "UPDATE appointments SET date = ?, timeSlot = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, newDate)
        _argIndex = 2
        _stmt.bindText(_argIndex, newTimeSlot)
        _argIndex = 3
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun submitRating(
    id: String,
    rating: Float,
    review: String,
  ) {
    val _sql: String = "UPDATE appointments SET ratingGiven = ?, reviewGiven = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindDouble(_argIndex, rating.toDouble())
        _argIndex = 2
        _stmt.bindText(_argIndex, review)
        _argIndex = 3
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun assignNurse(
    id: String,
    nurseId: String,
    nurseName: String,
    nursePhone: String,
    qualification: String,
    rating: Double,
  ) {
    val _sql: String =
        "UPDATE appointments SET assignedNurseId = ?, assignedNurseName = ?, assignedNursePhone = ?, assignedNurseQualification = ?, assignedNurseRating = ?, status = 'NURSE_ASSIGNED' WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, nurseId)
        _argIndex = 2
        _stmt.bindText(_argIndex, nurseName)
        _argIndex = 3
        _stmt.bindText(_argIndex, nursePhone)
        _argIndex = 4
        _stmt.bindText(_argIndex, qualification)
        _argIndex = 5
        _stmt.bindDouble(_argIndex, rating)
        _argIndex = 6
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
