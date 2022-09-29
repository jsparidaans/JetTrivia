package com.example.jettrivia.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettrivia.screens.QuestionsViewModel
import com.example.jettrivia.util.AppColors


@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()
    if (viewModel.data.value.loading == true) {
        CircularProgressIndicator()

    } else {

        questions?.forEach { questionItem ->

        }
    }
}

@Preview
@Composable
fun QuestionDisplay() {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), phase = 0f)
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionTracker()
            DrawDottedLine(pathEffect = pathEffect)
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