package com.neotestdev.fiauct.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FadingLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    edgeHeight: Dp = 28.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    content: LazyListScope.() -> Unit
) {
    Box(modifier = modifier) {
        LazyColumn(
            state = state,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )

        VerticalFadingEdges(
            showTop = state.canScrollBackward,
            showBottom = state.canScrollForward,
            edgeHeight = edgeHeight,
            backgroundColor = backgroundColor
        )
    }
}

@Composable
fun FadingLazyVerticalGrid(
    columns: GridCells,
    modifier: Modifier = Modifier,
    state: LazyGridState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    edgeHeight: Dp = 28.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    content: LazyGridScope.() -> Unit
) {
    Box(modifier = modifier) {
        LazyVerticalGrid(
            columns = columns,
            state = state,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding,
            verticalArrangement = verticalArrangement,
            horizontalArrangement = horizontalArrangement,
            content = content
        )

        VerticalFadingEdges(
            showTop = state.canScrollBackward,
            showBottom = state.canScrollForward,
            edgeHeight = edgeHeight,
            backgroundColor = backgroundColor
        )
    }
}

@Composable
fun FadingScrollColumn(
    modifier: Modifier = Modifier,
    edgeHeight: Dp = 28.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            content = content
        )

        VerticalFadingEdges(
            showTop = scrollState.canScrollBackward,
            showBottom = scrollState.canScrollForward,
            edgeHeight = edgeHeight,
            backgroundColor = backgroundColor
        )
    }
}

@Composable
private fun BoxScope.VerticalFadingEdges(
    showTop: Boolean,
    showBottom: Boolean,
    edgeHeight: Dp,
    backgroundColor: Color
) {
    val currentBackground by rememberUpdatedState(backgroundColor)
    val topBrush = remember(currentBackground) {
        Brush.verticalGradient(listOf(currentBackground, currentBackground.copy(alpha = 0f)))
    }
    val bottomBrush = remember(currentBackground) {
        Brush.verticalGradient(listOf(currentBackground.copy(alpha = 0f), currentBackground))
    }

    if (showTop) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(edgeHeight)
                .background(topBrush)
        )
    }

    if (showBottom) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(edgeHeight)
                .background(bottomBrush)
        )
    }
}
