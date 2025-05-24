package com.example.appmedisync.app.screens.home.reportes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import java.io.File

object PdfGenerator {

    fun createPdf(context: Context, content: String): String? {
        return try {
            val customDir = File(
                context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                "ReportesMedicos"
            )

            // Crea la carpeta si no existe
            if (!customDir.exists()) {
                customDir.mkdirs()
            }

            val fileName = "ReporteMedico_${System.currentTimeMillis()}.pdf"
            val file = File(customDir, fileName)

            val writer = PdfWriter(file)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)

            document.add(Paragraph(content))
            document.close()

            Toast.makeText(context, "PDF guardado en:\n${file.absolutePath}", Toast.LENGTH_LONG).show()

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al guardar el PDF", Toast.LENGTH_SHORT).show()
            null
        }
    }

    fun createAndSharePdf(context: Context, content: String) {
        val path = createPdf(context, content)
        if (path != null) {
            val file = File(path)
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "Compartir reporte médico"))
        }
    }
}
