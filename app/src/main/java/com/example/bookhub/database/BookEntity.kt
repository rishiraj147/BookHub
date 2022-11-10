package com.example.bookhub.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="books")
data class BookEntity (
    @PrimaryKey val BookId:Int,
    @ColumnInfo(name="book_name") val BookName: String,
    @ColumnInfo(name="book_author") val BookAuthor: String,
    @ColumnInfo(name="book_price") val BookPrice: String,
    @ColumnInfo(name="book_rating") val BookRating: String,
    @ColumnInfo(name="book_desc") val bookDesc: String,
    @ColumnInfo(name="book_image") val BookImage: String
    )


