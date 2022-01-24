package com.rikkei.training.activity.chatapp.view.fragment.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rikkei.training.activity.chatapp.R
import com.rikkei.training.activity.chatapp.databinding.FragmentEditProfileBinding
import com.rikkei.training.activity.chatapp.view.MainInterface
import com.rikkei.training.activity.chatapp.viewmodel.profile.EditProfileViewModel
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import android.graphics.Rect
import androidx.activity.result.contract.ActivityResultContracts
import com.rikkei.training.activity.chatapp.databinding.DialogEditProfileBinding

class EditProfileFragment(private val mainInterface: MainInterface) : Fragment() {

    private val binding by lazy { FragmentEditProfileBinding.inflate(layoutInflater) }
    private var imageUri: Uri? = null
    private var baseImage: String? = null
    private val viewModel: EditProfileViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        activity?.window?.statusBarColor =
            context?.let { ContextCompat.getColor(it, R.color.blue_start) }!!
        mainInterface.hideNavigation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner, {
            if (it != null) {
                binding.edtFullName.setText(it.name)
                binding.edtBirthday.setText(it.birthday)
                binding.edtPhoneNumber.setText(it.phone)
                baseImage = it.avatar
                val bytes: ByteArray = Base64.decode(it.avatar, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                binding.imageAvatar.setImageBitmap(bitmap)
                if (it.avatar != "") {
                    binding.imageAvatar.visibility = View.VISIBLE
                }
            }
        })
        binding.run {
            imageCameraPhoto.setOnClickListener {
                OpenDialog()
            }
            imgBack.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            tvSave.setOnClickListener {
                val name = binding.edtFullName.text.toString()
                val birthday = binding.edtBirthday.text.toString()
                val phone = binding.edtPhoneNumber.text.toString()
                if (
                    name.isNotEmpty() ||
                    birthday.isNotEmpty() ||
                    phone.isNotEmpty() ||
                    baseImage.toString() != null
                ) {
                    viewModel.updateInfomation(baseImage.toString(), name, phone, birthday)
                    viewModel.updateState.observe(viewLifecycleOwner, {
                        Toast.makeText(context, getString(it as Int), Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    })
                }
            }

            edtBirthday.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.edtBirthday.clearFocus()
                }
                false
            }
            edtPhoneNumber.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.edtPhoneNumber.clearFocus()
                }
                false
            }
            edtFullName.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.edtFullName.clearFocus()
                }
                false
            }
        }

        CheckKeyBoard()
    }
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if(it){
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            selectImageLauncher1.launch(intent)
            Log.e("TAG", ": granted")
        }else{
            Log.e("TAG", ": denied")
        }
    }
    private val selectImageLauncher1 =
        registerForActivityResult(ActivityResultContracts
        .StartActivityForResult()){
        if(it.resultCode == Activity.RESULT_OK){
            val photo = it.data?.extras?.get("data") as Bitmap
            val stream = ByteArrayOutputStream()
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bytes = stream.toByteArray()
            baseImage = Base64.encodeToString(bytes, Base64.DEFAULT)
            binding.imageAvatar.setImageBitmap(photo)
        }
    }
    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK){
            imageUri = it.data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver!!, imageUri!!)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bytes = stream.toByteArray()
            baseImage = Base64.encodeToString(bytes, Base64.DEFAULT)
            binding.imageAvatar.setImageBitmap(bitmap)
        }
    }

    private fun viewHide() {
        binding.imgBack.visibility = View.GONE
        binding.tvLabelEdit.visibility = View.GONE
        binding.tvSave.visibility = View.GONE
        binding.viewBackground.visibility = View.GONE
    }

    private fun viewShow() {
        binding.imgBack.visibility = View.VISIBLE
        binding.tvLabelEdit.visibility = View.VISIBLE
        binding.tvSave.visibility = View.VISIBLE
        binding.viewBackground.visibility = View.VISIBLE
    }

    private fun CheckKeyBoard() {
        binding.rootEdit.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.rootEdit.getWindowVisibleDisplayFrame(rect)
            val heightDiff = binding.rootEdit.rootView.height - rect.height()
            if (heightDiff > .25 * binding.rootEdit.rootView.height) {
                viewHide()
            } else {
                viewShow()
            }
        }
    }

    private fun OpenDialog() {
        val bindingDialog = DialogEditProfileBinding.inflate(layoutInflater)
        val dialog= context?.let { Dialog(it) }
        dialog?.setContentView(bindingDialog.root)
        dialog?.setTitle("Choose?")

        val window = dialog?.window ?: return

        window.setTitle("Choose?")
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        dialog.setCancelable(true)

        bindingDialog.btnAlbum.setOnClickListener {
            //open album
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImageLauncher.launch(intent)
            dialog.dismiss()
        }
        bindingDialog.btnCamera.setOnClickListener {
            //open camera
            requestPermission.launch(android.Manifest.permission.CAMERA)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainInterface.showNavigation()
    }

    companion object {
        fun Instance(mainInterface: MainInterface) = EditProfileFragment(mainInterface)
    }
}