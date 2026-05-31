package com.example.data.mapper

import com.example.data.dto.NoteDto
import com.example.domain.entity.Note

fun NoteDto.toDomain(): Note {
    return Note(
        id = this.id,
        content = this.content
    )
}