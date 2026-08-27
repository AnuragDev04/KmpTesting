package com.example.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _userDao: Lazy<UserDao> = lazy {
    UserDao_Impl(this)
  }

  private val _appointmentDao: Lazy<AppointmentDao> = lazy {
    AppointmentDao_Impl(this)
  }

  private val _supportTicketDao: Lazy<SupportTicketDao> = lazy {
    SupportTicketDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1,
        "2213d1e9ae5bffdc160a3ea574e567e4", "85795ec07bf89cfdfb39084300e53381") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `users` (`phone` TEXT NOT NULL, `name` TEXT NOT NULL, `email` TEXT NOT NULL, `address` TEXT NOT NULL, `pincode` TEXT NOT NULL, `emergencyContact` TEXT NOT NULL, `bloodGroup` TEXT NOT NULL, `medicalNotes` TEXT NOT NULL, `isLoggedIn` INTEGER NOT NULL, PRIMARY KEY(`phone`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `appointments` (`id` TEXT NOT NULL, `serviceId` TEXT NOT NULL, `serviceName` TEXT NOT NULL, `categoryName` TEXT NOT NULL, `price` REAL NOT NULL, `date` TEXT NOT NULL, `timeSlot` TEXT NOT NULL, `address` TEXT NOT NULL, `patientName` TEXT NOT NULL, `patientAge` INTEGER NOT NULL, `specialNotes` TEXT NOT NULL, `paymentGateway` TEXT NOT NULL, `paymentMethod` TEXT NOT NULL, `paymentStatus` TEXT NOT NULL, `transactionId` TEXT NOT NULL, `invoiceNumber` TEXT NOT NULL, `status` TEXT NOT NULL, `assignedNurseId` TEXT, `assignedNurseName` TEXT, `assignedNursePhone` TEXT, `assignedNurseQualification` TEXT, `assignedNurseRating` REAL, `ratingGiven` REAL, `reviewGiven` TEXT, `cancellationReason` TEXT, `timestamp` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `support_tickets` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `userPhone` TEXT NOT NULL, `subject` TEXT NOT NULL, `message` TEXT NOT NULL, `status` TEXT NOT NULL, `timestamp` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2213d1e9ae5bffdc160a3ea574e567e4')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `users`")
        connection.execSQL("DROP TABLE IF EXISTS `appointments`")
        connection.execSQL("DROP TABLE IF EXISTS `support_tickets`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsUsers: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUsers.put("phone", TableInfo.Column("phone", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("name", TableInfo.Column("name", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("email", TableInfo.Column("email", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("address", TableInfo.Column("address", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("pincode", TableInfo.Column("pincode", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("emergencyContact", TableInfo.Column("emergencyContact", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("bloodGroup", TableInfo.Column("bloodGroup", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("medicalNotes", TableInfo.Column("medicalNotes", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUsers.put("isLoggedIn", TableInfo.Column("isLoggedIn", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUsers: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUsers: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUsers: TableInfo = TableInfo("users", _columnsUsers, _foreignKeysUsers,
            _indicesUsers)
        val _existingUsers: TableInfo = read(connection, "users")
        if (!_infoUsers.equals(_existingUsers)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |users(com.example.data.models.UserEntity).
              | Expected:
              |""".trimMargin() + _infoUsers + """
              |
              | Found:
              |""".trimMargin() + _existingUsers)
        }
        val _columnsAppointments: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsAppointments.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("serviceId", TableInfo.Column("serviceId", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("serviceName", TableInfo.Column("serviceName", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("categoryName", TableInfo.Column("categoryName", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("price", TableInfo.Column("price", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("date", TableInfo.Column("date", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("timeSlot", TableInfo.Column("timeSlot", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("address", TableInfo.Column("address", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("patientName", TableInfo.Column("patientName", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("patientAge", TableInfo.Column("patientAge", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("specialNotes", TableInfo.Column("specialNotes", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("paymentGateway", TableInfo.Column("paymentGateway", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("paymentMethod", TableInfo.Column("paymentMethod", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("paymentStatus", TableInfo.Column("paymentStatus", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("transactionId", TableInfo.Column("transactionId", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("invoiceNumber", TableInfo.Column("invoiceNumber", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("status", TableInfo.Column("status", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("assignedNurseId", TableInfo.Column("assignedNurseId", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("assignedNurseName", TableInfo.Column("assignedNurseName", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("assignedNursePhone", TableInfo.Column("assignedNursePhone",
            "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("assignedNurseQualification",
            TableInfo.Column("assignedNurseQualification", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("assignedNurseRating", TableInfo.Column("assignedNurseRating",
            "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("ratingGiven", TableInfo.Column("ratingGiven", "REAL", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("reviewGiven", TableInfo.Column("reviewGiven", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("cancellationReason", TableInfo.Column("cancellationReason",
            "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsAppointments.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysAppointments: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesAppointments: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoAppointments: TableInfo = TableInfo("appointments", _columnsAppointments,
            _foreignKeysAppointments, _indicesAppointments)
        val _existingAppointments: TableInfo = read(connection, "appointments")
        if (!_infoAppointments.equals(_existingAppointments)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |appointments(com.example.data.models.AppointmentEntity).
              | Expected:
              |""".trimMargin() + _infoAppointments + """
              |
              | Found:
              |""".trimMargin() + _existingAppointments)
        }
        val _columnsSupportTickets: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSupportTickets.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSupportTickets.put("userPhone", TableInfo.Column("userPhone", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSupportTickets.put("subject", TableInfo.Column("subject", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSupportTickets.put("message", TableInfo.Column("message", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSupportTickets.put("status", TableInfo.Column("status", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSupportTickets.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSupportTickets: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSupportTickets: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSupportTickets: TableInfo = TableInfo("support_tickets", _columnsSupportTickets,
            _foreignKeysSupportTickets, _indicesSupportTickets)
        val _existingSupportTickets: TableInfo = read(connection, "support_tickets")
        if (!_infoSupportTickets.equals(_existingSupportTickets)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |support_tickets(com.example.data.models.SupportTicketEntity).
              | Expected:
              |""".trimMargin() + _infoSupportTickets + """
              |
              | Found:
              |""".trimMargin() + _existingSupportTickets)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "users", "appointments",
        "support_tickets")
  }

  public override fun clearAllTables() {
    super.performClear(false, "users", "appointments", "support_tickets")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(UserDao::class, UserDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(AppointmentDao::class, AppointmentDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SupportTicketDao::class, SupportTicketDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun userDao(): UserDao = _userDao.value

  public override fun appointmentDao(): AppointmentDao = _appointmentDao.value

  public override fun supportTicketDao(): SupportTicketDao = _supportTicketDao.value
}
