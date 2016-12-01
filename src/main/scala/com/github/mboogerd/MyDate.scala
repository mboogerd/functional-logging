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

import java.text.SimpleDateFormat
import java.util.Date

/**
 *
 */
class MyDate(date: Long) extends Date(date: Long) {

  def this(year: Int, month: Int, day: Int) = {
    this(new Date(year, month, day).getTime)
  }

  val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

  override def toString: String = {
    format.format(this)
  }
}
