package com.ruchij.services.hashing

trait Stringifier[-A] {
  def stringify(value: A): String
}

object Stringifier {
  implicit object StringStringifier extends Stringifier[String] {
    override def stringify(value: String): String = value
  }
}
