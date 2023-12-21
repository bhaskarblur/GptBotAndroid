package com.bhaskarblur.gptbot

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.bhaskarblur.gptbot.models.messageModel
import com.bhaskarblur.gptbot.ui.theme.GptBotTheme
import com.bhaskarblur.gptbot.viewModel.chatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ChatScreen : ComponentActivity() {
    private val viewModel: chatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var text by remember { mutableStateOf("") }
            var messageList = viewModel._chatStateFlow.collectAsState()
            val focusManager = LocalFocusManager.current
            val listState = rememberLazyListState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF111213)))
            {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF191A1B)),
                    horizontalArrangement = Arrangement.SpaceBetween) {

                    Row(modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(42.dp), verticalAlignment = Alignment.CenterVertically){
                        Image(
                            painter = rememberAsyncImagePainter("https://chatbot.design/images/chatbot/DIGITAL%20%28RGB%29/PNG/Mark_RGB_Blue.png"),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Gpt Bot",
                                fontSize = 16.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White,
                                modifier = Modifier.align(alignment = Alignment.CenterVertically)
                            )
                    }

                }

                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxHeight(0.88f)
                    .scrollable(listState, Orientation.Vertical)) {
                    items(items = messageList.value) {
                        MessageTile(it);
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .wrapContentHeight(Alignment.Bottom)
                    .background(Color(0xFF191A1B)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically) {

                        TextField(
                            value = text,
                            placeholder = {
                                Text(text = "Write anything...",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Start)
                            },
                            onValueChange = {
                                text = it },
                            modifier = Modifier
                                .fillMaxWidth(0.9f),
                            textStyle = TextStyle(textAlign = TextAlign.Start),
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedTextColor = Color.White,
                                focusedTextColor = Color.White,
                                focusedContainerColor = Color(0xFF111213),
                                unfocusedContainerColor = Color(0xFF111213),
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                cursorColor = Color.White
                            )
                        )
                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(painter = painterResource(id = R.drawable.sendmsg),
                        contentDescription = null,
                        Modifier
                            .size(28.dp)
                            .clickable {
                                if (text.length > 0) {
                                    viewModel.sendMessage(text)
                                    focusManager.clearFocus()
                                    text = ""
                                }
                            },
                        tint = Color.White)
                }
                }
            }
        }
    }
}

@Composable
fun MessageTile(messageModel: messageModel) {

    Column(
        modifier = Modifier
            .padding(horizontal = 18.dp)
            .fillMaxWidth(),
        horizontalAlignment = when {
            messageModel.sender.equals("user") -> Alignment.End
            else -> Alignment.Start
        },
    ) {
        Row(
            modifier = Modifier
                .background(
                    if(messageModel.sender.equals("user")) Color(0xFF222324) else Color(0xFF0746C4),
                    RoundedCornerShape(10.dp)
                )
                .padding(12.dp)) {
            Text(
                messageModel.message, color = Color.White, fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(
                getDateTime(messageModel.timestamp.toString())!!, color = Color.White, fontSize = 11.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

    }
}

private fun getDateTime(s: String): String? {
    return try {
        val sdf: SimpleDateFormat = SimpleDateFormat("MMM dd, hh:mm", Locale.getDefault())
        val netDate = Date(s.toLong())
        sdf.format(netDate)
    } catch (e: Exception) {
        e.toString()
    }
}

@Preview(showBackground = true)
@Composable
fun MessagePreview() {
    GptBotTheme {
        MessageTile(messageModel("Hello there! How are you, how can i help you", "assistant", System.currentTimeMillis()))
    }
}