package com.sparklingpandas.sparklingml.feature

import org.apache.spark.ml.param._
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.types._

import org.scalatest._

import com.holdenkarau.spark.testing.DataFrameSuiteBase

class NltkPosPythonSuite extends FunSuite with DataFrameSuiteBase with Matchers {

  override implicit def reuseContextIfPossible: Boolean = true

  override implicit def enableHiveSupport: Boolean = false

  test("verify that the transformer runs") {
    import spark.implicits._
    val transformer = new NltkPosPython()
    val input = spark.createDataset(
      List(InputData("Boo is happy"), InputData("Boo is sad")))
    transformer.setInputCol("input")
    transformer.setOutputCol("output")
    val result = transformer.transform(input).collect()
    result.size shouldBe 2
    result(0)(0) shouldBe "Boo is happy"
    // TODO(Holden): Figure out why the +- 0.1 matcher syntax wasn't working here
    result(0)(1) shouldBe  0.649
    result(1)(0) shouldBe "Boo is sad"
    result(1)(1) shouldBe 0.0
  }

}


class StrLenPlusKPythonSuite extends FunSuite with DataFrameSuiteBase with Matchers {

  override implicit def reuseContextIfPossible: Boolean = true

  override implicit def enableHiveSupport: Boolean = false

  test("verify that the transformer runs") {
    import spark.implicits._
    val transformer = new StrLenPlusKPython()
    transformer.setK(1)
    val input = spark.createDataset(
      List(InputData("hi"), InputData("boo"), InputData("boop")))
    transformer.setInputCol("input")
    transformer.setOutputCol("output")
    val result = transformer.transform(input).collect()
    result.size shouldBe 3
    result(0)(0) shouldBe "hi"
    result(0)(1) shouldBe 3
    result(1)(0) shouldBe "boo"
    result(1)(1) shouldBe 4
  }

}

class SpacyTokenizePythonSuite extends FunSuite with DataFrameSuiteBase with Matchers {

  override implicit def reuseContextIfPossible: Boolean = true

  override implicit def enableHiveSupport: Boolean = false

  test("verify spacy tokenization works") {
    import spark.implicits._
    val transformer = new SpacyTokenizePython()
    transformer.setLang("en")
    val input = spark.createDataset(
      List(InputData("hi boo"), InputData("boo")))
    transformer.setInputCol("input")
    transformer.setOutputCol("output")
    val result = transformer.transform(input).collect()
    result.size shouldBe 2
    result(0)(1) shouldBe Array("hi", "boo")
  }

}
