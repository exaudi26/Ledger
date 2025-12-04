package com.example.ledger

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


data class TransactionData(
    val id: Int = 0,
    val title: String,
    val amount: Int
)

class TransactionDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "transaction.db", null, 1) {

        override fun onCreate(db: SQLiteDatabase?) {
            val createTable = """
            CREATE TABLE transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                amount INTEGER
            )
        """.trimIndent()

            db?.execSQL(createTable)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS transactions")
            onCreate(db)
        }

        /** INSERT */
        fun insertTransaction(title: String, amount: Int): Long {
            val db = writableDatabase
            val cv = ContentValues()
            cv.put("title", title)
            cv.put("amount", amount)
            return db.insert("transactions", null, cv)
        }

        /** READ */
        fun getTransactions(): List<TransactionData> {
            val db = readableDatabase
            val list = mutableListOf<TransactionData>()
            val cursor = db.rawQuery("SELECT * FROM transactions", null)

            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow("title"))
                    val amount = cursor.getInt(cursor.getColumnIndexOrThrow("amount"))
                    list.add(TransactionData(id, title, amount))
                } while (cursor.moveToNext())
            }

            cursor.close()
            return list
        }
}