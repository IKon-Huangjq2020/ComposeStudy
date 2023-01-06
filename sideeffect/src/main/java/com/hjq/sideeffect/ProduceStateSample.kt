package com.hjq.sideeffect

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hjq.sideeffect.repository.Image
import com.hjq.sideeffect.repository.Result
import com.hjq.sideeffect.repository.ImageRepository


@Composable
fun loadNetWorkImage(
    url: String,
    imageRepository: ImageRepository
): State<Result<Image>> {

    return produceState(initialValue = Result.Loading as Result<Image>, url, imageRepository) {
        Log.d("huang", "Thread ${Thread.currentThread().name}")

        val image = imageRepository.load(url)
        value = if (image == null) {
            Result.Error
        } else {
            Result.Success(image)
        }
    }

}

@Composable
fun ProduceStateSample() {
    val imagesList = listOf<String>(
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F202103%2F09%2F20210309094501_jfyix.thumb.1000_0.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1675519368&t=e57691b7e88e52bebcc0fd16322ea898",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202105%2F19%2F20210519212438_ced7e.jpg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1675519368&t=c22c13dbb2488051f1e3af010ba4b339",
        "没有https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fblog%2F202106%2F13%2F20210613235426_7a793.thumb.1000_0.jpeg&refer=http%3A%2F%2Fc-ssl.duitang.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1675519403&t=864ec5039b279737999a34e9912f117e"
    )

    var index by remember {
        mutableStateOf(0)
    }

    val imageRepository = ImageRepository(LocalContext.current)
    val result = loadNetWorkImage(url = imagesList[index], imageRepository = imageRepository)

    Column {
        Button(onClick = {
            index %= imagesList.size
            if (++index == imagesList.size) index = 0
        }) {
            Text(text = "选择第 $index 张图片")

        }

        when (result.value) {
            is Result.Loading -> {
                CircularProgressIndicator()//进度条
            }
            is Result.Success -> {
                Image(
                    bitmap = (result.value as Result.Success).image.imageBitmap,
                    contentDescription = "image load success"
                )
            }
            is Result.Error -> {
                Image(
                    imageVector = Icons.Rounded.Warning,
                    contentDescription = "image load error",
                    modifier = Modifier.size(200.dp, 200.dp)
                )
            }
        }
    }
}
