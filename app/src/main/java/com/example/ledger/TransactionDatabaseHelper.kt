package com.example.ledger

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


data class TransactionData(
    val id: Int = 0,
    val title: String,
    val amount: Int,
    val date: Long
)

class TransactionDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "transaction.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTransactionTable = """
        CREATE TABLE transactions (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            amount INTEGER NOT NULL,
            date INTEGER NOT NULL
        )
    """.trimIndent()

        val createBudgetTable = """
        CREATE TABLE budget (
            id INTEGER PRIMARY KEY,
            amount INTEGER NOT NULL
        )
    """.trimIndent()

        db?.execSQL(createTransactionTable)
        db?.execSQL(createBudgetTable)
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
            cv.put("date", System.currentTimeMillis())
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
                    val date = cursor.getLong(cursor.getColumnIndexOrThrow("date"))

                    list.add(TransactionData(id, title, amount, date))
                } while (cursor.moveToNext())
            }

            cursor.close()
            return list
        }

    fun saveBudget(amount: Int) {
        val db = writableDatabase

        val cv = ContentValues()
        cv.put("id", 1)              // selalu row dengan id 1
        cv.put("amount", amount)

        db.insertWithOnConflict(
            "budget",
            null,
            cv,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    fun getBudget(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT amount FROM budget WHERE id = 1", null)

        return if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndexOrThrow("amount"))
        } else {
            0
        }.also { cursor.close() }
    }

}