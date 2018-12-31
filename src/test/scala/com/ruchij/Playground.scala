package com.ruchij
import com.ruchij.lambda.handlers.UrlInfoHandler

object Playground {
  def main(args: Array[String]): Unit = {
    println {
      UrlInfoHandler.extractKey("/url/hello/world")
    }
  }
}
