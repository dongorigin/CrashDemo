package cn.dong.crash

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.provider.BaseColumns
import androidx.appcompat.app.AppCompatActivity
import cn.dong.crash.databinding.ActivityMainBinding
import cn.dong.crash.sqlite.FeedReaderContract.FeedEntry
import cn.dong.crash.sqlite.FeedReaderDbHelper
import cn.dong.nativelib.NativeLibJni

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val dbHelper = FeedReaderDbHelper(this)
    private val cursors = mutableListOf<Cursor>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.text.text = NativeLibJni.stringFromJNI()


        binding.openCursor.setOnClickListener {
            for (i in 1..100000) {
                cursors.add(openCursor())
            }
        }
        binding.insert.setOnClickListener {
            insert()
        }
    }

    private fun insert() {
        // Gets the data repository in write mode
        val db = dbHelper.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(FeedEntry.COLUMN_NAME_TITLE, "testTitle")
            put(FeedEntry.COLUMN_NAME_SUBTITLE, "testSub")
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(FeedEntry.TABLE_NAME, null, values)
    }

    private fun openCursor(): Cursor {
        val db = dbHelper.readableDatabase

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        val projection =
            arrayOf(BaseColumns._ID, FeedEntry.COLUMN_NAME_TITLE, FeedEntry.COLUMN_NAME_SUBTITLE)

// Filter results WHERE "title" = 'My Title'
        val selection = "${FeedEntry.COLUMN_NAME_TITLE} = ?"
        val selectionArgs = arrayOf("My Title")

// How you want the results sorted in the resulting Cursor
        val sortOrder = "${FeedEntry.COLUMN_NAME_SUBTITLE} DESC"

        val cursor = db.query(
            FeedEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        return cursor
    }

}
