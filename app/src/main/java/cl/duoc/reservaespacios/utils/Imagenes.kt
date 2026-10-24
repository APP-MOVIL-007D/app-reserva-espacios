package cl.duoc.reservaespacios.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

fun leerFotoGaleria(context: Context, uri: Uri, ladoMaximo: Int = 1024): Bitmap? {
    val medidas = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, medidas) }

    var escala = 1
    while (medidas.outWidth / (escala * 2) >= ladoMaximo || medidas.outHeight / (escala * 2) >= ladoMaximo) {
        escala *= 2
    }

    val opciones = BitmapFactory.Options().apply { inSampleSize = escala }
    return context.contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opciones) }
}
