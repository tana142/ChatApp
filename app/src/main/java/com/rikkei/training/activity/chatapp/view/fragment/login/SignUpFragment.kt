package com.rikkei.training.activity.chatapp.view.fragment.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.databinding.FragmentSignUpBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.viewmodel.login.SignUpViewModel

class SignUpFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentSignUpBinding.inflate(layoutInflater) }
    private lateinit var viewModel: SignUpViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainInterface.hideNavigation()
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        binding.apply {

            //login
            reigsterBtn.setOnClickListener {
                if (isVaildate()) {
                    viewModel.SignUp(
                        binding.reigsiterEmail.text.toString(),
                        binding.registerPass.text.toString()
                    )
                    parentFragmentManager.commit {
                        replace(R.id.container_view, LoginFragment.Instance(mainInterface))
                    }
                } else {
                    Toast.makeText(context, "Field cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            registerDk.setOnClickListener {
                parentFragmentManager.commit {
                    replace(R.id.container_view, LoginFragment.Instance(mainInterface))
                }
            }
            registerBack.setOnClickListener {
                parentFragmentManager.commit {
                    replace(R.id.container_view, LoginFragment.Instance(mainInterface))
                }
            }
            checkboxRegister.setOnClickListener {
                val ic=AppCompatResources.getDrawable(context!!.applicationContext,R.drawable.ic_check)
                checkboxRegister.setImageDrawable(ic)
            }
        }
        viewModel.isSuccessFull.observe(this, Observer {
            if (it) {
                Log.d("Signup", "Signup success")

            } else {
                Log.d("Signup", "Signup failed")
            }
        })

        return binding.root
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = SignUpFragment(mainInterface)
    }

    fun isVaildate(): Boolean {
        if (binding.reigsiterEmail.text.isNullOrEmpty() || binding.registerPass.text.isNullOrEmpty() || binding.reigsiterName.text.isNullOrEmpty()||binding.registerPass.text.length<8) {
            return false
        }
        return true
    }
}


