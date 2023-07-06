package com.example.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chat.ui.theme.StudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Conversation(SampleData.conversationSample)
                }
            }
        }
    }
}

// 定义一个对象 数据类型
data class Message(val author: String, val body: String)

@Composable
fun Conversation(messages: List<Message>){
    LazyColumn(){
        items(messages){
            item: Message ->  MessageCard(msg = item)
        }

    }
}

@Composable
fun MessageCard(msg: Message) {
    Row(modifier = Modifier.padding(4.dp)) {
        Image(
            painter = painterResource(id = R.drawable.baseline_directions_bike_24),
            contentDescription = "用户头像",
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.secondary)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column() {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.headlineMedium
                )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(shape = RoundedCornerShape(4.dp)) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

}

@Preview
@Composable
fun PreviewConversation(){
    Conversation(SampleData.conversationSample)
}

@Preview
@Composable
fun PreviewMessageCard() {
    MessageCard(msg = Message("Leo Lee", "Hello! this is a jetpack compos app"))
}