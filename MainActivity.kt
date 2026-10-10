package com.arfanshah.schoolmanagement

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class UserRole { CHAIRMAN, TEACHER, PARENT }
enum class AppLanguage { ENGLISH, URDU, ARABIC }

data class Student(val name:String,val className:String,val attendance:String,val feeStatus:String)

private val students = listOf(
    Student("Muhammad Ahmed","Class 5-A","Present","Paid"),
    Student("Ayesha Noor","Class 5-A","Present","Due"),
    Student("Hassan Ali","Class 5-A","Absent","Paid")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SchoolApp() }
    }
}

@Composable fun SchoolApp() {
    var language by remember { mutableStateOf(AppLanguage.ENGLISH) }
    var role by remember { mutableStateOf<UserRole?>(null) }
    MaterialTheme {
        Surface(Modifier.fillMaxSize()) {
            if (role == null) LoginScreen(language,{language=it}){role=it}
            else DashboardScreen(role!!,language,{language=it}){role=null}
        }
    }
}

@Composable fun LanguageSelector(language:AppLanguage,onChange:(AppLanguage)->Unit) {
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.Center) {
        FilterChip(language==AppLanguage.ENGLISH,{onChange(AppLanguage.ENGLISH)},{Text("English")})
        Spacer(Modifier.width(6.dp))
        FilterChip(language==AppLanguage.URDU,{onChange(AppLanguage.URDU)},{Text("اردو")})
        Spacer(Modifier.width(6.dp))
        FilterChip(language==AppLanguage.ARABIC,{onChange(AppLanguage.ARABIC)},{Text("العربية")})
    }
}

fun appTitle(l:AppLanguage)=when(l){
    AppLanguage.ENGLISH->"Arfan Shah School Management System"
    AppLanguage.URDU->"عرفان شاہ اسکول مینجمنٹ سسٹم"
    AppLanguage.ARABIC->"نظام إدارة مدرسة عرفان شاہ"
}

@Composable fun LoginScreen(language:AppLanguage,onLanguageChange:(AppLanguage)->Unit,onRoleSelected:(UserRole)->Unit) {
    var mobile by remember{mutableStateOf("")}
    var password by remember{mutableStateOf("")}
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),horizontalAlignment=Alignment.CenterHorizontally){
        Spacer(Modifier.height(20.dp))
        Text("🏫",style=MaterialTheme.typography.displaySmall)
        Text(appTitle(language),style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold,textAlign=TextAlign.Center)
        Text("Demo Star School",style=MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(18.dp))
        LanguageSelector(language,onLanguageChange)
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(mobile,{mobile=it},Modifier.fillMaxWidth(),label={Text("Mobile / Email")},singleLine=true)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(password,{password=it},Modifier.fillMaxWidth(),label={Text("Password")},singleLine=true)
        Spacer(Modifier.height(14.dp))
        Button({onRoleSelected(UserRole.CHAIRMAN)},Modifier.fillMaxWidth()){Text("Chairman / Admin Demo")}
        Spacer(Modifier.height(7.dp))
        OutlinedButton({onRoleSelected(UserRole.TEACHER)},Modifier.fillMaxWidth()){Text("Teacher Demo")}
        Spacer(Modifier.height(7.dp))
        OutlinedButton({onRoleSelected(UserRole.PARENT)},Modifier.fillMaxWidth()){Text("Parent Demo")}
        Spacer(Modifier.height(18.dp))
        QuranCard(language)
        Spacer(Modifier.height(10.dp))
        Text("Mobile test build • Demo data only",style=MaterialTheme.typography.bodySmall)
    }
}

@Composable fun QuranCard(language:AppLanguage) {
    val h=when(language){AppLanguage.ENGLISH->"Quran Education";AppLanguage.URDU->"قرآن تعلیم";AppLanguage.ARABIC->"تعليم القرآن"}
    val b=when(language){AppLanguage.ENGLISH->"Quran education is a required part of the school program.";AppLanguage.URDU->"قرآن کی تعلیم اسکول کے پروگرام کا لازمی حصہ ہے۔";AppLanguage.ARABIC->"تعليم القرآن جزء أساسي من البرنامج المدرسي."}
    Card(Modifier.fillMaxWidth()){Column(Modifier.padding(15.dp)){Text(h,fontWeight=FontWeight.Bold);Spacer(Modifier.height(5.dp));Text(b);Spacer(Modifier.height(5.dp));Text("Arabic • Nazra • Hifz/Revision • Daily Lesson")}}
}

