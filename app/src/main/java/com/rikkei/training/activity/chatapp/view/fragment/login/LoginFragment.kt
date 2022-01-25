package com.rikkei.training.activity.chatapp.view.fragment.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.data.model.Message
import com.rikkei.training.activity.chatapp.databinding.FragmentLoginBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.view.fragment.message.MessageFragment
import com.rikkei.training.activity.chatapp.viewmodel.login.LoginViewModel

class LoginFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainInterface.hideNavigation()
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.apply {
            loginBtn.setOnClickListener {
                if (isVaildate()) {
                    viewModel.login(
                        binding.loginEmail.text.toString(),
                        binding.loginPass.text.toString()
                    )
                } else {
                    Toast.makeText(context, "Field cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            loginDk.setOnClickListener {
                parentFragmentManager.commit {
                    replace(R.id.container_view, SignUpFragment.Instance(mainInterface))
                }
            }
        }
        viewModel.isSuccessFul.observe(this, Observer {
            if (it) {
                Log.d("Login", "Login success")
                parentFragmentManager.commit {
                    replace(R.id.container_view, MessageFragment.Instance(mainInterface))
                }
            } else {
                Log.d("Login", "Login failed")
            }
        })
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = LoginFragment(mainInterface)
    }

    fun isVaildate(): Boolean {
        if (binding.loginEmail.text.isNullOrEmpty() || binding.loginPass.text.isNullOrEmpty()) {
            return false
        }
        return true
    }
}