package com.lie.puremusic.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import jp.wasabeef.glide.transformations.BitmapTransformation
import java.nio.charset.Charset
import java.security.MessageDigest

class CropTransformation(
    private val x: Int,
    private val y: Int,
    private val width: Int,
    private val height: Int
) :
    BitmapTransformation() {
    override fun transform(
        context: Context,
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return Bitmap.createBitmap(toTransform, x, y, width, height)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun equals(o: Any?): Boolean {
        return o is CropTransformation
    }

    override fun hashCode(): Int {
        return 0
    }

    companion object {
        private val ID_BYTES = Build.ID.toByteArray(Charset.forName("UTF-8"))
    }
}