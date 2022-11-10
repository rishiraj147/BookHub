package com.example.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bookhub.R
import com.example.bookhub.activity.DescriptionActivity
import com.example.bookhub.model.Book
import com.squareup.picasso.Picasso


class DashboardRecyclerAdapter(val context: Context,val itemList:ArrayList<Book>): RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    class DashboardViewHolder(view: View): RecyclerView.ViewHolder(view){
        val BookName: TextView= view.findViewById(R.id.txtBookName)
        val BookAuthor: TextView=view.findViewById(R.id.txtAuthorName)
        val BookCost: TextView=view.findViewById(R.id.txtPrice)
        val BookRating: TextView=view.findViewById(R.id.txtRate)
        val BookImg: ImageView=view.findViewById(R.id.imgBook)
        val llContent: LinearLayout=view.findViewById(R.id.llContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
       val view= LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book= itemList[position]
        holder.BookName.text=book.BookName
        holder.BookAuthor.text=book.BookAuthor
        holder.BookCost.text=book.BookPrice
        holder.BookRating.text=book.BookRating
       // holder.BookImg.setImageResource(book.BookImage)
        Picasso.get().load(book.BookImage).error(R.drawable.default_book_cover).into(holder.BookImg)

        holder.llContent.setOnClickListener{
            val intent= Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.BookId)
            context.startActivity(intent)
        }



    }

    override fun getItemCount(): Int {
       return itemList.size
    }


}