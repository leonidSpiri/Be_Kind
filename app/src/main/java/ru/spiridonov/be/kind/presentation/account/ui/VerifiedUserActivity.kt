package ru.spiridonov.be.kind.presentation.account.ui

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import ru.spiridonov.be.kind.BeKindApp
import ru.spiridonov.be.kind.databinding.ActivityVerifiedUserBinding
import ru.spiridonov.be.kind.presentation.account.AccountViewModel
import ru.spiridonov.be.kind.presentation.viewmodels.ViewModelFactory
import ru.spiridonov.be.kind.utils.ShowAlert
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class VerifiedUserActivity : AppCompatActivity() {
    private lateinit var pDialog: ProgressDialog
    private var imagePassportFirstUri: Uri? = null
    private var imagePassportSecondUri: Uri? = null
    private var imageCertUri: Uri? = null
    private var imageName = ""
    private var imageIndicatorName = ""
    private val binding by lazy {
        ActivityVerifiedUserBinding.inflate(layoutInflater)
    }
    private val component by lazy {
        (application as BeKindApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)[AccountViewModel::class.java]
        setContentView(binding.root)
        requestCameraPermission()
        handlerClickListener()
    }


    private fun handlerClickListener() {
        binding.btnFirstPassportCamera.setOnClickListener {
            if (allPermissionsGranted()) {
                imageIndicatorName = "FirstPassport"
                imagePassportFirstUri = null
                binding.imgFirstPassport.setImageDrawable(null)
                imagePassportFirstUri = openCameraInterface()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePassportFirstUri)
                resultPhotoUri.launch(intent)
            } else
                requestCameraPermission()
        }

        binding.btnFirstPassportGallery.setOnClickListener {
            imagePassportFirstUri = null
            binding.imgFirstPassport.setImageDrawable(null)
            val intent = Intent(Intent.ACTION_PICK)
            imageIndicatorName = "FirstPassport"
            intent.type = "image/*"
            resultPhotoUri.launch(intent)
        }

        binding.btnSecondPassportCamera.setOnClickListener {
            if (allPermissionsGranted()) {
                imagePassportSecondUri = null
                binding.imgSecondPassport.setImageDrawable(null)
                imageIndicatorName = "SecondPassport"
                imagePassportSecondUri = openCameraInterface()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePassportSecondUri)
                resultPhotoUri.launch(intent)
            } else
                requestCameraPermission()
        }

        binding.btnSecondPassportGallery.setOnClickListener {
            imagePassportSecondUri = null
            binding.imgSecondPassport.setImageDrawable(null)
            imageIndicatorName = "SecondPassport"
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultPhotoUri.launch(intent)
        }

        binding.btnCertCamera.setOnClickListener {
            if (allPermissionsGranted()) {
                imageCertUri = null
                binding.imgCert.setImageDrawable(null)
                imageIndicatorName = "Cert"
                imageCertUri = openCameraInterface()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCertUri)
                resultPhotoUri.launch(intent)
            } else
                requestCameraPermission()
        }

        binding.btnCertGallery.setOnClickListener {
            imageCertUri = null
            binding.imgCert.setImageDrawable(null)
            imageIndicatorName = "Cert"
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultPhotoUri.launch(intent)
        }

        binding.btnComplete.setOnClickListener {
            if (imagePassportFirstUri == null && imagePassportSecondUri == null && imageCertUri == null) {
                ShowAlert(this, "Загрузите хотя бы одно фото")
                return@setOnClickListener
            }
            if ((imagePassportFirstUri == null && imagePassportSecondUri != null)
                || (imagePassportFirstUri != null && imagePassportSecondUri == null)
            ) {
                ShowAlert(this, "Загрузите оба фото паспорта")
                return@setOnClickListener
            }
            pDialog = ProgressDialog(this)
            pDialog.setMessage("Загрузка...")
            pDialog.show()

            if (imagePassportFirstUri != null && imagePassportSecondUri != null) {
                var bitmapPhoto = BitmapFactory.decodeStream(
                    contentResolver.openInputStream(
                        imagePassportFirstUri!!
                    )
                )
                viewModel.uploadPhoto(bitmapPhoto, "FirstPassport")

                bitmapPhoto = BitmapFactory.decodeStream(
                    contentResolver.openInputStream(
                        imagePassportSecondUri!!
                    )
                )
                viewModel.uploadPhoto(bitmapPhoto, "SecondPassport")
            }

            if (imageCertUri != null) {
                val bitmapPhoto = BitmapFactory.decodeStream(
                    contentResolver.openInputStream(
                        imageCertUri!!
                    )
                )
                viewModel.uploadPhoto(bitmapPhoto, "Cert")
            }
            if (pDialog.isShowing) pDialog.dismiss()
            val text = "Фото будет загружено в фоновом режиме"
            ShowAlert(this, text)
        }
    }

    private fun openCameraInterface(): Uri? {
        imageName = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/be_kind")
            }
        }
        return contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    private var resultPhotoUri =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                when (imageIndicatorName) {
                    "FirstPassport" -> {
                        if (imagePassportFirstUri?.path == null)
                            imagePassportFirstUri = result.data?.data
                        binding.imgFirstPassport.setImageURI(imagePassportFirstUri)
                    }
                    "SecondPassport" -> {
                        if (imagePassportSecondUri?.path == null)
                            imagePassportSecondUri = result.data?.data
                        binding.imgSecondPassport.setImageURI(imagePassportSecondUri)
                    }
                    "Cert" -> {
                        if (imageCertUri?.path == null)
                            imageCertUri = result.data?.data
                        binding.imgCert.setImageURI(imageCertUri)
                    }
                }
            } else
                ShowAlert(this, "Ошибка при выборе фото")
        }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission(): Boolean {
        if (!allPermissionsGranted())
            requestMultiplePermissions.launch(REQUIRED_PERMISSIONS)
        return true
    }

    private val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            if (!it.value)
                ShowAlert(
                    this,
                    "${it.key} permission was denied. Unable to take a picture."
                )
        }
    }

    companion object {
        private const val FILENAME_FORMAT = "_yyyyMMdd_HHmmss"

        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

        fun newIntent(context: Context) = Intent(context, VerifiedUserActivity::class.java)
    }
}