package com.anthonycr.mezzanine.utils

import com.squareup.javapoet.JavaFile
import java.io.IOException
import java.io.Writer
import javax.annotation.processing.Filer

/**
 * Utils used to write a [JavaFile] to a file.
 */
object FileGenUtils {

    lateinit var filer: Filer

    /**
     * Writes a Java file to the file system after deleting the previous copy.
     *
     * @param file  the file to write.
     *
     * @throws IOException throws an exception if we are unable to write the file to the filesystem.
     */
    @Throws(IOException::class)
    fun writeToFile(file: JavaFile) {
        val fileName = if (file.packageName.isEmpty()) file.typeSpec.name else file.packageName + '.' + file.typeSpec.name
        val originatingElements = file.typeSpec.originatingElements
        val filerSourceFile = filer.createSourceFile(fileName, *originatingElements.toTypedArray())
        filerSourceFile.delete()
        var writer: Writer? = null
        try {
            writer = filerSourceFile.openWriter()
            file.writeTo(writer!!)
        } catch (e: Exception) {
            try {
                filerSourceFile.delete()
            } catch (ignored: Exception) {
            }

            throw e
        } finally {
            if (writer != null) {
                try {
                    writer.close()
                } catch (ignored: IOException) {
                }

            }
        }
    }
}
