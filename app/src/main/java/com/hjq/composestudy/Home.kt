package com.hjq.composestudy

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


private fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit
): Modifier = composed {
    val offsetX = remember {
        Animatable(0f)
    }

    pointerInput(Unit) {
        // 衰减动画通常在投掷姿势后使用，用于计算投掷动画最后的固定位置
        val decay = splineBasedDecay<Float>(this)

        coroutineScope {
            while (true) {
                // 等待触摸按下事件
                // awaitPointerEventScope:挂起并安装指针输入块。该块可以等待输入事件并立即响应它们
                // awaitFirstDown：读取事件，直到收到第一个down
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }

                val velocityTracker = VelocityTracker()
                // 等待拖动事件
                awaitPointerEventScope {
                    // 监听水平滑动
                    horizontalDrag(pointerId) { change ->
                        val horizontalOffset = change.positionChange().x + offsetX.value
                        // 启动协和，执行动画
                        launch {
                            offsetX.snapTo(horizontalOffset)
                        }
                        // 记录滑动的位置
                        velocityTracker.addPosition(change.uptimeMillis, change.position)

                        // 消费掉手势事件，而不是传递给外部
                        change.consumePositionChange()
                    }
                }
                // 托动完成，计算投掷的速度
                val velocity = velocityTracker.calculateVelocity().x
                // 我们计算投掷的最终位置，以决定是将元素滑回原始位置，还是将其滑开并调用回调
                val targetOffsetX = decay.calculateTargetValue(offsetX.value, velocity)

                offsetX.updateBounds(
                    lowerBound = -size.width.toFloat(),
                    upperBound = size.width.toFloat()
                )

                launch {
                    if (targetOffsetX.absoluteValue <= size.width) {
                        // 滑回来
                        offsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        // 启动衰减动画
                        offsetX.animateDecay(velocity, decay)
                        onDismissed()
                    }
                }
            }
        }

    }.offset {
        IntOffset(offsetX.value.roundToInt(), 0)
    }
}
