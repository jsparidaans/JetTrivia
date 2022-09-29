package com.example.jettrivia.repository

import com.example.jettrivia.data.DataOrException
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.network.QuestionAPI
import javax.inject.Inject

class QuestionRepository
@Inject constructor(private val api: QuestionAPI) {
    private val questions = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()

}