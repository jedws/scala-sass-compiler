package net.fyrie.sass
package specs

import org.specs._

import scala.util.parsing.combinator._

class SassSpec extends Specification {
  "sass parser" should {

    "parse a simple script" in {
      val result = Sass("div#main a, .sidebar p\n  font-size: 10px\n  font-color: black\n")
      result.successful mustBe true
      result.get must beEqualTo("div#main a, .sidebar p { font-size:10px; font-color:black; }\n")
    } 

    "parse a simple script with 2 selector lists" in {
      val result = Sass("div#main a, .sidebar p\n  font-size: 10px\n  font-color: black\n\nh1\n  font-size: 1em\n")
      result.successful mustBe true
      result.get must beEqualTo("div#main a, .sidebar p { font-size:10px; font-color:black; }\nh1 { font-size:1em; }\n")
    }

    "parse a script with child rulesets" in {
      val result = Sass("div#main a, .sidebar p\n  font-size: 10px\n  font-color: black\n  h1\n    span, div\n      font-family: Arial\n")
      result.successful mustBe true
      result.get must beEqualTo("div#main a, .sidebar p { font-size:10px; font-color:black; }\ndiv#main a h1 span, div#main a h1 div, .sidebar p h1 span, .sidebar p h1 div { font-family:Arial; }\n")
    }
    
    // Taken from the sass-lang examples.
    "parse a script with nested ruleset" in {
      val result = Sass("table.h1\n  margin: 2em 0\n  td.ln\n    text-align: right\n")
      println(result)
      result.successful mustBe true
      result.get must beEqualTo("table.h1 { margin:2em 0; }\ntable.h1 td.ln { text-align:right; }\n")
    }

    "parse a script with nested properties" in {
      val result = Sass("div#main a, .sidebar p\n  font:\n    size: 10px\n    color: black\n")
      result.successful mustBe true
      result.get must beEqualTo("div#main a, .sidebar p { font-size:10px; font-color:black; }\n")
    }

    "parse a script with parent references in selectors" in {
      val result = Sass("div#main a, .sidebar p\n  font:\n    size: 10px\n  &:after\n    margin-top: 10px\n")
      result.successful mustBe true
      result.get must beEqualTo("div#main a, .sidebar p { font-size:10px; }\ndiv#main a:after, .sidebar p:after { margin-top:10px; }\n")
    }
    
    "parse a script with parent references in child selector" in {
      val result = Sass("""a
  font-weight: bold
  text-decoration: none
  &:hover
    text-decoration: underline
  body.firefox &
    font-weight: normal
""")
      val expected = """a { font-weight:bold; text-decoration:none; }
a:hover { text-decoration:underline; }
body.firefox a { font-weight:normal; }
"""
      result.successful mustBe true
      result.get must beEqualTo(expected)
    }
 
    "parse a script with deeply nested parent selectors" in {
      val result = Sass("""#main
  color: black
  a
    font-weight: bold
    &:hover
      color: red
""")
      val expected = """#main { color:black; }
#main a { font-weight:bold; }
#main a:hover { color:red; }
"""
      result.successful mustBe true
      result.get must beEqualTo(expected)
    }
    
    "parse a script with font namespaces" in {
      val result = Sass(""".funky
  font:
    family: fantasy
    size: 30em
    weight: bold
""")
      val expected = """.funky { font-family:fantasy; font-size:30em; font-weight:bold; }
"""
      result.successful mustBe true
      result.get must beEqualTo(expected)
    }

    /*
    "parse a script with value namespace" in {
      val result = Sass(""".funky
  font: 2px/3px
    family: fantasy
    size: 30em
    weight: bold
""")

      val expected = """.funky { font:2px/3px; font-family:fantasy; font-size:30em; font-weight:bold; }
"""
      result.successful mustBe true
      result.get must beEqualTo(expected)
    }*/
    
    "parse a script with simple math expressions" in {
      val result = Sass("a\n  color: #123 + 20\n")
      result.successful mustBe true
      result.get must beEqualTo("a { color:#253647; }\n")
    }
    
    "parse a script with simple constants" in {
      val result = Sass("$test_color: black\ndiv#main a, .sidebar p\n  font-size: 10px\n  font-color: $test_color\n")
      result.successful mustBe true
      result.get must beEqualTo("div#main a, .sidebar p { font-size:10px; font-color:black; }\n")
    }

    "parse a script with calculated constants" in {
      val result = Sass("$test_size: 10px\ndiv#main a\n  font-size: $test_size + 13px\n")
      result.successful mustBe true
      result.get must beEqualTo("div#main a { font-size:23px; }\n")
    }

    "parse a script with calculated color constants" in {
      val result = Sass("$test_color: #123\n$other_color: $test_color + 20\ndiv#main a\n  background-color: $other_color\n")
      result.successful mustBe true
      result.get must beEqualTo("div#main a { background-color:#253647; }\n")
    }

    "parse a script with complex calculated constants" in {
      val result = Sass("$partial_border: 5px / 2 \"solid\"\n$test_color: #123\n$other_color: $test_color - 1\n$border: $partial_border ($other_color + #111)\ndiv#main a\n  border: $border\ndiv#other\n  border: 10/2px ($other_color + #222) dashed\n")
      result.successful mustBe true
      result.get must beEqualTo("div#main a { border:2.5px solid #213243; }\ndiv#other { border:5px #324354 dashed; }\n")
    }
    
    //"parse a script with mixins" in {}
    
    //"parse a script with selector inheritence" in {}

  }
}
