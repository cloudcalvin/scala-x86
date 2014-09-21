package com.scalaAsm.x86
package Instructions
package Standard

import com.scalaAsm.x86.Operands._

abstract class POP_1[-O1, OpEn <: OneOperandEncoding[O1]] extends OneOperandInstruction[O1, OpEn, OneOpcode]("POP")

object POP_1 {

  implicit object pop1 extends POP_1[r32, O] {
    def opcode = 0x58 + rd
  }

  implicit object pop3 extends POP_1[r64, O] {
    def opcode = 0x58 + rd
    override val defaultsTo64Bit = true
  }

  implicit object pop2 extends POP_1[DS, DSFormat] {
    def opcode = 0x1F
  }
}