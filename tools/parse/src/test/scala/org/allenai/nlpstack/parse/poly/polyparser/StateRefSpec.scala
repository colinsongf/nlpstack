package org.allenai.nlpstack.parse.poly.polyparser

import org.allenai.common.testkit.UnitSpec
import org.allenai.nlpstack.parse.poly.core.{AnnotatedSentence, Sentence, NexusToken, Token}
import spray.json._

class StateRefSpec extends UnitSpec {
  // scalastyle:off


  /** This represents the following parser state:
    *
    *   ---------
    *   |        |
    *   |        V
    * NEXUS_0  saw_2  a_3  white_4  cat_5 |||  with_6  a_7  telescope_8
    *            |
    *            V
    *          we_1
    *
    */
  val state1: TransitionParserState = TransitionParserState(
    stack = Vector(5, 4, 3, 2, 0),
    bufferPosition = 6,
    breadcrumb = Map(0 -> -1, 1 -> 2, 2 -> 0),
    children = Map(0 -> Set(2), 2 -> Set(1)),
    arcLabels = Map(Set(0, 2) -> 'root, Set(2, 1) -> 'nsubj),
    annotatedSentence = AnnotatedSentence(
      Sentence(Vector(NexusToken, Token('we), Token('saw), Token('a),
        Token('white), Token('cat), Token('with), Token('a), Token('telescope))),
      IndexedSeq()))


  "Constructing a StackRef" should "throw IllegalArgumentException for a negative index" in {
    intercept[IllegalArgumentException] {
      StackRef(-1)
    }
  }

  "Calling StackRef's apply" should "give back the stack top" in {
    StackRef(0)(state1) shouldBe List(5)
  }

  it should "give back the final stack item" in {
    StackRef(4)(state1) shouldBe List(0)
  }

  it should "give back the empty set when it reaches the end of the stack" in {
    StackRef(5)(state1) shouldBe List()
  }

  "Serializing a StackRef" should "preserve a StackRef with index 3" in {
    val stateRef: StateRef = StackRef(3)
    stateRef.toJson.convertTo[StateRef] shouldBe StackRef(3)
  }

  "Constructing a BufferRef" should "throw IllegalArgumentException for a negative index" in {
    intercept[IllegalArgumentException] {
      BufferRef(-1)
    }
  }

  "Calling BufferRef's apply" should "give back the front buffer item given arg 0" in {
    BufferRef(0)(state1) shouldBe List(6)
  }

  it should "give back the final buffer item" in {
    BufferRef(2)(state1) shouldBe List(8)
  }

  it should "give back the empty set when it reaches the end of the buffer" in {
    BufferRef(3)(state1) shouldBe List()
  }

  "Serializing a BufferRef" should "preserve a BufferRef with index 2" in {
    val stateRef: StateRef = BufferRef(2)
    stateRef.toJson.convertTo[StateRef] shouldBe BufferRef(2)
  }

  "Constructing a BreadcrumbRef" should "throw IllegalArgumentException for a negative index" in {
    intercept[IllegalArgumentException] {
      BreadcrumbRef(-1)
    }
  }

  "Calling BreadcrumbRef's apply" should "give back the empty set when a valid stack item has no crumb" in {
    BreadcrumbRef(0)(state1) shouldBe List()
  }

  it should "give back the breadcrumb when a valid stack item has a crumb" in {
    BreadcrumbRef(3)(state1) shouldBe List(0)
  }

  it should "give back the empty set when the breadcrumb is negative" in {
    BreadcrumbRef(4)(state1) shouldBe List()
  }

  it should "give back the empty set when it reaches the end of the stack" in {
    BreadcrumbRef(5)(state1) shouldBe List()
  }

  "Serializing a BreadcrumbRef" should "preserve a BreadcrumbRef with index 4" in {
    val stateRef: StateRef = BreadcrumbRef(4)
    stateRef.toJson.convertTo[StateRef] shouldBe BreadcrumbRef(4)
  }

  "Calling StackGretelsRef's apply" should "give back the empty set when a valid stack item " +
    "has no gretels" in {
    StackGretelsRef(0)(state1) shouldBe List()
  }

  it should "give back all gretels when a valid stack item has gretels" in {
    StackGretelsRef(3)(state1) shouldBe List(1)
  }

  it should "give back the empty set when it reaches the end of the stack" in {
    StackGretelsRef(5)(state1) shouldBe List()
  }

  "Serializing a StackGretelsRef" should "preserve a StackGretelsRef with index 4" in {
    val stateRef: StateRef = StackGretelsRef(4)
    stateRef.toJson.convertTo[StateRef] shouldBe StackGretelsRef(4)
  }

  "Calling LastRef's apply" should "give back the last sentence token" in {
    LastRef(state1) shouldBe List(8)
  }

  "Serializing a LastRef" should "preserve a LastRef" in {
    val stateRef: StateRef = LastRef
    stateRef.toJson.convertTo[StateRef] shouldBe LastRef
  }

  "Calling FirstRef's apply" should "give back the first (non-nexus) sentence token" in {
    FirstRef(state1) shouldBe List(1)
  }

  "Serializing a FirstRef" should "preserve a FirstRef" in {
    val stateRef: StateRef = FirstRef
    stateRef.toJson.convertTo[StateRef] shouldBe FirstRef
  }
}
