package ch.epfl.directembedding.test

import scala.language.experimental.macros

import org.scalatest._
import ch.epfl.directembedding._
import Parser._
class ParsersSpec extends FlatSpec with ShouldMatchers {

  trait Parser {
    def rule(block: Rule): Rule = macro ParserObj.rule
  }

  class SubParser extends Parser {
    def a = rule { "a" } // ConstRule("a")
    def b = rule { "b" } // ConstRule("b")
  }

  class SimpleParser extends Parser {
    val subParser = new SubParser()
    // Rule(Sequence(Apply(Symbol, Nil), Apply(Symbol, Nil)))
    def aa = rule { subParser.a ~ subParser.b }
  }

  "New parboiled" should "be able to reify function calls" in {
    new SimpleParser().aa should be(Sequence(Apply("method a", List()), Apply("method b", List())))
  }

}