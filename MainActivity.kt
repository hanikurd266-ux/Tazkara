package com.tazkera.felsaree

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Green = Color(0xFF087F5B)
private val Dark = Color(0xFF10251F)
private val Pale = Color(0xFFF2F7F4)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(primary = Green, background = Pale)) {
                TicketApp(onOpenOfficial = {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.tazkarti.com/")))
                })
            }
        }
    }
}

data class Match(val home: String, val away: String, val competition: String, val status: String, val date: String)

private val demoMatches = listOf(
    Match("الأهلي", "الزمالك", "الدوري المصري", "بيانات تجريبية", "الموعد غير مؤكد"),
    Match("منتخب مصر", "خصم المباراة", "مباريات المنتخبات", "بيانات تجريبية", "الموعد غير مؤكد"),
    Match("فريقك المفضل", "الفريق المنافس", "بطولات أخرى", "بيانات تجريبية", "الموعد غير مؤكد")
)

@Composable
fun TicketApp(onOpenOfficial: () -> Unit) {
    var selected by remember { mutableStateOf("الكل") }
    var showInfo by remember { mutableStateOf(false) }
    val filters = listOf("الكل", "الأهلي", "الزمالك", "المنتخبات")
    val visibleMatches = when (selected) {
        "الأهلي" -> demoMatches.filter { it.home == "الأهلي" || it.away == "الأهلي" }
        "الزمالك" -> demoMatches.filter { it.home == "الزمالك" || it.away == "الزمالك" }
        "المنتخبات" -> demoMatches.filter { it.competition.contains("المنتخبات") }
        else -> demoMatches
    }

    Scaffold(containerColor = Pale) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Column(Modifier.fillMaxWidth().background(Dark).padding(horizontal = 20.dp, vertical = 22.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ConfirmationNumber, contentDescription = null, tint = Color(0xFF7DE0B5), modifier = Modifier.size(36.dp))
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text("تذكرة في السريع", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("المباريات وروابط الحجز الرسمية", color = Color(0xFFD4E6DE), fontSize = 13.sp)
                    }
                    IconButton(onClick = { showInfo = !showInfo }) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = "التنبيهات", tint = Color.White)
                    }
                }
                Spacer(Modifier.height(18.dp))
                Text("تابع المباراة، وافتح مصدر التذاكر الرسمي", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                if (showInfo) {
                    Spacer(Modifier.height(8.dp))
                    Text("هذه نسخة تجريبية ولا ترسل إشعارات حقيقية بعد. سنضيف التنبيهات بعد ربط مصدر بيانات موثوق.", color = Color(0xFFFFE6A6), fontSize = 12.sp)
                }
            }

            Column(Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                Text("المباريات", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = Dark)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    filters.forEach { filter ->
                        FilterChip(selected = selected == filter, onClick = { selected = filter }, label = { Text(filter) })
                    }
                }
                Spacer(Modifier.height(12.dp))
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3D8)), shape = RoundedCornerShape(14.dp)) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SportsSoccer, contentDescription = null, tint = Color(0xFF8A5A00))
                        Spacer(Modifier.width(8.dp))
                        Text("ملاحظة: المباريات المعروضة أمثلة للتصميم وليست جدولًا حقيقيًا.", fontSize = 12.sp, color = Color(0xFF654500))
                    }
                }
                Spacer(Modifier.height(12.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 20.dp)) {
                    items(visibleMatches) { match ->
                        MatchCard(match = match, onOpenOfficial = onOpenOfficial)
                    }
                    item {
                        Text("الحجز والدفع يتمان على موقع الجهة الرسمية. لا تدخل بيانات الدفع داخل هذا التطبيق التجريبي.", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MatchCard(match: Match, onOpenOfficial: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(match.competition, color = Green, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                Surface(color = Color(0xFFE8F5EE), shape = RoundedCornerShape(20.dp)) {
                    Text(match.status, color = Green, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp))
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(match.home, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Dark)
                Text("VS", color = Green, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 10.dp))
                Text(match.away, modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontWeight = FontWeight.Bold, fontSize = 17.sp, color = Dark)
            }
            Spacer(Modifier.height(8.dp))
            Text(match.date, color = Color.Gray, fontSize = 12.sp)
            Spacer(Modifier.height(14.dp))
            Button(onClick = onOpenOfficial, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Default.ConfirmationNumber, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("افتح موقع التذاكر الرسمي")
            }
        }
    }
}
