package com.sandesh.notesapplication

import android.R.*
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit


class BeforeLoginActivity : AppCompatActivity() {

    //private var mDatabase: DatabaseReference? = null
    private var phoneNumberAuthenticationTrigger = ""
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mVerificationId: String
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null
    private var phoneNumberMainValueForOfficialDetails: String? = null
//
//    var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
//        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//                // This callback will be invoked in two situations:
//                // 1 - Instant verification. In some cases the phone number can be instantly
//                //     verified without needing to send or enter a verification code.
//                // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                //     detect the incoming verification SMS and perform verification without
//                //     user action.
//                Log.d("TAG", "onVerificationCompleted:$credential")
//                signInWithPhoneAuthCredential(credential)
//
//            }
//
//
//            override fun onVerificationFailed(p0: FirebaseException) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w("TAG", "onVerificationFailed", p0)
//                val btnOpenSignIn: Button = findViewById<View>(R.id.btnOpenSignin) as Button
//                val btnOpenSignup: Button = findViewById<View>(R.id.btnOpenSignup) as Button
//                btnOpenSignIn.setVisibility(View.VISIBLE)
//                btnOpenSignup.setVisibility(View.VISIBLE)
//                if (p0 is FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                    // ...
//                } else if (p0 is FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                    // ...
//                }
//
//                // Show a message and update the UI
//                // ...
//            }
//
//            override fun onCodeSent(
//                verificationId: String,
//                token: PhoneAuthProvider.ForceResendingToken
//            ) {
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//                super.onCodeSent(verificationId, token)
//                Log.d("TAG", "onCodeSent:$verificationId")
//                if (phoneNumberAuthenticationTrigger === "signup") {
//                    mVerificationId = verificationId
//                    mResendToken = token
//                    changeViews()
//                } else {
//                    mVerificationId = verificationId
//                    mResendToken = token
//                    val loginFormContainer =
//                        findViewById<View>(R.id.llloginFormContainer) as LinearLayout
//                    val askCodeDuringLogin =
////                        findViewById<View>(R.id.llAskCodeDuringLogin) as LinearLayout
//                    loginFormContainer.visibility = View.GONE
//                    askCodeDuringLogin.visibility = View.VISIBLE
//                }
//
//
//                // Save verification ID and resending token so we can use them later
//                //mVerificationId = verificationId;
//                //mResendToken = token;
//
//                // ...
//            }
//        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_before_login)


        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            openAfterLoginActivity()
        }
        val decorView: View = window.decorView
        val uiOptions: Int = View.SYSTEM_UI_FLAG_FULLSCREEN
        decorView.setSystemUiVisibility(uiOptions)
        val actionbar: ActionBar? = supportActionBar
        if (actionbar != null) {
            actionbar.hide()
        }


        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //LinearLayout llAskPhone = (LinearLayout) findViewById(R.id.llAskPhoneNumber);
        //LinearLayout llAskCode = (LinearLayout) findViewById(R.id.llAskCode);
        val llSignInModule = findViewById<View>(R.id.llSignInContainer) as LinearLayout
        val llSignUpModule = findViewById<View>(R.id.llSignUpContainer) as LinearLayout
        val openSignInModule: Button = findViewById<View>(R.id.btnOpenSignin) as Button
        val openSignUpModule: Button = findViewById<View>(R.id.btnOpenSignup) as Button
        openSignInModule.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                llSignInModule.visibility = View.VISIBLE
                llSignUpModule.visibility = View.GONE
                openSignInModule.setBackgroundColor(resources.getColor(R.color.colorBlack))
                openSignInModule.setTextColor(resources.getColor(R.color.colorWhite))
                openSignUpModule.setBackgroundColor(resources.getColor(R.color.colorWhite))
                openSignUpModule.setTextColor(resources.getColor(R.color.colorBlack))
            }
        })
        openSignUpModule.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                llSignInModule.visibility = View.GONE
                llSignUpModule.visibility = View.VISIBLE
                openSignInModule.setBackgroundColor(resources.getColor(R.color.colorWhite))
                openSignInModule.setTextColor(resources.getColor(R.color.colorBlack))
                openSignUpModule.setBackgroundColor(resources.getColor(R.color.colorBlack))
                openSignUpModule.setTextColor(resources.getColor(R.color.colorWhite))
            }
        })
        val btnRegister: Button = findViewById<View>(R.id.btnRegisterEmail) as Button
