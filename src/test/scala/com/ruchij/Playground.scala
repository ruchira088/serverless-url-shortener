package com.ruchij
import com.ruchij.lambda.handlers.UrlFetchHandler

object Playground {
  def main(args: Array[String]): Unit = {
    println {
      UrlFetchHandler.extractKey("/url/hello/world")
    }
  }
}
