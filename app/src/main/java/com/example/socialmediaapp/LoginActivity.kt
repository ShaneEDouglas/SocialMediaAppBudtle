package com.example.socialmediaapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.socialmediaapp.ViewModels.FirebaseViewModels.AuthViewModel
import com.example.socialmediaapp.ViewModels.FirebaseViewModels.AutheticationState
import com.example.socialmediaapp.ViewModels.FirebaseViewModels.DatabaseViewModel
import com.example.socialmediaapp.ViewModels.FirebaseViewModels.UserRepository
import com.example.socialmediaapp.ui.theme.SocialMediaAppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel: AuthViewModel by viewModels()
        val databaseViewModel: DatabaseViewModel by viewModels()


        setContent {
            val CustomColorScheme = lightColorScheme(
                primary = Color(0xFF3f08e6),
                secondary = Color(0xFF752e7d),
                onPrimary = Color.White,
                onSecondary = Color.White
                // Add other colors if necessary
            )
            SocialMediaAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFF0)
                ) {
                    val context = LocalContext.current
                    loginScreen(authViewModel, databaseViewModel, context)


                }
            }
        }
        observeAuthenticationState(authViewModel,databaseViewModel)
    }
    private fun observeAuthenticationState(authViewModel: AuthViewModel, databaseViewModel: DatabaseViewModel){

        authViewModel.authenticationState.observe(this) {state ->
            when (state) {
                AutheticationState.REGISTERED -> {
                    authViewModel.currentUser.observe(this) {user ->
                        user?.let {
                            databaseViewModel.createUser(it)
                            databaseViewModel.createUserWithFireStore(it)
                            authViewModel.clearCurrentUser()
                            Toast.makeText(this, "Sign-up Successful",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                AutheticationState.AUTHENTICATED -> {
                    Toast.makeText(this, "Login Successful",Toast.LENGTH_SHORT).show()
                }

                AutheticationState.UNAUTHENTICATED -> {
                    Toast.makeText(this, "Login or Sign-up Failed",Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }
}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun loginScreen(authViewModel: AuthViewModel, databaseViewModel: DatabaseViewModel,context: Context) {

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3f08e6),
            Color(0xFF752e7d)
        ),

        )

    val customFont = FontFamily(Font(R.font.magmitos))

    var email by remember{ mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSignedUp by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {

        Image(
            painter = painterResource(id = R.drawable.peoplehavingfun),
            contentDescription = "People Having Fun",
            modifier = Modifier
                .size(200.dp)
                .offset(50.dp, (-50).dp)
                .clip(CircleShape)
                .border(4.dp, Color(0xFFFAFAFA), CircleShape)
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
            contentScale = ContentScale.Crop
        )

        //Bottom Image
        Image(
            painter = painterResource(id = R.drawable.friendshavingfun),
            contentDescription = "People Having Fun on beach",
            modifier = Modifier
                .size(160.dp)
                .offset((-50).dp, 50.dp)
                .clip(CircleShape)
                .border(4.dp, Color(0xFFFAFAFA), CircleShape)
                .align(Alignment.BottomStart),


            contentScale = ContentScale.Crop
        )


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = "Budtle",
                textAlign = TextAlign.Center,
                fontSize = 60.sp,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = customFont,
                color = Color(0xFF212121)

            )

            Spacer(modifier = Modifier.padding(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), shape = RoundedCornerShape(50.dp)
            ) {
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent, // Hide underline when focused
                        unfocusedIndicatorColor = Color.Transparent, // Hide underline when unfocused
                        disabledIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                )
            }

            Spacer(modifier = Modifier.padding(10.dp))

            if (isSignedUp) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), shape = RoundedCornerShape(50.dp)
                ) {
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Username") },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                    )
                }
            }

            Spacer(modifier = Modifier.padding(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), shape = RoundedCornerShape(50.dp)
            ) {
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                )
            }


            Button(
                onClick = {
                          if (isSignedUp) {
                              authViewModel.handleSignUp(username,email,password,context)
                          } else {
                              authViewModel.handleLogin(email,password,context)
                            }
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .width(100.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    text = if (isSignedUp) "Sign up" else "Login",
                    fontSize = 20.sp,
                )
            }

            Text(
                modifier = Modifier
                    .clickable { isSignedUp = !isSignedUp }
                    .padding(8.dp),
                text = if (isSignedUp) "Already have an account? Login" else "Don't have an account? Sign Up",
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Divider(
                modifier = Modifier.padding(horizontal = 50.dp), thickness = 3.dp
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                onClick = { /* Handle click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .width(100.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)

                ) {
                    Text(
                        text = if (isSignedUp) "Sign up with Google" else "Login with Google",
                        fontSize = 15.sp,
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Image(
                        painter = painterResource(id = R.drawable.googlelogo),
                        contentDescription = "GoogleLogo",
                        modifier = Modifier.size(32.dp),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SocialMediaAppTheme {

    }
}