//        val editSMScode = findViewById<View>(R.id.editSmsCode) as EditText
//        val btnMatchSMScode: Button = findViewById<View>(R.id.matchSmsCode) as Button
//        btnMatchSMScode.setOnClickListener(object : View.OnClickListener {
//            override fun onClick(view: View?) {
//                val txtViewProgress = findViewById<View>(R.id.progressBar) as TextView
//                txtViewProgress.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
//                txtViewProgress.text = "PLEASE WAIT"
//                editSMScode.isEnabled = false
//                btnMatchSMScode.setEnabled(false)
//                val credential: PhoneAuthCredential =
//                    PhoneAuthProvider.getCredential(mVerificationId, editSMScode.text.toString())
//                signInWithPhoneAuthCredential(credential)
//            }
//        })
        btnRegister.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val registerEmail = findViewById<View>(R.id.editTextRegisterEmail) as EditText
                val registerPassword = findViewById<View>(R.id.editTextRegisterPassword) as EditText
                if (registerEmail.text.toString().isEmpty() || registerPassword.text.toString()
                        .isEmpty()
                ) {
                    Toast.makeText(applicationContext,"Try again.",Toast.LENGTH_SHORT).show()
                }else{
                    mAuth.createUserWithEmailAndPassword(registerEmail.text.toString(), registerPassword.text.toString()).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success")
//                            val user = auth.currentUser
                            //updateUI(user)
                            Toast.makeText(applicationContext,"register successful",Toast.LENGTH_SHORT).show()
                            openAfterLoginActivity()
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                            Toast.makeText(
//                                baseContext,
//                                "Authentication failed.",
//                                Toast.LENGTH_SHORT,
//                            ).show()
                            //updateUI(null)
                            Toast.makeText(applicationContext,"register unsuccessful",Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        })
        val signInButton: Button = findViewById<View>(R.id.signInButton) as Button
        signInButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val etEmailId = findViewById<View>(R.id.editLogInEmailID) as EditText
                val etPassword = findViewById<View>(R.id.editLogInPassword) as EditText
//                val etPhoneNumber = findViewById<View>(R.id.editLoginWithPhone) as EditText
//                if (etEmailId.text.toString().isEmpty() && etPassword.text.toString()
//                        .isEmpty() && etPhoneNumber.text.toString().isEmpty()
//                ) {
//                    Toast.makeText(
//                        applicationContext,
//                        "Enter details. cannot login",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else if (etPhoneNumber.text.toString().isEmpty() && (etEmailId.text.toString()
//                        .isEmpty() || etPassword.text.toString().isEmpty())
//                ) {
//                    Toast.makeText(
//                        applicationContext,
//                        "Email id or password cannot be empty",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else if (!etPhoneNumber.text.toString().isEmpty()) {
//                    phoneNumberAuthenticationTrigger = "signin"
//                    val btnOpenSignIn: Button = findViewById<View>(R.id.btnOpenSignin) as Button
//                    val btnOpenSignup: Button = findViewById<View>(R.id.btnOpenSignup) as Button
//                    btnOpenSignIn.setVisibility(View.GONE)
//                    btnOpenSignup.setVisibility(View.GONE)
//                    phoneNumberMainValueForOfficialDetails = etPhoneNumber.text.toString()
//                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                        etPhoneNumber.text.toString(),  // Phone number to verify
//                        90,  // Timeout duration
//                        TimeUnit.SECONDS,  // Unit of timeout
//                        this@BeforeLoginActivity,  // Activity (for callback binding)
//                        mCallbacks
//                    )
//                } else if (etPhoneNumber.text.toString().isEmpty()) {
//                    //sign-in with email here
//                    //sign-in with email here
//                }

                if (etEmailId.text.toString().isEmpty() || etPassword.text.toString()
                        .isEmpty()
                ) {
                    Toast.makeText(applicationContext,"Try again.",Toast.LENGTH_SHORT).show()
                }else{
                    mAuth.signInWithEmailAndPassword(etEmailId.text.toString(), etPassword.text.toString())
                        .addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
//                                Log.d(TAG, "signInWithEmail:success")
//                                val user = auth.currentUser
//                                updateUI(user)
                                Toast.makeText(applicationContext,"signin successful",Toast.LENGTH_SHORT).show()
                                openAfterLoginActivity()
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(applicationContext,"signin unsuccessful",Toast.LENGTH_SHORT).show()
                            }
                        }
                }
//                mAuth.createUserWithEmailAndPassword(etEmailId.text.toString(), etPassword.text.toString()).addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            // Sign in success, update UI with the signed-in user's information
////                            Log.d(TAG, "createUserWithEmail:success")
////                            val user = auth.currentUser
//                            //updateUI(user)
//                            Toast.makeText(applicationContext,"sign in successful",Toast.LENGTH_SHORT).show()
//                        } else {
//                            // If sign in fails, display a message to the user.
////                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
////                            Toast.makeText(
////                                baseContext,
////                                "Authentication failed.",
////                                Toast.LENGTH_SHORT,
////                            ).show()
//                            //updateUI(null)
//                        }
//                    }


            }
        })
    }

    fun openAfterLoginActivity() {
        val myIntent = Intent(this@BeforeLoginActivity, MainActivity::class.java)
        this@BeforeLoginActivity.startActivity(myIntent)
        finish()
        overridePendingTransition(0, 0)
    }

//    fun addPersonalDetails(
//        valueFirstName: String?,
//        valueMiddleName: String?,
//        valueLastName: String?
//    ) {
//        val userID: String = FirebaseAuth.getInstance().getCurrentUser()!!.getUid()
//        mDatabase = FirebaseDatabase.getInstance().reference
//        mDatabase!!.child("users").child(userID).child("personalDetails").child("first_name")
//            .setValue(valueFirstName)
//        mDatabase!!.child("users").child(userID).child("personalDetails").child("middle_name")
//            .setValue(valueMiddleName)
//        mDatabase!!.child("users").child(userID).child("personalDetails").child("last_name")
//            .setValue(valueLastName)
//        //mDatabase.child("users").child(userID).child("personalDetails").addListenerForSingleValueEvent(new ValueEventListener() {
//        //@Override
//        //public void onDataChange(@NonNull DataSnapshot snapshot) {
//        //if (snapshot.exists()){
//        //openAfterLoginActivity();
//        //progBar.setIndeterminate(false);
//        //progBar.setProgress(0);
//        //}else{
//
//        //Toast.makeText(getApplicationContext(),"its empty",Toast.LENGTH_SHORT).show();
//
//        //}
//        //}
//        //@Override
//        //public void onCancelled(@NonNull DatabaseError error) {
//
//        //}
//        //});
//    }
}

