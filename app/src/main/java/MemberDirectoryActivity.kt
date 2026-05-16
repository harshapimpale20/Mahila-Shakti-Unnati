package com.example.mahilashaktiunnati

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MemberDirectoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MemberScreen()
            }
        }
    }
}

data class Member(
    val name: String,
    val id: String,
    val group: String,
    val image: String,
    val date: String
)

@Composable
fun MemberScreen() {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("MEMBER_DATA", Context.MODE_PRIVATE)

    var name by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }
    var group by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val memberList = remember { mutableStateListOf<Member>() }

    // LOAD DATA (FIXED SAFETY)
    LaunchedEffect(Unit) {
        val saved = prefs.getString("members", null)

        if (!saved.isNullOrEmpty()) {
            val arr = JSONArray(saved)

            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)

                memberList.add(
                    Member(
                        o.optString("name"),
                        o.optString("id"),
                        o.optString("group"),
                        o.optString("image"),
                        o.optString("date")
                    )
                )
            }
        }
    }

    // SAVE DATA
    fun saveMembers() {
        val arr = JSONArray()

        memberList.forEach {
            val obj = JSONObject()
            obj.put("name", it.name)
            obj.put("id", it.id)
            obj.put("group", it.group)
            obj.put("image", it.image)
            obj.put("date", it.date)
            arr.put(obj)
        }

        prefs.edit().putString("members", arr.toString()).apply()
    }

    // DATE
    fun getDate(): String {
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    }

    // IMAGE PICKER (FIXED URI HANDLING)
    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Member Management",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6A1B9A)
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Member Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("Member ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = group,
            onValueChange = { group = it },
            label = { Text("Group Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        // IMAGE PICK
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clickable { picker.launch("image/*") },
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(10.dp))

        // ADD BUTTON
        Button(
            onClick = {

                if (name.isNotBlank() && id.isNotBlank()) {

                    memberList.add(
                        Member(
                            name,
                            id,
                            group,
                            imageUri?.toString() ?: "",
                            getDate()
                        )
                    )

                    saveMembers()

                    name = ""
                    id = ""
                    group = ""
                    imageUri = null
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A))
        ) {
            Text("Add Member")
        }

        Spacer(Modifier.height(15.dp))

        // LIST
        LazyColumn {

            itemsIndexed(memberList) { index, member ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.Start
                    ) {

                        // PROFILE IMAGE (FIXED)
                        Image(
                            painter = rememberAsyncImagePainter(member.image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(85.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color(0xFF6A1B9A), CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            "Name: ${member.name}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text("ID: ${member.id}", fontSize = 14.sp, color = Color.Gray)
                        Text("Group: ${member.group}", fontSize = 14.sp, color = Color.Gray)
                        Text("Date: ${member.date}", fontSize = 14.sp, color = Color(0xFF2E7D32))

                        Spacer(Modifier.height(10.dp))

                        Button(
                            onClick = {
                                memberList.removeAt(index)
                                saveMembers()
                            },
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) {
                            Icon(Icons.Default.Delete, null)
                            Spacer(Modifier.width(5.dp))
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}