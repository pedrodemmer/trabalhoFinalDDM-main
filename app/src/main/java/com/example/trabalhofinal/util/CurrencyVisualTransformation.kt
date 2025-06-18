package com.example.trabalhofinal.util

import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.AnnotatedString
import java.text.NumberFormat
import java.util.*

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }
        val parsed = digits.toDoubleOrNull() ?: 0.0
        val formatted = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(parsed / 100)
        val out = formatted
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = out.length
            override fun transformedToOriginal(offset: Int) = digits.length
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}