package com.lihan.qrcraft.core.`data`.local

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
public class QRCraftDatabase_Impl : QRCraftDatabase() {
  private val _qRCodeHistoryDao: Lazy<QRCodeHistoryDao> = lazy {
    QRCodeHistoryDao_Impl(this)
  }

  public override val qrCodeHistoryDao: QRCodeHistoryDao
    get() = _qRCodeHistoryDao.value

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(1, "77dbef9008531045715c128f2fe2e802", "7da7f3be1d101a991d26d84fdac94862") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `QRCodeHistoryEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `title` TEXT, `type` INTEGER NOT NULL, `content` TEXT NOT NULL, `createdAt` INTEGER NOT NULL, `isGenerated` INTEGER NOT NULL, `isFavorite` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '77dbef9008531045715c128f2fe2e802')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `QRCodeHistoryEntity`")
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

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsQRCodeHistoryEntity: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsQRCodeHistoryEntity.put("id", TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQRCodeHistoryEntity.put("title", TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQRCodeHistoryEntity.put("type", TableInfo.Column("type", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQRCodeHistoryEntity.put("content", TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQRCodeHistoryEntity.put("createdAt", TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQRCodeHistoryEntity.put("isGenerated", TableInfo.Column("isGenerated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsQRCodeHistoryEntity.put("isFavorite", TableInfo.Column("isFavorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysQRCodeHistoryEntity: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesQRCodeHistoryEntity: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoQRCodeHistoryEntity: TableInfo = TableInfo("QRCodeHistoryEntity", _columnsQRCodeHistoryEntity, _foreignKeysQRCodeHistoryEntity, _indicesQRCodeHistoryEntity)
        val _existingQRCodeHistoryEntity: TableInfo = read(connection, "QRCodeHistoryEntity")
        if (!_infoQRCodeHistoryEntity.equals(_existingQRCodeHistoryEntity)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |QRCodeHistoryEntity(com.lihan.qrcraft.core.data.local.QRCodeHistoryEntity).
              | Expected:
              |""".trimMargin() + _infoQRCodeHistoryEntity + """
              |
              | Found:
              |""".trimMargin() + _existingQRCodeHistoryEntity)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "QRCodeHistoryEntity")
  }

  public override fun clearAllTables() {
    super.performClear(false, "QRCodeHistoryEntity")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(QRCodeHistoryDao::class, QRCodeHistoryDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }
}
