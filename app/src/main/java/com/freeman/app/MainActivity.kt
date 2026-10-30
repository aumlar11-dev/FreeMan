package com.freeman.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext

private val Purple = Color(0xFF9B78FF)
private val Dark = Color(0xFF100D1B)
private val Panel = Color(0xFF211B32)
private val Mint = Color(0xFF65D6C4)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { FreeManApp() }
    }
}

data class Scene(
    val title: String,
    val visual: String,
    val dialogue: String,
    val voice: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreeManApp() {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var idea by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("رعب وتشويق") }
    var length by remember { mutableStateOf("فيلم قصير") }
    var voice by remember { mutableStateOf("راوي عراقي") }
    var scenes by remember { mutableStateOf(listOf<Scene>()) }
    var tab by remember { mutableIntStateOf(0) }

    val voices = listOf(
        "راوي عراقي", "شاب طيب", "شخصية شريرة",
        "امرأة عراقية", "طفل", "شخصية مسنّة"
    )

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Purple,
            secondary = Mint,
            background = Dark,
            surface = Panel
        )
    ) {
        Scaffold(
            containerColor = Dark,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("FREE MAN", fontWeight = FontWeight.Black)
                            Text(
                                "استوديو صناعة الأفلام",
                                style = MaterialTheme.typography.labelSmall,
                                color = Mint
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Dark
                    )
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Panel) {
                    NavigationBarItem(
                        selected = tab == 0,
                        onClick = { tab = 0 },
                        icon = { Text("✍") },
                        label = { Text("المشاريع") }
                    )
                    NavigationBarItem(
                        selected = tab == 1,
                        onClick = { tab = 1 },
                        icon = { Text("🎙") },
                        label = { Text("الأصوات") }
                    )
                    NavigationBarItem(
                        selected = tab == 2,
                        onClick = { tab = 2 },
                        icon = { Text("⚙") },
                        label = { Text("الخدمات") }
                    )
                }
            }
        ) { padding ->
            when (tab) {
                0 -> LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            "حوّل خيالك إلى فيلم",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            "ابدأ بفكرتك ورتّب مشاهد قصتك.",
                            color = Color.LightGray
                        )
                    }

                    item {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Panel
                            )
                        ) {
                            Column(
                                Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text("مشروع جديد", color = Mint)

                                OutlinedTextField(
                                    value = title,
                                    onValueChange = { title = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text("اسم الفيلم") },
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = idea,
                                    onValueChange = { idea = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp),
                                    label = { Text("اكتب فكرة القصة") }
                                )

                                Text("نوع الفيلم")
                                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                    listOf("رعب وتشويق", "دراما", "كوميديا").forEach {
                                        FilterChip(
                                            selected = genre == it,
                                            onClick = { genre = it },
                                            label = { Text(it) }
                                        )
                                    }
                                }

                                Text("مدة الفيلم")
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    listOf("فيلم قصير", "فيلم طويل").forEach {
                                        FilterChip(
                                            selected = length == it,
                                            onClick = { length = it },
                                            label = { Text(it) }
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        scenes = makeScenes(
                                            title.ifBlank { "فيلم بلا عنوان" },
                                            idea.ifBlank { "بداية غامضة تقود إلى سر كبير" },
                                            genre,
                                            length
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("إنشاء مخطط المشاهد")
                                }
                            }
                        }
                    }

                    if (scenes.isNotEmpty()) {
                        item {
                            Text(
                                "مشاهد المشروع",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        items(scenes) { scene ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Panel
                                )
                            ) {
                                Column(
                                    Modifier.padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(7.dp)
                                ) {
                                    Text(scene.title, color = Mint)
                                    Text(scene.visual)
                                    Text("الحوار: ${scene.dialogue}")
                                    Text("الصوت: ${scene.voice}", color = Purple)
                                }
                            }
                        }

                        item {
                            OutlinedButton(
                                onClick = {
                                    val body = scenes.joinToString("\n\n") {
                                        "${it.title}\n${it.visual}\n${it.dialogue}\n${it.voice}"
                                    }
                                    val send = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, title)
                                        putExtra(Intent.EXTRA_TEXT, body)
                                    }
                                    context.startActivity(
                                        Intent.createChooser(send, "تصدير المشروع")
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("تصدير ومشاركة المشاهد")
                            }
                        }
                    }
                }

                1 -> Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "مكتبة الشخصيات الصوتية",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        "اختر الدور الصوتي للمشروع. هذه أسماء مقترحة وليست أصواتاً مولّدة."
                    )
                    voices.forEach { v ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { voice = v },
                            colors = CardDefaults.cardColors(
                                containerColor =
                                    if (voice == v) Purple.copy(alpha = 0.25f)
                                    else Panel
                            )
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(v)
                                Text(if (voice == v) "✓" else "اختيار")
                            }
                        }
                    }
                    Text("الصوت المحدد: $voice", color = Mint)
                }

                else -> Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        "مركز الخدمات",
                        style = MaterialTheme.typography.titleLarge
                    )
                    listOf(
                        "كتابة النصوص: مخطط محلي حالياً",
                        "الصوت العراقي: يحتاج ربط مزود صوت",
                        "توليد الصور: يحتاج ربط خدمة خارجية",
                        "توليد الفيديو: يحتاج مزود فيديو ومفتاح API",
                        "المونتاج والتصدير: مرحلة تطوير لاحقة"
                    ).forEach {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Panel
                            )
                        ) {
                            Text(it, Modifier.padding(16.dp))
                        }
                    }
                    Text(
                        "لن نعرض أي خدمة على أنها تعمل قبل ربطها واختبارها.",
                        color = Mint
                    )
                }
            }
        }
    }
}

private fun makeScenes(
    title: String,
    idea: String,
    genre: String,
    length: String
): List<Scene> {
    val beats = listOf(
        "المقدمة والتعريف بالشخصيات",
        "ظهور المشكلة الأولى",
        "اكتشاف سر غير متوقع",
        "تصاعد الخطر والصراع",
        "مواجهة بين الشخصيات",
        "انقلاب الأحداث",
        "الذروة والقرار الحاسم",
        "النهاية وترك أثر"
    )
    val count = if (length == "فيلم طويل") 8 else 4

    return (0 until count).map { i ->
        Scene(
            "${i + 1}. ${beats[i]}",
            "مشهد $genre من فيلم $title. الفكرة: $idea. استخدم إضاءة سينمائية وحركة كاميرا مناسبة.",
            if (i == 0) "أشعر أن وراء هذا المكان سراً كبيراً."
            else "لن أتراجع، يجب أن أعرف الحقيقة.",
            if (i % 3 == 0) "راوي عراقي"
            else if (i % 3 == 1) "شخصية طيبة"
            else "شخصية شريرة"
        )
    }
}
