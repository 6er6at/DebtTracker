package com.example.debttracker

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.debttracker.ui.theme.DebtTrackerTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DebtTrackerTheme {
                DebtTrackerApp()
            }
        }
    }
}
@Composable
fun DebtTrackerApp() {
    var selectedTab by remember {
        mutableStateOf(0)
    }
    var showAddDebt by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    val debtviewModel: DebtViewModel = viewModel(
        factory = AndroidViewModelFactory.getInstance(
            context.applicationContext as Application
        )
    )
    val debts by debtviewModel.debts.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Учёт долгов",
                fontSize = 28.sp
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text(
                text = "Контроль ваших долгов в одном месте",
                fontSize = 20.sp
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Text(
                text = "Долгов в базе: ${debts.size}",
                fontSize = 18.sp
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedTab = 0
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Мне должны",
                        fontSize = 15.sp
                    )

                    if (selectedTab == 0) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(Color.Black)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedTab = 1
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Я должен",
                        fontSize = 15.sp
                    )

                    if (selectedTab == 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(Color.Black)
                        )
                    }
                }
            }

        }
        Button(
            onClick = {
                showAddDebt = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("Добавить долг")
        }
    }
}

