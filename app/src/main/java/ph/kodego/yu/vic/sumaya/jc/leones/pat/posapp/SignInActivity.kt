package ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.ActivityMainBinding
import ph.kodego.yu.vic.sumaya.jc.leones.pat.posapp.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //TODO: ADD FIREBASE SIGN IN
    }
}