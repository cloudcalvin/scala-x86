package com.scalaAsm.x86.Instructions

import com.scalaAsm.x86._
import com.scalaAsm.x86.AddressingFormSpecifier
import x86Registers._

trait ADD extends ModRM with Operands

trait ADD_2[-O1, -O2] extends ADD {
  def get(op1: O1, op2: O2): Instruction
}

abstract class MI[X <: OperandSize](op1: ModRM.reg[X], op2: imm8) extends Instruction2[ModRM.reg[X], imm8] {
  val opcodeExtension = Some(0.toByte)
  val operand1 = op1
  val operand2 = op2
}

trait MR[X <: OperandSize] extends ADD_2[ModRM.rm[X], ModRM.reg[X]]

object ADD {
  implicit object add1 extends ADD_2[r32, imm8] {
    def get(x: r32, y: imm8) = new MI[DwordOperand](x,y) {
      val opcode = 0x83.toByte
      val modRM: Option[AddressingFormSpecifier] = Some(getAddressingForm2(this))
     }
  }
  
  implicit object add2 extends ADD_2[r16, imm8] {
    def get(x: r16, y: imm8) = new MI[WordOperand](x,y) {
      val opcode = 0x83.toByte
      val modRM: Option[AddressingFormSpecifier] = Some(getAddressingForm2(this))
     }
  }
}