package com.scalaAsm.x86
package Operands

import Memory._
import com.scalaAsm.x86.OpcodeFormat

object NoOperand

object NoAddressingForm extends InstructionFormat(addressingForm = NoModRM(), immediate = None)

trait OperandFormat

class NoOperandFormat extends ResolvedZeroOperand(null, Seq()) {
  val size = 1
}


abstract class TwoOperandFormat[-X, -Y] {
  def getAddressingForm(op1: X, op2: Y, opcodeExtension: Byte): InstructionFormat
  def getPrefix(prefix: Seq[Prefix]): Array[Byte] = prefix.map(_.get).foldLeft(Array[Byte]()){ _ ++ _ }
}

abstract class OneOperandFormat[-X] {
  def getAddressingForm(op1: X, opcodeExtension: Byte): InstructionFormat
  def getPrefix(prefix: Seq[Prefix]): Array[Byte] = prefix.map(_.get).foldLeft(Array[Byte]()){ _ ++ _ }
}

abstract class ResolvedZeroOperand(opcode: OpcodeFormat, prefix: Seq[Prefix]) {
  def getPrefix: Array[Byte] = Array()
  def size: Int
}




