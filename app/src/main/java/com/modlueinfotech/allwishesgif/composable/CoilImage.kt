//package com.modlueinfotech.allwishesgif.composable
//
//import android.graphics.drawable.Animatable
//import android.graphics.drawable.Drawable
//import android.os.Build.VERSION.SDK_INT
//import androidx.annotation.Px
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Size.Companion.Zero
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.unit.Constraints.Companion.Infinity
//import androidx.core.graphics.drawable.toBitmap
//import coil.ImageLoader
//import coil.decode.GifDecoder
//import coil.decode.ImageDecoderDecoder
//import coil.request.CachePolicy
//import coil.request.LoadRequest
//import coil.request.LoadRequestBuilder
//import coil.size.Scale
//import coil.target.Target
//import kotlinx.coroutines.*
//import androidx.compose.ui.graphics.asImageBitmap
//import androidx.compose.ui.platform.LocalContext
//
//@Composable
//fun CoilImage(
//    model: Any,
//    modifier : Modifier = Modifier,
//    customize: LoadRequestBuilder.() -> Unit = {}
//) {
//    BoxWithConstraints(modifier) {
//        var width =
//            if (constraints.maxWidth > Zero.width && constraints.maxWidth < Infinity) {
//                constraints.maxWidth
//            } else {
//                -1
//            }
//
//        var height =
//            if (constraints.maxHeight > Zero.height && constraints.maxHeight < Infinity) {
//                constraints.maxHeight
//            } else {
//                -1
//            }
//
//        //if height xor width not able to be determined, make image a square of the determined dimension
//        if (width == -1) width = height
//        if (height == -1) height = width
//
//        val image = remember<MutableState<ImageBitmap>> { mutableStateOf(ImageBitmap(width,height)) }
//        val context = LocalContext.current
//        var animationJob : Job? = remember { null }
//        DisposableEffect(model) {
//            val target = object : Target {
//                override fun onStart(placeholder: Drawable?) {
//                    placeholder?.apply {
//                        animationJob?.cancel()
//                        if(height != -1 && width != -1) {
//                            animationJob = image.update(this, width, height)
//                        } else if (height == -1) {
//                            val scaledHeight = intrinsicHeight * (width / intrinsicWidth )
//                            animationJob = image.update(this, width, scaledHeight)
//                        } else if (width == -1) {
//                            val scaledWidth = intrinsicWidth * (height / intrinsicHeight)
//                            animationJob = image.update(this, scaledWidth, height)
//                        }
//                    }
//                }
//
//                override fun onSuccess(result: Drawable) {
//                    animationJob?.cancel()
//                    animationJob = image.update(result)
//                }
//
//                override fun onError(error: Drawable?) {
//                    error?.run {
//                        animationJob?.cancel()
//                        animationJob = image.update(error)
//                    }
//                }
//            }
//
//
//
//            val loader = ImageLoader.Builder(context)
//                .componentRegistry {
//                    if (SDK_INT >= 28) {
//                        add(ImageDecoderDecoder())
//                    } else {
//                        add(GifDecoder())
//                    }
//                }.build()
//
//
//            val request = LoadRequest.Builder(context)
//                .data(model)
//                .size(width, height)
//                .scale(Scale.FILL)
//                .diskCachePolicy(CachePolicy.ENABLED)
//                .apply{customize(this)}
//                .target(target)
//
//            val requestDisposable = loader.execute(request.build())
//
//            onDispose {
//                image.value = ImageBitmap(width,height)
//                requestDisposable.dispose()
//                animationJob?.cancel()
//            }
//        }
////        Image(modifier = modifier, asset = image.value)
//        Image(bitmap = image.value, contentDescription = "")
//    }
//}
//
//internal fun MutableState<ImageBitmap>.update(drawable: Drawable, @Px width: Int? = null, @Px height: Int? = null) : Job? {
//    if (drawable is Animatable) {
//        (drawable as Animatable).start()
//
//        return GlobalScope.launch(Dispatchers.Default) {
//            while (true) {
//                val asset = drawable.toBitmap(
//                    width = width ?: drawable.intrinsicWidth,
//                    height =  height ?: drawable.intrinsicHeight)
//                    .asImageBitmap()
//                withContext(Dispatchers.Main) {
//                    value = asset
//                }
//                delay(16)
//                //1000 ms / 60 fps = 16.666 ms/fps
//                //TODO: figure out most efficient way to dispaly a gif
//            }
//        }
//    } else {
//        value = drawable.toBitmap(
//            width = width ?: drawable.intrinsicWidth,
//            height =  height ?: drawable.intrinsicHeight)
//            .asImageBitmap()
//        return null
//    }
//}