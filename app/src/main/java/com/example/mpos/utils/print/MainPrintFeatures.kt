package com.example.mpos.utils.print

import android.app.Activity
import android.content.Context
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import com.example.mpos.data.confirmOrder.response.json.PrintReceiptInfo
import com.example.mpos.utils.getEmojiByUnicode
import com.example.mpos.utils.msg
import com.itextpdf.text.*
import com.itextpdf.text.Rectangle.NO_BORDER
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import java.io.File
import java.io.FileOutputStream


class MainPrintFeatures(
    private val activity: Activity,
    private val fileName: String,
    private val responseBody: PrintReceiptInfo
) {


    private val orderNo = "Order No.:"
    private val desc = "Description"
    private val qty = "Qty"
    private val price = "Price"
    private val amt = "Amount"
    private val totalWithOutGst = "Total Amt Excl. Of GST."
    private val noOfItem = "No. of Items"
    private val totalWithGst = "Total Amt Inc. Of GST."
    private val total = "Total"

    fun createFile(path: String) {
        val file = File(path)
        if (file.exists()) {
            file.delete()
        }
        try {

            val rectangle = Rectangle(204.0944f, 841.8892f)
            val document = Document(rectangle, 1f, 1f, 1f, 1f)
            //SAVE
            PdfWriter.getInstance(document, FileOutputStream(path))
            document.open()
            //Setting
            document.addCreationDate()
            document.addAuthor("MPOS")
            document.addCreator("MPOS")

            //FONT SETTING
            val baseColor = BaseColor(173, 216, 230)
            val fontSize = 11.0f
            val valueFontSize = 11.0f

            //U can add custom font!!
            val fontTitle =
                Font(Font.FontFamily.COURIER, fontSize, Font.BOLD, BaseColor.BLACK)
            val fontTitle2 =
                Font(Font.FontFamily.COURIER, valueFontSize, Font.NORMAL, BaseColor.BLACK)
            //Restaurant_Name
            addLineSeparator(document)
            addNewItem(document, responseBody.headerTxt, font = fontTitle)
            addNewItem(document, responseBody.headerTxt2, font = fontTitle2)
            addDottedSeparator(document)

            //Order Details with timeStamps
            //addNewItem(document, orderNo, Element.ALIGN_LEFT, fontTitle2)
            addNewItemLeftAndRight(
                document,
                "$orderNo ${responseBody.orderId}",
                responseBody.datetime,
                fontTitle2,
                fontTitle2
            )
            addDottedSeparator(document, false)
            //Order Details
            addLineSpace(document)
            setOrderTable(document)
            addDottedSeparator(document)
            //Total item withOut gst
            addNewItemLeftAndRight(
                document,
                totalWithOutGst,
                "${responseBody.amtExclGST}\t",
                fontTitle2,
                fontTitle
            )
            addDottedSeparator(document)
            //Total item in bill
            addNewItemLeftAndRight(
                document,
                noOfItem,
                "${responseBody.itemCount}\t",
                fontTitle2,
                fontTitle
            )
            addDottedSeparator(document)
            //Total item with Gst
            addNewItemLeftAndRight(
                document,
                totalWithGst,
                "${responseBody.amtInclGST}\t",
                fontTitle2,
                fontTitle
            )
            addDottedSeparator(document)
            //total amount
            addNewItemLeftAndRight(
                document,
                total,
                "${responseBody.amtInclGST}\t",
                fontTitle2,
                fontTitle
            )
            addLineSeparator(document)
            activity.msg("Generating Receipt ${getEmojiByUnicode(0x1F5A8)}")
            document.close()
            printPdf()

        } catch (e: Exception) {
            activity.msg("Cannot Print Bill")
            Log.i("create", "createFile: ${e.localizedMessage}")
        }

    }

    private fun setOrderTable(document: Document) {
        val pointColumnWidths = floatArrayOf(30f, 10f, 15f, 20f)
        val table = PdfPTable(4)
        table.setWidths(pointColumnWidths)
        val fontTitle2 =
            Font(Font.FontFamily.COURIER, 10f, Font.NORMAL, BaseColor.BLACK)
        //DESCRIPTION
        setTableData(desc, table, fontTitle2)
        //QTY
        setTableData(qty, table, fontTitle2)
        //PRICE
        setTableData(price, table, fontTitle2)
        //AMOUNT
        setTableData(amt, table, fontTitle2)

        //Setting table column name
        document.add(table)
        table.deleteBodyRows()
        addDottedSeparator(document, false)
        responseBody.itemList.forEach { foodItem ->
            setTableData(
                foodItem.description,
                table, fontTitle2
            )
            setTableData(foodItem.qty.toString(), table, fontTitle2)
            setTableData(foodItem.price.toString(), table, fontTitle2)
            setTableData(foodItem.amount.toString(), table, fontTitle2)
        }


        //1 Order Item
        /*setTableData("Pizza", table)
        setTableData("3", table)
        setTableData("100", table)
        setTableData("300", table)

        //2 Order Item
        setTableData("Samosa", table)
        setTableData("4", table)
        setTableData("100", table)
        setTableData("400", table)

        //3 Order Item
        setTableData("Chocolate", table)
        setTableData("5", table)
        setTableData("500", table)
        setTableData("2500", table)*/

        document.add(table)
    }


    private fun setTableData(value: String, table: PdfPTable, fontTitle2: Font) {
        val cell = PdfPCell(Phrase(value, fontTitle2))
        cell.border = NO_BORDER
        cell.setPadding(0f)
        table.addCell(cell)
    }


    private fun printPdf() {
        val printPdf = activity.getSystemService(Context.PRINT_SERVICE) as PrintManager

        try {
            val pdfAdapter =
                PdfDocumentAdaptor(PrintUtils.getFileSaveLocation() + fileName, activity)
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

    private fun addDottedSeparator(document: Document, flag: Boolean = true) {
        val customLine = CustomDashedLineSeparator()
        customLine.dash = 3f
        customLine.gap = 4f
        customLine.lineWidth = 1f
        if (flag)
            addLineSpace(document)
        document.add(Chunk(customLine))
        addLineSpace(document)
    }

    private fun addLineSeparator(document: Document, flag: Boolean = true) {
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0, 0, 0)
        if (flag)
            addLineSpace(document)
        document.add(Chunk(lineSeparator))
        addLineSpace(document)
    }

    private fun addLineSpace(doc: Document) {
        doc.add(Paragraph(""))

    }

    private fun addNewItem(
        document: Document,
        title: String,
        alignCenter: Int = Element.ALIGN_CENTER,
        font: Font
    ) {
        val chunk = Chunk(title, font)
        val paragraph = Paragraph(chunk)
        paragraph.alignment = alignCenter
        document.add(paragraph)
    }


}