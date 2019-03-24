package com.ruchij.providers
import java.util.UUID

trait UuidProvider {
  def uuid(): UUID = UUID.randomUUID()
}
