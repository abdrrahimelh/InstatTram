package com.example.instatram.ui.main.image

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.example.instatram.MainActivity
import com.example.instatram.R
import com.example.instatram.data.Tram
import com.example.instatram.ui.main.Maps.Map2
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import io.opencensus.internal.Utils
import kotlinx.android.synthetic.main.activity_image_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.random.Random


class image_activity : AppCompatActivity(),OnImageItemClickListner {
    val baos = ByteArrayOutputStream()
    val CAMERA_REQUEST_CODE = 0
    var curFile: Uri? = null
    val storageRef = Firebase.storage.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_activity)
        val station = intent.getStringExtra("name")
        val actionBar = supportActionBar
        actionBar!!.title = station


        buttoncamera.setOnClickListener{
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(callCameraIntent.resolveActivity(packageManager) !=null){
                startActivityForResult(callCameraIntent,CAMERA_REQUEST_CODE)
            }
        }
        listFiles()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val storageRef = Firebase.storage.reference
        
        val message = intent.getStringExtra("id")+"/"
        when(requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK && data != null){
                    showDialogg(data,storageRef,message)

            }
            }
            else -> {
                Toast.makeText(this, "Unrecognised request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun listFiles() = CoroutineScope(Dispatchers.IO).launch {
        var recyclerView: RecyclerView = imagerecyclerView
        try {
            val dossier = intent.getStringExtra("id")+"/"
            val images = storageRef.child(dossier).listAll().await()

            val imageUrls = mutableListOf<String>()
            val imageNames=mutableListOf<String>()
            for(image in images.items) {
                val url = image.downloadUrl.await()
                val name=image.name
                imageNames.add( name)
                imageUrls.add(url.toString())
            }
            withContext(Dispatchers.Main) {
                val dossier = intent.getStringExtra("id")
                val imageAdapter = dossier?.let { ImageAdapter(imageUrls,imageNames, it,clickListnerr = this@image_activity) }
                recyclerView.adapter =imageAdapter

            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                //add toast
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.home -> {val intent1 = Intent(this, MainActivity::class.java)
            startActivity(intent1)}

            R.id.map -> {val intent2 = Intent(this, Map2::class.java)
                intent2.putExtra("name",intent.getStringExtra("name"))
                intent2.putExtra("lat",intent.getStringExtra("lat"))
                intent2.putExtra("lon",intent.getStringExtra("lon"))
                startActivity(intent2)}
            R.id.themes -> showTheme()
            R.id.language -> Translate()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun Translate() {
        val listitems = arrayOf("español", "English")

        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle(R.string.mode2)
        mBuilder.setSingleChoiceItems(listitems, -1){dialog, which->
            if (which==0){
                setLocate("es")
                recreate()
            }
            else if (which == 1){
                setLocate("eng")
                recreate()
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun setLocate(Lang: String){
        val locale = Locale(Lang)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)

        val editor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit()
        editor.putString("My_Lang",Lang)
        editor.apply()
    }
    private fun showTheme() {

        var dark: String = getString(R.string.dark)
        var light: String = getString(R.string.light)
        val listitems = arrayOf(dark,light)


        val mBuilder = AlertDialog.Builder(this)
        mBuilder.setTitle(R.string.mode1)
        mBuilder.setSingleChoiceItems(listitems, -1){dialog, which->
            if (which==0){
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES)
                recreate()
            }
            else if (which == 1){
                AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO)
                recreate()
            }
            dialog.dismiss()
        }
        val mDialog = mBuilder.create()
        mDialog.show()
    }
    fun showDialogg(data: Intent?,storageRef: StorageReference,message:String) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)

        dialog.setContentView(R.layout.dialog)
        var msg1: String = getString(R.string.upload1)
        var msg2: String = getString(R.string.upload2)

        val photoImageView=dialog.findViewById<ImageView>(R.id.photo)

        photoImageView.setImageBitmap(data?.extras?.get("data") as Bitmap)
        val ok=dialog.findViewById<Button>(R.id.ok)
        val cancel=dialog.findViewById<Button>(R.id.cancel)

        val text=dialog.findViewById<EditText>(R.id.imagename)
        var imagenamee:String
        ok.setOnClickListener {
            imagenamee=text.text.toString()
            if (imagenamee==""){
                val string: String = getString(R.string.error)

                Toast.makeText(this, string, Toast.LENGTH_SHORT).show()            }
            else{
            val baos = ByteArrayOutputStream()
            (data.extras?.get("data") as Bitmap).compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val dataa = baos.toByteArray()
            val thename="/"+message+imagenamee
            var uploadTask = storageRef.child(thename).putBytes(dataa)
            uploadTask.addOnFailureListener {
                Toast.makeText(this,"upload failed",Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener { Toast.makeText(this,msg1+" "+imagenamee+" "+msg2,Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()

        }
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()



    }
    override fun clickedd(name:String,dossier:String): Boolean {
        deleteimage(name,dossier)
        return true

    }
    private fun deleteimage(imagename:String,dossier: String) {
        val storageRef = Firebase.storage.reference
        var title: String = getString(R.string.delete)
        var cancel: String = getString(R.string.cancel)
        var success: String = getString(R.string.success)

        val desertRef = storageRef.child("$dossier/$imagename")


        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle(title+" "+imagename)
        alertDialog.setPositiveButton(
                "ok"
        ) {_, _ ->

        desertRef.delete().addOnSuccessListener {
                Toast.makeText(this, success, Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show()
            }
        }
        alertDialog.setNegativeButton(
                cancel
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}