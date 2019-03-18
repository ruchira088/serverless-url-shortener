package com.ruchij.configuration

import com.typesafe.config.Config

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.util.Try

object ConfigLoader {
  def parse[A](config: Config): Try[A] = macro parseImpl[A]

  def parseImpl[A](c: blackbox.Context)(config: c.Expr[Config])(implicit wtt: c.WeakTypeTag[A]): c.universe.Tree = {
    import c.universe._

    val parameterList =
      wtt.tpe.companion.member(TermName("apply")).asMethod.paramLists.flatten

    val valuesList =
      parameterList
        .map {
          symbol =>
            q"""
               ${typeOf[ConfigValueParser.type].termSymbol}
                .getValue[${symbol.typeSignature}](
                  ${s"${wtt.tpe.typeSymbol.name.toString}.${symbol.name.toString}"},
                  ${config.tree}
                )
             """
        }
        .foldLeft(q"scala.util.Success(scala.collection.immutable.List.empty)") {
          (paramValues, configValue) =>
            q"""
               $paramValues.flatMap(values => $configValue.map(values :+ _))
             """
        }

    q"""$valuesList.map {
      list =>
        ${wtt.tpe.typeSymbol.name.toTermName}(
            ..${parameterList.indices.map {
                index => q"list($index).asInstanceOf[${parameterList(index).typeSignature}]"
              }
            }
          )
      }
    """
  }
}
