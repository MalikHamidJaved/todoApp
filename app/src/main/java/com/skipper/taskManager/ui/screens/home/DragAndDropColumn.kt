package com.skipper.taskManager.ui.screens.home

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun <T> DragAndDropColumn(
    items: List<T>,
    onMove: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    itemContent: @Composable (T) -> Unit
) {
    var draggedItem by remember { mutableStateOf<T?>(null) }
    var draggedOffset by remember { mutableStateOf(Offset.Zero) }
    var overIndex by remember { mutableStateOf(-1) }

    Column(modifier) {
        items.forEachIndexed { index, item ->
            val isDragged = draggedItem == item
            val zIndex = if (isDragged) 1f else 0f
            val offset = if (isDragged) draggedOffset else Offset.Zero

            Column(
                Modifier
                    .zIndex(zIndex)
                    .absoluteOffset(offset.x.dp, offset.y.dp)
                    .Draggable(
                        item = item,
                        onDragStart = { draggedItem = item },
                        onDragChange = { draggedOffset += it },
                        onDragEnd = {
                            draggedItem?.let { fromItem ->
                                val fromIndex = items.indexOf(fromItem)
                                if (fromIndex != overIndex && overIndex != -1) {
                                    onMove(fromIndex, overIndex)
                                }
                                draggedItem = null
                                draggedOffset = Offset.Zero
                                overIndex = -1
                            }
                        },
                        onOver = { overIndex = index }
                    )
            ) {
                itemContent(item)
            }
        }
    }
}

fun <T> Modifier.Draggable(
    item: T,
    onDragStart: (Offset) -> Unit,
    onDragChange: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    onOver: () -> Unit
): Modifier = this.then(
    Modifier.pointerInput(Unit) {
        detectDragGesturesAfterLongPress(
            onDragStart = { offset -> onDragStart(offset  ) },
            onDrag = { change, dragAmount ->
                change.consume()
                onDragChange(dragAmount)
                onOver()
            },
            onDragEnd = { onDragEnd() },
            onDragCancel = { onDragEnd() }
        )
    }
)