package com.rikkei.training.activity.chatapp.view.fragment.profile

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.databinding.FragmentProfileBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.view.fragment.login.LoginFragment
import com.rikkei.training.activity.chatapp.viewmodel.profile.ProfileViewModel

class ProfileFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }

    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainInterface.showNavigation()

        viewModel.user.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.tvUserName.setText(it.name)
                binding.tvUserEmail.setText(it.email)
                val bytes: ByteArray = Base64.decode(it.avatar, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.imageAvatarUser.setImageBitmap(bitmap)
                binding.imageProfile.setImageBitmap(bitmap)
                if (it.avatar != "") {
                    binding.imageAvatarUser.visibility = View.VISIBLE
                    binding.imageAvatarUserDefault.visibility = View.GONE
                }
            }
        })
        // Inflate the layout for this fragment
        binding.editProfile.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.container_view, EditProfileFragment.Instance(mainInterface))
                addToBackStack("Edit")
            }
        }
        binding.imageLogout.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.container_view,LoginFragment.Instance(mainInterface))
                FirebaseAuth.getInstance().signOut()
            }
        }
//        binding

        return binding.root
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = ProfileFragment(mainInterface)
    }
}