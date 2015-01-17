package ch.epfl.directembedding.test

import scala.language.experimental.macros

import ch.epfl.directembedding._
import scala.reflect.macros.blackbox.Context

// IR mock for Parboiled
case class Sequence(self: Rule, rhs: Rule) extends Rule
case class ConstRule(rule: String) extends Rule
case class Apply(function: String, args: List[Any]) extends Rule
trait Rule {
  def apply(in: String): Boolean = ???
  @reifyAs(Sequence)
  def ~(rhs: Rule): Rule = ???
}

object ParserObj {
  def rule(c: Context)(block: c.Expr[Rule]): c.Expr[Rule] = {
    import c.universe._
    val reified = new Macros.ReificationTransformer[c.type](c).transform(block.tree)
    c.Expr(q"$reified")
  }
}
object Parser {

  @reifyAs(ConstRule)
  implicit def const(t: String): Rule = ???
}