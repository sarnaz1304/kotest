package io.kotest.mpp

import io.kotest.common.KotestInternal
import io.kotest.common.TimeMarkCompat
import io.kotest.common.console

@KotestInternal
actual fun writeLog(start: TimeMarkCompat, t: Throwable?, f: () -> String) {
   console.log(start.elapsedNow().inWholeMicroseconds.toString())
   console.log("  ")
   console.log(f())
   console.log("\n")
}