@Composable fun DashboardScreen(role:UserRole,language:AppLanguage,onLanguageChange:(AppLanguage)->Unit,onLogout:()->Unit) {
    Scaffold(topBar={TopAppBar(title={Text(when(role){UserRole.CHAIRMAN->"Chairman / Admin";UserRole.TEACHER->"Teacher Dashboard";UserRole.PARENT->"Parent Dashboard"})})}){pad->
        Column(Modifier.fillMaxSize().padding(pad).verticalScroll(rememberScrollState()).padding(16.dp)){
            LanguageSelector(language,onLanguageChange);Spacer(Modifier.height(14.dp))
            when(role){UserRole.CHAIRMAN->ChairmanDashboard();UserRole.TEACHER->TeacherDashboard();UserRole.PARENT->ParentDashboard()}
            Spacer(Modifier.height(18.dp));OutlinedButton(onLogout,Modifier.fillMaxWidth()){Text("Logout")}
        }
    }
}

@Composable fun ChairmanDashboard(){
    Text("School Overview",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
    Spacer(Modifier.height(8.dp))
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){StatCard("Students","512",Modifier.weight(1f));StatCard("Teachers","48",Modifier.weight(1f))}
    Spacer(Modifier.height(8.dp))
    Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)){StatCard("Classes","24",Modifier.weight(1f));StatCard("Staff","32",Modifier.weight(1f))}
    Spacer(Modifier.height(10.dp))
    listOf(
        "Manage Students — Add, edit, search and view records",
        "Teachers & Salary — Profile, class assignment, salary and payment status",
        "Classes — Classes, sections, subjects and strength",
        "Fees & Accounts — Fee collection, dues, advances and accounts",
        "Attendance & Reports — Overall attendance and performance",
        "Quran Education — Syllabus, lessons, Nazra and revision progress",
        "School Notices — Notices for teachers and parents"
    ).forEach{DashboardAction(it.substringBefore(" — "),it.substringAfter(" — "))}
}

@Composable fun TeacherDashboard(){
    Text("My Class",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
    Text("Assigned: Class 5-A • 32 students");Spacer(Modifier.height(10.dp))
    listOf(
        "My Students — Only students assigned to this teacher",
        "Mark Attendance — Present / absent / leave",
        "Homework — Add and review homework",
        "Daily Learning — Add daily lesson and notes",
        "Quran Lesson — Record today's Quran lesson and progress",
        "Leave Request — Submit teacher leave request",
        "Notices — Read school announcements"
    ).forEach{DashboardAction(it.substringBefore(" — "),it.substringAfter(" — "))}
    Spacer(Modifier.height(10.dp));Text("Sample Students",fontWeight=FontWeight.Bold)
    students.forEach{StudentRow(it)}
}

@Composable fun ParentDashboard(){
    Text("My Child",style=MaterialTheme.typography.headlineSmall,fontWeight=FontWeight.Bold)
    Spacer(Modifier.height(8.dp))
    listOf(
        "Child Profile — Class 5-A • Student information",
        "Attendance — View attendance and absence record",
        "Homework — View current homework",
        "Fees — Paid, due and previous balance",
        "Performance — Academic and Quran education progress",
        "Notices — School announcements for parents"
    ).forEach{DashboardAction(it.substringBefore(" — "),it.substringAfter(" — "))}
}

@Composable fun DashboardAction(title:String,description:String){
    Card(Modifier.fillMaxWidth().padding(vertical=3.dp)){Column(Modifier.padding(13.dp)){Text(title,fontWeight=FontWeight.Bold);Text(description)}}
}

@Composable fun StatCard(title:String,value:String,modifier:Modifier=Modifier){
    Card(modifier){Column(Modifier.padding(13.dp),horizontalAlignment=Alignment.CenterHorizontally){Text(value,style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold);Text(title)}}
}

@Composable fun StudentRow(s:Student){
    Card(Modifier.fillMaxWidth().padding(vertical=2.dp)){Column(Modifier.padding(11.dp)){Text(s.name,fontWeight=FontWeight.Bold);Text("${s.className} • Attendance: ${s.attendance} • Fee: ${s.feeStatus}")}}
}
