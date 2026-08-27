package com.example.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.`data`.models.UserEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UserDao_Impl(
  __db: RoomDatabase,
) : UserDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserEntity: EntityInsertAdapter<UserEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfUserEntity = object : EntityInsertAdapter<UserEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `users` (`phone`,`name`,`email`,`address`,`pincode`,`emergencyContact`,`bloodGroup`,`medicalNotes`,`isLoggedIn`) VALUES (?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserEntity) {
        statement.bindText(1, entity.phone)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.email)
        statement.bindText(4, entity.address)
        statement.bindText(5, entity.pincode)
        statement.bindText(6, entity.emergencyContact)
        statement.bindText(7, entity.bloodGroup)
        statement.bindText(8, entity.medicalNotes)
        val _tmp: Int = if (entity.isLoggedIn) 1 else 0
        statement.bindLong(9, _tmp.toLong())
      }
    }
  }

  public override suspend fun insertOrUpdateUser(user: UserEntity): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfUserEntity.insert(_connection, user)
  }

  public override fun getLoggedInUser(): Flow<UserEntity?> {
    val _sql: String = "SELECT * FROM users WHERE isLoggedIn = 1 LIMIT 1"
    return createFlow(__db, false, arrayOf("users")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfPhone: Int = getColumnIndexOrThrow(_stmt, "phone")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfPincode: Int = getColumnIndexOrThrow(_stmt, "pincode")
        val _columnIndexOfEmergencyContact: Int = getColumnIndexOrThrow(_stmt, "emergencyContact")
        val _columnIndexOfBloodGroup: Int = getColumnIndexOrThrow(_stmt, "bloodGroup")
        val _columnIndexOfMedicalNotes: Int = getColumnIndexOrThrow(_stmt, "medicalNotes")
        val _columnIndexOfIsLoggedIn: Int = getColumnIndexOrThrow(_stmt, "isLoggedIn")
        val _result: UserEntity?
        if (_stmt.step()) {
          val _tmpPhone: String
          _tmpPhone = _stmt.getText(_columnIndexOfPhone)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpAddress: String
          _tmpAddress = _stmt.getText(_columnIndexOfAddress)
          val _tmpPincode: String
          _tmpPincode = _stmt.getText(_columnIndexOfPincode)
          val _tmpEmergencyContact: String
          _tmpEmergencyContact = _stmt.getText(_columnIndexOfEmergencyContact)
          val _tmpBloodGroup: String
          _tmpBloodGroup = _stmt.getText(_columnIndexOfBloodGroup)
          val _tmpMedicalNotes: String
          _tmpMedicalNotes = _stmt.getText(_columnIndexOfMedicalNotes)
          val _tmpIsLoggedIn: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsLoggedIn).toInt()
          _tmpIsLoggedIn = _tmp != 0
          _result =
              UserEntity(_tmpPhone,_tmpName,_tmpEmail,_tmpAddress,_tmpPincode,_tmpEmergencyContact,_tmpBloodGroup,_tmpMedicalNotes,_tmpIsLoggedIn)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getUserByPhone(phone: String): UserEntity? {
    val _sql: String = "SELECT * FROM users WHERE phone = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, phone)
        val _columnIndexOfPhone: Int = getColumnIndexOrThrow(_stmt, "phone")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfEmail: Int = getColumnIndexOrThrow(_stmt, "email")
        val _columnIndexOfAddress: Int = getColumnIndexOrThrow(_stmt, "address")
        val _columnIndexOfPincode: Int = getColumnIndexOrThrow(_stmt, "pincode")
        val _columnIndexOfEmergencyContact: Int = getColumnIndexOrThrow(_stmt, "emergencyContact")
        val _columnIndexOfBloodGroup: Int = getColumnIndexOrThrow(_stmt, "bloodGroup")
        val _columnIndexOfMedicalNotes: Int = getColumnIndexOrThrow(_stmt, "medicalNotes")
        val _columnIndexOfIsLoggedIn: Int = getColumnIndexOrThrow(_stmt, "isLoggedIn")
        val _result: UserEntity?
        if (_stmt.step()) {
          val _tmpPhone: String
          _tmpPhone = _stmt.getText(_columnIndexOfPhone)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpEmail: String
          _tmpEmail = _stmt.getText(_columnIndexOfEmail)
          val _tmpAddress: String
          _tmpAddress = _stmt.getText(_columnIndexOfAddress)
          val _tmpPincode: String
          _tmpPincode = _stmt.getText(_columnIndexOfPincode)
          val _tmpEmergencyContact: String
          _tmpEmergencyContact = _stmt.getText(_columnIndexOfEmergencyContact)
          val _tmpBloodGroup: String
          _tmpBloodGroup = _stmt.getText(_columnIndexOfBloodGroup)
          val _tmpMedicalNotes: String
          _tmpMedicalNotes = _stmt.getText(_columnIndexOfMedicalNotes)
          val _tmpIsLoggedIn: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsLoggedIn).toInt()
          _tmpIsLoggedIn = _tmp != 0
          _result =
              UserEntity(_tmpPhone,_tmpName,_tmpEmail,_tmpAddress,_tmpPincode,_tmpEmergencyContact,_tmpBloodGroup,_tmpMedicalNotes,_tmpIsLoggedIn)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun logoutAll() {
    val _sql: String = "UPDATE users SET isLoggedIn = 0"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
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
