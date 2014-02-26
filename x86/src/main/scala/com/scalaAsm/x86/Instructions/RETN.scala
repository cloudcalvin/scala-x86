package com.scalaAsm.x86.Instructions

import com.scalaAsm.x86.Operands._
import com.scalaAsm.x86.OneOperand
import com.scalaAsm.x86.OperandEncoding
import com.scalaAsm.x86.{ModRM, x86Instruction, OperandSize, Opcodes, OneOpcode, Immediate, DwordOperand, WordOperand}
import com.scalaAsm.x86.AddressingFormSpecifier
import com.scalaAsm.x86.x86Registers._
import scala.annotation.implicitNotFound
import com.scalaAsm.utils.Endian
import com.scalaAsm.x86.Instruction

abstract class RETN extends x86Instruction("RETN")

trait RETN_1[-O1] extends RETN with OneOperand[O1] with OperandEncoding

object RETN {
  
  implicit object retn1 extends RETN_1[imm16] {
      def operands = I(x)
      val opcode: Opcodes = 0xC2
  }
}