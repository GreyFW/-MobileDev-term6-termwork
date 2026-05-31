package com.example.data.db

import androidx.room.TypeConverter
import com.example.domain.entity.Exercise
import com.example.domain.entity.Note
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromExerciseList(value: List<Exercise>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toExerciseList(value: String): List<Exercise> {
        val type = object : TypeToken<List<Exercise>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun fromNoteList(value: List<Note>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toNoteList(value: String): List<Note> {
        val type = object : TypeToken<List<Note>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }
}