package com.example.callanalyzer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.telephony.PhoneNumberUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

data class CallItem(val name: String, val number: String, val duration: Long)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions()

        setContent {
            MaterialTheme {
                Surface {
                    AppUI()
                }
            }
        }
    }

    private fun requestPermissions() {
        val perms = arrayOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS
        )

        if (!perms.all {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(this, perms, 1)
        }
    }

    private val contactCache = mutableMapOf<String, String>()

    private fun normalize(number: String) =
        PhoneNumberUtils.normalizeNumber(number)

    private fun getName(number: String): String {
        return contactCache.getOrPut(number) {
            val uri = ContactsContract.PhoneLookup.CONTENT_FILTER_URI
                .buildUpon().appendPath(number).build()

            val cursor = contentResolver.query(uri,
                arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
                null, null, null)

            cursor?.use {
                if (it.moveToFirst()) return@getOrPut it.getString(0)
            }
            number
        }
    }

    private fun format(sec: Long): String {
        val m = sec / 60
        val s = sec % 60
        return "${m}m ${s}s"
    }

    private fun getData(year: Int, month: Int): List<CallItem> {
        val map = mutableMapOf<String, Long>()

        val cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)

        cursor?.use {
            val num = it.getColumnIndex(CallLog.Calls.NUMBER)
            val dateIdx = it.getColumnIndex(CallLog.Calls.DATE)
            val dur = it.getColumnIndex(CallLog.Calls.DURATION)

            while (it.moveToNext()) {
                val n = normalize(it.getString(num) ?: continue)
                val d = it.getLong(dateIdx)
                val duration = it.getLong(dur)

                val cal = Calendar.getInstance().apply { timeInMillis = d }

                if (cal.get(Calendar.YEAR) == year &&
                    cal.get(Calendar.MONTH) == month) {
                    map[n] = map.getOrDefault(n, 0) + duration
                }
            }
        }

        return map.entries
            .sortedByDescending { it.value }
            .take(20)
            .map {
                CallItem(getName(it.key), it.key, it.value)
            }
    }

    @Composable
    fun AppUI() {

        val months = listOf("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec")
        val yearNow = Calendar.getInstance().get(Calendar.YEAR)
        val years = (yearNow - 5..yearNow)

        var month by remember { mutableStateOf(months[0]) }
        var year by remember { mutableStateOf(yearNow) }
        var data by remember { mutableStateOf(listOf<CallItem>()) }

        var mExpand by remember { mutableStateOf(false) }
        var yExpand by remember { mutableStateOf(false) }

        Column(Modifier.padding(16.dp)) {

            Row {
                Box {
                    OutlinedButton({ mExpand = true }) { Text(month) }
                    DropdownMenu(mExpand, { mExpand = false }) {
                        months.forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = { month = it; mExpand = false }
                            )
                        }
                    }
                }

                Spacer(Modifier.width(8.dp))

                Box {
                    OutlinedButton({ yExpand = true }) { Text(year.toString()) }
                    DropdownMenu(yExpand, { yExpand = false }) {
                        years.forEach {
                            DropdownMenuItem(
                                text = { Text(it.toString()) },
                                onClick = { year = it; yExpand = false }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Button(onClick = {
                data = getData(year, months.indexOf(month))
            }) {
                Text("Generate Report")
            }

            Spacer(Modifier.height(16.dp))

            OutlinedCard(border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {

                Column {
                    // Header
                    Row(Modifier.padding(8.dp)) {
                        Text("Name", Modifier.weight(1f), style = MaterialTheme.typography.labelLarge)
                        Text("Number", Modifier.weight(1f))
                        Text("Duration", Modifier.weight(1f))
                    }

                    Divider()

                    LazyColumn {
                        items(data) {
                            Row(Modifier.padding(8.dp)) {
                                Text(it.name, Modifier.weight(1f))
                                Text(it.number, Modifier.weight(1f))
                                Text(format(it.duration), Modifier.weight(1f))
                            }
                            Divider()
                        }
                    }
                }
            }
        }
    }
}
