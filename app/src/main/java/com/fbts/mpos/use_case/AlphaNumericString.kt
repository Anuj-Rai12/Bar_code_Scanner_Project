package com.fbts.mpos.use_case

import java.nio.charset.Charset
import java.util.*

class AlphaNumericString {

    companion object {
        fun getAlphaNumericString(n: Int): String {

            // length is bounded by 256 Character
            var n = n
            val array = ByteArray(256)
            Random().nextBytes(array)
            val randomString = String(array, Charset.forName("UTF-8"))

            // Create a StringBuffer to store the result
            val r = StringBuffer()

            // Append first 20 alphanumeric characters
            // from the generated random String into the result
            for (element in randomString) {
                if ((element in 'a'..'z'
                            || element in 'A'..'Z'
                            || element in '0'..'9')
                    && n > 0
                ) {
                    r.append(element)
                    n--
                }
            }

            // return the resultant string
            return r.toString()
        }
    }
}