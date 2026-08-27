package com.example.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.`data`.models.SupportTicketEntity
import javax.`annotation`.processing.Generated
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
public class SupportTicketDao_Impl(
  __db: RoomDatabase,
) : SupportTicketDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSupportTicketEntity: EntityInsertAdapter<SupportTicketEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSupportTicketEntity = object : EntityInsertAdapter<SupportTicketEntity>()
        {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `support_tickets` (`id`,`userPhone`,`subject`,`message`,`status`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SupportTicketEntity) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.userPhone)
        statement.bindText(3, entity.subject)
        statement.bindText(4, entity.message)
        statement.bindText(5, entity.status)
        statement.bindLong(6, entity.timestamp)
      }
    }
  }

  public override suspend fun insertTicket(ticket: SupportTicketEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSupportTicketEntity.insert(_connection, ticket)
  }

  public override fun getAllTickets(): Flow<List<SupportTicketEntity>> {
    val _sql: String = "SELECT * FROM support_tickets ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("support_tickets")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfUserPhone: Int = getColumnIndexOrThrow(_stmt, "userPhone")
        val _columnIndexOfSubject: Int = getColumnIndexOrThrow(_stmt, "subject")
        val _columnIndexOfMessage: Int = getColumnIndexOrThrow(_stmt, "message")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<SupportTicketEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SupportTicketEntity
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpUserPhone: String
          _tmpUserPhone = _stmt.getText(_columnIndexOfUserPhone)
          val _tmpSubject: String
          _tmpSubject = _stmt.getText(_columnIndexOfSubject)
          val _tmpMessage: String
          _tmpMessage = _stmt.getText(_columnIndexOfMessage)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item =
              SupportTicketEntity(_tmpId,_tmpUserPhone,_tmpSubject,_tmpMessage,_tmpStatus,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
