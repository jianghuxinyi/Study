package com.linanqing.passwordmanager

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.linanqing.passwordmanager.ui.theme.StudyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var showAddCountDialog = remember { mutableStateOf(false)}
            StudyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainContent(showAddCountDialog)
                }
            }
        }
    }
}

@Composable
fun MessageCard(account: Account) {
    Row(modifier = Modifier.padding(4.dp)) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "用户头像",
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                .background(color = MaterialTheme.colorScheme.inverseSurface)
        )

        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false) }

        val surfaceColor by animateColorAsState(
            targetValue = if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )

        Column(
            modifier = Modifier.clickable { isExpanded = !isExpanded }
        ) {
            Text(
                text = account.account,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(4.dp),
                shadowElevation = 1.dp,
                color = surfaceColor
            ) {
                Text(
                    text = account.password,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1
                )
            }
        }
    }

}

@Composable
fun Conversation(accounts: List<Account>) {
    LazyColumn() {
        items(accounts) { item: Account ->
            MessageCard(account = item)
        }

    }
}



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(dialogState: MutableState<Boolean>){
    //var acc = rememberSaveable{mutableStateListOf<Account>()}
    var acc = mutableStateListOf<Account>()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { dialogState.value = true }) {
                /* FAB content */
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
                AddAccountDialog(dialogState,acc)
            }
        }
    ) {
        Conversation(acc)
        // Screen content
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountDialog(dialogState: MutableState<Boolean>, acc: MutableList<Account>){
    val app = LocalContext.current
    Toast.makeText(app,"dialogState:"+dialogState.value,Toast.LENGTH_SHORT).show()
    if(dialogState.value){
        Dialog(onDismissRequest = {dialogState.value = false  },
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
        ) {


            var account by rememberSaveable {
                mutableStateOf(Account("","","",""))
            }
            Card(modifier = Modifier
                .padding(15.dp)
                .wrapContentSize(),
            elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column() {
                    TextField(value = account.account, onValueChange = {
                        account = account.copy(account = it)
                    },
                    label = {
                        Text(text = "账号")
                    })
                    TextField(value = account.password, onValueChange = {
                        account = account.copy(password = it)
                    },
                        label = {
                            Text(text = "密码")
                        })
                    TextField(value = account.email, onValueChange = {
                        account = account.copy(email = it)
                    },
                        label = {
                            Text(text = "邮箱")
                        })
                    TextField(value = account.phone, onValueChange = {
                        account = account.copy(phone = it)
                    },
                        label = {
                            Text(text = "电话")
                        })

                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(onClick = { dialogState.value = false}) {

                            Text(text = "取消")
                        }
                        Button(onClick = {
                            Toast.makeText(app,account.account+""+account.password+""+account.email+""+account.phone,Toast.LENGTH_SHORT).show()
                            acc.add(Account("1","2","3","4"))

                        }) {
                            Text(text = "确认")

                        }

                    }
                }
            }
        }
    }




}
