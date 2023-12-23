package com.bhaskarblur.gptbot

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.compose.rememberAsyncImagePainter
import com.bhaskarblur.gptbot.models.messageModel
import com.bhaskarblur.gptbot.ui.theme.GptBotTheme
import com.bhaskarblur.gptbot.viewModel.chatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.security.auth.callback.Callback

@AndroidEntryPoint
class ChatScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: chatViewModel by viewModels()
        setContent {
            var text by remember { mutableStateOf("") }
            val focusManager = LocalFocusManager.current
            val context = LocalContext.current
            val listScrollState = rememberLazyListState()
            val chatContinueAsk = remember {
                mutableStateOf(false)
            }
            val showDialog = remember {
                mutableStateOf(false)
            }
            if (showDialog. value) {
                AlertDialogComponent(context, onYesPressed = {
                    chatContinueAsk.value = true
                    viewModel.clearMessageHistory()
                }, onClose = {
                    showDialog.value = false;
                })
            }
            val messageList = viewModel.chatList.value!!

            viewModel.chatList.observe(this@ChatScreen) {list ->
                list.forEach {
                    if(!messageList.contains(it)) {
                        messageList.add(it)
                    }
                }

            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF111213))
            )
            {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF191A1B))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        modifier = Modifier
                            .height(42.dp), verticalAlignment = Alignment.CenterVertically
                    ) {
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

                    Icon(Icons.Filled.Refresh, "Restart Icon",
                        tint = Color.White, modifier = Modifier.clickable {
                            showDialog. value = true
                        })
                }

                LazyColumn(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .fillMaxHeight(0.86f)
                        .scrollable(listScrollState, Orientation.Vertical)
                ) {
                    lifecycleScope.launch {
                        if (messageList.size > 0) {
                            listScrollState.animateScrollToItem(messageList.size - 1)
                        }
                    }
                    items(items = messageList) {
                        MessageTile(it);
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .wrapContentHeight(Alignment.Bottom)
                        .background(Color(0xFF191A1B)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    if (messageList.size > 1 && !chatContinueAsk.value) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp, top = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Button(
                                onClick = {
                                    chatContinueAsk.value = true
                                },
                                modifier = Modifier
                                    .height(50.dp)
                                    .weight(1f)
                                    .background(Color(0xFF0746C4), RoundedCornerShape(48.dp)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF0746C4),
                                    contentColor = Color.White
                                )
                            ) {

                                Text(
                                    "Continue Chat", color = Color.White,
                                    fontSize = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(18.dp))
                            Button(
                                onClick = {
                                    showDialog. value = true
                                },
                                modifier = Modifier
                                    .height(50.dp)
                                    .weight(1f)
                                    .background(Color.White, RoundedCornerShape(48.dp)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                )
                            ) {

                                Text(
                                    "Start New Chat", color = Color.Black,
                                    fontSize = 15.sp
                                )
                            }
                        }

                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            TextField(
                                value = text,
                                placeholder = {
                                    Text(
                                        text = "Write anything...",
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Start
                                    )
                                },
                                onValueChange = {
                                    text = it
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.88f)
                                    .height(60.dp),
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

                            Icon(
                                painter = painterResource(id = R.drawable.sendmsg),
                                contentDescription = null,
                                Modifier
                                    .size(28.dp)
                                    .clickable {
                                        if (text.length > 0) {
                                            chatContinueAsk.value = true
                                            viewModel.sendMessage(text)
                                            focusManager.clearFocus()
                                            text = ""
                                        }
                                    },
                                tint = Color.White
                            )
                        }
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
                    if (messageModel.sender.equals("user")) Color(0xFF222324) else Color(0xFF0746C4),
                    RoundedCornerShape(10.dp)
                )
                .padding(12.dp)
        ) {
            Text(
                messageModel.message, color = Color.White, fontSize = 14.sp,
                fontWeight = FontWeight.Medium, fontFamily = FontFamily.Default,
                lineHeight = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(
                getDateTime(messageModel.timestamp.toString())!!,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

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
        MessageTile(
            messageModel(
                "Hello there! How are you, how can i help you",
                "assistant",
                System.currentTimeMillis()
            )
        )
    }
}

@Composable
fun AlertDialogComponent(context: Context, onYesPressed: () -> Unit,
                         onClose : () -> Unit) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {

        AlertDialog(

            onDismissRequest = {
                openDialog.value = false
                onClose()
                               },
            title = { Text(text = "Start new chat?", color = Color.White) },

            text = {
                Text(
                    "Are you sure you want to start a new chat, old chat history will be deleted.",
                    color = Color.White
                )
            },


            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onYesPressed()
                        onClose()
                    }
                ) {
                    Text("Yes", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        onClose()
                    }
                ) {
                    // adding text to our button.
                    Text("No", color = Color.White)
                }
            },
            containerColor = Color(0xFF191A1B),
            textContentColor = Color.White
        )

    }
}

