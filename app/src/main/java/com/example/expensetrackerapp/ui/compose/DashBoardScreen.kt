package com.example.expensetrackerapp.ui.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.expensetrackerapp.R
import com.example.expensetrackerapp.data.Payment
import com.example.expensetrackerapp.data.Transaction
import com.example.expensetrackerapp.db.entiity.Recipient
import com.example.expensetrackerapp.viewmodel.DashboardViewModel
import com.example.expensetrackerapp.viewmodel.uistate.RecipientUI
import com.example.expensetrackerapp.viewmodel.uistate.RecipientUI.Error
import com.example.expensetrackerapp.viewmodel.uistate.RecipientUI.Loading
import com.example.expensetrackerapp.viewmodel.uistate.RecipientUI.Success
import com.example.expensetrackerapp.viewmodel.uistate.TransactionUI
import java.util.Locale


@Composable
fun HomeScreen(dashboardViewModel: DashboardViewModel = hiltViewModel()) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        GreetingAndNotificationSection()

        BalanceDisplaySection(dashboardViewModel.userBalance.collectAsState().value)

        Spacer(modifier = Modifier.height(12.dp))

        GraphPlaceholder(dashboardViewModel.userChartData.collectAsState().value)

        Spacer(modifier = Modifier.height(12.dp))

        RecipientsSection(dashboardViewModel.recipientUiState.collectAsState().value)

        Spacer(modifier = Modifier.height(12.dp))

        TransactionHistorySection(dashboardViewModel.transactionUiState.collectAsState().value)

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun GreetingAndNotificationSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            buildAnnotatedString {
                append("Welcome, ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("John!")
                }
            },
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Black
        )

        Icon(
            Icons.Outlined.Notifications,
            contentDescription = "Notifications",
            tint = Color.Black.copy(alpha = 0.6f)
        )
    }
    HorizontalDivider()
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun BalanceDisplaySection(balance: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = "$ ${String.format(Locale.US,"%.2f", balance)}",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Balance",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black.copy(alpha = 0.6f),
            modifier = Modifier.align(Alignment.Bottom)
        )
    }
}

@Composable
fun GraphPlaceholder(yPoints: List<Float>) {
    Box(
        modifier= Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        CubicChart(yPoints = yPoints)
    }

}


