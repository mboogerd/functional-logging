/*
 * Copyright 2015 Merlijn Boogerd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.mboogerd

import java.util.Date

import org.scalacheck.Arbitrary._
import org.scalacheck.{ Arbitrary, Gen }

/**
 *
 */
object TestData {

  implicit class GenOps[T](gen: Gen[T]) {
    def toStream: Stream[T] = Stream.continually(gen.sample).flatten
  }

  case class ProcessId(processId: String)

  /* === dummy entries === */
  trait ProcessEvent
  case object START extends ProcessEvent
  case object FINISH extends ProcessEvent
  case object ERROR extends ProcessEvent

  trait LogEntry
  case class LogEntry1(process: ProcessId, event: ProcessEvent, s: String, date: MyDate) extends LogEntry {
    def toLogEntry2(i: Int): LogEntry2 = LogEntry2(process, event, s, i)
  }
  case class LogEntry2(process: ProcessId, event: ProcessEvent, s: String, i: Int) extends LogEntry

  /* === arbitrary instances for dummy log entries === */
  implicit val arbitraryProcess: Arbitrary[ProcessId] = Arbitrary(Gen.oneOf("process1", "process2", "process3").map(ProcessId.apply))

  implicit val arbitraryLogEntry1: Arbitrary[LogEntry1] =
    Arbitrary(Gen.zip(arbitrary[ProcessId], arbitrary[ProcessEvent], Gen.alphaStr, arbitrary[MyDate]).map(d ⇒ LogEntry1.apply(d._1, d._2, d._3, d._4)))

  implicit val arbitraryLogEntry2: Arbitrary[LogEntry2] =
    Arbitrary(Gen.zip(arbitrary[LogEntry1], arbitrary[Int]).map(x ⇒ x._1.toLogEntry2(x._2)))

  implicit val arbitraryLogEntry: Arbitrary[LogEntry] = Arbitrary(Gen.oneOf(arbitrary[LogEntry1], arbitrary[LogEntry2]))

  implicit val arbitraryProcessEvent: Arbitrary[ProcessEvent] = Arbitrary(Gen.oneOf(START, FINISH, ERROR))

  val earliestPossibleDate = new MyDate(0l)
  val lastPossibleDate = new MyDate(Long.MaxValue)
  def genDateBetween(
    lowerBound: Gen[MyDate] = earliestPossibleDate,
    upperBound: Gen[MyDate] = lastPossibleDate
  ) = for {
    first <- lowerBound
    second <- upperBound
    lowest = if (first before second) first else second
    highest = if (first before second) second else first
    between <- Gen.choose(lowest.getTime, highest.getTime)
  } yield new MyDate(between)

  implicit val arbitraryDate: Arbitrary[MyDate] = Arbitrary(genDateBetween(
    lowerBound = new MyDate(2016 - 1900, 10, 1),
    upperBound = new MyDate(2016 - 1900, 11, 1)
  ))
}
