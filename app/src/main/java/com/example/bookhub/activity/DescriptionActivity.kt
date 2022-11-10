package com.example.bookhub.activity


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.bookhub.R
import com.example.bookhub.database.BookDatabase
import com.example.bookhub.database.BookEntity
import com.example.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class DescriptionActivity : AppCompatActivity() {

    lateinit var txtBookName: TextView
    lateinit var txtAuthorName:TextView
    lateinit var txtBookPrice:TextView
    lateinit var txtRating:TextView
    lateinit var imgBook: ImageView
    lateinit var progressBar:ProgressBar
    lateinit var progressLayout: RelativeLayout
    lateinit var txtBookDes: TextView
    lateinit var btnAddToFav:Button

    lateinit var toolBar:Toolbar


    var bookId: String?="100"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        txtBookName = findViewById(R.id.txtBookName)
        txtAuthorName = findViewById(R.id.txtAuthorName)
        txtBookPrice = findViewById(R.id.txtPrice)
        txtRating = findViewById(R.id.txtRate)
        imgBook = findViewById(R.id.imgBook)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        txtBookDes = findViewById(R.id.txtBookDes)
        btnAddToFav = findViewById(R.id.btnAddToFav)

        toolBar=findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)
        supportActionBar?.title="Book Details"



        if (intent != null) {
            bookId = intent.getStringExtra("book_id")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "some unexpected error occurred!!",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (bookId == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "some unexpected error occurred!!",
                Toast.LENGTH_SHORT
            ).show()

        }

        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jasonParam = JSONObject()
        jasonParam.put("book_id", bookId)

        if(ConnectionManager().checkConnectivity(this@DescriptionActivity)){
        val jsonRequest =
            object : JsonObjectRequest(Request.Method.POST,url, jasonParam, Response.Listener {
                try {
                    val success = it.getBoolean("success")


                    if (success) {

                        val bookJsonObject = it.getJSONObject("book_data")
                        progressLayout.visibility = View.GONE

                        val bookImageUrl=bookJsonObject.getString("image")
                        txtBookName.text = bookJsonObject.getString("name")
                        txtAuthorName.text = bookJsonObject.getString("author")
                        txtBookPrice.text = bookJsonObject.getString("price")
                        txtRating.text = bookJsonObject.getString("rating")
                        txtBookDes.text = bookJsonObject.getString("description")
                        Picasso.get().load(bookJsonObject.getString("image")).error(R.drawable.default_book_cover).into(imgBook)


                        val bookEntity=BookEntity(
                            bookId?.toInt() as Int ,
                            txtBookName.text.toString(),
                            txtAuthorName.text.toString(),
                            txtBookPrice.text.toString(),
                            txtRating.text.toString(),
                            txtBookDes.text.toString(),
                            bookImageUrl
                        )

                        val checkFav= DBAsyncTask(applicationContext,bookEntity,1).execute()
                        val isFav=checkFav.get()

                        if(isFav){
                            btnAddToFav.text="Remove From Favourites"
                            val favColor= ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                            btnAddToFav.setBackgroundColor(favColor)
                        }else{
                            btnAddToFav.text="Add to Favourites"
                            val favColor= ContextCompat.getColor(applicationContext,R.color.teal_700)
                            btnAddToFav.setBackgroundColor(favColor)
                        }

                        btnAddToFav.setOnClickListener {

                            if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get()){
                                val async=DBAsyncTask(applicationContext,bookEntity,2).execute()
                                val result=async.get()
                                if(result){
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Book added to favourite",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    btnAddToFav.text="Remove From Favourites"
                                    val favColor= ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                                    btnAddToFav.setBackgroundColor(favColor)

                                }else{
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Some error occurred",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }else{
                                val async=DBAsyncTask(applicationContext,bookEntity,3).execute()
                                val result=async.get()

                                if(result){
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Book removed from favourite",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    btnAddToFav.text="Add to Favourites"
                                    val nofavColor= ContextCompat.getColor(applicationContext,R.color.teal_700)
                                    btnAddToFav.setBackgroundColor(nofavColor)

                                }else{
                                    Toast.makeText(
                                        this@DescriptionActivity,
                                        "Some error occurred",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }


                            }
                        }


                    } else {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some error occurred",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Some error occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }, Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity, "Volley error $it", Toast.LENGTH_SHORT)
                    .show()

            }) {

                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "9bf534118365f1"

                    return headers

                }

            }
        queue.add(jsonRequest)
        }else{
            val dialog= AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection is not Found")
            dialog.setPositiveButton("Open Settings"){ text,listener->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }

            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }

    class DBAsyncTask(val context:Context,val bookEntity:BookEntity,val mode:Int): AsyncTask<Void,Void,Boolean>(){
        /*
        * mode 1--> check DB if the book is favorite or not
        * mode2--> save the book into DB as favorite
        * mode3--> Remove the favorite book
        * */

        val db = Room.databaseBuilder(context,BookDatabase::class.java,"books-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {

            when(mode){

                1-> {
                    //check DB if the book is favorite or not
                    val book: BookEntity?=db.bookDao().getBookById(bookEntity.BookId.toString())
                    db.close()
                    return book!=null

                }

                2->{
                       //save the book into DB as favorite
                     db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true

                }

                3->{
                     //Remove the favorite book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true

                }


            }

            return false
        }

    }
}