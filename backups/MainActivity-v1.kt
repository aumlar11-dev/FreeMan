package com.freeman.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { FreeManApp() } }
}

data class Scene(val title: String, val visual: String, val dialogue: String, val voice: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreeManApp() {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var idea by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("رعب وتشويق") }
    var length by remember { mutableStateOf("فيلم طويل") }
    var voice by remember { mutableStateOf("راوي عراقي") }
    var scenes by remember { mutableStateOf(listOf<Scene>()) }
    var selectedTab by remember { mutableIntStateOf(0) }
    val voices = listOf("راوي عراقي", "شاب طيب", "شخصية شريرة", "امرأة عراقية", "طفل", "شخصية مسنّة")

    MaterialTheme(colorScheme = darkColorScheme(primary = androidx.compose.ui.graphics.Color(0xFFB89BFF), secondary = androidx.compose.ui.graphics.Color(0xFF65D6C4))) {
        Scaffold(topBar = { TopAppBar(title = { Column { Text("FREE MAN", fontWeight = FontWeight.Black); Text("استوديو القصص والأفلام", style = MaterialTheme.typography.labelSmall) } }) },
            bottomBar = { NavigationBar { NavigationBarItem(selected = selectedTab == 0, onClick = { selectedTab = 0 }, icon = { Text("✍") }, label = { Text("مشروع") }); NavigationBarItem(selected = selectedTab == 1, onClick = { selectedTab = 1 }, icon = { Text("🎙") }, label = { Text("الأصوات") }); NavigationBarItem(selected = selectedTab == 2, onClick = { selectedTab = 2 }, icon = { Text("⚙") }, label = { Text("الخدمات") }) } }) { pad ->
            when (selectedTab) {
                0 -> LazyColumn(Modifier.fillMaxSize().padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    item { Image(painter = painterResource(id = R.drawable.freeman_home), contentDescription = "صورة واجهة Free Man", modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(20.dp)), contentScale = ContentScale.Crop) }
                    item { Text("اصنع قصتك من الفكرة إلى المشاهد", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
                    item { OutlinedTextField(title, { title = it }, Modifier.fillMaxWidth(), label = { Text("عنوان الفيلم") }, singleLine = true) }
                    item { OutlinedTextField(idea, { idea = it }, Modifier.fillMaxWidth().height(130.dp), label = { Text("اكتب فكرة القصة باللهجة أو بالفصحى") }) }
                    item { Text("النوع") }
                    item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("رعب وتشويق", "دراما", "كوميديا", "مغامرة").forEach { FilterChip(selected = genre == it, onClick = { genre = it }, label = { Text(it) }) } } }
                    item { Text("طول العمل") }
                    item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("فيلم قصير", "فيلم طويل").forEach { FilterChip(selected = length == it, onClick = { length = it }, label = { Text(it) }) } } }
                    item { Button(onClick = { scenes = makeScenes(title.ifBlank { "فيلم بلا عنوان" }, idea.ifBlank { "بداية غامضة تقود إلى سر كبير" }, genre, length) }, Modifier.fillMaxWidth()) { Text("إنشاء مخطط المشاهد") } }
                    if (scenes.isNotEmpty()) {
                        item { Text("المشاهد المقترحة", style = MaterialTheme.typography.titleLarge) }
                        items(scenes) { s -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { Text(s.title, fontWeight = FontWeight.Bold); Text(s.visual); Text("الحوار: ${s.dialogue}"); Text("الصوت المقترح: ${s.voice}", color = MaterialTheme.colorScheme.secondary) } } }
                        item { OutlinedButton(onClick = { val body = scenes.joinToString("\n\n") { "${it.title}\nالصورة: ${it.visual}\nالحوار: ${it.dialogue}\nالصوت: ${it.voice}" }; val send = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_SUBJECT, title); putExtra(Intent.EXTRA_TEXT, body) }; context.startActivity(Intent.createChooser(send, "تصدير مخطط الفيلم")) }, Modifier.fillMaxWidth()) { Text("تصدير ومشاركة المخطط") } }
                    }
                    item { Text("هذه نسخة أولية: تنشئ مخططاً نصياً محلياً. توليد الصوت والفيديو وربط واجهات الخدمات يحتاج إعداد مفاتيح الخدمات أو نماذج محلية لاحقاً.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary) }
                }
                1 -> Column(Modifier.fillMaxSize().padding(pad).padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("مكتبة الشخصيات الصوتية", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("اختار نوع الصوت لتعيينه للمشهد. هذه تسميات وأدوار مقترحة وليست ملفات صوت مولّدة بعد.")
                    voices.forEach { v -> Card(onClick = { voice = v }, colors = CardDefaults.cardColors(containerColor = if (voice == v) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)) { Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(v); Text(if (voice == v) "✓" else "اختيار") } } }
                    Text("الصوت المحدد: $voice")
                    Text("نخطط لدعم أصوات عراقية اصطناعية عامة، من دون استنساخ أصوات يوتيوبرية أو أشخاص حقيقيين.", style = MaterialTheme.typography.bodySmall)
                }
                else -> Column(Modifier.fillMaxSize().padding(pad).padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("مركز الخدمات", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    listOf("مولّد النصوص: محلي حالياً، وربط مزود مجاني لاحقاً", "الصوت العراقي: نموذج مفتوح المصدر يحتاج اختبار توافق الهاتف", "الصور والفيديو: ربط مزودين حسب حدود الاستخدام المجاني", "المونتاج والتصدير: مرحلة تطوير لاحقة").forEach { Card { Text(it, Modifier.padding(14.dp)) } }
                    Text("لا توجد مفاتيح API مضمّنة. لا تشارك مفاتيحك مع أي شخص، وأي خدمة خارجية قد تفرض حدوداً أو تغيّر خطتها المجانية.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}

private fun makeScenes(title: String, idea: String, genre: String, length: String): List<Scene> {
    val count = if (length == "فيلم طويل") 8 else 4
    val beats = listOf("المقدمة والتعريف بالشخصيات", "ظهور المشكلة الأولى", "اكتشاف سر غير متوقع", "تصاعد الخطر والصراع", "مواجهة بين الشخصيات", "انقلاب الأحداث", "الذروة والقرار الحاسم", "النهاية وترك أثر")
    return (0 until count).map { i ->
        val beat = beats[i]
        Scene("${i + 1}. $beat", "مشهد ${genre}: اعرض الفكرة التالية بصرياً: $idea. استخدم إضاءة سينمائية وحركة كاميرا مناسبة.", if (i == 0) "ما الذي يحدث هنا؟ أشعر أن وراء هذا المكان سراً." else "لن أتراجع الآن، يجب أن أعرف الحقيقة.", if (i % 3 == 0) "راوي عراقي" else if (i % 3 == 1) "شخصية طيبة" else "شخصية شريرة")
    }
}
