package com.rikkei.training.activity.chatapp.view.fragment.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.databinding.FragmentProfileBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.view.SHARE_PREF_LANG
import com.rikkei.training.activity.chatapp.view.SHARE_PREF_NAME
import com.rikkei.training.activity.chatapp.view.fragment.login.LoginFragment
import com.rikkei.training.activity.chatapp.viewmodel.profile.ProfileViewModel

class ProfileFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }

    private val viewModel: ProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainInterface.showNavigation()
        val sharePref = context?.getSharedPreferences(SHARE_PREF_NAME, AppCompatActivity.MODE_PRIVATE)
        val lang = sharePref?.getString(SHARE_PREF_LANG,"")
        if(lang != null){
            if(lang == "vi"){
                binding.tvLanguages.text = context?.resources?.getString(R.string.viet_nam)
            }
            if(lang == "en"){
                binding.tvLanguages.text = context?.resources?.getString(R.string.english)
            }
        }else{
            binding.tvLanguages.text = context?.resources?.getString(R.string.viet_nam)
        }

        viewModel.user.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.tvUserName.text = it.name
                binding.tvUserEmail.text = it.email
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
        binding.viewLogout.setOnClickListener {
            Firebase.auth.signOut()
            parentFragmentManager.commit {
                replace(R.id.container_view, LoginFragment.Instance(mainInterface))
            }
        }

        binding.imageLanguagesArrow.setOnClickListener {
            showChangeLanguagesDialog()
        }
        binding.tvLanguages.setOnClickListener {
            showChangeLanguagesDialog()
        }
        return binding.root
    }
    fun showChangeLanguagesDialog(){
        val listLanguages = arrayOf("Việt Nam", "English")
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context?.resources?.getString(R.string.choose_language))
        builder.setSingleChoiceItems(listLanguages, -1, object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                if(which == 0){
                    mainInterface.setLocal("vi")
                }
                if(which == 1){
                    mainInterface.setLocal("en")
                }
                dialog?.dismiss()
            }
        })
        val dialog = builder.create()
        dialog.show()
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = ProfileFragment(mainInterface)
    }
}