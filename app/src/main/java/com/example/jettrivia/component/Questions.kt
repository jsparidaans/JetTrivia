package com.example.jettrivia.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettrivia.model.QuestionItem
import com.example.jettrivia.screens.QuestionsViewModel
import com.example.jettrivia.util.AppColors


@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()
    val index = remember {
        mutableStateOf(0)
    }
    if (viewModel.data.value.loading == true) {
        CircularProgressIndicator()

    } else {
        if (questions != null) {
            val question = try {
                questions[index.value]
            } catch (e: Exception) {
                null
            }
            if (question != null) {
                QuestionDisplay(
                    question = question,
                    idx = index,
                    viewModel = viewModel,
                    onNextClicked = {
                        index.value += 1
                    })
            }
        }
    }
}

//@Preview
@Composable
fun QuestionDisplay(
    question: QuestionItem,
    idx: MutableState<Int>,
    viewModel: QuestionsViewModel,
    onNextClicked: (Int) -> Unit = {}
) {

    val choicesState = remember(question) {
        question.choices.toMutableList()
    }
    val answerState = remember(question) {
        mutableStateOf<Int?>(null)
    }
    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }
    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer
        }
    }

    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), phase = 0f)
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (idx.value >= 3) ShowProgress(score = idx.value)
            QuestionTracker(idx.value)
            DrawDottedLine(pathEffect = pathEffect)

            Column {
                Text(
                    modifier = Modifier
                        .fillMaxHeight(.3f)
                        .padding(6.dp)
                        .align(alignment = Alignment.Start),
                    text = question.question,
                    color = AppColors.mOffWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )

                choicesState.forEachIndexed { index, answer ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.mOffDarkPurple,
                                        AppColors.mOffDarkPurple
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    bottomStartPercent = 50
                                )
                            )
                            .background(color = Color.Transparent),
                        verticalAlignment = CenterVertically
                    ) {
                        RadioButton(
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor =
                                if (correctAnswerState.value == true && index == answerState.value)
                                    Color.Green.copy(alpha = .2f)
                                else Color.Red.copy(alpha = .2f)
                            ),
                            selected = (answerState.value == index),
                            onClick = { updateAnswer(index) }
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color =
                                    if (correctAnswerState.value == true && index == answerState.value)
                                        Color.Green
                                    else if (correctAnswerState.value == false && index == answerState.value)
                                        Color.Red
                                    else
                                        AppColors.mOffWhite,
                                    fontSize = 17.sp
                                )
                            ) {
                                append(answer)
                            }
                        }
                        Text(modifier = Modifier.padding(6.dp), text = annotatedString)
                    }
                }
                Button(
                    modifier = Modifier
                        .padding(3.dp),
                    shape = RoundedCornerShape(34.dp),
                    colors = buttonColors(backgroundColor = AppColors.mLightBlue),
                    onClick = { onNextClicked(idx.value) }) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp,
                        text = "Next"
                    )

                }
            }
        }
    }
}

@Preview
@Composable
fun QuestionTracker(counter: Int = 10, outOf: Int = 100) {
    Text(
        modifier = Modifier.padding(20.dp),
        text = buildAnnotatedString {
            withStyle(
                style = ParagraphStyle(textIndent = TextIndent.None)
            ) {
                withStyle(
                    style = SpanStyle(
                        color = AppColors.mLightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 27.sp
                    )
                ) {
                    append("Question $counter/")
                    withStyle(
                        style = SpanStyle(
                            color = AppColors.mLightGray,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    ) {
                        append("$outOf")
                    }
                }
            }
        }
    )
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = AppColors.mLightGray,
            start = Offset(x = 0f, y = 0f),
            end = Offset(size.width, y = 0f),
            pathEffect = pathEffect
        )
    }
}

@Preview
@Composable
fun ShowProgress(score: Int = 20) {
    val gradient = Brush.linearGradient(
        listOf(
            Color(0xfff95075),
            Color(0xffbe6be5)
        )
    )
    val progressFactor by remember(score) {
        mutableStateOf(score * 0.005f)
    }
    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.mLightPurple,
                        AppColors.mLightPurple
                    )
                ),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .background(Color.Transparent),
        verticalAlignment = CenterVertically
    ) {
        Button(modifier = Modifier
            .fillMaxWidth(progressFactor)
            .background(brush = gradient),
            contentPadding = PaddingValues(1.dp),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            ),
            onClick = {}) {
            Text(
                modifier = Modifier
                    .clip(RoundedCornerShape(23.dp))
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColors.mOffWhite,
                textAlign = TextAlign.Center,
                text = (score * 10).toString()
            )

        }
    }

}