package com.example.guia1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*


class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabasOpenHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        dbHelper = DatabasOpenHelper(this)
        setContent {
            AddUser()

            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddUser(){
        var name by remember { mutableStateOf("") }
        var lastname by remember { mutableStateOf("") }
        var age by remember { mutableStateOf("") }

        //estado para el genero
        var gender by remember { mutableStateOf("") }
        val genderOptions = listOf("Male", "Female","Other")
        var expanded by remember { mutableStateOf(false) }


        var phone by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }

        var users by remember { mutableStateOf(dbHelper.getAllUsers()) }

        Column (modifier = Modifier.padding(50.dp)){
            TextField(value = name, onValueChange = {name=it}, label = { Text("Name")})
            TextField(value = lastname, onValueChange = {lastname=it}, label = { Text("LastName")})
            TextField(value = age, onValueChange = {age=it}, label = { Text("Age")})


            //menu desplegable
            OutlinedTextField(
                value = gender,
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender")},
                modifier = Modifier.width(280.dp),
                trailingIcon = {
                    IconButton(onClick = {expanded = !expanded}) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Expand Menu"
                        )
                    }
                }

            )

            //DropDown
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {expanded=false}
            ) {
                genderOptions.forEach{ selectionOption ->
                    DropdownMenuItem (
                        text = {Text(selectionOption)},
                        onClick = {
                            gender = selectionOption
                            expanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = phone, onValueChange = {phone=it}, label = { Text("Phone")})
            TextField(value = email, onValueChange = {email=it}, label = { Text("Email")})

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (dbHelper.InsertUser(name, lastname, age.toIntOrNull()?:0, gender, phone, email ) ) {

                    Toast.makeText(this@MainActivity, "Usuario ingresado correctamente", Toast.LENGTH_LONG).show()
                    users = dbHelper.getAllUsers()
                }else{
                    Toast.makeText(this@MainActivity, "Error al insertar el usuario", Toast.LENGTH_LONG).show()
                }
            }){
                Text(text ="Insert User")
            }

            Spacer(modifier = Modifier.height(16.dp))

            //lista de usuarios

            LazyColumn(modifier = Modifier.fillMaxSize()){
              items(users){ user ->
                  UserRow(user)
                }
            }

        }

    }

    @Composable
    fun UserRow(user: Map<String, Any>) {
        Column(modifier = Modifier.padding(8.dp).fillMaxSize()) {
            Text(text="Name: ${user["name"]}")
            Text(text="Last Name: ${user["lastname"]}")
            Text(text="Age: ${user["age"]}")
            Text(text="Gender: ${user["gender"]}")
            Text(text="Phone: ${user["phone"]}")
            Text(text="Email: ${user["email"]}")

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}









