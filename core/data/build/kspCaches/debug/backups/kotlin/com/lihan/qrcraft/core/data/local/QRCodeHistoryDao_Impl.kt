package com.lihan.qrcraft.core.`data`.local

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.EntityUpsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class QRCodeHistoryDao_Impl(
  __db: RoomDatabase,
) : QRCodeHistoryDao {
  private val __db: RoomDatabase

  private val __upsertAdapterOfQRCodeHistoryEntity: EntityUpsertAdapter<QRCodeHistoryEntity>
  init {
    this.__db = __db
    this.__upsertAdapterOfQRCodeHistoryEntity = EntityUpsertAdapter<QRCodeHistoryEntity>(object : EntityInsertAdapter<QRCodeHistoryEntity>() {
      protected override fun createQuery(): String = "INSERT INTO `QRCodeHistoryEntity` (`id`,`title`,`type`,`content`,`createdAt`,`isGenerated`,`isFavorite`) VALUES (?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: QRCodeHistoryEntity) {
        val _tmpId: Long? = entity.id
        if (_tmpId == null) {
          statement.bindNull(1)
        } else {
          statement.bindLong(1, _tmpId)
        }
        val _tmpTitle: String? = entity.title
        if (_tmpTitle == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmpTitle)
        }
        statement.bindLong(3, entity.type.toLong())
        statement.bindText(4, entity.content)
        statement.bindLong(5, entity.createdAt)
        val _tmp: Int = if (entity.isGenerated) 1 else 0
        statement.bindLong(6, _tmp.toLong())
        val _tmp_1: Int = if (entity.isFavorite) 1 else 0
        statement.bindLong(7, _tmp_1.toLong())
      }
    }, object : EntityDeleteOrUpdateAdapter<QRCodeHistoryEntity>() {
      protected override fun createQuery(): String = "UPDATE `QRCodeHistoryEntity` SET `id` = ?,`title` = ?,`type` = ?,`content` = ?,`createdAt` = ?,`isGenerated` = ?,`isFavorite` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: QRCodeHistoryEntity) {
        val _tmpId: Long? = entity.id
        if (_tmpId == null) {
          statement.bindNull(1)
        } else {
          statement.bindLong(1, _tmpId)
        }
        val _tmpTitle: String? = entity.title
        if (_tmpTitle == null) {
          statement.bindNull(2)
        } else {
          statement.bindText(2, _tmpTitle)
        }
        statement.bindLong(3, entity.type.toLong())
        statement.bindText(4, entity.content)
        statement.bindLong(5, entity.createdAt)
        val _tmp: Int = if (entity.isGenerated) 1 else 0
        statement.bindLong(6, _tmp.toLong())
        val _tmp_1: Int = if (entity.isFavorite) 1 else 0
        statement.bindLong(7, _tmp_1.toLong())
        val _tmpId_1: Long? = entity.id
        if (_tmpId_1 == null) {
          statement.bindNull(8)
        } else {
          statement.bindLong(8, _tmpId_1)
        }
      }
    })
  }

  public override suspend fun upsertHistory(qrCodeHistoryEntity: QRCodeHistoryEntity): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __upsertAdapterOfQRCodeHistoryEntity.upsertAndReturnId(_connection, qrCodeHistoryEntity)
    _result
  }

  public override fun getHistories(): Flow<List<QRCodeHistoryEntity>> {
    val _sql: String = "SELECT * FROM QRCodeHistoryEntity ORDER BY isFavorite"
    return createFlow(__db, false, arrayOf("QRCodeHistoryEntity")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfIsGenerated: Int = getColumnIndexOrThrow(_stmt, "isGenerated")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "isFavorite")
        val _result: MutableList<QRCodeHistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QRCodeHistoryEntity
          val _tmpId: Long?
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null
          } else {
            _tmpId = _stmt.getLong(_columnIndexOfId)
          }
          val _tmpTitle: String?
          if (_stmt.isNull(_columnIndexOfTitle)) {
            _tmpTitle = null
          } else {
            _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          }
          val _tmpType: Int
          _tmpType = _stmt.getLong(_columnIndexOfType).toInt()
          val _tmpContent: String
          _tmpContent = _stmt.getText(_columnIndexOfContent)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpIsGenerated: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsGenerated).toInt()
          _tmpIsGenerated = _tmp != 0
          val _tmpIsFavorite: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp_1 != 0
          _item = QRCodeHistoryEntity(_tmpId,_tmpTitle,_tmpType,_tmpContent,_tmpCreatedAt,_tmpIsGenerated,_tmpIsFavorite)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getScannedHistories(): Flow<List<QRCodeHistoryEntity>> {
    val _sql: String = "SELECT * FROM QRCodeHistoryEntity WHERE isGenerated = 0 ORDER BY isFavorite DESC,createdAt DESC"
    return createFlow(__db, false, arrayOf("QRCodeHistoryEntity")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfIsGenerated: Int = getColumnIndexOrThrow(_stmt, "isGenerated")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "isFavorite")
        val _result: MutableList<QRCodeHistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QRCodeHistoryEntity
          val _tmpId: Long?
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null
          } else {
            _tmpId = _stmt.getLong(_columnIndexOfId)
          }
          val _tmpTitle: String?
          if (_stmt.isNull(_columnIndexOfTitle)) {
            _tmpTitle = null
          } else {
            _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          }
          val _tmpType: Int
          _tmpType = _stmt.getLong(_columnIndexOfType).toInt()
          val _tmpContent: String
          _tmpContent = _stmt.getText(_columnIndexOfContent)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpIsGenerated: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsGenerated).toInt()
          _tmpIsGenerated = _tmp != 0
          val _tmpIsFavorite: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp_1 != 0
          _item = QRCodeHistoryEntity(_tmpId,_tmpTitle,_tmpType,_tmpContent,_tmpCreatedAt,_tmpIsGenerated,_tmpIsFavorite)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getGeneratedHistories(): Flow<List<QRCodeHistoryEntity>> {
    val _sql: String = "SELECT * FROM QRCodeHistoryEntity WHERE isGenerated = 1 ORDER BY isFavorite DESC,createdAt DESC"
    return createFlow(__db, false, arrayOf("QRCodeHistoryEntity")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfIsGenerated: Int = getColumnIndexOrThrow(_stmt, "isGenerated")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "isFavorite")
        val _result: MutableList<QRCodeHistoryEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: QRCodeHistoryEntity
          val _tmpId: Long?
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null
          } else {
            _tmpId = _stmt.getLong(_columnIndexOfId)
          }
          val _tmpTitle: String?
          if (_stmt.isNull(_columnIndexOfTitle)) {
            _tmpTitle = null
          } else {
            _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          }
          val _tmpType: Int
          _tmpType = _stmt.getLong(_columnIndexOfType).toInt()
          val _tmpContent: String
          _tmpContent = _stmt.getText(_columnIndexOfContent)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpIsGenerated: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsGenerated).toInt()
          _tmpIsGenerated = _tmp != 0
          val _tmpIsFavorite: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp_1 != 0
          _item = QRCodeHistoryEntity(_tmpId,_tmpTitle,_tmpType,_tmpContent,_tmpCreatedAt,_tmpIsGenerated,_tmpIsFavorite)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getHistoryById(id: Int): Flow<QRCodeHistoryEntity?> {
    val _sql: String = "SELECT * FROM QRCodeHistoryEntity WHERE id=?"
    return createFlow(__db, false, arrayOf("QRCodeHistoryEntity")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfContent: Int = getColumnIndexOrThrow(_stmt, "content")
        val _columnIndexOfCreatedAt: Int = getColumnIndexOrThrow(_stmt, "createdAt")
        val _columnIndexOfIsGenerated: Int = getColumnIndexOrThrow(_stmt, "isGenerated")
        val _columnIndexOfIsFavorite: Int = getColumnIndexOrThrow(_stmt, "isFavorite")
        val _result: QRCodeHistoryEntity?
        if (_stmt.step()) {
          val _tmpId: Long?
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null
          } else {
            _tmpId = _stmt.getLong(_columnIndexOfId)
          }
          val _tmpTitle: String?
          if (_stmt.isNull(_columnIndexOfTitle)) {
            _tmpTitle = null
          } else {
            _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          }
          val _tmpType: Int
          _tmpType = _stmt.getLong(_columnIndexOfType).toInt()
          val _tmpContent: String
          _tmpContent = _stmt.getText(_columnIndexOfContent)
          val _tmpCreatedAt: Long
          _tmpCreatedAt = _stmt.getLong(_columnIndexOfCreatedAt)
          val _tmpIsGenerated: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsGenerated).toInt()
          _tmpIsGenerated = _tmp != 0
          val _tmpIsFavorite: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsFavorite).toInt()
          _tmpIsFavorite = _tmp_1 != 0
          _result = QRCodeHistoryEntity(_tmpId,_tmpTitle,_tmpType,_tmpContent,_tmpCreatedAt,_tmpIsGenerated,_tmpIsFavorite)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun updateFavoriteStatus(id: Long, isFavorite: Boolean) {
    val _sql: String = "UPDATE QRCodeHistoryEntity SET isFavorite = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        val _tmp: Int = if (isFavorite) 1 else 0
        _stmt.bindLong(_argIndex, _tmp.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteById(id: Long) {
    val _sql: String = "DELETE FROM QRCodeHistoryEntity WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllScanned() {
    val _sql: String = "DELETE FROM QRCodeHistoryEntity WHERE isGenerated = 0"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllGenerated() {
    val _sql: String = "DELETE FROM QRCodeHistoryEntity WHERE isGenerated = 1"
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