@Composable
fun CubicChart(
    modifier: Modifier = Modifier,
    yPoints: List<Float> = listOf(199f, 52f, 193f, 290f, 150f, 445f),
    graphColor: Color = Color.White
) {
    val spacing = 80f  // Increased left padding for 3-digit Y-axis labels
    val extraSpace = 50f  // Right padding for last X-axis label
    val extraBottomHeight = 50f  // Extra height for bottom padding and X-axis labels
    val extraTopHeight = 50f  // Extra height for top padding
    val xLabels = listOf("1D", "5D", "1M", "3M", "6M", "1Y")
    val maxAmount = yPoints.maxOrNull()?.let {
        kotlin.math.ceil(it / 100f) * 100f  // Round up to the nearest hundred
    } ?: 100f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(350.dp)  // Increased height for Y-axis and padding
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFF1F1F1F))
        ) {
            val gridColor = Color(0xFF3A3A3A)
            val horizontalLines = 5
            val verticalLines = xLabels.size - 1

            val yStep = maxAmount / horizontalLines
            val xStep = (size.width - spacing - extraSpace) / verticalLines

            // Draw horizontal grid lines and Y-axis labels
            for (i in 0..horizontalLines) {
                val y = extraTopHeight + i * ((size.height - extraTopHeight - extraBottomHeight) / horizontalLines)
                drawLine(
                    color = gridColor,
                    start = Offset(spacing, y),  // Start grid lines after left padding
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        (maxAmount - i * yStep).toInt().toString(),
                        spacing - 20f,  // Move labels further left for visibility
                        y + 10f,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.LTGRAY
                            textSize = 28f
                            textAlign = android.graphics.Paint.Align.RIGHT  // Align labels to the right edge
                        }
                    )
                }
            }

            // Draw vertical grid lines and X-axis labels
            for (i in 0..verticalLines) {
                val x = spacing + i * xStep
                drawLine(
                    color = gridColor,
                    start = Offset(x, extraTopHeight),  // Start from extraTopHeight
                    end = Offset(x, size.height - extraBottomHeight),  // End before extraBottomHeight
                    strokeWidth = 1.dp.toPx()
                )
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        xLabels[i],
                        x,
                        size.height - 10f,  // X-axis labels stay at bottom
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.LTGRAY
                            textSize = 28f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }

            val spacePerHour = (size.width - spacing - extraSpace) / yPoints.size
            val normX = mutableListOf<Float>()
            val normY = mutableListOf<Float>()

            // Create a smooth cubic path
            val strokePath = Path().apply {
                for (i in yPoints.indices) {
                    val currentX = spacing + i * spacePerHour
                    val currentY = size.height - extraBottomHeight - (yPoints[i] / maxAmount * (size.height - extraTopHeight - extraBottomHeight))

                    if (i == 0) {
                        moveTo(currentX, currentY)
                    } else {
                        val previousX = spacing + (i - 1) * spacePerHour
                        val previousY = size.height - extraBottomHeight - (yPoints[i - 1] / maxAmount * (size.height - extraTopHeight - extraBottomHeight))

                        val conX1 = (previousX + currentX) / 2f
                        val conX2 = (previousX + currentX) / 2f

                        cubicTo(
                            x1 = conX1,
                            y1 = previousY,
                            x2 = conX2,
                            y2 = currentY,
                            x3 = currentX,
                            y3 = currentY
                        )
                    }
                    normX.add(currentX)
                    normY.add(currentY)
                }
            }

            // Draw smooth path
            drawPath(
                path = strokePath,
                color = graphColor,
                style = Stroke(
                    width = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            // Draw points and markers
            normX.indices.forEach { index ->
                drawCircle(
                    color = graphColor,
                    radius = 6.dp.toPx(),
                    center = Offset(normX[index], normY[index])
                )
                if (index == normX.size - 2) {
                    drawCircle(
                        color = Color.White,
                        radius = 6.dp.toPx(),
                        center = Offset(normX[index], normY[index])
                    )
                }
            }
        }
    }
}









@Composable
fun RecipientsSection(state: RecipientUI) {
    Text(
        text = "Recipients",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(8.dp))

    when (state) {
        Loading -> {
            // Todo show shimmer effect for loading
        }

        is Success -> {
            if (state.recipients.isEmpty())
                Text(text = "No Recipients available ")
            else
                RecipientsGrid(state.recipients)
        }

        is Error -> {
            // Handle error state
        }
    }
}

@Composable
fun TransactionHistorySection(state:TransactionUI) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Transactions history",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    when (state) {
        TransactionUI.Loading -> {
            // Todo show shimmer effect for loading
        }

        is TransactionUI.Success -> {
            if (state.recipients.isEmpty())
                Text(text = "No Transaction available ")
            else
                LazyColumn {
                    items(state.recipients) { transaction ->
                        TransactionItem(transaction)
                    }
                }
        }

        is TransactionUI.Error -> {
            // Handle error state
        }
    }
}



@Composable
fun RecipientsGrid(recipients :List<Recipient> = listOf(Recipient("1","karthik","email","https://i.pravatar.cc/150?img=10"))) {

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(recipients.take(5)) {index, recipient ->
            Box(
                modifier = Modifier.size(52.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    ImageRequest.Builder(LocalContext.current)
                        .data(recipient.imageUrl)
                        .crossfade(true)
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_place_holder)
                        .build(),
                    contentDescription = "profile_icn",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                )
                if (index == 4 && recipients.size>5) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+${recipients.size-5}",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

        }

    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.DateRange, contentDescription = transaction.title, tint = Color.White)
            AsyncImage(
                ImageRequest.Builder(LocalContext.current)
                    .data(transaction.imageUrl)
                    .crossfade(true)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .build(),
                contentDescription = "profile_icn",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = transaction.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = transaction.paymentType.type, fontSize = 12.sp, color = Color.Black)
        }

        Text(
            text = "${if (transaction.paymentType == Payment.Credit()) "+" else "-"} $${String.format(Locale.US,"%.2f",transaction.amount)}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }

}


