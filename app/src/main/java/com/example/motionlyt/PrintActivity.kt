package com.example.motionlyt

import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.motionlyt.common.Common
import com.example.motionlyt.common.PdfDocumentAdaptor
import com.example.motionlyt.databinding.PrintReciptLayoutBinding
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import java.io.File
import java.io.FileOutputStream

class PrintActivity : AppCompatActivity() {

    private lateinit var binding: PrintReciptLayoutBinding

    private val fileName = "ORDER_FILE.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PrintReciptLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainRecycleLayout.setOnClickListener {
            createFile(Common.getFileSaveLocation() + fileName)
        }
    }

    private fun createFile(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
        try {

            val document = Document()
            //SAVE
            PdfWriter.getInstance(document, FileOutputStream(path))
            document.open()
            //Setting

            document.pageSize = PageSize.A0
            document.addCreationDate()
            document.addAuthor("Anuj Author")
            document.addCreator("Anuj Creator")

            //FONT SETTING
            val baseColor = BaseColor(173, 216, 230)
            val fontSize = 20.0f
            val valueFontSize = 28.0f

            //U can add custom font!!
            val fontTitle =
                Font(Font.FontFamily.TIMES_ROMAN, fontSize, Font.NORMAL, BaseColor.BLACK)
            addNewItem(document, "Order Detail", Element.ALIGN_CENTER, fontTitle)

            //Order Attributes
            val fontTitle2 =
                Font(Font.FontFamily.TIMES_ROMAN, valueFontSize, Font.NORMAL, BaseColor.BLACK)
            addNewItem(document, "Order NO", Element.ALIGN_LEFT, fontTitle2)

            //Order VALUE
            val fontTitle3 =
                Font(Font.FontFamily.TIMES_ROMAN, valueFontSize, Font.NORMAL, BaseColor.BLACK)
            addNewItem(document, "143904", Element.ALIGN_LEFT, fontTitle3)

            addLineSeparator(document)


            //Add order DATE
            addNewItem(document, "Order DATE:", Element.ALIGN_LEFT, fontTitle3)
            addNewItem(document, "33/03/3003", Element.ALIGN_LEFT, fontTitle3)


            addLineSeparator(document)
            addNewItem(document, "Name", Element.ALIGN_LEFT, fontTitle3)
            addNewItem(document, "Anuj Rai", Element.ALIGN_LEFT, fontTitle3)

            addLineSeparator(document)
            addNewItem(document, "Product Detail", Element.ALIGN_CENTER, fontTitle)
            addLineSeparator(document)

            //Item 1
            addNewItemLeftAndRight(document, "Pizza 25", "(0.0%)", fontTitle, fontTitle)
            addNewItemLeftAndRight(document, "12.0*10000", "12000.0", fontTitle, fontTitle)
            addLineSeparator(document)

            addNewItemLeftAndRight(document, "Pizza 26", "(0.0%)", fontTitle, fontTitle)
            addNewItemLeftAndRight(document, "12.0*10000", "12000.0", fontTitle, fontTitle)

            addLineSeparator(document)
            addLineSeparator(document)

            addNewItemLeftAndRight(document, "Total", "2000", fontTitle, fontTitle)

            toastMsg("Created PDF")
            document.close()
            printPdf()

        } catch (e: Exception) {
            toastMsg("Error")
            Log.i("create", "createFile: ${e.localizedMessage}")
        }

    }

    private fun printPdf() {
        val printPdf = getSystemService(Context.PRINT_SERVICE) as PrintManager

        try {
            val pdfAdapter =
                PdfDocumentAdaptor(Common.getFileSaveLocation() + fileName, this)
            printPdf.print(
                "Document", pdfAdapter,
                PrintAttributes.Builder().build()
            )
        } catch (e: Exception) {
            Log.i("Error", "printPdf: ${e.localizedMessage}")
        }

    }

    private fun addNewItemLeftAndRight(
        document: Document,
        txtLeft: String,
        txtRight: String,
        txtLeftFont: Font,
        txtRightFont: Font,
    ) {

        val chunkTxtLeft = Chunk(txtLeft, txtLeftFont)
        val chunkTxtRight = Chunk(txtRight, txtRightFont)
        val p = Paragraph(chunkTxtLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chunkTxtRight)
        document.add(p)

    }

    private fun addLineSeparator(document: Document) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0)
        addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    private fun addLineSpace(doc: Document) {
        doc.add(Paragraph(""))

    }

    private fun addNewItem(document: Document, title: String, alignCenter: Int, font: Font) {
        val chunk = Chunk(title, font)
        val paragraph = Paragraph(chunk)
        paragraph.alignment = alignCenter
        document.add(paragraph)
    }

}